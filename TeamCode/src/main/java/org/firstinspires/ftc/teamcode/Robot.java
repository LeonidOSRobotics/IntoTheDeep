package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Robot {

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    DcMotor slide = null;
    DcMotor intakeArm = null;


    Servo forward_s = null;
    Servo backward_s = null;

    Servo bucket = null;
    Servo intake = null;

    IMU imu = null;

    static final double Ticksperrev = 537.7;
    static final double Wheeldiameter_cm = 10.4;
    static final double WHEEL_CIRCUMFERENCE_CM = 14.2; // Circumference of the wheel in cm
    static final int NUMBER_OF_WHEELS = 3; // Three wheels in the system
    /* local OpMode members.*/
    HardwareMap hwMap = null; //hardware map
    private final ElapsedTime period = new ElapsedTime();
    public ElapsedTime getPeriod() {
        return period;
    }
    /* Constructor */
    public Robot() {
    }

    public void init(HardwareMap ahwMap) {
// Save reference to hardware map
        hwMap = ahwMap;


        leftFront = hwMap.get(DcMotor.class, "leftFront");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        slide = hwMap.get(DcMotor.class, "slide");
        intakeArm = hwMap.get(DcMotor.class, "intakeArm");

        forward_s = hwMap.get(Servo.class, "forward_s");
        backward_s = hwMap.get(Servo.class, "backward_s");

        bucket = hwMap.get(Servo.class, "bucket");
        intake = hwMap.get(Servo.class, "intake");

        imu = hwMap.get(IMU.class, "imu");

        // Retrieve the IMU from the hardware map
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Temporary Directions for drive train, change after testing if needed.
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        slide.setDirection(DcMotor.Direction.FORWARD);
        intakeArm.setDirection(DcMotor.Direction.FORWARD);

        forward_s.setDirection(Servo.Direction.FORWARD);
        backward_s.setDirection(Servo.Direction.FORWARD);

        bucket.setDirection(Servo.Direction.FORWARD);
        intake.setDirection(Servo.Direction.FORWARD);


        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Adding Encoder Functionality for Controlled Movement
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // set all motors to zero power
        stopDriveTrain();

        slide.setPower(0);
        intakeArm.setPower(0);
    }


    public void drive(double forward, double strafe, double rotateLeft, double rotateRight) {
        double y = forward;
        double x = strafe;
        double rotate = rotateRight - rotateLeft;
        //Slows speed of wheels
        double dampening = .75;

        //Calculating the power for the wheels
        double frontLeftPower = (y + x + rotate) * dampening;
        double backLeftPower = (y - x + rotate) * dampening;
        double frontRightPower = (y - x - rotate) * dampening;
        double backRightPower = (y + x - rotate) * dampening;

        //Set Power
        leftFront.setPower(frontLeftPower);
        rightFront.setPower(frontRightPower);
        leftBack.setPower(backLeftPower);
        rightBack.setPower(backRightPower);
    }

    public void moveToPosition(int targetPosition, double power) {
        // Reset encoders after position movement
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(targetPosition);
        leftBack.setTargetPosition(targetPosition);
        rightFront.setTargetPosition(targetPosition);
        rightBack.setTargetPosition(targetPosition);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {
            // Wait until the motors reach the target position
        }

        stopDriveTrain();


    }

    public int getTicksPerCm() {
        double circumference = Math.PI * Wheeldiameter_cm; // Wheel circumference in cm
        return (int) (Ticksperrev / circumference); // Ticks per cm
    }

    public int getLinearSlideTicksPerCm() {
        return (int) (Ticksperrev / WHEEL_CIRCUMFERENCE_CM) * NUMBER_OF_WHEELS; // New formula considering multiple wheels
    }



    public void stopDriveTrain() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

}
