package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="AutoFar" , group="Autonomous")
public class AutoFar extends LinearOpMode {
    Robot bot = new Robot();
    @Override
    public void runOpMode() {

        bot.init(hardwareMap);

        waitForStart();

        sleep(5000);
        bot.drive(-1, 0, 0, 0);
        sleep(1800);
        bot.stopDriveTrain();
        sleep(150);
        bot.drive(0, -1, 0, 0);
        sleep(200);
        bot.stopDriveTrain();
        sleep(150);
        bot.drive(1, 0, 0, 0);
        sleep(2500);
        bot.stopDriveTrain();

    }
}
