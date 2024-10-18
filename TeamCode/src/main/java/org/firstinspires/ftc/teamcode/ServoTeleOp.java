package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="TeleOp" , group="Linear OpMode")

public class ServoTeleOp extends LinearOpMode {
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
          if (gamepad1.a) {
              robot.backward_s.setPosition(Servo.MAX_POSITION);
              robot.forward_s.setPosition(Servo.MAX_POSITION);
          }
          if (gamepad1.b) {
              robot.backward_s.setPosition(Servo.MIN_POSITION);
              robot.forward_s.setPosition(Servo.MIN_POSITION);
          }

        }
    }


}
