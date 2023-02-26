package org.firstinspires.ftc.teamcode.auton;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@Config
public class AutoConstants {


    public static Pose2d initialPositionRight = new Pose2d(35.25, -61.5, toRadians(90));
    public static Pose2d highPoleRight = new Pose2d(33.5,-6.5,toRadians(135));
    public static double highPoleForward = 3.5;
    public static Pose2d stackPositionRight = new Pose2d(60.8,-9,toRadians(0));
    public static Pose2d cycleMidwayPoint = new Pose2d(38.4,-12.0,toRadians(0));
    public static double cycleForward = 11;
    public static Pose2d mediumPoleRight = new Pose2d();

    public static int firstConePos = -490;
    public static int secondConePos = -320;

}
