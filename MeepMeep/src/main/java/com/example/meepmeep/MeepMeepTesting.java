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
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40,40,toRadians(180),toRadians(180),15)
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
                                drive.trajectorySequenceBuilder(new Pose2d(-35.25, -61.5, toRadians(90)))
                                        .forward(20)
                                        .splineTo(new Vector2d(-31,-30),toRadians(45))
                                        .forward(2)
                                        .lineToLinearHeading(new Pose2d(-39,-35,toRadians(90)))
                                        .forward(20)
                                        .splineTo(new Vector2d(-50,-10),toRadians(180))
                                        .forward(12.1)
                                        .back(13)
                                        .lineToLinearHeading(new Pose2d(-34.5,-15,toRadians(-45)))
                                        .waitSeconds(0.2)
                                        .forward(3.5)
                                        .back(10)
                                        .lineToSplineHeading(new Pose2d(-62.1,-10,toRadians(180)))




                                        // First cone


//
//                                        // cycle #2
//                                        .back(10)
//                                        .lineToSplineHeading(new Pose2d(61.1,-10,toRadians(0)))
//                                        .waitSeconds(0.2)
//
//                                        .waitSeconds(0.5)
//                                        .back(13)
//                                        .lineToLinearHeading(new Pose2d(33,-15,toRadians(-135)))
//                                        .forward(2.7)
//                                        .waitSeconds(0.3)
//
//                                        // park
//                                        .back(8)
//                                        .lineToLinearHeading(new Pose2d(11,-11,toRadians(90)))
//                                        .back(4)

//
//                                        // Cycles
///                                        .waitSeconds(0.5)
//                                        // Note: this is the one I guessed on.
//                                        .lineToLinearHeading(new Pose2d(-33.5,-15,toRadians(-45)))
//                                        .waitSeconds(0.5)
//
//                                        // Cycles
//                                        .lineToLinearHeading(new Pose2d(-60.8,-9,toRadians(180)))
//                                        .waitSeconds(0.5)
//                                        // Note: this is the one I guessed on.
//                                        .lineToLinearHeading(new Pose2d(-33.5,-15,toRadians(-45)))
//                                        .waitSeconds(0.5)



                                        .waitSeconds(0.2)


                                        .build()

                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot.setDimensions(17,17))
                .start();
    }
}