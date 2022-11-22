package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

// Class representing our vertical slides

@Config
public class Slides extends Mechanism{

    public static double SLIDES_HOLDING_CURRENT = 0.1;
    public static double SLIDES_TOP_POS = -3300;
    public static double SLIDES_BOTTOM_POS = -100;


    private DcMotor lift;
    private int moveDirection = 0; // 0 equals not moving, 1 equals up, 2 equals down

    @Override
    public void init(HardwareMap hwMap) {
        lift = hwMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void hold(){
        moveDirection = 0;
        lift.setPower(SLIDES_HOLDING_CURRENT);
    }

    public void setPower(double power){
        lift.setPower(power);
    }

    public int getPosition(){
        return lift.getCurrentPosition();
    }

    public void setLiftMode(DcMotor.RunMode runMode)  {
            lift.setMode(runMode);
    }

    public DcMotor.RunMode getRunmode(){
        return lift.getMode();
    }


    public void moveUp() {
        if (lift.getCurrentPosition() > SLIDES_TOP_POS) {
            moveDirection = 1;
            lift.setPower(1);
        }
    }

    public void moveDown() {
        if (lift.getCurrentPosition() < SLIDES_BOTTOM_POS) {
            moveDirection = 2;
            lift.setPower(-1);
        }
    }
    public void raise(){
        moveDirection = 1;
        lift.setTargetPosition(2900);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(1);
    }
    public void lower(){
        moveDirection = 2;
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(1);

    }

    public int getTargetPosition() {
        return  lift.getTargetPosition();
    }
    public int getMoveDirection() {
        return moveDirection; 
    }
    



}


