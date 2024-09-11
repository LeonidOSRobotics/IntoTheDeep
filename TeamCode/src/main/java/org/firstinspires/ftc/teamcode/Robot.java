package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Robot {

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftBack = null;
    DcMotor rightBack = null;

    static final double Ticksperrev = 537.7;
    static final double Wheeldiameter_cm = 10.4;
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
        leftFront.setPower(frontLeftPower);
        rightFront.setPower(frontRightPower);
        leftBack.setPower(backLeftPower);
        rightBack.setPower(backRightPower);
    }

    public void stop() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

}
