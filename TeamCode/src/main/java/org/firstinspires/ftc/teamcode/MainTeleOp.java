package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="TeleOp" , group="Linear OpMode")

public class MainTeleOp extends LinearOpMode {
    float forward = 0;
    float strafe = 0;
    float turnRight = 0;
    float turnLeft = 0;


    Robot robot = new Robot();  //Using Robot.Java class
    //TODO: make robot.java

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {



            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                robot.imu.resetYaw();
            }

            forward = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            turnRight = gamepad1.right_trigger;
            turnLeft = gamepad1.left_trigger;

            double botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - Math.PI      ;

            // Rotate the movement direction counter to the bot's rotation
            double rotStrafe = strafe * Math.cos(botHeading) - forward * Math.sin(-botHeading);
            double rotForward = strafe * Math.sin(-botHeading) + forward * Math.cos(botHeading);



            robot.drive(rotForward, rotStrafe, turnRight, turnLeft);

        }
    }


}
