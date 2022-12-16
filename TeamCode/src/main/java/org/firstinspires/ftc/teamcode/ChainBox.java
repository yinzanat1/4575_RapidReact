package org.firstinspires.ftc.teamcode;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import java.util.Timer;
import java.util.TimerTask;

public class ChainBox {
    public DcMotorEx chainBoxTop;
    public DcMotorEx chainBoxBottom;
private boolean timerOn; // FWF - added this to make the code work
    // constructor
    public ChainBox (DcMotor motor1, DcMotor motor2) {
        chainBoxTop = (DcMotorEx)motor1;
        chainBoxBottom = (DcMotorEx)motor2;
    }
    //basic motor run
    public void runChainBox (float chainY) {
        chainBoxTop.setPower(chainY);
        chainBoxBottom.setPower(chainY);
    }
    public void clawUpDown (float clawY) {
        chainBoxTop.setPower(-1 * clawY);
        chainBoxBottom.setPower(clawY);
    }
    public void chainBoxTime (float chainForTime, float chainY, float clawY) {

        Timer timer = new Timer();
        timerOn = true;
/*
        timer.schedule(new TimerTask() {
            @Override
            public void runn() {
                timerOn = false;
                chainBoxTop.setPower(0);
                chainBoxBottom.setPower(0);
            }
        }, (long) (chainForTime * 1000));
 */
        chainBoxTop.setPower(0.5 * (chainY - clawY));
        chainBoxBottom.setPower(0.5 * (chainY + clawY));
    }
    public void runChainBoxFull (boolean topUp, boolean topDown, boolean bottomUp, boolean bottomDown) {
        if (topUp) {
            chainBoxTop.setPower(0.5);
        } else if (topDown) {
            chainBoxTop.setPower(-0.5);
        } else {
            chainBoxTop.setPower(0.0);
        }
        if (bottomUp) {
            chainBoxBottom.setPower(0.5);
        } else if (bottomDown) {
            chainBoxBottom.setPower(-0.5);
        } else {
            chainBoxBottom.setPower(0.0);
        }
    }
}
