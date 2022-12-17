package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.Tfod;

import java.util.Date;
import java.util.List;


@Autonomous(name = "AutoMode", preselectTeleOp = "PowerPlay")
public class AutoMode extends LinearOpMode{
    private DcMotorEx northEastMotor;
    private DcMotorEx southEastMotor;
    private DcMotorEx southWestMotor;
    private DcMotorEx northWestMotor;
    private DcMotorEx chainTop;
    private DcMotorEx chainBottom;
    private IMU emuIMU1;
    private IMU emuIMU2;
    private Servo claw;
    private ColorSensor colorer;
    private Integer currentStep = 0;
    private Long eTime = 0L;

    private VuforiaCurrentGame vuforiaPOWERPLAY;
    private Tfod tfod;
    Recognition recognition;

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

        // set up some time
        Date date = new Date();

        // camera
        List<Recognition> recognitions;
        int index;
        vuforiaPOWERPLAY = new VuforiaCurrentGame();
        tfod = new Tfod();
        vuforiaPOWERPLAY.initialize(
                "", // vuforiaLicenseKey
                hardwareMap.get(WebcamName.class, "Webcam 1"), // cameraName
                "", // webcamCalibrationFilename
                false, // useExtendedTracking
                false, // enableCameraMonitoring
                VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE, // cameraMonitorFeedback
                0, // dx
                0, // dy
                0, // dz
                AxesOrder.XZY, // axesOrder
                90, // firstAngle
                90, // secondAngle
                0, // thirdAngle
                true); // useCompetitionFieldTargetLocations
        tfod.useDefaultModel();
        // Set min confidence threshold to 0.7
        tfod.initialize(vuforiaPOWERPLAY, (float) 0.7, true, true);
        // Initialize TFOD before waitForStart.
        // Activate TFOD here so the object detection labels are visible
        // in the Camera Stream preview window on the Driver Station.
        tfod.activate();
        // Enable following block to zoom in on target.
        tfod.setZoom(1, 16 / 9);
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        // Get a list of recognitions from TFOD.
        recognitions = tfod.getRecognitions();
        // If list is empty, inform the user. Otherwise, go
        // through list and display info for each recognition.
        if (JavaUtil.listLength(recognitions) == 0) {
            telemetry.addData("TFOD", "No items detected.");
        } else {
            index = 0;
            // Iterate through list and call a function to
            // display info for each recognized object.
            for (Recognition recognition_item : recognitions) {
                recognition = recognition_item;
                // Display info.
                displayInfo(index);
                // Increment index.
                index = index + 1;
            }
        }


        //Initiates two IMUs for the control and expansion hubs
        emuIMU1 = hardwareMap.get(IMU.class, "imu1");
        emuIMU2 = hardwareMap.get(IMU.class, "imu2");

        emuIMU1.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
            )
        );
        emuIMU2.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
            )
        );
//        MxHeroXYZ tuxEmu1 = new MxHeroXYZ(emuIMU1);
//        MxHeroXYZ tuxEmu2 = new MxHeroXYZ(emuIMU2);

        //init color
        colorer = hardwareMap.get(ColorSensor.class, "color");
        TheDomino dominate = new TheDomino(colorer);

        Integer signalColor = 0;
        Integer dominoColor = 8;
        //auto
        //identify signal color ->  = Loc 1, = Loc 2, = Loc 3
        //domino color -> blue 5 = Blue A5 or Red F2, Red 1 = Blue A2 or Red F2
        dominoColor = dominate.getColorNumber();
        telemetry.speak("I found " + dominoColor.toString() );
        telemetry.addData ("Init", "I found " + dominoColor.toString() );
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
        /*if domino blue turn left, deposit cone on ground, if domino red turn right and do the same
        turn back to original position
        */
            date = new Date();

            telemetry.addData("eTime", eTime);
            telemetry.addData("theTime", date.getTime());
            telemetry.addData("diff", date.getTime() - eTime);
            telemetry.addData("step", currentStep);

            if (currentStep == 0) {
                if (eTime == 0) {   // start of step 1
                    eTime = date.getTime();
                    if (6 >= dominoColor && dominoColor >= 4) {     // green blue purple
                        myDrive.setDrivePower (0, 0.5f,0,0.5f);
                    } else {            // everything else
                        myDrive.setDrivePower (0, 0.5f,0,0.5f);
                    }
                }

                if ((date.getTime() - eTime) < 2000L) {
                    // nothing while waiting
                } else {        // end of step 1
                    eTime = 0L;
                    currentStep++;
                    myDrive.setDrivePower (0, 0,0,0);
                }
            }

            if (currentStep == 1) {
                if (eTime == 0) {   // start of step 1
                    eTime = date.getTime();
                    if (6 >= dominoColor && dominoColor >= 4) {     // green blue purple
                        myDrive.setDrivePower (0, 0.5f,0,-0.5f);
                    } else {            // everything else
                        myDrive.setDrivePower (0, -0.5f,0,0.5f);
                    }
                }

                if ((date.getTime() - eTime) < 1000L) {
                    // nothing while waiting
                } else {        // end of step 1
                    eTime = 0L;
                    currentStep++;
                    myDrive.setDrivePower (0, 0,0,0);
                }
            }


            /*
            //extend the tower
            boxerDoggo.chainBoxTime(10, 1, 0);
            //drive forward into Loc 2
            myDrive.setDriveTime(5, 0, 0.5f, 0);
            //If Loc = 1 drive left, Loc = 2 stay, Loc = 3 drive right
            if (signalColor == 1) {
                myDrive.setDriveTime(5, 0.5f, 0, 0);
            } else if (signalColor == 3) {
                myDrive.setDriveTime(5, -0.5f, 0, 0);
            } else {
                myDrive.setDriveTime(0, 0, 0, 0);
            }

            myDrive.xSaved = 0.0;
            myDrive.ySaved = 0.0;

            telemetry.addData("leftMotorB", myDrive.leftMotorB.getCurrentPosition());
  */
            telemetry.update();
        }
    }
    /**
     * Display info (using telemetry) for a recognized object.
     */
    private void displayInfo(int i) {
        // Display label info.
        // Display the label and index number for the recognition.
        telemetry.addData("label " + i, recognition.getLabel());
        // Display upper corner info.
        // Display the location of the top left corner
        // of the detection boundary for the recognition
        telemetry.addData("Left, Top " + i, Double.parseDouble(JavaUtil.formatNumber(recognition.getLeft(), 0)) + ", " + Double.parseDouble(JavaUtil.formatNumber(recognition.getTop(), 0)));
        // Display lower corner info.
        // Display the location of the bottom right corner
        // of the detection boundary for the recognition
        telemetry.addData("Right, Bottom " + i, Double.parseDouble(JavaUtil.formatNumber(recognition.getRight(), 0)) + ", " + Double.parseDouble(JavaUtil.formatNumber(recognition.getBottom(), 0)));
    }

}

