package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.VariableStorage;

@Config
public class Drivetrain extends Mechanism{

    DcMotor motorFrontLeft,motorBackLeft,motorFrontRight,motorBackRight;
    IMU imu;
    public static double SPEED_MULTIPLIER = 0.9;
    private double offset = 0;

    @Override
    public void init(HardwareMap hwMap) {
         motorFrontLeft = hwMap.dcMotor.get("leftFront");
         motorBackLeft = hwMap.dcMotor.get("leftBack");
         motorFrontRight = hwMap.dcMotor.get("rightFront");
         motorBackRight = hwMap.dcMotor.get("rightBack");

        // Reverse the left side motors
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        // default behaviour is to brake
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        offset = VariableStorage.angle;

        // Retrieve the IMU from the hardware map
        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));

        imu.initialize(parameters);
        imu.resetYaw();

    }
    public void resetHeading(){
        imu.resetYaw();
    }
    public double getHeading(){
        return  -Math.toDegrees(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
    }

    public void drive(double left_stick_y,double left_stick_x, double right_stick_x){
        double y = -left_stick_y; // Remember, this is reversed!
        double x = left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = right_stick_x;

        // Read inverse IMU heading, as the IMU heading is CW positive
        double botHeading =  -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        motorFrontLeft.setPower(frontLeftPower * SPEED_MULTIPLIER);
        motorBackLeft.setPower(backLeftPower * SPEED_MULTIPLIER);
        motorFrontRight.setPower(frontRightPower * SPEED_MULTIPLIER);
        motorBackRight.setPower(backRightPower * SPEED_MULTIPLIER);

    }


}
