package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;

@TeleOp(name = "New Teleop - Not ready yet!")

public class TeleopRewrite extends LinearOpMode {

    private enum LIFT_STATE {
        AUTO_MOVE,
        MANUAL_UP,
        MANUAL_DOWN,
        HOLDING
    };
    private double loopTime = 0;


    @Override
    public void runOpMode() throws InterruptedException {

        // Dashboard telemetry
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        // Photon
        PhotonCore.enable();
        //Mechanism setup
        Drivetrain drive = new Drivetrain();
        drive.init(hardwareMap);
        // Hardware mechanisms
        Slides slides = new Slides();
        Intake intake = new Intake();
        slides.init(hardwareMap);
        intake.init(hardwareMap);

        // State of the lift
        LIFT_STATE lift_state = LIFT_STATE.HOLDING;
        // Wait for start
        if (isStopRequested()) return;

        // Loop ran durring teleop
        while (opModeIsActive()) {

            // Pass the stick inputs to the drivetrain class for driver oriented controls
            drive.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


            // Slides state machine
            switch (lift_state) {

                case HOLDING:
                    // Manual moving
                    slides.hold();
                    if((gamepad1.right_bumper) && Math.abs(slides.getPosition()) < slides.SLIDES_TOP_POS) {
                        slides.moveUp();
                        lift_state = LIFT_STATE.MANUAL_UP;
                    } else if ((gamepad1.left_bumper) && Math.abs(slides.getPosition()) > slides.SLIDES_BOTTOM_POS){
                        slides.moveDown();
                        lift_state = LIFT_STATE.MANUAL_DOWN;
                    }

                    // Transition to automatic moving

                    // Move to the top
                    if(gamepad1.a){
                        slides.raiseToTop();
                        lift_state = LIFT_STATE.AUTO_MOVE;
                    }

                    // Move to the bottom
                    if(gamepad1.b){
                        slides.lowerToBottom();
                        lift_state = LIFT_STATE.AUTO_MOVE;
                    }
                    break;

                case MANUAL_UP:
                    if(!gamepad1.right_bumper || Math.abs(slides.getPosition()) > slides.SLIDES_TOP_POS){
                        slides.hold();
                        lift_state = LIFT_STATE.HOLDING;
                    }
                    break;

                case MANUAL_DOWN:
                    if(!gamepad1.right_bumper || Math.abs(slides.getPosition()) < slides.SLIDES_BOTTOM_POS){
                        slides.hold();
                        lift_state = LIFT_STATE.HOLDING;
                    }
                    break;
                case AUTO_MOVE:
                    slides.moveTowardsGoal();

                    if(Math.abs(slides.getPosition() - slides.getTargetPosition()) < 10){
                        lift_state = LIFT_STATE.HOLDING;
                    }
                    break;

            }

            // Driver intake controls
            if(gamepad1.right_trigger > 0.1 || gamepad2.right_trigger > 0.1){
                intake.intake();
            } else if (gamepad1.left_trigger > 0.1 || gamepad2.left_trigger > 0.1){
                intake.drop();
            } else{
                intake.stop();
            }

            double loop = System.nanoTime();
            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            loopTime = loop;
            telemetry.update();

        }
    }
}
