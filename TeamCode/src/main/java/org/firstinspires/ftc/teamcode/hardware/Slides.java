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
    // These are bogus values!
    public static double maxVel = 10;
    public static double maxAccel = 10;
    public static double maxJerk = 0;

    private MotionProfile profile;
    private DcMotorEx lift;

    private ElapsedTime timer;

    @Override
    public void init(HardwareMap hwMap) {
        lift = (DcMotorEx) hwMap.dcMotor.get("arm");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setTargetPosition(0);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.timer = new ElapsedTime();
        timer.reset();
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
    // Warning: Very untested and sketchy code below!
    public void setupMP(){
        timer.reset();
        this.profile = MotionProfileGenerator.generateSimpleMotionProfile(new MotionState(this.getPosition(),0),new MotionState(targetPosition,0), maxVel,maxAccel);
    }
    public void moveTowardsGoalMP(){
        MotionState state = this.profile.get(timer.seconds());
        BasicPID controller = new BasicPID(new PIDCoefficients(Kp, Ki, Kd));
        double power = controller.calculate(state.getX(), this.getPosition()) + Kg;
        lift.setPower(power);
    }





}


