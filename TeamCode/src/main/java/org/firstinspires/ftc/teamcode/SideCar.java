package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class SideCar {
    private CRServo priLelft;
    private CRServo priWright;
    private Servo priKlaw;

    //Changed all the servo names to camel case because it was annoying me, added public in front of the functions for the same reason -Z

    public SideCar (CRServo lelft_in, CRServo wright_in, Servo klaw_in){
        priLelft = lelft_in;
        priWright = wright_in;
        priKlaw = klaw_in;
    }

    public void Drive (boolean stick_up, boolean stick_left, boolean stick_down, boolean stick_right) {
        int left_speed = 0;
        int right_speed = 0;

        if(stick_up){
            left_speed = left_speed-1;
            right_speed = right_speed+1;
        }
        if(stick_left){
            left_speed = left_speed+1;
            right_speed = right_speed+1;
        }
        if(stick_down){
            left_speed = left_speed+1;
            right_speed = right_speed-1;
        }
        if(stick_right){
            left_speed = left_speed-1;
            right_speed = right_speed-1;
        }

        priLelft.setPower(left_speed);
        priWright.setPower(right_speed);
    }
    public void Drive_old (boolean stick_up, boolean stick_left, boolean stick_down, boolean stick_right){
        int left_speed = 0;
        int right_speed = 0;
        /*
        if(stick_up){
            left_speed = left_speed-1;
            right_speed = right_speed+1;
        }
        if(stick_left){
            left_speed = left_speed+1;
            right_speed = right_speed+1;
        }
        if(stick_down){
            left_speed = left_speed+1;
            right_speed = right_speed-1;
        }
        if(stick_right){
            left_speed = left_speed-1;
            right_speed = right_speed-1;
        }
*/
        priLelft.setPower(left_speed);
        priWright.setPower(right_speed);
    }
}
