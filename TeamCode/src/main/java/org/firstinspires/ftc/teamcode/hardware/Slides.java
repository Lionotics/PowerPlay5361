package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

// Class representing our vertical slides


@Config
public class Slides extends Mechanism{

    // Constants
    public static double SLIDES_HOLDING_CURRENT = 0.1;
    public static int SLIDES_TOP_POS = 3200;
    public static int SLIDES_BOTTOM_POS = 200;

    private DcMotor lift;

    @Override
    public void init(HardwareMap hwMap) {
        lift = hwMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        lift.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void hold(){
        lift.setPower(SLIDES_HOLDING_CURRENT);
    }

    public void setPower(double power){
        lift.setPower(power);
    }

    public int getPosition(){
        return lift.getCurrentPosition();
    }

    public void setMode(DcMotor.RunMode runMode)  {
            lift.setMode(runMode);
    }

    public DcMotor.RunMode getRunmode(){
        return lift.getMode();
    }


    public void moveUp() {
            lift.setPower(1);
    }

    public void moveDown() {
            lift.setPower(-1);
    }

    public void raiseToTop(){
        lift.setTargetPosition(SLIDES_TOP_POS);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.8);

    }
    public void lower(){
        lift.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setPower(0.8);

        while(Math.abs(lift.getCurrentPosition()) > 20){
        }

        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public int getTargetPosition() {
        return  lift.getTargetPosition();
    }
    public boolean isBusy(){return lift.isBusy();}
    public  void setTargetPosition (int a) {lift.setTargetPosition(a);}



}


