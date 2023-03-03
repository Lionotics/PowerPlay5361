package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.Slides;

//@TeleOp(name = "New Teleop")
@TeleOp(name = "Teleop")
@Config

public class NewTeleop extends LinearOpMode {
    public static int THRESHOLD = 15;
    private double loopTime = 0;

    private double maxVel = 0;
    private int slidesPos;

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
        waitForStart();
        if (isStopRequested()) return;


        // Loop ran durring teleop
        while (opModeIsActive()) {
            // Put all hardware reads here
            slidesPos = slides.getPosition();


            // Pass the stick inputs to the drivetrain class for driver oriented controls
            drive.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            // Slides state machine
            switch (lift_state) {

                case HOLDING:
                    // Manual moving
                    slides.hold();
                    if (gamepad2.dpad_up || gamepad1.y) {
                        lift_state = LIFT_STATE.MANUAL_UP;
                    } else if (gamepad2.dpad_down || gamepad1.x) {
                        lift_state = LIFT_STATE.MANUAL_DOWN;
                    } else if (gamepad1.right_bumper || gamepad2.y) {
                        // Transition to automatic moving
                        // Move to the top
                        // RaiseToTop sets the target position, and then by transitioning the state the PID controller will run.
                        slides.raiseToTop();
                        lift_state = LIFT_STATE.AUTO_MOVE;
                        slides.setupMP();
                    } else if (gamepad1.left_bumper || gamepad2.a) {
                        // or move to the bottom automatically
                        slides.lowerToBottom();
                        intake.close();

                        lift_state = LIFT_STATE.AUTO_MOVE;
                        slides.setupMP();

                    } else if (gamepad1.dpad_left || gamepad2.x) {
                        slides.raiseToLow();

                        lift_state = LIFT_STATE.AUTO_MOVE;
                        slides.setupMP();

                    } else if (gamepad1.dpad_right || gamepad2.b) {
                        slides.raiseToMiddle();
                        lift_state = LIFT_STATE.AUTO_MOVE;
                        slides.setupMP();

                    }
                    break;

                case MANUAL_UP:
                    if ((!gamepad1.y && !gamepad2.dpad_up) || slidesPos < Slides.SLIDES_TOP_POS) {
                        lift_state = LIFT_STATE.HOLDING;
                    } else {
                        slides.moveUp();
                    }
                    break;

                case MANUAL_DOWN:
                    if ((!gamepad2.dpad_down && !gamepad1.x) || slidesPos > Slides.SLIDES_BOTTOM_POS) {
                        lift_state = LIFT_STATE.HOLDING;
                    } else {
                        slides.moveDown();
                    }
                    break;
                case AUTO_MOVE:
                    slides.moveTowardsGoal();
                    if (gamepad2.dpad_down || gamepad1.x) {
                        lift_state = LIFT_STATE.MANUAL_DOWN;
                    } else if (gamepad2.dpad_up || gamepad1.y) {
                        lift_state = LIFT_STATE.MANUAL_DOWN;
                    }

                    if (Math.abs(slidesPos - slides.getTargetPosition()) < THRESHOLD) {
                        // TODO: Tune the sensitivity of that
                        lift_state = LIFT_STATE.HOLDING;
                    }

                    if (slides.getPosition() < Slides.SLIDES_TILT_SWITCH_POS) {
                        intake.tiltUp();

                    } else {
                        intake.tiltDown();
                    }
                    break;

            }

            // Driver intake controls
            if (gamepad1.right_trigger > 0.1) {
                intake.open();
            } else if (gamepad1.left_trigger > 0.1 ) {
                intake.close();
            }
            if (gamepad1.b) {
                intake.tiltUp();
            } else if (gamepad1.a) {
                intake.tiltDown();
            }


            if (slides.getVelocity() > maxVel) {
                maxVel = slides.getVelocity();
            }

            telemetry.addData("state", lift_state);
            telemetry.addData("Slides position", slidesPos);
            telemetry.addData("Target", slides.getTargetPosition());
            telemetry.addData("Slides power", slides.getCurrentPower());
            telemetry.addData("heading", drive.getHeading());
            telemetry.addData("MaxVel", maxVel);
            telemetry.addData("Velocity", slides.getVelocity());
            telemetry.addData("pos", intake.getPosition());


            double loop = System.nanoTime();
            telemetry.addData("hz ", 1000000000 / (loop - loopTime));
            loopTime = loop;

            telemetry.update();

        }
        drive.resetHeading();
    }


    private enum LIFT_STATE {
        AUTO_MOVE,
        MANUAL_UP,
        MANUAL_DOWN,
        HOLDING
    }
}
