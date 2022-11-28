package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; //AHUHIRI
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;

@TeleOp(name = "TELEOP")





public class TestingDriving extends LinearOpMode {

    enum SLIDES_STATE {
        AUTO_MOVE,
        MANUAL_MOVE,
        HOLDING
    };


    @Override
    public void runOpMode() throws InterruptedException {

        SLIDES_STATE slides_state = SLIDES_STATE.HOLDING;

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        // Declare our motors
        // Make sure your ID's match your configuration
        PhotonCore.enable();

        //Mechanism setup
        Drivetrain drive = new Drivetrain();
        drive.init(hardwareMap);

        // mechanism
        Slides slides = new Slides();
        slides.init(hardwareMap);

        Intake intake = new Intake();
        intake.init(hardwareMap);



        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            drive.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


            switch (slides_state){
                case AUTO_MOVE:
                    if(!slides.isBusy()){
                        slides.setLiftMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        slides.setPower(0.1);
                    }
                    break;

                case MANUAL_MOVE:
                    if (!gamepad1.dpad_down || gamepad1.dpad_up){
                        slides.hold();
                        slides_state = SLIDES_STATE.HOLDING;
                        }
                    break;

                case HOLDING:
                    if (gamepad1.dpad_up && Math.abs(slides.getPosition()) <= 2800){
                        slides.moveUp();
                        slides_state = SLIDES_STATE.MANUAL_MOVE;

                    } else if (gamepad1.dpad_down &&  Math.abs(slides.getPosition()) >= 100){
                        slides.moveDown();
                        slides_state = SLIDES_STATE.MANUAL_MOVE;

                    } else if(gamepad1.y){
                        slides.raiseToTop();
                        slides_state = SLIDES_STATE.AUTO_MOVE;

                    } else if(gamepad1.a){
                        slides.lower();
                        slides_state = SLIDES_STATE.AUTO_MOVE;
                    }
                    break;
            }

            if(gamepad1.x){
                intake.intake();
            } else if (gamepad1.b){
              intake.drop();
            } else{
                intake.stop();
            }

            //Telemetry
            telemetry.addData("Slides Position", slides.getPosition());
            telemetry.addData("SLides mode", slides.getRunmode());
            telemetry.addData("Slides target position",slides.getTargetPosition());
            telemetry.addData("Slides state", slides_state);
            telemetry.update();

        }
    }
}
