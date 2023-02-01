package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; //AHUHIRI
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Button;
import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;
import org.firstinspires.ftc.robotcore.external.Telemetry;
@Disabled
@TeleOp(name = "TELEOP")

public class Teleop extends LinearOpMode {

    private enum SLIDES_STATE {
        AUTO_MOVE,
        MANUAL_MOVE,
        HOLDING
    };

    @Override
    public void runOpMode() throws InterruptedException {

        SLIDES_STATE slides_state = SLIDES_STATE.HOLDING;

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        PhotonCore.enable();

        //Mechanism setup
        Drivetrain drive = new Drivetrain();
        drive.init(hardwareMap);

        // Hardware mechanisms
        Slides slides = new Slides();
        slides.init(hardwareMap);

        Intake intake = new Intake();
        intake.init(hardwareMap);

        // Advanced button tracking
        Button gamepad1y = new Button(false);
        Button gamepad1a = new Button(false);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            gamepad1a.update(gamepad1.x);
            gamepad1y.update(gamepad1.y);

            drive.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


           /* switch (slides_state){
                case AUTO_MOVE:
                    if(!slides.isBusy()){
                        slides.setLiftMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        slides.setPower(0.1);
                    }
                    break;

                case MANUAL_MOVE:
                    if (!gamepad1.dpad_down || !gamepad1.dpad_up){
                        slides.hold();
                        slides_state = SLIDES_STATE.HOLDING;
                        }
                    break;

                case HOLDING:
                    if (gamepad1.dpad_up && slides.getPosition() < 3200){
                        slides.moveUp();
                        slides_state = SLIDES_STATE.MANUAL_MOVE;

                    } else if (gamepad1.dpad_down &&  slides.getPosition() > 100){
                        slides.moveDown();
                        slides_state = SLIDES_STATE.MANUAL_MOVE;

                    } if(gamepad1y.isNewlyPressed()){
                        slides.raiseToTop();
                        slides_state = SLIDES_STATE.AUTO_MOVE;
                    } else if(gamepad1a.isNewlyPressed()){
                        slides.lower();
                        slides_state = SLIDES_STATE.AUTO_MOVE;
                    }
                    break;
            } */

            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                if(Math.abs(slides.getPosition()) < 3100) {
                    slides.moveUp();
                }
            } else if (gamepad1.left_bumper || gamepad2.left_bumper) {
                if(Math.abs(slides.getPosition()) > 250) {
                    slides.moveDown();
                }
            } else{
                slides.hold();
            }

            if(gamepad1.right_trigger > 0.1 || gamepad2.right_trigger > 0.1){
                intake.close();
            } else if (gamepad1.left_trigger > 0.1 || gamepad2.left_trigger > 0.1){
              intake.open();
            }
            if(gamepad1.x){
                slides.moveDown();
            }

            //Telemetry
            telemetry.addData("Slides Position", slides.getPosition());
            telemetry.addData("SLides mode", slides.getRunmode());
            telemetry.addData("Slides target position",slides.getTargetPosition());
            telemetry.addData("Slides state", slides_state);
            telemetry.addData("isBusy",slides.isBusy());
            telemetry.update();

        }
    }
}
