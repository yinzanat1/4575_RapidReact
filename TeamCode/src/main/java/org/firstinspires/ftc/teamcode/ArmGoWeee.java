package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import java.util.Timer;
import java.util.TimerTask;
public class ArmGoWeee {
    private DcMotorEx rightArm;
    private DcMotorEx leftArm;
    private int danceTime = 0;
    private float rightArmsPower = 0.5f;
    private float leftArmsPower = -0.5f;
    private boolean danced = false;
    private int rightStart = 0;
    private int leftStart = 0;

    public ArmGoWeee (DcMotor arm1, DcMotor arm2) {
        rightArm = (DcMotorEx) arm1;
        leftArm = (DcMotorEx) arm2;
        rightStart = rightArm.getCurrentPosition();
        leftStart = leftArm.getCurrentPosition();
    }
    public void armButtons (float rightArmPower, float leftArmPower, boolean everybodyDance){
       rightArm.setPower(rightArmPower * 0.875);
       leftArm.setPower(leftArmPower);

       if (everybodyDance == true && !danced) {
           danced = true;
           everyBodyDanceNow();
       }
    }

    public void everyBodyDanceNow() {
        if (danceTime < 1000) {
            danceTime++;
            //rightArmsPower = rightArmsPower * -1;
            //leftArmsPower = leftArmsPower * -1;
            angleLimit();
            rightArm.setPower(rightArmsPower * 0.875);
            leftArm.setPower(leftArmsPower);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    everyBodyDanceNow();
                }
            }, 10 );
        } else {
            rightArm.setPower(0);
            leftArm.setPower(0);
            danceTime = 0;
            danced = false;
        }
    }
    public void angleLimit() {
        if ((rightArm.getCurrentPosition() >= rightStart + 280 && rightArmsPower > 0) || (rightArm.getCurrentPosition() <= rightStart - 280 && rightArmsPower < 0)) {
            rightArmsPower = rightArmsPower * -1;
        }
        if ((leftArm.getCurrentPosition() >= leftStart + 72 && leftArmsPower > 0) || (leftArm.getCurrentPosition() <= leftStart - 72 && leftArmsPower < 0)) {
            leftArmsPower = leftArmsPower * -1;
        }
    }
}
