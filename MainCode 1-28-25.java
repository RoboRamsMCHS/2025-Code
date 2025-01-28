/*     1/28/2025
       This is the main robot code
       This is expecting to use sparkmax for the drive train
       This is also expecting to use talon for control systems
       This has acceleration
       This has a live feed camera
       This has arcade drive.

*/
package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.math.filter.SlewRateLimiter;

public class Robot extends TimedRobot {

  //Initializing motor controllers
  private final Talon leftHoof = new Talon(0);
  private final Talon rightHoof = new Talon(1);
  private final PWMSparkMax multipurpose1 = new PWMSparkMax(2);
  private final PWMSparkMax multipurpose2 = new PWMSparkMax(3);


  //The acceleration aspect of the Robot
  SlewRateLimiter filter = new SlewRateLimiter(0.8);

  
  //Initializing drivetrain, xbox controller, and timer, and multi.
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftHoof::set, rightHoof::set);
  private final XboxController pilot = new XboxController(0);
  private final Timer m_timer = new Timer();
  


  //This is called at the start of the program.
  public Robot() {
    //This method is the equivalent of RobotInit()
    // Camera start
    CameraServer.startAutomaticCapture();

    //So that robot goes straight instead of turning
    rightHoof.setInverted(true);

    //If you run into any problems check this.
    rightHoof.setSafetyEnabled(false);
    leftHoof.setSafetyEnabled(false);

  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
    // Drive for 5 seconds then turn in place.
    if (m_timer.get() < 5) {robotDrive.arcadeDrive(0.1, 0.0, false); }
    else if(m_timer.get() < 10){ robotDrive.arcadeDrive(0, 0.1, false); }
    else { robotDrive.stopMotor(); } 
   }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {

    // If any uncoordination with driving occurs, check here.

    robotDrive.arcadeDrive(filter.calculate(-pilot.getLeftY()), -pilot.getRightX());


    // This can be changed for whatever control mechanisms we decide to have later
    if(pilot.getAButton()==true){multipurpose1.set(-0.5);} else if(pilot.getBButton()==true){multipurpose1.set(0.5);} else{multipurpose1.stopMotor();}
    if(pilot.getXButton()==true){multipurpose2.set(-0.5);} else if(pilot.getYButton()==true){multipurpose2.set(0.5);} else{multipurpose2.stopMotor();}
  }
  

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
