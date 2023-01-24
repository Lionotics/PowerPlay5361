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
                .setConstraints(30,20,toRadians(180),toRadians(180),15)
                .followTrajectorySequence(drive ->
//
//                        //  HIGH GOAL
//                        drive.trajectorySequenceBuilder(new Pose2d(38.8, -61.5, toRadians(90)))
//                                .forward(35)
//                                .splineTo(new Vector2d(29,-5),toRadians(135))
//                                .waitSeconds(1.5)
//                                .back(13)
//                                .turn(toRadians(-90))
//                                .splineTo(new Vector2d(60.5,-11.5),toRadians(0))
//                                .waitSeconds(1.5)
//                                .back(13)
//                                .turn(toRadians(90))
//                                .splineTo(new Vector2d(29,-5),toRadians(135))
//                                .waitSeconds(1.5)
//                                .build()

                                //  Medium GOAL
                                // Note - Should probably be rewritten so there are "forwards and backs" into poles / stack, will lead to more reliablity.
                                drive.trajectorySequenceBuilder(new Pose2d(35.25, -61.5, toRadians(90)))
                                        .forward(45)
                                        .lineToLinearHeading(new Pose2d(44.8,-20,toRadians(-45)))
                                        .waitSeconds(1.5)
                                        .back(10)
                                        .lineToLinearHeading(new Pose2d(60.5,-11.5,toRadians(0)))
                                        .waitSeconds(1.5)
                                        .lineToLinearHeading(new Pose2d(49,-20,toRadians(-135)))
                                        .waitSeconds(1.5)
                                        .lineToLinearHeading(new Pose2d(60.5,-11.5,toRadians(0)))
                                        .waitSeconds(1.5)
                                        .lineToLinearHeading(new Pose2d(49,-20,toRadians(-135)))
                                        .waitSeconds(1.5)
                                        .lineToLinearHeading(new Pose2d(60.5,-11.5,toRadians(0)))
                                        .waitSeconds(1.5)
                                        .build()

                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot.setDimensions(14,17))
                .start();
    }
}