package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
@Configurable
public class TestOpMode extends LinearOpMode {

    TelemetryManager mDashboard;
    public static float POWER = 0;

    public void runOpMode() {

        DcMotor front_left = hardwareMap.get(DcMotor.class, "front-left");

        mDashboard = PanelsTelemetry.INSTANCE.getTelemetry();

        waitForStart();
        while (opModeIsActive())
        {
            front_left.setPower(POWER);

            telemetry.addLine("Power is : " + POWER);
            mDashboard.addLine("Power is : " + POWER);

            telemetry.update();
            mDashboard.update();
        }
    }
}

