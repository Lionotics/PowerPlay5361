package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.hardware.Slides.SLIDES_TOP_POS;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

@Config
@Autonomous(name = "Auto - Right side")
public class AutoRight extends LinearOpMode
{
    // Create a RobotHardware object to be used to access robot hardware.
    // Prefix any hardware functions with "robot." to access this class.
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


    @Override
    public void runOpMode()
    {

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

        Intake intake = new Intake();
        intake.init(hardwareMap);

        Slides slides = new Slides();
        slides.init(hardwareMap);

        PARKING_LOCATION parking_location = PARKING_LOCATION.LEFT;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(35.25, -61.5, toRadians(90)));

        TrajectorySequence stepOne = drive.trajectorySequenceBuilder(new Pose2d(35.25, -61.5, toRadians(90)))
                .forward(40)
                .splineTo(new Vector2d(33,-7),toRadians(135))
                .build();
        TrajectorySequence stepOnePointFive = drive.trajectorySequenceBuilder(stepOne.end())
                .forward(2.5)
                .build();



        TrajectorySequence stepTwo = drive.trajectorySequenceBuilder(stepOnePointFive.end())
                // 33.3
                .back(13)
                .turn(toRadians(-135))
                .lineToSplineHeading(new Pose2d(61,-9,toRadians(0)))
                .build();
        TrajectorySequence stepThree = drive.trajectorySequenceBuilder(stepTwo.end())
                .lineToSplineHeading(new Pose2d(38.3,-12.0,toRadians(0)))
                .turn(toRadians(135))
                .forward(9)
                .build();
        TrajectorySequence stepFour = drive.trajectorySequenceBuilder(stepThree.end())
                .back(24.5)
                .turn(toRadians(135))
                .forward(6.7)
                .build();

           TrajectorySequence parkLeft = drive.trajectorySequenceBuilder(stepFour.end())
                   .back(7.2)
                   .turn(toRadians(45))
                   .forward(24)
                   .turn(toRadians(-90))
                   .build();
        TrajectorySequence parkRight = drive.trajectorySequenceBuilder(stepFour.end())
                .back(7)
                .turn(toRadians(45))
                .back(14)
                .turn(toRadians(-90))
                .strafeRight(10)

                .build();
        TrajectorySequence parkCenter = drive.trajectorySequenceBuilder(stepFour.end())
                .back(7)
                .turn(toRadians(45))
                .turn(toRadians(-90))
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
            parking_location = PARKING_LOCATION.LEFT;
            // Move left for testing
//            robot.driveRobot(-1, 1, 1, -1);
        } else if(tagOfInterest.id == MIDDLE){
            // Park Middle
            parking_location = parking_location.MIDDLE;
        } else {
            // Park Right
            parking_location = parking_location.RIGHT;
        }
        // close claw and raise slides a bit
        intake.open();
        sleep(250);
        slides.setTarget(-100);
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        // move to top pole
        drive.followTrajectorySequence(stepOne);
        // slides up
        slides.raiseToTop();
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 30){
            slides.moveTowardsGoal();
            telemetry.addData("pos",slides.getPosition());
            telemetry.addData("target",slides.getTargetPosition());
            telemetry.update();
        }
        slides.hold();
        // finish moving and drop
        drive.followTrajectorySequence(stepOnePointFive);
        intake.close();
        sleep(500);
        // move to stack
        drive.followTrajectorySequence(stepTwo);
        // slides down
        slides.setTarget(-430);
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        sleep(250);
        intake.open();
        sleep(600);
        // and up
        slides.raiseToTop();
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 30){
            slides.moveTowardsGoal();
            telemetry.addData("pos",slides.getPosition());
            telemetry.addData("target",slides.getTargetPosition());
            telemetry.update();
        }
        slides.hold();
        // back to top pole
        drive.followTrajectorySequence(stepThree);
        sleep(500);
        intake.close();
        sleep(250);

        // move to stack again!
        drive.followTrajectorySequence(stepTwo);
        // slides down
        // TODO: tune height for second cone
        slides.setTarget(-430);
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        sleep(250);
        intake.open();
        sleep(600);
        // and up
        slides.raiseToTop();
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 30){
            slides.moveTowardsGoal();
            telemetry.addData("pos",slides.getPosition());
            telemetry.addData("target",slides.getTargetPosition());
            telemetry.update();
        }
        slides.hold();
        // back to top pole
        drive.followTrajectorySequence(stepThree);
        sleep(500);
        intake.close();
        sleep(250);




////        drive.followTrajectorySequence(parkLeft);
//
//
//        // cycles
//        drive.followTrajectorySequence(stepTwo);
//        slides.setTarget(-480);
//        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
//            slides.moveTowardsGoal();
//        }
//        slides.hold();
//        drive.followTrajectorySequence(stepThree);
//        intake.open();
//        sleep(500);
//        slides.raiseToTop();
//        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
//            slides.moveTowardsGoal();
//        }
//        slides.hold();
//        drive.followTrajectorySequence(stepFour);
//        intake.close();
//        sleep(500);
//        // Parking!
//        if (parking_location == PARKING_LOCATION.LEFT){
//            drive.followTrajectorySequence(parkLeft);
//        } else if (parking_location == PARKING_LOCATION.RIGHT){
//            drive.followTrajectorySequence(parkRight);
//        } else{
//            drive.followTrajectorySequence(parkCenter);
//        }
//
//
//        slides.lowerToBottom();
//        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
//            slides.moveTowardsGoal();
//        }

        // Save state for teleop
        VariableStorage.currentPose = drive.getPoseEstimate();
        VariableStorage.angle = drive.getRawExternalHeading();
        VariableStorage.slidesPos = slides.getPosition();

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
