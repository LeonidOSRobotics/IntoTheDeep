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

            drive(forward, stafe, turnRight, turnLeft);

        }
    }

    public void drive(double forward, double strafe, double rotateLeft, double rotateRight) {
        double y = forward;
        double x = strafe;
        double rotate = rotateLeft - rotateRight;
        //Slows speed of wheels
        double dampening = 1;

        //Calculating the power for the wheels
        double frontLeftPower = (y + x + rotate) * dampening;
        double frontRightPower = (y - x - rotate) * dampening;
        double backLeftPower = (y - x + rotate) * dampening;
        double backRightPower = (y + x - rotate) * dampening;

        //Set Power
        robot.leftFront.setPower(frontLeftPower);
        robot.rightFront.setPower(frontRightPower);
        robot.leftBack.setPower(backLeftPower);
        robot.rightBack.setPower(backRightPower);
    }
}
