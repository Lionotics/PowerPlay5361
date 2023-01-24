package org.firstinspires.ftc.teamcode.hardware;

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

        Slides slides = new Slides();
        slides.init(hardwareMap);
        slides.raiseToTop();
        sleep(3000);
        slides.lowerToBottom();
        sleep(3000);

    }


}
