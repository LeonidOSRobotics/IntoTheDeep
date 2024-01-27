package org.firstinspires.ftc.teamcode.autons;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.hardware.SensorRevTOFDistance;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.commands.DriveMotorCommand;
import org.firstinspires.ftc.teamcode.subbys.DriveMotorSubsystem;
import org.firstinspires.ftc.teamcode.vision.pipeline.CapstoneDetector;
import org.firstinspires.ftc.teamcode.vision.pipeline.CapstoneDetectorBlue;

@Autonomous(name = "Meet4")
public class Meet4Auton extends CommandOpMode {
    private Motor fL, fR, bL, bR, arm;

    private SimpleServo sensorTurner;
    private CapstoneDetectorBlue capstoneDetector;

    private DriveMotorSubsystem subsystem;
    private DriveMotorCommand command, command2;
    private SensorRevTOFDistance distance;
    @Override
    public void initialize() {
        capstoneDetector = new CapstoneDetectorBlue(hardwareMap, "coolio");
        capstoneDetector.init();

        distance = new SensorRevTOFDistance(hardwareMap, "dist");
        sensorTurner = new SimpleServo(hardwareMap, "ss", -180, 180);

        fL = new Motor(hardwareMap,"fL");
        fR = new Motor(hardwareMap,"fR");
        bL = new Motor(hardwareMap,"bL");
        bR = new Motor(hardwareMap,"bR");

        arm = new Motor(hardwareMap, "arm");

        fL.motor.setDirection(DcMotor.Direction.REVERSE);
        fR.motor.setDirection(DcMotor.Direction.FORWARD);
        bL.motor.setDirection(DcMotor.Direction.REVERSE);
        bR.motor.setDirection(DcMotor.Direction.FORWARD);

        fL.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fR.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bL.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bR.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        arm.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        subsystem = new DriveMotorSubsystem(fL, fR, bL, bR, distance);
        command = new DriveMotorCommand(subsystem, 0.3, 0.3, 0.3, 0.325, 30);
        command2 = new DriveMotorCommand(subsystem, 0.3, 0.3, 0.3, 0.325, 37);

        if(capstoneDetector.getPlacement() == CapstoneDetectorBlue.Placement.LEFT){
//        turn = new DriveMotorCommand(subsystem, -0.3, -0.3, 0.3, 0.3, 31)
            schedule(new WaitUntilCommand(this::isStarted)
                    .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(180)))
                    .andThen(command.interruptOn(() -> distance.getDistance(DistanceUnit.INCH) > 30))
//                        .andThen(command.withTimeout(4000))
//                .andThen(new WaitCommand(1000))
//                .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(-58)))
                    .andThen(
                            new SequentialCommandGroup(
                                    new ParallelCommandGroup(
                                            new InstantCommand(() -> fL.set(-0.3)),
                                            new InstantCommand(() -> fR.set(0.3)),
                                            new InstantCommand(() -> bL.set(-0.3)),
                                            new InstantCommand(() -> bR.set(0.3))
                                    ),
                                    new WaitCommand(1100),
                                    new ParallelCommandGroup(
                                            new InstantCommand(() -> fL.set(0)),
                                            new InstantCommand(() -> fR.set(0)),
                                            new InstantCommand(() -> bL.set(0)),
                                            new InstantCommand(() -> bR.set(0))
                                    ),
                                    new InstantCommand(() -> arm.set(-1)),
//                                new ParallelCommandGroup(
//                                        new InstantCommand(() -> fL.set(-0.3)),
//                                        new InstantCommand(() -> fR.set(-0.3)),
//                                        new InstantCommand(() -> bL.set(-0.3)),
//                                        new InstantCommand(() -> bR.set(-0.3))
//                                ),
                                    new WaitCommand(600),
                                    new InstantCommand(() -> arm.set(0)))));

        } else if (capstoneDetector.getPlacement() == CapstoneDetectorBlue.Placement.CENTER) {
            schedule(new WaitUntilCommand(this::isStarted)
                    .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(180)))
                    .andThen(command2.interruptOn(() -> distance.getDistance(DistanceUnit.INCH) > 37))
//                .andThen(new WaitCommand(1000))
//                .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(-58)))
                    .andThen(
                            new SequentialCommandGroup(
                                    new InstantCommand(() -> arm.set(-0.5)),
//                                new ParallelCommandGroup(
//                                        new InstantCommand(() -> fL.set(-0.3)),
//                                        new InstantCommand(() -> fR.set(-0.3)),
//                                        new InstantCommand(() -> bL.set(-0.3)),
//                                        new InstantCommand(() -> bR.set(-0.3))
//                                ),
                                    new WaitCommand(1400),
                                    new InstantCommand(() -> arm.set(0)))));
        }else{
            schedule(new WaitUntilCommand(this::isStarted)
                    .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(180)))
                    .andThen(command.interruptOn(() -> distance.getDistance(DistanceUnit.INCH) > 30))
//                .andThen(new WaitCommand(1000))
//                .andThen(new InstantCommand(() -> sensorTurner.turnToAngle(-58)))
                    .andThen(
                            new SequentialCommandGroup(
                                    new ParallelCommandGroup(
                                            new InstantCommand(() -> fL.set(0.3)),
                                            new InstantCommand(() -> fR.set(-0.3)),
                                            new InstantCommand(() -> bL.set(0.3)),
                                            new InstantCommand(() -> bR.set(-0.3))
                                    ),
                                    new WaitCommand(800),
                                    new ParallelCommandGroup(
                                            new InstantCommand(() -> fL.set(0)),
                                            new InstantCommand(() -> fR.set(0)),
                                            new InstantCommand(() -> bL.set(0)),
                                            new InstantCommand(() -> bR.set(0))
                                    ),
                                    new InstantCommand(() -> arm.set(-0.5)),
//                                new ParallelCommandGroup(
//                                        new InstantCommand(() -> fL.set(-0.3)),
//                                        new InstantCommand(() -> fR.set(-0.3)),
//                                        new InstantCommand(() -> bL.set(-0.3)),
//                                        new InstantCommand(() -> bR.set(-0.3))
//                                ),
                                    new WaitCommand(1400),
                                    new InstantCommand(() -> arm.set(0)))));
        }
    }
}
