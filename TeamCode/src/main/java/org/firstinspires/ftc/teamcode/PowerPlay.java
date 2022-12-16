package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;

import java.util.Date;
import java.util.Random;

@TeleOp
public class PowerPlay extends LinearOpMode{
    private DcMotorEx northEastMotor;
    private DcMotorEx southEastMotor;
    private DcMotorEx southWestMotor;
    private DcMotorEx northWestMotor;
    private DcMotorEx chainTop;
    private DcMotorEx chainBottom;
    private BNO055IMU emuIMU1;
    private BNO055IMU emuIMU2;
    private Servo claw;

    @Override
    public void runOpMode() {
        northEastMotor = hardwareMap.get(DcMotorEx.class, "northeastMotor");
        northWestMotor = hardwareMap.get(DcMotorEx.class, "northwestMotor");
        southEastMotor = hardwareMap.get(DcMotorEx.class, "southeastMotor");
        southWestMotor = hardwareMap.get(DcMotorEx.class, "southwestMotor");
        southWestMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        northWestMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        DriveTrain myDrive = new DriveTrain(northWestMotor, northEastMotor, southWestMotor, southEastMotor, 3);

        //initiates chain box
        chainTop = hardwareMap.get(DcMotorEx.class, "chainTop");
        chainBottom = hardwareMap.get(DcMotorEx.class, "chainBottom");
        chainTop.setDirection(DcMotorSimple.Direction.REVERSE);
        chainBottom.setDirection(DcMotorSimple.Direction.REVERSE);
        ChainBox boxerDoggo = new ChainBox(chainTop, chainBottom);

        //initiates claw
        claw = hardwareMap.get(Servo.class, "claw1");
        boolean clawFlag = false;

        //Initiates two IMUs for the control and expansion hubs
        emuIMU1 = hardwareMap.get(BNO055IMU.class, "imu1");
        emuIMU2 = hardwareMap.get(BNO055IMU.class, "imu2");
        MxHeroXYZ tuxEmu1 = new MxHeroXYZ(emuIMU1);
        MxHeroXYZ tuxEmu2 = new MxHeroXYZ(emuIMU2);

        waitForStart();
        myDrive.xSaved = 0.0;
        myDrive.ySaved = 0.0;
        telemetry.speak("I Have the Power! Rangers");
        telemetry.update();

        while (opModeIsActive()) {
//            float powerRightY, float powerLeftY, float powerRightX, float powerLeftX
            myDrive.setDrivePower(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
            //          myDrive.dPadDrive(gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.dpad_right, gamepad1.dpad_left);
            //boxerDoggo.runChainBox(gamepad2.left_stick_y, gamepad2.right_stick_y);
            boxerDoggo.clawUpDown(gamepad2.right_stick_y);
            if (gamepad2.left_bumper) {
                boxerDoggo.runChainBox(gamepad2.right_stick_y);
            } else {
                boxerDoggo.clawUpDown(gamepad2.right_stick_y);
            }

         //   boxerDoggo.runChainBoxFull(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.dpad_right, gamepad2.dpad_left);

            if (clawFlag) {
                if (gamepad2.a) {
                    clawFlag = true;
                    claw.setPosition(1 - claw.getPosition());
                } else {
                    clawFlag = false;
                }
            }

 //          myDrive.whereAmI(tuxEmu1.getAngleZExtrinsic());

           telemetry.addData("Never Eat Soggy Waffles", myDrive.soggyWafflesScheduler(gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.dpad_right, gamepad1.dpad_left, gamepad1.a));
/*
           telemetry.addData("tuxEmu1 Intrinsic", tuxEmu1.getAngleZIntrinsic());
           telemetry.addData("tuxEmu2 Intrinsic", tuxEmu2.getAngleZIntrinsic());
           telemetry.addData("tuxEmu1 Extrinsic", tuxEmu1.getAngleZExtrinsic());
           telemetry.addData("tuxEmu2 Extrinsic", tuxEmu2.getAngleZExtrinsic());
*/
            telemetry.addData("Where is Me", myDrive.xSaved.toString() + "," + myDrive.ySaved.toString());
            telemetry.addData("rightMotorF", myDrive.rightMotorF.getPower());
            telemetry.addData("leftMotorF", myDrive.leftMotorF.getPower());
            telemetry.addData("rightMotorB", myDrive.rightMotorB.getPower());
            telemetry.addData("leftMotorB", myDrive.leftMotorB.getPower());
            /*
            telemetry.addData("rightMotorF", myDrive.rightMotorF.getCurrentPosition());
            telemetry.addData("leftMotorF", myDrive.leftMotorF.getCurrentPosition());
            telemetry.addData("rightMotorB", myDrive.rightMotorB.getCurrentPosition());
            telemetry.addData("leftMotorB", myDrive.leftMotorB.getCurrentPosition());
             */
            telemetry.update();
        }
    }
}

