package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.navigate.AprilTagWebcam;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@TeleOp(name = "Motor_Navigate", group = "TeleOp")
public class Motor_Navigate extends OpMode {
    private AprilTagWebcam RedTagReader;

    private static final double APRILTAGDEADBAND = Math.abs(10);

    private DcMotor leftFront;
    private IMU imu;
    private long lastImuLogTimeMs;

    @Override
    public void init() {
        RedTagReader = new AprilTagWebcam();
        RedTagReader.init(hardwareMap, telemetry);
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setPower(0.0);


        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientationOnRobot =
                new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Motor", "leftFront ready");
        telemetry.addData("Deadband", APRILTAGDEADBAND);
        telemetry.update();
    }

    @Override
    public void loop() {
        RedTagReader.updateDetections();
        double leftStickY = -gamepad1.left_stick_y*0.5;
        double motorPower = applyDeadband(leftStickY);
        motorPower = Range.clip(motorPower, -1.0, 1.0);

        leftFront.setPower(motorPower);

        telemetry.addData("Status", "Running");
        telemetry.addData("Deadband", APRILTAGDEADBAND);
        telemetry.addData("Left Stick Y (inverted)", "%.3f", leftStickY);
        telemetry.addData("Motor Power", "%.3f", motorPower);
        telemetry.update();
    }

    private double applyDeadband(double value) {
        if (Math.abs(value) <= APRILTAGDEADBAND) {
            return 0.0;
        }
        return value;
    }



        }



