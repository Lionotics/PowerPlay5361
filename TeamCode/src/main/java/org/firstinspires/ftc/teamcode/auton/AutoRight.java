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
                .forward(52)
                .turn(toRadians(45))
                .forward(6)
                .build();

        TrajectorySequence stepTwo = drive.trajectorySequenceBuilder(new Pose2d(36.8, -6.5, toRadians(135)))
                // 33.3
                .back(7)
                .turn(toRadians(-135))
                .forward(20)
                .build();
        TrajectorySequence stepThree = drive.trajectorySequenceBuilder(new Pose2d(60.3,-10,toRadians(0)))
                .forward(4.5)
                .build();
        TrajectorySequence stepFour = drive.trajectorySequenceBuilder(new Pose2d(64.8,-10,toRadians(0)))
                .back(25)
                .turn(toRadians(135))
                .forward(7)
                .build();


        intake.open();


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
        slides.raiseToTop();
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        drive.followTrajectorySequence(stepOne);
        intake.close();
        sleep(1000);
        drive.followTrajectorySequence(stepTwo);
        slides.setTarget(-420);
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        drive.followTrajectorySequence(stepThree);
        intake.open();
        sleep(500);
        slides.raiseToTop();
        while(Math.abs(slides.getPosition() - slides.getTargetPosition()) > 15){
            slides.moveTowardsGoal();
        }
        slides.hold();
        drive.followTrajectorySequence(stepFour);
        intake.close();
        sleep(1000);





//        // Save state for teleop
//        VariableStorage.currentPose = drive.getPoseEstimate();
//        VariableStorage.angle = drive.getRawExternalHeading();
//        VariableStorage.slidesPos = slides.getPosition();

    }

}
