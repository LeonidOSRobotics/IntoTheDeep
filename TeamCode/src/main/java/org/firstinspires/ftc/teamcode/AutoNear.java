package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="AutoNear" , group="Autonomous")
public class AutoNear extends LinearOpMode {
    Robot bot = new Robot();
    @Override
    public void runOpMode() {

        bot.init(hardwareMap);

        waitForStart();

        bot.drive(-1, 0, 0, 0);
        sleep(1000);
        bot.drive(0, -1, 0, 0);
        sleep(500);
        bot.drive(1, 0, 0, 0);
        sleep(2000);
        bot.drive(0, 1, 0, 0);
        sleep(700);
        bot.stopDriveTrain();

    }
}
