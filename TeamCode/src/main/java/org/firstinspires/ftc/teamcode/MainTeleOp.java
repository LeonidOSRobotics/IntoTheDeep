package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * MainTeleOp class for controlling the robot during teleoperated mode.
 * This class extends LinearOpMode and uses a Robot object for hardware control.
 */
@TeleOp(name="TeleOp" , group="Linear OpMode")
public class MainTeleOp extends LinearOpMode {

    // Variables for movement input
    float forward = 0; // Stores the forward/backward input from the gamepad
    float strafe = 0;  // Stores the sideways movement input from the gamepad
    float turnRight = 0; // Stores the right-turn input from the gamepad triggers
    float turnLeft = 0;  // Stores the left-turn input from the gamepad triggers

    // Create an instance of the Robot class for hardware control
    Robot robot = new Robot();  // Using Robot.Java class to interface with the robot's hardware


    private static final double BUCKET_HOLD_POSITION = 0.3;
    private static final double BUCKET_DUMP_POSITION = 0;
    //SLide Positions in cm
    private static final int SLIDE_BASE_POSITION = 0;
    private static final int SLIDE_BASKET = 53;
    private static final int SLIDE_LOWBAR = 16;
    private static final int SLIDE_HIGHBAR = 51;

    @Override
    public void runOpMode() {
        // Initialize the robot hardware by calling the init method from the Robot class
        robot.init(hardwareMap);

        // Wait for the user to start the TeleOp mode (e.g., pressing the "play" button in the FTC Driver Station)
        waitForStart();

        // Main control loop that runs while the OpMode is active
        while (opModeIsActive()) {

            // Reset the robot's yaw (orientation angle) if the "options" button is pressed
            // This functionality allows recalibration of the IMU during operation to correct orientation errors
            if (gamepad1.options) {
                robot.imu.resetYaw();
            }

            // Read inputs from the gamepad joysticks and triggers:
            forward = -gamepad1.left_stick_y; // Negative because pushing the joystick forward gives a negative value
            strafe = gamepad1.left_stick_x;  // Horizontal movement from the left joystick
            turnRight = gamepad1.right_trigger; // Trigger value for rightward rotation
            turnLeft = gamepad1.left_trigger;  // Trigger value for leftward rotation

            // Retrieve the robot's current heading (yaw) from the IMU sensor in radians
            // Subtracting Math.PI aligns the robot's coordinate system with the desired orientation
            double botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - Math.PI;

            // Adjust the movement direction to account for the robot's current rotation:
            // The forward and strafe components are rotated by the negative of the robot's heading
            double rotStrafe = strafe * Math.cos(botHeading) - forward * Math.sin(-botHeading);
            double rotForward = strafe * Math.sin(-botHeading) + forward * Math.cos(botHeading);

            // Pass the adjusted movement values and turn inputs to the robot's drivetrain
            // The drive method in the Robot class handles motor power distribution for movement
            robot.drive(rotForward, rotStrafe, turnRight, turnLeft);


            if (gamepad1.x) {
                robot.bucket.setPosition(BUCKET_DUMP_POSITION); // Rotate bucket to a specific position when button X is pressed
            } else {
                robot.bucket.setPosition(BUCKET_HOLD_POSITION); // Default position
            }
                //Control the slide position
                /*if (gamepad1.dpad_up) {
                    //move to top position
                    robot.slide.setTargetPosition(robot.getLinearSlideTicksPerCm(SLIDE_HIGHBAR));
                    robot.slide.setPower(0.75);
                } else if (gamepad1.dpad_left) {
                    //move to middle position
                    robot.slide.setTargetPosition(robot.getLinearSlideTicksPerCm(SLIDE_BASKET));
                    robot.slide.setPower(0.75);
                } else if (gamepad1.dpad_down) {
                    //move to bottom position
                    robot.slide.setTargetPosition(robot.getLinearSlideTicksPerCm(SLIDE_LOWBAR));
                    robot.slide.setPower(0.75);
                } else {
                    //hold position
                    robot.slide.setPower(0.1);
                }

                if (gamepad1.dpad_up) {

                    robot.intake.setPosition(0.7);

                }  else if (gamepad1.dpad_down) {

                    robot.intake.setPosition(0);

                } else {
                    robot.intakeArm.setPower(0);
                }

                if (-gamepad1.right_stick_y < 0) {
                    robot.intakeArm.setPower(gamepad1.right_stick_y * .3);
                } else if (-gamepad1.right_stick_y > 0) {
                    robot.intakeArm.setPower(gamepad1.right_stick_y * .9);

                }
*/

            //  Linear slide Overide
                if (gamepad1.b) {
                    robot.slide.setPower(.3);
                } else if (gamepad1.a) {
                    robot.slide.setPower(-0.75);
                } else {
                    robot.slide.setPower(0);
                }
            }
        }
    }

