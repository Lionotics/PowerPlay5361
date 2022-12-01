package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.Slides;

@TeleOp
public class SlidesTroubleshooting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Slides slides = new Slides();
        slides.init(hardwareMap);
        slides.setTargetPosition(1000);
        slides.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(0.8);
        while(slides.isBusy()){
            sleep(20);
            telemetry.addData("target position",slides.getTargetPosition());
            telemetry.update();
        }
    }
}
