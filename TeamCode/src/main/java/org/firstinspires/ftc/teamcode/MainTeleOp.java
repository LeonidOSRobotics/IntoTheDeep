package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOp" , group="Linear OpMode")

public class MainTeleOp extends LinearOpMode {
    double forward = gamepad1.left_stick_y;
    double stafe = gamepad1.left_stick_x;
    double turnRight = gamepad1.right_trigger;
    double turnLeft;


    Robot robot = new Robot();  //Using Robot.Java class
    //TODO: make robot.java

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {


            forward = gamepad1.left_stick_y;
            stafe = gamepad1.left_stick_x;
            turnRight = gamepad1.right_trigger;
            turnLeft = gamepad1.left_trigger;

            robot.drive(forward, stafe, turnRight, turnLeft);

        }
    }


}
