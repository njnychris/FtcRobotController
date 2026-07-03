package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Chassis mecanum pilotable en robot-centric ou field-centric (bouton "A" pour basculer).
 * Le bouton "Y" recale le cap (utile en field-centric pour redefinir "l'avant").
 */
@TeleOp(name = "Mecanum: Field/Robot Centric")
public class MecanumFieldOrRobotCentricOpMode extends LinearOpMode {

    private IMU imu;
    private MecanumDrive drive;
    private GamepadEx driverGamepad;

    private boolean fieldCentric = true;

    @Override
    public void runOpMode() {
        Motor frontLeft = new Motor(hardwareMap, "front-left");
        Motor frontRight = new Motor(hardwareMap, "front-right");
        Motor backLeft = new Motor(hardwareMap, "back-left");
        Motor backRight = new Motor(hardwareMap, "back-right");

        drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
        driverGamepad = new GamepadEx(gamepad1);

        imu = hardwareMap.get(IMU.class, "imu");
        // A adapter selon le sens de montage du Control/Expansion Hub sur le chassis.
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT);
        imu.initialize(new IMU.Parameters(orientation));

        telemetry.addLine("Pret. 'A' = bascule field/robot centric, 'Y' = reset du cap");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            driverGamepad.readButtons();

            if (driverGamepad.wasJustPressed(GamepadKeys.Button.A)) {
                fieldCentric = !fieldCentric;
            }
            if (driverGamepad.wasJustPressed(GamepadKeys.Button.Y)) {
                imu.resetYaw();
            }

            double strafe = - driverGamepad.getLeftX();
            double forward = driverGamepad.getLeftY();
            double turn = driverGamepad.getRightX();

            if (fieldCentric) {
                double headingDegrees = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                drive.driveFieldCentric(strafe, forward, turn, headingDegrees, true);
            } else {
                drive.driveRobotCentric(strafe, forward, turn, true);
            }

            telemetry.addData("Mode", fieldCentric ? "Field-centric" : "Robot-centric");
            telemetry.update();
        }
    }
}
