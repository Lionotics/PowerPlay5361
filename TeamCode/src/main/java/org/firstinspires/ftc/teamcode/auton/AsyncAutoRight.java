//package org.firstinspires.ftc.teamcode.auton;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//import org.firstinspires.ftc.teamcode.hardware.Intake;
//import org.firstinspires.ftc.teamcode.hardware.Slides;
//import org.firstinspires.ftc.teamcode.util.VariableStorage;
//// Note: this is currenlty basically copy / pasted from LRR, I will work on it more.
//@Autonomous
//public class AsyncAutoRight extends LinearOpMode {
//    // TODO: Fix this for real stuff
//    enum State {
//        TRAJECTORY_1,
//        TRAJECTORY_2,
//        TURN_1,
//        TRAJECTORY_3,
//        WAIT_1,
//        TURN_2,
//        IDLE
//    }
//    State currentState = State.IDLE;
//
//    // Define our start pose
//    // This assumes we start at x: 15, y: 10, heading: 180 degrees
//    Pose2d startPose = new Pose2d(35.25, -61.5, Math.toRadians(90));
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        // TODO: Add vision
//
//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//        Intake intake = new Intake();
//        intake.init(hardwareMap);
//
//        Slides slides = new Slides();
//        slides.init(hardwareMap);
//
//        // Set inital pose
//        drive.setPoseEstimate(startPose);
//
//        Trajectory trajectory1 = drive.trajectoryBuilder(startPose).build();
//        Trajectory trajectory2 = drive.trajectoryBuilder(trajectory1.end())
//                .build();
//
//        double waitTime1 = 1;
//        ElapsedTime waitTimer1 = new ElapsedTime();
//
//        double turnAngle = Math.toRadians(90);
//
//
//        waitForStart();
//        if (isStopRequested()) return;
//        // Start is now pressed!
//
//        currentState = State.TRAJECTORY_1;
//        drive.followTrajectoryAsync(trajectory1);
//
//        // FSM
//        while (opModeIsActive() && !isStopRequested()) {
//            // Our state machine logic
//            // You can have multiple switch statements running together for multiple state machines
//            // in parallel. This is the basic idea for subsystems and commands.
//
//            // We essentially define the flow of the state machine through this switch statement
//            switch (currentState) {
//                case TRAJECTORY_1:
//                    // Check if the drive class isn't busy
//                    // `isBusy() == true` while it's following the trajectory
//                    // Once `isBusy() == false`, the trajectory follower signals that it is finished
//                    // We move on to the next state
//                    // Make sure we use the async follow function
//                    if (!drive.isBusy()) {
//                        currentState = State.TRAJECTORY_2;
//                        drive.followTrajectoryAsync(trajectory2);
//                    }
//                    break;
//                case TRAJECTORY_2:
//                    // Check if the drive class is busy following the trajectory
//                    // Move on to the next state, TURN_1, once finished
//                    if (!drive.isBusy()) {
//                        currentState = State.TURN_1;
//                        drive.turnAsync(turnAngle);
//                    }
//                    break;
//                case TURN_1:
//                    // Check if the drive class is busy turning
//                    // If not, move onto the next state, TRAJECTORY_3, once finished
//                    if (!drive.isBusy()) {
//                        currentState = State.TRAJECTORY_3;
//                        drive.followTrajectoryAsync(trajectory3);
//                    }
//                    break;
//                case TRAJECTORY_3:
//                    // Check if the drive class is busy following the trajectory
//                    // If not, move onto the next state, WAIT_1
//                    if (!drive.isBusy()) {
//                        currentState = State.WAIT_1;
//
//                        // Start the wait timer once we switch to the next state
//                        // This is so we can track how long we've been in the WAIT_1 state
//                        waitTimer1.reset();
//                    }
//                    break;
//                case WAIT_1:
//                    // Check if the timer has exceeded the specified wait time
//                    // If so, move on to the TURN_2 state
//                    if (waitTimer1.seconds() >= waitTime1) {
//                        currentState = State.TURN_2;
//                        drive.turnAsync(turnAngle);
//                    }
//                    break;
//                case TURN_2:
//                    // Check if the drive class is busy turning
//                    // If not, move onto the next state, IDLE
//                    // We are done with the program
//                    if (!drive.isBusy()) {
//                        currentState = State.IDLE;
//                    }
//                    break;
//                case IDLE:
//                    // Do nothing in IDLE
//                    // currentState does not change once in IDLE
//                    // This concludes the autonomous program
//                    break;
//            }
//
//            // Things to keep running
//            // We update drive continuously in the background, regardless of state
//            drive.update();
//            // We update our lift PID continuously in the background, regardless of state
//            slides.moveTowardsGoal();
//
//            // Read pose
//            Pose2d poseEstimate = drive.getPoseEstimate();
//
//            // Continually write pose
//            VariableStorage.currentPose = poseEstimate;
//
//
//        }
//    }
//}
