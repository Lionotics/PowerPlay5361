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

//                        //  RIGHT
//                        drive.trajectorySequenceBuilder(new Pose2d(38.8, -61.5, toRadians(90)))
//                                .splineTo(new Vector2d(38.8-22,-61.5+10),toRadians(90))
//                                .forward(9)
//                                .splineTo(new Vector2d(8.8+2,-30.5+2), toRadians(90+45))
//                                .forward(0.5)
//                                .waitSeconds(2)
//                                .back(2)
//                                .turn(toRadians(-45))
////                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
//                                .forward(20)
//
//                                .turn(toRadians(-90))
//                                .forward(45)
//                                .turn(toRadians(90))


                                // BLUE LEFT
                                drive.trajectorySequenceBuilder(new Pose2d(-38.8, -61.5, toRadians(90)))
//                                        .splineTo(new Vector2d(-16.8,-51.5),toRadians(90))
//                                        .forward(9)
//                                        .splineTo(new Vector2d(-10.8,-30.5+2), toRadians(90+45))
//                                        .forward(0.5)
//                                        .waitSeconds(2)
//                                        .back(2)
//                                        .turn(toRadians(-45))
//                                        .forward(20)
//                                        .turn(toRadians(90))
//                                        .forward(50)
//                                        .turn(toRadians(-90))
                                        .splineTo(new Vector2d(-16.8,-51.5),toRadians(90))
                                        .forward(9)
                                        .splineTo(new Vector2d(-12.8,-28.5), toRadians(45))
                                        .forward(2.5)
                                        .back(5)
                                        .turn(toRadians(45))
//                                .splineTo(new Vector2d(11.5,-7), toRadians(90))
                                        .forward(20)
//                                .strafeRight(20)
//                .splineTo(new Vector2d(35.5,-10),toRadians(90))
                                        .turn(toRadians(90))
                                        .forward(25)
                                        .turn(toRadians(-90))


//                .lineToLinearHeading(new Pose2d(8.8,-30.5,toRadians(90+45)))
                                        .build()

//
//                                        .turn(toRadians(-90))
//                                        .forward(45)
//                                        .turn(toRadians(90))



//                                .build();
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}