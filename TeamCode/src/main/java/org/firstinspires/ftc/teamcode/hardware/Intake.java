package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config

public class Intake extends Mechanism{

    private Servo claw;
    public static double OPEN_POSITION = 0.18;
    public static double CLOSE_POSITION = 0.43;
    public static double INIT_POSITION = 0.48;
    @Override
    public void init(HardwareMap hwMap) {
        claw = hwMap.servo.get("claw");

    }

    public void close(){
            claw.setPosition(CLOSE_POSITION);
    }

    public void open(){
        claw.setPosition(OPEN_POSITION);
    }
    public void moveForInit(){claw.setPosition(INIT_POSITION);}
}
