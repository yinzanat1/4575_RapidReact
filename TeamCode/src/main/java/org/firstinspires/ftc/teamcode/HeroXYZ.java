package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

public class HeroXYZ {
    private BNO055IMU emu;

    //private DcMotorEx rightMotor;
    //private DcMotorEx leftMotor;

    //private DcMotorEx[] leftMotorArray = new DcMotorEx[1];
    //private DcMotorEx[] rightMotorArray = new DcMotorEx[1];

    //rightMotorArray[0] = hardwareMap.get(DcMotorEx.class, "rightMotor");
    //leftMotorArray[0] = hardwareMap.get(DcMotorEx.class, "leftMotor");
    //leftMotorArray[0].setDirection(DcMotorSimple.Direction.REVERSE);

    public HeroXYZ (BNO055IMU emuIn) {
        BNO055IMU.Parameters imuParameters;
        imuParameters = new BNO055IMU.Parameters();
        imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        emu = emuIn;
        emu.initialize(imuParameters);

        Position startPos = new Position(MM, 0.0, 0.0, 0.0, 10);
        Velocity startVel = new Velocity(MM, 0.0, 0.0, 0.0,10);
        emu.startAccelerationIntegration(startPos, startVel, 10);

        //DriveTrain myDrive = new DriveTrain(rightMotorArray, leftMotorArray, 1);
    }

    public String getPositionXYZ () {
        return emu.getPosition().toString();
    }
    public String getAngles () {
        return emu.getAngularOrientation().toString();
    }
    public int getAngleY () {
        return (int) emu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).secondAngle;
    }
    public String getAcceleration () {
        return emu.getAcceleration().toString();
    }
    public String getQuaternion () {
        return ((Float) emu.getQuaternionOrientation().x).toString() + ", " + ((Float) emu.getQuaternionOrientation().y).toString() + ", " + ((Float) emu.getQuaternionOrientation().z).toString() + ", " + ((Float) emu.getQuaternionOrientation().w).toString();
    }
}