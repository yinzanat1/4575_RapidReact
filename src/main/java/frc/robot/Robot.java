// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  /* start of hardware declarations */
        /* motor and motor controller declarations */
  private final CANSparkMax m_frontLeft = new CANSparkMax(1, MotorType.kBrushless);
  private final CANSparkMax m_backLeft = new CANSparkMax(2, MotorType.kBrushless);
  MotorControllerGroup m_leftMotor = new MotorControllerGroup(m_frontLeft, m_backLeft);

  private final CANSparkMax m_frontRight = new CANSparkMax(4, MotorType.kBrushless);
  private final CANSparkMax m_backRight = new CANSparkMax(5, MotorType.kBrushless);
  MotorControllerGroup m_rightMotor = new MotorControllerGroup(m_frontRight, m_backRight);

          // external intake used to be neo 550 but it was changed to a 775 motor 
  //private final CANSparkMax externalIntake = new CANSparkMax(3, MotorType.kBrushless);
//  private final CANSparkMax climber = new CANSparkMax(6, MotorType.kBrushless);
  private final CANSparkMax climber = new CANSparkMax(7, MotorType.kBrushless);   // sparkMax 6 was acting up, switching to 7
//  private final CANSparkMax climber2 = new CANSparkMax(7, MotorType.kBrushless);
//  private final CANSparkMax climber3 = new CANSparkMax(8, MotorType.kBrushless);

  private final Spark intake = new Spark(0);
  private final Spark advancer = new Spark(1);
  private final Spark shooter = new Spark(2);
  private final Spark intakeInOut = new Spark(3);
  private final Spark externalIntake = new Spark(5);          /* 775 motor version of external intake */

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
      /* end of motor and controller delcarations */

      /* controller declarations */
  private final Joystick driverStick = new Joystick(0);
//  private final Joystick opStick = new Joystick(1);
  private final XboxController opStick = new XboxController(1);
  private final XboxController climbStick = new XboxController(2);
      /* end of controller declarations */

      /* limit switch and sensor declarations */
  private DigitalInput climberlimitSwitch = new DigitalInput(0);
  private DigitalInput advancerLimitSwitch = new DigitalInput(1);
      /* end of switch & sensor declarations */
  /* end of hardware declarations */


        /* define variables here that will hold their values from pass to pass */
  private double shooterTime;
  private int autoStep = 0;
  private double autoTime = 0;
  private double driveOffset = 0;   // delay in ms to wait before driving off tarmac
  private double extraDrive = 0;   // extra time to drive to get clear of teammates
private double outputValue = 0;   // for whatever we need, remove for production
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightMotor.setInverted(true);
  }

  @Override
  public void autonomousInit() {

//    SmartDashboard.putNumber("Drive Delay", driveOffset);
//    SmartDashboard.putNumber("Extra Drive Time", extraDrive);

    driveOffset = SmartDashboard.getNumber("Drive Delay (in ms)", driveOffset);
    extraDrive = SmartDashboard.getNumber("Extra Drive Time (in ms)", extraDrive);
    autoStep = 0;
    autoTime = 0;
  }
  public void autonomousPeriodic() {
/* new way to write auto that fran thinks will work
    autoStep tracks what step through auto we are on and autoTime tracks the time in that step.
    each step needs an init event and an end event 
      example:
        if (sautoStep == 4 && autoTime == 0) {   // init event for step 4
          // set some motor to run
        }

        if (autoStep == 4 && autoTime >= 4000) {   // end event for step 4
          // stop some motors, maybe leave them on for a future step to turn off

          autoStep++;         // move to the next step
          autoTime = 0;       // this will tell the next step its time to init
        }
*/
SmartDashboard.putNumber("autoStep", autoStep);
SmartDashboard.putNumber("autoTime", autoTime);
        /* step 0 */
    if (autoStep == 0) {
      shooter.set(-0.8);
    }
    if (autoStep == 0 && autoTime >= 1350) {
      // leaving the shooter on, will turn it off at the end of step 1

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

        /* step 1 */
    if (autoStep == 1 && autoTime == 0) {
      advancer.set(1);
    }

    if (autoStep == 1 && autoTime >= 1000) {
      advancer.set (0);
      shooter.set (0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 2 */
    if (autoStep == 2) {   // tank drive needs constant speed settings, so keep hitting this 
      m_robotDrive.tankDrive(-0.5, -0.5);
    }

    if (autoStep == 2 && autoTime >= 1000) {
      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 3 */
    if (autoStep == 3 && autoTime == 0) {   
      // do nothing, this is a wait step just to let the robot settle before turning
    }

//    if (autoStep == 3 && autoTime >= driveOffset) { 
    if (autoStep == 3 && autoTime >= 0) { 
      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 4 */
    if (autoStep == 4) {      
      intakeInOut.set (-1);
      m_robotDrive.tankDrive(0.5, -0.5);
    }

    if (autoStep == 4 && autoTime >= 950) {
      intakeInOut.set(0);
      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 5 */
    if (autoStep == 5) {      // tank drive needs constant speed settings, so keep hitting this 
        // do nothing, this is just to stop the rotation
    }
    if (autoStep == 5 && autoTime >= 200) {
      m_robotDrive.tankDrive(0, 0);
      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

     /* step 6 */
     if (autoStep == 6) {   // tank drive needs constant speed settings, so keep hitting this 
      intake.set (1);
      externalIntake.set(1);
      m_robotDrive.tankDrive(0.5, 0.5);
    }

//    if (autoStep == 6 && autoTime >= 2200 + extraDrive) {
    if (autoStep == 6 && autoTime >= 2200) {
      m_robotDrive.tankDrive(0, 0);
      intake.set (0);
      externalIntake.set(0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 7 */
    if (autoStep == 7) {      // tank drive needs constant speed settings, so keep hitting this 
      m_robotDrive.tankDrive(-0.5, 0.5);
    }

    if (autoStep == 7 && autoTime >= 1150) {
      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 8 */
    if (autoStep == 8) {   
      m_robotDrive.tankDrive(0.5, 0.5);
      shooter.set(-0.8);
    }

//    if (autoStep == 8 && autoTime >= 3100 + extraDrive) {
    if (autoStep == 8 && autoTime >= 1100) {
//      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

        /* step 9 */
    if (autoStep == 9) {
      advancer.set(1);
    }

    if (autoStep == 9 && autoTime >= 2000) {
      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }
    
        /* step 10 */
    if (autoStep == 10) {
      advancer.set(1);
    }

    if (autoStep == 10 && autoTime >= 500) {
      advancer.set (0);
      shooter.set (0);
      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }

    /* step 11 */
    if (autoStep == 11) {   
      m_robotDrive.tankDrive(-0.5, -0.5);
    }

//    if (autoStep == 11 && autoTime >= 3100 + extraDrive) {
    if (autoStep == 11 && autoTime >= 3100) {
      m_robotDrive.tankDrive(0, 0);

      autoStep++;         // move to the next step
      autoTime = 0;   // this will tell the next step its time to init
    }


    // autonomousPeriodic runs every 20ms so add 20ms to the counter
    autoTime+= 20;
  }
  public void autonomousExit() {
          // turn off all the motors
    m_frontLeft.set(0);
    m_backLeft.set(0);
    m_frontRight.set(0);
    m_backRight.set(0);
    climber.set(0);
//    climber2.set(0);
//    climber3.set(0);
    intake.set(0);
    advancer.set(0);
    shooter.set(0);
    intakeInOut.set(0);
    externalIntake.set(0);
  }
  public void teleopInit() {
        // nothing in here yet
      outputValue = 0;
  }

  public void teleopPeriodic() {
    /* variable definitions. variables defined here will not hold their values on the next pass through */
      // not using X right now but I'm including it here to be complete
    double speedLimit = 1;
    /* start of driver stick buttons */
    if (driverStick.getRawButton(8)) {
      speedLimit = 0.5;
    }
    /* end of driver stick buttons */

    double cleanX = deadbandJoystick(driverStick.getX() * speedLimit),
        cleanY = deadbandJoystick(driverStick.getY() * speedLimit),
        cleanZ = deadbandJoystick(driverStick.getZ() * speedLimit),

        climberSpeedTarget = 0,
        advancerMax = SmartDashboard.getNumber("Advancer Max", 1.0),
        advancerDelay = SmartDashboard.getNumber("Advancer Delay (ms)", 3000),
advancerTime = SmartDashboard.getNumber("Advancer Time (ms)", 0),
        shooterMax = SmartDashboard.getNumber("Shooter Max", 1.0);

           
    m_robotDrive.arcadeDrive(-cleanY, cleanZ);
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the Z turns left and right.
    /* start of operator buttons */
    if (opStick.getYButton() && opStick.getXButton()) {
      intake.set(1);
      externalIntake.set(1);
    } else if (opStick.getYButton()) {
      intake.set(-1);
      externalIntake.set(-1);
    } else {
      intake.set(0);
      externalIntake.set(0);
    }

    if (opStick.getBButton() && opStick.getXButton()) {
      advancer.set(-1 * advancerMax);
advancerTime += 20;
    } else if (opStick.getBButton()) {
      advancer.set(advancerMax);
advancerTime += 20;
    } else {
      advancer.set(0);
    }

    if (opStick.getAButton() && opStick.getXButton()) {
      shooter.set(shooterMax);
      if (shooterTime == 0) {
        shooterTime = System.currentTimeMillis();
      }
      if (System.currentTimeMillis() - shooterTime >= advancerDelay) {
        advancer.set(advancerMax);
      } else {
        advancer.set(0);
      }      
    } else if (opStick.getAButton()) {
      shooter.set(-1 * shooterMax);
      if (shooterTime == 0) {
        shooterTime = System.currentTimeMillis();
      }
      if (System.currentTimeMillis() - shooterTime >= advancerDelay) {
        advancer.set(advancerMax);
      } else {
        advancer.set(0);
      }        
    } else {
      shooter.set(0);
      shooterTime = 0;
    }

    if (opStick.getPOV() == 90) {
      intakeInOut.set(1);
      outputValue+=0.2;
    } else if (opStick.getPOV() == 270) {
      intakeInOut.set(-1);
      outputValue+=0.2;
    } else {
      intakeInOut.set(0);
    }
        /* end of operator buttons */

        /* X Box Controller stick buttons */
        // climber #1
    climberSpeedTarget = climbStick.getLeftY();
    if (climberlimitSwitch.get()) {
      climberSpeedTarget = -1 * climberSpeedTarget;
    }
    climber.set(climberSpeedTarget);
    /* end of X Box Controller buttons */

      /* dashboard logic */
    SmartDashboard.putNumber("climber E", climber.getEncoder().getPosition());

    SmartDashboard.putBoolean("limit switch", climberlimitSwitch.get());

    SmartDashboard.putNumber("climberSpeedTarget", climberSpeedTarget);

    SmartDashboard.putNumber("outputValue", outputValue);
    SmartDashboard.putNumber("Advancer Max", advancerMax);
    SmartDashboard.putNumber("Advancer Delay (ms)", advancerDelay);
SmartDashboard.putNumber("Advancer Time (ms)", advancerTime);
    SmartDashboard.putNumber("Shooter Max", shooterMax);
    SmartDashboard.putBoolean("Advancer Limit", advancerLimitSwitch.get());

    
      /* end of dashboard logic */
  }
  public void teleopEnd() {
      // turn off all motors
    m_frontLeft.set(0);
    m_backLeft.set(0);
    m_frontRight.set(0);
    m_backRight.set(0);
    climber.set(0);
//    climber2.set(0);
//    climber3.set(0);
    intake.set(0);
    advancer.set(0);
    shooter.set(0);
    intakeInOut.set(0);
    externalIntake.set(0);    
}

  private double deadbandJoystick (double inAxis) {
    double retVal;
    if (Math.abs(inAxis) < 0.1) {
      retVal = 0;
    } else {
      retVal = inAxis;
    }
        // square the value for better sensitivity
//    retVal = Math.signum(retVal) * Math.pow(retVal, 2);
  
    return retVal;
  }
}