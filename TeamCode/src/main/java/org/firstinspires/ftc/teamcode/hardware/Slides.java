package org.firstinspires.ftc.teamcode.hardware;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.VariableStorage;

// Class representing our vertical slides
// This version uses PIDF control for the slides, it's not perfect but it works pretty well

@Config
public class Slides extends Mechanism{

    // Constants
    public static double SLIDES_HOLDING_CURRENT = 0.09;
    public static int SLIDES_TOP_POS = -2650;
    public static int SLIDES_HIGH_POS = -2600;
    public static int SLIDES_BOTTOM_POS = 0;
    public static int SLIDES_LOW_POS = -870;
    public static int SLIDES_MIDDLE_POS = -1765;

    public static int SLIDES_TILT_SWITCH_POS = -700;

    public static int targetPosition = 0;

    // PID + mp
    // Not the best tune ever, but could use some work
    public static double Kg = 0;
    public static double Kp = 0.006;
    public static double Ki = 0;
    public static double Kd = 0.001;
    // Not currently using MP, tbd
    public static double maxVel = 500;
    public static double maxAccel = 3000;
    public static double mpTarget = 0;

    private DcMotorEx lift;

    private ElapsedTime timer;
    private int offset = 0;
    private double currentPower = 0;
    private MotionProfile profile;
    private BasicPID controller = new BasicPID(new PIDCoefficients(Kp, Ki, Kd));

    @Override
    public void init(HardwareMap hwMap) {
        lift = (DcMotorEx) hwMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        offset = VariableStorage.slidesPos;
        this.timer = new ElapsedTime();
        timer.reset();
    }

    public void setPower(double power){
        currentPower = power;
        lift.setPower(power);
    }

    public void hold(){
        this.setPower(SLIDES_HOLDING_CURRENT);
    }

    public void moveUp() {
        currentPower = 1;
        this.setPower(1);
    }

    public void moveDown() {
        currentPower = -1;
        this.setPower(-1);
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

    public int getTargetPosition() {
        return targetPosition;
    }

    public boolean isBusy(){return lift.isBusy();}

    public void setTarget (int a) {targetPosition = a;}
    public double getCurrentPower(){
        return currentPower;
    }
    public void raiseToTop(){
        this.setTarget(SLIDES_TOP_POS);
    }
    public void lowerToBottom(){
        this.setTarget(SLIDES_BOTTOM_POS);
    }
    public void raiseToLow(){this.setTarget(SLIDES_LOW_POS);}
    public void raiseToMiddle(){this.setTarget(SLIDES_MIDDLE_POS);}
    public double getVelocity(){return lift.getVelocity();}

    public void moveTowardsGoal(){
        double power = -(controller.calculate(targetPosition, this.getPosition()) + Kg);
        currentPower = power;
        this.setPower(power);
    }

    // Warning: Very untested and sketchy code below!
    public void setupMP(){
        timer.reset();
        this.profile = MotionProfileGenerator.generateSimpleMotionProfile(new MotionState(this.getPosition(),0),new MotionState(targetPosition,0), maxVel,maxAccel);
    }
    public void moveTowardsGoalMP(){
        BasicPID controller = new BasicPID(new PIDCoefficients(Kp, Ki, Kd));
        MotionState state = this.profile.get(timer.seconds());
        double power = -controller.calculate(state.getX(), this.getPosition()) + Kg;
        this.setPower(power);
    }





}


