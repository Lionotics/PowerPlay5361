package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake extends Mechanism{

    private CRServo servo1, servo2;

    @Override
    public void init(HardwareMap hwMap) {
        servo1 = hwMap.crservo.get("servo1");
        servo2 = hwMap.crservo.get("servo2");

    }

    public void intake(){
        servo1.setPower(-1);
        servo2.setPower(1);
    }
    public void stop(){
        servo1.setPower(-0.1);
        servo2.setPower(0.1);
    }
    public void drop(){
        servo1.setPower(1);
        servo2.setPower(-1);
    }
    public void stopForReal(){

        servo1.setPower(0);
        servo2.setPower(0);
    }}
