package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;
@TeleOp
@Disabled
public class EncoderTesting extends LinearOpMode {

    DcMotorEx test;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        test = hardwareMap.get(DcMotorEx.class,"leftBack");
        test.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while(opModeIsActive()){

            telemetry.addData("position",test.getCurrentPosition());
            telemetry.update();
        }

    }
}
