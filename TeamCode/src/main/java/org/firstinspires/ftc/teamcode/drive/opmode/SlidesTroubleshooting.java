package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.Slides;

@TeleOp
public class SlidesTroubleshooting extends LinearOpMode {
    private DcMotor lift;
    public static double SLIDES_HOLDING_CURRENT = 0.1;
    public static int SLIDES_TOP_POS = 3200;
    public static double SLIDES_BOTTOM_POS = 100;

    @Override
    public void runOpMode() throws InterruptedException {

        lift = hardwareMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        telemetry.addData("status","raising");
        telemetry.addData("pos",lift.getCurrentPosition());
        telemetry.update();

        lift.setTargetPosition(-SLIDES_TOP_POS);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.8);

        telemetry.addData("status","holding");
        telemetry.addData("pos",lift.getCurrentPosition());

        telemetry.update();

        sleep(4000);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setPower(0.8);

        while(Math.abs(lift.getCurrentPosition()) > 20){
            telemetry.addData("Pos",lift.getCurrentPosition());
            telemetry.update();
        }

        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(2000);
        telemetry.addData("status","holding");
        telemetry.addData("pos",lift.getCurrentPosition());

        telemetry.update();
        sleep(2000);

    }


}
