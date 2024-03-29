package org.firstinspires.ftc.teamcode.auton;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.VariableStorage;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

// Note: this is currenlty basically copy / pasted from LRR, I will work on it more.
@Autonomous(name="Just park - LEFT SIDE")
public class AsyncAutoMiddleLeft extends LinearOpMode {

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    enum PARKING_LOCATION{
        LEFT,
        MIDDLE,
        RIGHT
    };
    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int LEFT = 1;
    int MIDDLE = 2;
    int RIGHT =3;
    AprilTagDetection tagOfInterest = null;

    enum State {
        TRAJECTORY_1,
        WAIT_1,
        TRAJECTORY_2,
        TRAJECTORY_3,
        TRAJECTORY_4,
        TRAJECTORY_5,
        TRAJECTORY_6,
        TRAJECTORY_7,
        TRAJECTORY_8,
        PARKING,
        IDLE
    }
    State currentState = State.IDLE;

    // Define our start pose
    // This assumes we start at x: 15, y: 10, heading: 180 degrees
    Pose2d startPose = new Pose2d(-35.25, -61.5, Math.toRadians(90));

    @Override
    public void runOpMode() throws InterruptedException {
        PhotonCore.enable();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {}
        });

        telemetry.setMsTransmissionInterval(50);


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // Set initial pose
        drive.setPoseEstimate(AutoConstants.initialPositionLeft);

        Intake intake = new Intake();
        intake.init(hardwareMap);

        Slides slides = new Slides();
        slides.init(hardwareMap);
        slides.lowerToBottom();
        intake.open();
        intake.tiltForInit();

        AutoRight.PARKING_LOCATION parking_location = AutoRight.PARKING_LOCATION.LEFT;
        // drive to pole
        TrajectorySequence stepOne = drive.trajectorySequenceBuilder(AutoConstants.initialPositionLeft)
                .addDisplacementMarker(() -> {slides.lowerToBottom(); })
                .addDisplacementMarker(0.4,()-> {intake.tiltUp();})
                .addDisplacementMarker(5,()-> {slides.raiseToMiddle();})
                .forward(20)
                .splineTo(new Vector2d(AutoConstants.mediumPoleLeft.getX(),AutoConstants.mediumPoleLeft.getY()),toRadians(45))
                .build();
        // after the slides go up, drive forward and place on pole
        TrajectorySequence stepOnePointFive = drive.trajectorySequenceBuilder(stepOne.end())
                .forward(AutoConstants.highPoleForward)
                .addDisplacementMarker(AutoConstants.highPoleForward - 0.1,() -> {intake.close();})
                .waitSeconds(0.3)
                .build();
// GO back from the pole and approach the stack
        TrajectorySequence stepTwo = drive.trajectorySequenceBuilder(stepOnePointFive.end())
                .lineToLinearHeading(new Pose2d(-39,-35,toRadians(90)))
                .forward(20)
                .splineTo(new Vector2d(-50,-10),toRadians(180))
                .forward(11.1)
//                .splineTo(new Vector2d(-61.1,-10),toRadians(180))
                .addDisplacementMarker(10, () -> {slides.setTarget(AutoConstants.firstConePos);   intake.tiltDown();})
                .build();

        // Go from stack back to the top pole
        TrajectorySequence stepThree = drive.trajectorySequenceBuilder(stepTwo.end())
                .waitSeconds(0.5)
                .addDisplacementMarker(()->{slides.raiseToMiddle();})
                .addDisplacementMarker(4,()-> {intake.tiltUp();})
                .back(13)
                .lineToLinearHeading(new Pose2d(-34.5,-15,toRadians(-45)))
                .waitSeconds(0.2)
                .forward(3.5)
                .build();

// same as before, but lower for the second cone
        TrajectorySequence stepTwoSecondCone = drive.trajectorySequenceBuilder(stepThree.end())

                .back(10)
                .addDisplacementMarker(10, () -> {slides.setTarget(AutoConstants.secondConePos);  intake.tiltDown();})
                .lineToSplineHeading(AutoConstants.stackPositionLeft)
                .waitSeconds(0.1)
                .build();

        TrajectorySequence stepTwoThirdCone = drive.trajectorySequenceBuilder(stepThree.end())

                .back(10)
                .addDisplacementMarker(10, () -> {slides.setTarget(AutoConstants.thirdConePos);  intake.tiltDown();})
                .lineToSplineHeading(AutoConstants.stackPositionLeft)
                .waitSeconds(0.1)
                .build();



// PARKING!
        TrajectorySequence parkRight = drive.trajectorySequenceBuilder(stepThree.end())
                .back(7)
                .addDisplacementMarker(5,()->{slides.lowerToBottom(); intake.tiltForInit();})
                .lineToLinearHeading(new Pose2d(-12,-11,toRadians(90)))
                .build();
        TrajectorySequence parkLeft = drive.trajectorySequenceBuilder(stepThree.end())
                .back(7)
                .addDisplacementMarker(5,()->{slides.lowerToBottom(); intake.tiltForInit();})
                .lineToLinearHeading(new Pose2d(-60,-11,toRadians(90)))
                .build();

        TrajectorySequence parkCenter = drive.trajectorySequenceBuilder(stepThree.end())
                .back(8)
                .turn(toRadians(-135))
                .back(2)
                .addDisplacementMarker(6,()->{slides.lowerToBottom(); intake.tiltForInit();})
//                .lineToLinearHeading(new Pose2d(35,-11,toRadians(90)))
                .build();

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }
            telemetry.addData("target",slides.getTargetPosition());
            telemetry.addData("pos", slides.getPosition());
            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        if (isStopRequested() || !opModeIsActive()) return;


        /* Actually do something useful */
        if(tagOfInterest == null || tagOfInterest.id == LEFT){
            // Park Left
            parking_location = AutoRight.PARKING_LOCATION.LEFT;
        } else if(tagOfInterest.id == MIDDLE){
            // Park Middle
            parking_location = parking_location.MIDDLE;
        } else {
            // Park Right
            parking_location = parking_location.RIGHT;
        }


        if (isStopRequested()) return;
        // Start is now pressed!

        currentState = State.TRAJECTORY_1;
        slides.lowerToBottom();
        intake.open();
        drive.followTrajectorySequenceAsync(stepOne);


        // FSM
        while (opModeIsActive() && !isStopRequested()) {

            // Our state machine logic
            // We essentially define the flow of the state machine through this switch statement
            switch (currentState) {
                case TRAJECTORY_1:

                    // Drive to pole
                    if (!drive.isBusy()) {
                        currentState = State.TRAJECTORY_2;
                        slides.raiseToMiddle();
                        intake.tiltUp();
                        drive.followTrajectorySequenceAsync(stepOnePointFive);
                    }
                    break;
                case TRAJECTORY_2:

                    if (!drive.isBusy()) {
                        currentState = State.TRAJECTORY_3;
                        drive.followTrajectorySequenceAsync(stepTwo);
                    }
                    break;
                case TRAJECTORY_3:

                    if (!drive.isBusy()) {
                        intake.open();
                        currentState = State.TRAJECTORY_4;
                        drive.followTrajectorySequenceAsync(stepThree);

                    }
                    break;

                case TRAJECTORY_4:

                    if (!drive.isBusy()) {
                        intake.close();
                        currentState = State.TRAJECTORY_5;
                        drive.followTrajectorySequenceAsync(stepTwoSecondCone);
                    }
                    break;

                case TRAJECTORY_5:
                    if (!drive.isBusy()) {
                        intake.open();
                        currentState = State.TRAJECTORY_8;
                        drive.followTrajectorySequenceAsync(stepThree);
                    }
                    break;
//                case TRAJECTORY_6:
//                    if(!drive.isBusy()){
//                        intake.close();
//                        currentState = State.PARKING;
////                        drive.followTrajectorySequenceAsync(stepTwoThirdCone);
//                    }
//                    break;
//                case TRAJECTORY_7:
//                    if (!drive.isBusy()) {
//                        intake.open();
//                        currentState = State.TRAJECTORY_8;
//                        drive.followTrajectorySequenceAsync(stepThree);
//
//                    }
//                    break;

                case TRAJECTORY_8:
                    if (!drive.isBusy()) {

                        intake.close();
                        currentState = State.PARKING;

                        if(parking_location == AutoRight.PARKING_LOCATION.LEFT) {
                            drive.followTrajectorySequenceAsync(parkRight);
                        } else if (parking_location == AutoRight.PARKING_LOCATION.RIGHT){
                            drive.followTrajectorySequenceAsync(parkLeft);
                        } else{
                            drive.followTrajectorySequenceAsync(parkCenter);
                        }
                    }
                    break;
                case PARKING:
                    if(!drive.isBusy()){
                        currentState = State.IDLE;
                    }
                    break;

                case IDLE:
                    // Do nothing in IDLE
                    // currentState does not change once in IDLE
                    // This concludes the autonomous program
                    break;
            }

            // Things to keep running
            // We update drive continuously in the background, regardless of state
            drive.update();
            // We update our lift PID continuously in the background, regardless of state
            slides.moveTowardsGoal();

            // Read pose
            Pose2d poseEstimate = drive.getPoseEstimate();

            // Continually write pose
            VariableStorage.currentPose = poseEstimate;
            telemetry.addData("target",slides.getTargetPosition());
            telemetry.addData("pos", slides.getPosition());
            telemetry.addData("servo",intake.getPosition());
            telemetry.addData("state",currentState);
            telemetry.update();


        }
    }
    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}

