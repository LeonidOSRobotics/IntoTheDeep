package org.firstinspires.ftc.team6220_2021;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team6220_2021.ResourceClasses.DriverInput;

public abstract class MasterOpMode extends LinearOpMode {
    // Motors
    public static DcMotor motorFrontLeft;
    public static DcMotor motorFrontRight;
    public static DcMotor motorBackLeft;
    public static DcMotor motorBackRight;
    public static DcMotor motorLeftDuck;
    public static DcMotor motorDuck;
    public static DcMotor motorArm;
    public static DcMotor motorBelt;

    // Other Devices
    public static Servo servoGrabber;
    public static Servo servoArm;

    // Create Drivers
    public DriverInput driver1;
    public DriverInput driver2;

    // IMUs
    public BNO055IMU imu;

    // Initializes the motors, servos, IMUs, and drivers
    public void Initialize() {
        // Drive train motors
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorArm = hardwareMap.dcMotor.get("motorArm");
        motorDuck = hardwareMap.dcMotor.get("motorDuck");
        motorBelt = hardwareMap.dcMotor.get("motorBelt");
        motorLeftDuck = hardwareMap.dcMotor.get("motorLeftDuck");
        servoGrabber = hardwareMap.servo.get("servoGrabber");
        servoArm = hardwareMap.servo.get("servoArm");

        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        motorLeftDuck.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDuck.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorArm.setTargetPosition(0);
        motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driver1 = new DriverInput(gamepad1);
        driver2 = new DriverInput(gamepad2);

        // Retrieve and initialize the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public void forward(double driveInches, double power) {
        Initialize();

        double TotalTicks = 537.6 * driveInches / 12.57;
        int targetticks = (int) TotalTicks;
        motorFrontLeft.setTargetPosition(targetticks);
        motorBackLeft.setTargetPosition(targetticks);
        motorFrontRight.setTargetPosition(-targetticks);
        motorBackRight.setTargetPosition(-targetticks);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        while(motorBackRight.isBusy() || motorBackLeft.isBusy() || motorFrontLeft.isBusy() || motorFrontRight.isBusy()) {
            telemetry.addData("Currently Running",motorFrontLeft.getCurrentPosition());
        }
        pauseMillis(100);
    }
    public void stopBase() {
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }
    public void blueDuck() {
        motorLeftDuck = hardwareMap.dcMotor.get("motorDuck");
        double x = -0.7;
        while (true) {
            motorLeftDuck.setPower(x);
            pauseMillis(150);
            x -= 0.05;
            telemetry.addData("duckPower", motorLeftDuck.getPower());
            telemetry.update();
            if (x <= -0.85){
                pauseMillis(1500);
                motorLeftDuck.setPower(-.1);
                pauseMillis(30);
                motorLeftDuck.setPower(0);
                x=0.7;
                break;
            }
        }
    }
    public void turnAngle(double turnDegree) {
        Initialize();

        double TotalTicks = 537.6 * 20/4 * (turnDegree/360);
        int targetticks = (int) TotalTicks;
        motorFrontLeft.setTargetPosition(targetticks);
        motorBackLeft.setTargetPosition(targetticks);
        motorFrontRight.setTargetPosition(targetticks);
        motorBackRight.setTargetPosition(targetticks);
        motorFrontLeft.setPower(0.9);
        motorBackLeft.setPower(0.9);
        motorFrontRight.setPower(0.9);
        motorBackRight.setPower(0.9);
        while(motorBackRight.isBusy() || motorBackLeft.isBusy() || motorFrontLeft.isBusy() || motorFrontRight.isBusy()) {
            telemetry.addData("Currently Running",motorFrontLeft.getCurrentPosition());
        }
        pauseMillis(100);

    }

    // Pauses for time milliseconds
    public void pauseMillis(double time) {
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < time && opModeIsActive()) {
            idle();
        }
    }
}