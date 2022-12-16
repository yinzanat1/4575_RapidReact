package org.firstinspires.ftc.teamcode;

    /*
        properties:
            private rightArm dcmotor
            private leftArm dcmotor
            private rightStart integer
            private leftStart integer



        methods:
            void moveArm (targetPosition)
            void rotateArm (direction, times)  // -1 is non stop, 0 to stop
            double getCurrentPosition ()
            boolean atTarget ()
     */

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Arm {
    private DcMotorEx theArm;
    private double gearRatio;
    public int ticksPerRotation;
    private int startPos = 0;
    public double oneDegree = 1120.0/360; //1 degrees = 3.1111
    private static double slowSpeed = 0.3;

    public Arm (DcMotorEx armIn) {
        theArm = armIn;
        startPos = theArm.getCurrentPosition();
        theArm.setTargetPosition(startPos);
        gearRatio = 40;
    }

    public Arm (DcMotorEx armIn, double gearRatioIn, int ticksPerRotationIn) {
        theArm = armIn;
        startPos = theArm.getCurrentPosition();
        theArm.setTargetPosition(startPos);
        gearRatio = gearRatioIn;
        ticksPerRotation = ticksPerRotationIn;
        oneDegree = (ticksPerRotation * gearRatio)/360.0;
    }

    public void moveArm (double targetAngle) {
        //one rotation = 1120 ticks in the woods
        theArm.setTargetPosition ((int)(targetAngle*oneDegree) + ((theArm.getCurrentPosition()/(int)(ticksPerRotation * gearRatio)) * (int)(ticksPerRotation * gearRatio)));
        theArm.setPower (slowSpeed*Math.signum (theArm.getTargetPosition() - theArm.getCurrentPosition()));
    }
    public void moveArmFast (double targetAngle) {
        //one rotation = 1120 ticks in the woods
        theArm.setTargetPosition ((int)(targetAngle*oneDegree) + ((theArm.getCurrentPosition()/(int)(ticksPerRotation * gearRatio)) * (int)(ticksPerRotation * gearRatio)));
        theArm.setPower (Math.signum (theArm.getTargetPosition() - theArm.getCurrentPosition()));
    }
    public void rotateArm (int directionMultiplier, double rotationTimes) {
        theArm.setTargetPosition((int)((ticksPerRotation * gearRatio)*directionMultiplier*rotationTimes));
        theArm.setPower(directionMultiplier);
    }
    public double getCurrentAngle () {
        return (double)(theArm.getCurrentPosition()/oneDegree);
    }
    public boolean atTarget () {
        if (Math.signum(theArm.getPower()) == Math.signum (theArm.getCurrentPosition() - theArm.getTargetPosition())){
            theArm.setPower (0);
            return true;
        }
        return false;
    }
    public boolean atTargetPrecision () {
//        theArm.setPower ((theArm.getTargetPosition() - theArm.getCurrentPosition())/280.0);
        theArm.setPower ((theArm.getTargetPosition() - theArm.getCurrentPosition())/(28.0 * gearRatio / 4));
        if (theArm.getPower() > 0 && theArm.getPower() <= slowSpeed) {
            theArm.setPower(slowSpeed);
        }
        if (theArm.getPower() < 0 && theArm.getPower() >= -1 * slowSpeed) {
            theArm.setPower(-1 * slowSpeed);
        }
        if (theArm.getTargetPosition() - theArm.getCurrentPosition() >= -1 * oneDegree && theArm.getTargetPosition() - theArm.getCurrentPosition() <= oneDegree) {
            theArm.setPower(0);
        }
        return 0 == Math.abs(theArm.getPower());
    }
    public void setArmPower (double inPower) {
        theArm.setPower (inPower);
    }
}

