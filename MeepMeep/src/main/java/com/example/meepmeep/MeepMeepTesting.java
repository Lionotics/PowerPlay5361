package com.example.meepmeep;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(45,30,toRadians(60),toRadians(60),15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(38.8, -61.5, toRadians(90)))
                                .splineTo(new Vector2d(38.8-22,-61.5+10),toRadians(90))
                                .forward(9)
                                .splineTo(new Vector2d(8.8+2,-30.5+2), toRadians(90+45))
                                .forward(0.3)
                                .waitSeconds(2)
                                .back(2)
                                .turn(toRadians(-45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                                .forward(20)
//                                .strafeRight(20)
//                                .splineTo(new Vector2d(35.5,-10),toRadians(90))
                                .turn(toRadians(-90))
                                .forward(45)
                                .turn(toRadians(90))



//                                .forward(-5)
//                                .lineToLinearHeading(new Pose2d(11,-10,toRadians(0)))
//                                .lineToLinearHeading(new Pose2d(62,-12,toRadians(0)))
//                                .waitSeconds(2)
//                                .forward(-20)
//                                .lineToLinearHeading(new Pose2d(29, -5, toRadians(135)))
//                                .waitSeconds(2)
//                                // 1
////                                .lineToLinearHeading(new Pose2d(55,-10,toRadians(90)))
//                                // 2
////                                .lineToLinearHeading(new Pose2d(35,-10,toRadians(90)))
//                                // 3
//                                .forward(-10)
//                                .lineToLinearHeading(new Pose2d(60,-10,toRadians(90)))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}