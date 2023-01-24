package org.firstinspires.ftc.teamcode.hardware;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

// Class representing our vertical slides

// This is an experiemntel version with PIDF control, we'll see how it works

@Config
public class Slides extends Mechanism{

    // Constants
    public static double SLIDES_HOLDING_CURRENT = 0.1;
    public static int SLIDES_TOP_POS = 3200;
    public static int SLIDES_BOTTOM_POS = 0;

    public static int targetPosition = 0;

    // TODO: Tune these
    public static double Kg = 0.1;
    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;


    private DcMotor lift;

    @Override
    public void init(HardwareMap hwMap) {
        lift = hwMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

    public int getTargetPosition() {
        return targetPosition;
    }

    public boolean isBusy(){return lift.isBusy();}

    public void setTarget (int a) {targetPosition = a;}

    public void raiseToTop(){
        this.setTarget(SLIDES_TOP_POS);
    }
    public void lowerToBottom(){
        this.setTarget(SLIDES_BOTTOM_POS);
    }

    public void moveTowardsGoal(){
        BasicPID controller = new BasicPID(new PIDCoefficients(Kp, Ki, Kd));
        double power = controller.calculate(targetPosition, this.getPosition()) + Kg;
        lift.setPower(power);
    }





}


