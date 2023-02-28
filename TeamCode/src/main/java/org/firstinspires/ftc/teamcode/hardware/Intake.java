package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake extends Mechanism{

    private Servo claw, tilt;
    // Positions for the claw
    public static double OPEN_POSITION = 0.08;
    public static double CLOSE_POSITION = 0.31;
    public static double INIT_POSITION = 0.4;
    // Positions for claw tilt
    public static double TILT_DOWN = 0.35;
    public static double TILT_UP = 0.2;

    @Override
    public void init(HardwareMap hwMap) {
        // tilt it set on init, make sure to put a moves on init sticker!
        claw = hwMap.servo.get("claw");
        tilt = hwMap.servo.get("tilt");
        tilt.setPosition(TILT_DOWN);
    }

    public void close(){
            claw.setPosition(CLOSE_POSITION);
    }
    public void open(){
        claw.setPosition(OPEN_POSITION);
    }
    public void moveForInit(){claw.setPosition(INIT_POSITION);}
    public double getPosition(){return claw.getPosition();}

    public void tiltUp(){
        tilt.setPosition(TILT_UP);
    }
    public void tiltDown(){
        tilt.setPosition(TILT_DOWN);
    }
}

