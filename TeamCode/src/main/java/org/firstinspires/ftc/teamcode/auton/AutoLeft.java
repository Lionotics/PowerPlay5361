package org.firstinspires.ftc.teamcode.auton;

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
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Config
@Autonomous(name = "Auto - BLUE LEFT side")
public class AutoLeft extends LinearOpMode
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
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        Intake intake = new Intake();
        intake.init(hardwareMap);
//        Slides slides = new Slides();
//        slides.init(hardwareMap);

        intake.stop();

        PARKING_LOCATION parking_location = PARKING_LOCATION.LEFT;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-38.8, -61.5, toRadians(90)));



        TrajectorySequence stepOne = drive.trajectorySequenceBuilder(new Pose2d(-38.8, -61.5, toRadians(90)))
                .splineTo(new Vector2d(-16.8,-51.5),toRadians(90))
                .forward(9)
                .splineTo(new Vector2d(-12.8,-28.5), toRadians(45))
                .forward(2.5)

//                .lineToLinearHeading(new Pose2d(8.8,-30.5,toRadians(90+45)))
                .build();

        TrajectorySequence parkLeft = drive.trajectorySequenceBuilder(new Pose2d(-14.6,-26.8, toRadians(45)))
                .back(5)
                .turn(toRadians(45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
                .build();

        TrajectorySequence parkMiddle = drive.trajectorySequenceBuilder(new Pose2d(-14.6,-26.8, toRadians(45)))
                .back(5)
                .turn(toRadians(45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
//                                .strafeRight(20)
//                .splineTo(new Vector2d(35.5,-10),toRadians(90))
                .turn(toRadians(90))
                .forward(25)
                .turn(toRadians(-90))
                .build();

        TrajectorySequence parkRight = drive.trajectorySequenceBuilder(new Pose2d(-14.6,-26.8, toRadians(45)))                                   .back(5)
                .turn(toRadians(45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
                .turn(toRadians(90))
                .forward(45)
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
//        slides.raiseToTop();
        drive.followTrajectorySequence(stepOne);
//        slides.setLiftMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        slides.hold();
        intake.drop();
        sleep(1250);
        intake.stop();

        if(parking_location == PARKING_LOCATION.RIGHT){
            drive.followTrajectorySequence(parkRight);
        } else if (parking_location == PARKING_LOCATION.MIDDLE){
            drive.followTrajectorySequence(parkMiddle);
        }  else{
            drive.followTrajectorySequence(parkLeft);

        }
//        slides.lower();
        sleep(1000);





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
