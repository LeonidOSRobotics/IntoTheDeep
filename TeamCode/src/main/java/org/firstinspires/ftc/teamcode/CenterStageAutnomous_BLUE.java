package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CenterStageRobot.commands.ElevatorCommand;
import org.firstinspires.ftc.teamcode.CenterStageRobot.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.CenterStageRobot.subsystems.IntakeArmSubsystem;
import org.firstinspires.ftc.teamcode.CenterStageRobot.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.CenterStageRobot.subsystems.OuttakeSusystem;
import org.firstinspires.ftc.teamcode.CenterStageRobot.subsystems.PixelFingerSubsystem;
import org.firstinspires.ftc.teamcode.roadRunner.drive.SampleMecanumDrive;

@Autonomous(name = "CenterStageAutonomous_BLUE", group = "Final Autonomous")
public class CenterStageAutnomous_BLUE extends CommandOpMode {

    private OuttakeSusystem outtakeSusystem;
    private ElevatorSubsystem elevatorSubsystem;
    private ElevatorCommand elevatorCommand;
    private IntakeArmSubsystem intakeArmSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private PixelFingerSubsystem pixelFingerSubsystem;

    private SampleMecanumDrive drive;
    private RoadRunnerCommand_BLUE RR_Blue;
    private RoadRunnerSubsystem_BLUE.Randomization rand;

    private Pose2d HomePose_SHORT = new Pose2d(RoadRunnerSubsystem_BLUE.Tile/2, 3 * RoadRunnerSubsystem_BLUE.Tile - 6.93 - 2.56, Math.toRadians(270));
    private Pose2d HomePose_LONG = new Pose2d(1.5 * RoadRunnerSubsystem_BLUE.TileInverted, 3 * RoadRunnerSubsystem_BLUE.TileInverted + (RoadRunnerSubsystem_BLUE.RobotY/2), Math.toRadians(90));

    private SequentialCommandGroup temp;
    public SequentialCommandGroup randomizationPixelElevator(){
        return new SequentialCommandGroup(
                new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.AUTO),
                new InstantCommand(outtakeSusystem::go_outtake_first),
                new WaitCommand(80),
                new InstantCommand(outtakeSusystem::go_outtake_second)
        );
    }

    public SequentialCommandGroup elevator(){
        return new SequentialCommandGroup(
                new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.LOW),
                new InstantCommand(outtakeSusystem::go_outtake_first),
                new WaitCommand(80),
                new InstantCommand(outtakeSusystem::go_outtake_second)
        );
    }

    public SequentialCommandGroup scoring_randomization(){
        return new SequentialCommandGroup(
                new InstantCommand(outtakeSusystem::wheel_release),
                new WaitCommand(1000),
                new InstantCommand(outtakeSusystem::wheel_stop)
        );
    }

    public SequentialCommandGroup scoring(){
        return new SequentialCommandGroup(
                new InstantCommand(outtakeSusystem::wheel_release),
                new WaitCommand(2000),
                new InstantCommand(outtakeSusystem::wheel_stop)
        );
    }

    public SequentialCommandGroup resetElevator() {
        return new SequentialCommandGroup(
                new InstantCommand(outtakeSusystem::go_intake_second),
                new WaitCommand(80),
                new InstantCommand(outtakeSusystem::go_intake_first),
                new ElevatorCommand(elevatorSubsystem, ElevatorSubsystem.Level.LOADING)
        );
    }

    public SequentialCommandGroup stackStationIntake(int index) {
        return new SequentialCommandGroup(
                new InstantCommand(intakeSubsystem::run),
                new InstantCommand(outtakeSusystem::wheel_grab),
                new WaitCommand(150),
                new SequentialCommandGroup(
                        new InstantCommand(()-> intakeArmSubsystem.auto_pixel(index)),
                        new WaitCommand(400),
                        new InstantCommand(()-> intakeArmSubsystem.auto_pixel(index - 1)),
                        new WaitCommand(700)
                ),
                new ParallelCommandGroup(
                        new InstantCommand(intakeSubsystem::stop),
                        new InstantCommand(()-> intakeArmSubsystem.auto_pixel(6)),
                        new InstantCommand(outtakeSusystem::wheel_stop)
                ),
                new SequentialCommandGroup(
                        new InstantCommand(intakeSubsystem::reverse),
                        new WaitCommand(600),
                        new InstantCommand(intakeSubsystem::stop)
                )
        );
    }

    @Override
    public void initialize() {
        outtakeSusystem = new OuttakeSusystem(hardwareMap);
        elevatorSubsystem = new ElevatorSubsystem(hardwareMap, telemetry, () -> 0);
        intakeSubsystem = new IntakeSubsystem(hardwareMap, telemetry);
        intakeArmSubsystem = new IntakeArmSubsystem(hardwareMap);
        pixelFingerSubsystem = new PixelFingerSubsystem(hardwareMap);

        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        drive = new SampleMecanumDrive(hardwareMap);
        RR_Blue = new RoadRunnerCommand_BLUE(drive, HomePose_SHORT, RoadRunnerSubsystem_BLUE.StartingPosition.SHORT,
                RoadRunnerSubsystem_BLUE.Path.INNER, RoadRunnerSubsystem_BLUE.PixelStack.INNER,
                RoadRunnerSubsystem_BLUE.ParkingPosition.INNER, telemetry, outtakeSusystem, elevatorSubsystem);

        rand = RoadRunnerSubsystem_BLUE.Randomization.RIGHT;

        RR_Blue.spikeRandomizationPath(rand);
        RR_Blue.cycle();
        RR_Blue.parking();
        RR_Blue.TrajectoryInit();
    }

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        // SPIKE
        new InstantCommand(intakeArmSubsystem::LOCK_PIXEL, intakeArmSubsystem).schedule();
        RR_Blue.runSpike(rand);
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        // BACKDROP - YELLOW
        new InstantCommand(intakeArmSubsystem::raiseArm, intakeArmSubsystem).schedule();
        randomizationPixelElevator().schedule();
        RR_Blue.runSpike_RandomizedBackdrop();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        temp = randomizationPixelElevator();
        temp.schedule();
        while (!isStopRequested() && opModeIsActive() && CommandScheduler.getInstance().isScheduled(temp)) {
            run();
        }
        // STACK FIRST 2
        temp = new SequentialCommandGroup(
                new WaitCommand(600),
                resetElevator()
        );
        temp.schedule();
        RR_Blue.runBackdrop_Station_First_Cycle();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        temp = stackStationIntake(5);
        temp.schedule();
        while (!isStopRequested() && opModeIsActive() && CommandScheduler.getInstance().isScheduled(temp)) {
            run();
        }

        temp = new SequentialCommandGroup(
                new WaitCommand(1800),
                elevator()
        );

        temp.schedule();

        RR_Blue.runStation_Backdrop_First_Cycle();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        temp = scoring();
        temp.schedule();
        while (!isStopRequested() && opModeIsActive() && CommandScheduler.getInstance().isScheduled(temp)) {
            run();
        }

        // STACK Second 2
        temp = new SequentialCommandGroup(
                new WaitCommand(600),
                resetElevator()
        );
        temp.schedule();

        RR_Blue.runBackdrop_Station_Second_Cycle();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        temp = stackStationIntake(3);
        temp.schedule();
        while (!isStopRequested() && opModeIsActive() && CommandScheduler.getInstance().isScheduled(temp)) {
            run();
        }

        temp = new SequentialCommandGroup(
                new WaitCommand(1800),
                elevator()
        );

        temp.schedule();

        RR_Blue.runStation_Backdrop_Second_Cycle();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        temp = new SequentialCommandGroup(
                scoring(), resetElevator());
        temp.schedule();
        while (!isStopRequested() && opModeIsActive() && CommandScheduler.getInstance().isScheduled(temp)) {
            run();
        }


        RR_Blue.runParking();
        while (!isStopRequested() && opModeIsActive() && drive.isBusy()) {
            run();
            drive.update();
        }
        drive.setWeightedDrivePower(new Pose2d(0, 0, 0));

        reset();
    }
}
