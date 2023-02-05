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
                .setConstraints(50,30,toRadians(180),toRadians(180),15)
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
                                        .forward(64)
                                        .back(12)
                                        .turn(toRadians(-42))
                                        .forward(5.5)
                                        .back(6.5)
                                        .turn(toRadians(132))
                                        .forward(20)
                                        .forward(4)
                                        .back(24)
                                        .turn(toRadians(-132))
                                        .forward(6.5)
                                        // PARKING
                                        .back(7)
                                        // right
                                         .turn(toRadians(-48))
                                        .forward(  25)
                                        // LEFT
//                                        .turn(toRadians(-48))
//                                        .back(25)
                                        // CENTER
//                                        .turn(toRadians(-48))


                                        // Preload
//                                        .strafeRight(52)
//                                        .turn(toRadians(-45))
//                                        .forward(5.5)
//                                        .waitSeconds(0.5)
//                                        // Cycles 1
//                                        .back(7)
//                                        .turn(toRadians(-135))
//                                        .forward(24)
//                                        .waitSeconds(2)
//                                        .back(24)
//                                        .turn(toRadians(135))
//                                        .forward(6.5)
//                                        .waitSeconds(0.5)
//                                        // Cycle 2
//                                        .back(7)
//                                        .turn(toRadians(-135))
//                                        .forward(24)
//                                        .waitSeconds(2)
//                                        .back(24)
//                                        .turn(toRadians(135))
//                                        .forward(6.5)
//                                        .waitSeconds(0.5)
////                                        // LEFT
//                                        .back(5.5)
//                                        .turn(toRadians(45))
//                                        .forward(25)
//                                        .turn((toRadians(-90)))
//                                        // right
//                                        .back(5.5)
//                                        .turn(toRadians(-135))
//                                        .forward(25)
//                                        .turn((toRadians(90)))
//                                        // Center
//                                        .back(6.5)
//                                        .turn(toRadians(-45))

//                                        .waitSeconds(1)
//                                        .lineToLinearHeading(new Pose2d(60.3,-10,toRadians(0)))
//                                        .forward(4.5)
//                                        .waitSeconds(2)
//                                        .back(4.5)
//                                        .lineToLinearHeading(new Pose2d(36.8, -6.5, toRadians(135)))
//                                        .waitSeconds(1)
//                                        .lineToLinearHeading(new Pose2d(60.3,-10,toRadians(0)))
//                                        .forward(4.5)
//                                        .waitSeconds(2)
//                                        .back(4.5)
//                                        .lineToLinearHeading(new Pose2d(36.8, -6.5, toRadians(135)))
//                                        .waitSeconds(1)
//                                        .lineToLinearHeading(new Pose2d(60.3,-10,toRadians(0)))
//                                        .forward(4.5)
//                                        .waitSeconds(2)
//                                        .back(4.5)

//                                        .forward(52)
//                                        .turn(toRadians(45))
//                                        .forward(6)
//                                        .waitSeconds(1)
//                                        .back(9)
//                                        .turn(toRadians(-135))
//                                        .forward(20)
//                                        .waitSeconds(2)
//                                        .forward(5)
//                                        .waitSeconds(1)
//                                        .back(25)
//                                        .turn(toRadians(135))
//                                        .forward(7)

                                        .build()

                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot.setDimensions(15,17))
                .start();
    }
}