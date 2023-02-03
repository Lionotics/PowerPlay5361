package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;

@TeleOp(name = "Manual Testing Opmode. Don't use it.")
public class FullManualTeleop extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        // Photon
        PhotonCore.enable();
        //Mechanism setup
        // Note: This uses the RoadRunner sampleMechanumDrive for drivetrain rather than the drivetrain class.
        // Plan accordingly. This is done for use of the localizer for auto tuning.
//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Hardware mechanisms
        Slides slides = new Slides();
        Intake intake = new Intake();
        slides.init(hardwareMap);
        intake.init(hardwareMap);
        DcMotorEx fr,fl,br,bl;
        fl = hardwareMap.get(DcMotorEx.class, "leftFront");
        bl = hardwareMap.get(DcMotorEx.class, "leftBack");
        br = hardwareMap.get(DcMotorEx.class, "rightBack");
        fr = hardwareMap.get(DcMotorEx.class, "rightFront");
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Warning! This Opmode has no hardware protections! Be careful!");
        telemetry.addLine("BE CAREFUL YOU NERD");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;

        // Loop ran durring teleop
        while (opModeIsActive()) {
            // Driving

            fl.setPower(0.5);
            bl.setPower(0.5);
            br.setPower(0.5);
            fr.setPower(0.5);




            if(gamepad1.right_bumper){
                slides.moveUp();
            } else if (gamepad1.left_bumper){
                slides.moveDown();
            } else{
                slides.hold();
            }

            if(gamepad1.x){
                slides.moveTowardsGoal();
            }

            if(gamepad1.a) {
                intake.open();
            } else if (gamepad1.b){
                intake.close();
            }
//            } else if (gamepad1.left_trigger > 0.1 || gamepad2.left_trigger > 0.1){
//                intake.drop();
//            }

//            Pose2d poseEstimate = drive.getPoseEstimate();
//            telemetry.addData("x", poseEstimate.getX());
//            telemetry.addData("y", poseEstimate.getY());
//            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("Slides position",slides.getPosition());
            telemetry.addData("Target",slides.getTargetPosition());
            telemetry.update();

        }


    }
}
