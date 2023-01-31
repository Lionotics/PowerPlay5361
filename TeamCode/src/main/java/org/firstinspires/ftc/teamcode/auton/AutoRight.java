package org.firstinspires.ftc.teamcode.auton;

import static java.lang.Math.getExponent;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.teamcode.hardware.Vision;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.VariableStorage;

@Config
@Autonomous(name = "Auto - Right side")
public class AutoRight extends LinearOpMode
{
   public enum PARKING_LOCATION{
      LEFT,
      MIDDLE,
      RIGHT
    };

    @Override
    public void runOpMode()
    {

        PhotonCore.enable();

        telemetry.setMsTransmissionInterval(50);

        Intake intake = new Intake();
        intake.init(hardwareMap);

        Slides slides = new Slides();
        slides.init(hardwareMap);

        Vision vision = new Vision();
        vision.init(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d(38.8, -61.5, toRadians(90)));
        PARKING_LOCATION parking_location = PARKING_LOCATION.LEFT;

        TrajectorySequence stepOne = drive.trajectorySequenceBuilder(new Pose2d(38.8, -61.5, toRadians(90)))
                .splineTo(new Vector2d(15.8,-51.5),toRadians(90))
                .forward(9)
                .splineTo(new Vector2d(11.8,-29.5), toRadians(135))
                .forward(2.5)

//                .lineToLinearHeading(new Pose2d(8.8,-30.5,toRadians(90+45)))
                .build();

        TrajectorySequence parkLeft = drive.trajectorySequenceBuilder(new Pose2d(14.6,-26.8, toRadians(135)))
                .back(5)
                .turn(toRadians(-45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
                .build();

        TrajectorySequence parkMiddle = drive.trajectorySequenceBuilder(new Pose2d(14.6,-26.8, toRadians(135)))
                .back(5)
                .turn(toRadians(-45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
//                                .strafeRight(20)
//                .splineTo(new Vector2d(35.5,-10),toRadians(90))
                .turn(toRadians(-90))
                .forward(25)
                .turn(toRadians(90))
                .build();

        TrajectorySequence parkRight = drive.trajectorySequenceBuilder(new Pose2d(14.6,-26.8, toRadians(135)))
                .back(5)
                .turn(toRadians(-45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                .forward(20)
                .turn(toRadians(-90))
                .forward(45)
                .turn(toRadians(90))
                .build();






        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Apriltag detected:",vision.getSide());
            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */
        double latestTag = vision.getSide();

        /* Actually do something useful */
        if(latestTag == 0 || latestTag == vision.LEFT){

            // Park Left
            parking_location = PARKING_LOCATION.LEFT;

        } else if(latestTag == vision.MIDDLE){
            // Park Middle
            parking_location = parking_location.MIDDLE;
        } else {
            // Park Right
            parking_location = parking_location.RIGHT;
        }

        sleep(500);
        slides.raiseToTop();
        drive.followTrajectorySequence(stepOne);
        slides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slides.hold();
        intake.open();
        sleep(1250);

        if(parking_location == PARKING_LOCATION.RIGHT){
            drive.followTrajectorySequence(parkRight);
        } else if (parking_location == PARKING_LOCATION.MIDDLE){
            drive.followTrajectorySequence(parkMiddle);
        }  else{
            drive.followTrajectorySequence(parkLeft);

        }
        slides.lowerToBottom();
        sleep(3000);

        // Save state for teleop
        VariableStorage.currentPose = drive.getPoseEstimate();
        VariableStorage.angle = drive.getRawExternalHeading();
        VariableStorage.slidesPos = slides.getPosition();

    }

}
