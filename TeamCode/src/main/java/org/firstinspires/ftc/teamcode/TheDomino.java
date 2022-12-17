package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

public class TheDomino {

    private ColorSensor theColorinator;
    private NormalizedColorSensor theColorNormal;

    public TheDomino (ColorSensor Colorer) {
        theColorinator = Colorer;
        theColorNormal = (NormalizedColorSensor) theColorinator;
    }

    public Integer getColorNumber () {
        //int gain;
        NormalizedRGBA normalizedColors;
        int color;
        float hue;
        float saturation;
        float value;
//float gain = 2;
/* */
//        ((NormalizedColorSensor) theColorinator).setGain(gain);
        // Read color from the sensor.
        normalizedColors = ((NormalizedColorSensor) theColorinator).getNormalizedColors();
        // Convert RGB values to Hue, Saturation, and Value.
        // See https://en.wikipedia.org/wiki/HSL_and_HSV for details on HSV color model.
        color = normalizedColors.toColor();
        hue = JavaUtil.colorToHue(color);
        saturation = JavaUtil.colorToSaturation(color);
        value = JavaUtil.colorToValue(color);


        // Show the color on the Robot Controller screen.
        //JavaUtil.showColor(hardwareMap.appContext, color);
        // Use hue to determine if it's red, green, blue, etc..
    if (hue < 8) {
            return 0;
        } else if (hue < 30) {
            return 1;
        } else if (hue < 60) {
            return 2;
        } else if (hue < 95) { //wus 90
            return 3;
        } else if (hue < 165) { //wus 150
            return 4;
        } else if (hue < 225) {
            return 5;
        } else if (hue < 350) {
            return 6;
        } else {
            return 7;
            /*  FWF - this in't working, we need t0 get a better way to find black and white
            if (saturation < 0.2) {
                return 7;
            } else if (value < 0.16) {
                return 0;
            } else
                return 1;
            */
        }
    }
    public String getColorName () {

        int colorNumber = getColorNumber ();
        switch (colorNumber) {
        case 0:
            return "black";
        case 1:
            return "red";
        case 2:
            return "orange";
        case 3:
            return "yellow";
        case 4:
            return "green";
        case 5:
            return "blue";
        case 6:
            return "purple";
        case 7:
            return "white";
        }
        return "Dunno";
    }

    public Float getHue () {
        //int gain;
        NormalizedRGBA normalizedColors;
        int color;
        float gain = 2;
        /* */
        ((NormalizedColorSensor) theColorinator).setGain(gain);
        // Read color from the sensor.
        normalizedColors = ((NormalizedColorSensor) theColorinator).getNormalizedColors();
        color = normalizedColors.toColor();
        return (Float)JavaUtil.colorToHue(color);
    }
        /* */
        public int getColorRed () {
        return theColorinator.red();
    }

    public int getColorGreen () {
        return theColorinator.green();
    }

    public int getColorBlue () {
        return theColorinator.blue();
    }

}
