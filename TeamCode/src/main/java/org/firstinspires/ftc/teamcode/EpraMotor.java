package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

public class EpraMotor {
    private DcMotor motor;
    private double gearRatio;
    private double wheelDiameter;
    private int ticks;

    EpraMotor (DcMotor motorIn, double ratioIn, double wheelDiamIn, int ticksIn) {
        motor = motorIn;
        gearRatio = ratioIn;
        wheelDiameter = wheelDiamIn;
        ticks = ticksIn;
    }
}
