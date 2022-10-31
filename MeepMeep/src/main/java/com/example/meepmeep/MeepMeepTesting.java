package com.example.meepmeep;

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
//        MeepMeep meepMeep = new MeepMeep(800);
//
//        AddTrajectorySequenceCallback redCarouselSide =
//                drive -> drive.trajectorySequenceBuilder(new Pose2d(-30.5, -62, Math.toRadians(270)))
//                        .setReversed(true)
//                        .splineTo(new Vector2d(-32, -32), Math.toRadians(15)) // start extending before this
//                        .waitSeconds(3.0) // actually deposit, retract
//                        .turn(Math.toRadians(165))
//                        .splineTo(new Vector2d(-60, -40), Math.toRadians(270))
//                        .waitSeconds(3.0) // do ducks
//                        .forward(5.0)
//                        .build();
//
//        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(100, 200, Math.toRadians(180), Math.toRadians(180), 14)
//                .followTrajectorySequence(drive ->
//                        drive.trajectorySequenceBuilder(new Pose2d(-38, -60, Math.toRadians(90)))
//                                .splineTo(new Vector2d(-13, -60), Math.toRadians(90))
//                                .splineTo(new Vector2d(-13, -35), Math.toRadians(45))
//                                .build()
//
//                )
//
//
//
//
//
//        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();

        AddTrajectorySequenceCallback redCarouselSide =
                drive -> drive.trajectorySequenceBuilder(new Pose2d(-30.5, -62, Math.toRadians(270)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-32, -32), Math.toRadians(15)) // start extending before this
                        .waitSeconds(3.0) // actually deposit, retract
                        .turn(Math.toRadians(165))
                        .splineTo(new Vector2d(-60, -40), Math.toRadians(270))
                        .waitSeconds(3.0) // do ducks
                        .forward(5.0)
                        .build();

        AddTrajectorySequenceCallback blueCarouselSide =
                drive -> drive.trajectorySequenceBuilder(new Pose2d(-30.5, 62, Math.toRadians(-270)))
                        .setReversed(true)
                        .splineTo(new Vector2d(-32, 32), Math.toRadians(-15)) // start extending before this
                        .waitSeconds(3.0) // actually deposit, retract
                        .turn(Math.toRadians(165))
                        .splineTo(new Vector2d(-60, 40), Math.toRadians(-270))
                        .waitSeconds(3.0) // do ducks
                        .forward(5.0)
                        .build();

        AddTrajectorySequenceCallback redWarehouseSide =
                drive -> drive.trajectorySequenceBuilder(new Pose2d(6.5, -62, Math.toRadians(270)))
                        .setReversed(true)
                        .splineTo(new Vector2d(8, -32), Math.toRadians(165)) // start extending before this
                        .waitSeconds(3.0) // actually deposit, retract
                        .turn(Math.toRadians(-75))
                        .setReversed(false)
                        .splineTo(new Vector2d(8, -60), Math.toRadians(270))
                        .turn(Math.toRadians(90))
                        .strafeTo(new Vector2d(8, -70))
                        .forward(36.0)
                        .build();

        AddTrajectorySequenceCallback blueWarehouseSide  =
                drive -> drive.trajectorySequenceBuilder(new Pose2d(6.5, 62, Math.toRadians(-270)))
                        .setReversed(true)
                        .splineTo(new Vector2d(8, 32), Math.toRadians(-165)) // start extending before this
                        .waitSeconds(3.0) // actually deposit, retract
                        .turn(Math.toRadians(-75))
                        .setReversed(false)
                        .splineTo(new Vector2d(8, 60), Math.toRadians(-270))
                        .turn(Math.toRadians(-90))
                        .strafeTo(new Vector2d(8, 70))
                        .forward(36.0)
                        .build();

//        MeepMeep mm = new MeepMeep(800)
//                // Set field im800age
//                .setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
//                // Set theme
//                .setTheme(new ColorSchemeRedDark())
//                // Background opacity from 0-1
//                .setBackgroundAlpha(1f)
//                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(45, 30, Math.toRadians(233), Math.toRadians(180), 14)
//                .followTrajectorySequence(redCarouselSide)
//                .start();
    }
}