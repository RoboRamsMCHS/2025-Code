/*     2/9/2025
       This is the main robot code
       This has acceleration
       This has a live feed camera
       This has arcade drive using an Xbox Controller
       This has an autonomous that drives forward and then turns in place

       Driver Controller Controls
        -Left Joystick Y controls forward backward
        -Right Joystick X controls left and right
        -A button and B button controls servo

      Controller Controls 
        -Left Joystick controls climber
        -Right Joystick controls Algae arm
        -X button and Y button control Algae Intake
        -A button and B button control Coral Intake
*/
package frc.robot;


import edu.wpi.first.wpilibj.Servo;
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

  // Motor Controllers 12
  private final PWMSparkMax rightHoof = new PWMSparkMax(0);        // Right Drive Train      PWM port 0
  private final PWMSparkMax leftHoof = new PWMSparkMax(1);         // Left Drive Train       PWM port 1
  private final Talon coralIntake = new Talon(2);                  // Coral Intake           PWM port 2
  private final Talon algaeArm = new Talon(3);                     // Algae Arm              PWM port 3
  private final Talon algaeIntake = new Talon(4);                  // Algae Intake           PWM port 4
  private final Talon climber = new Talon(5);                      // Climber arm            PWM port 5
  private final Servo servo = new Servo(9);                        // Servo                  PWM port 9

  // Xbox Controllers
  private final XboxController driver = new XboxController(0);        // Driver Controller      USB port 0
  private final XboxController controller = new XboxController(1);    // Controller Controller  USB port 1

  //The acceleration aspect of the Robot
  SlewRateLimiter filter = new SlewRateLimiter(0.8);          

  
  //Initializing drivetrain and timer.
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftHoof::set, rightHoof::set);
  private final Timer timer = new Timer();
  


  //This is called at the start of the program.
  //This method is the equivalent of RobotInit()
  public Robot() {
    
    CameraServer.startAutomaticCapture(); // Camera start
    rightHoof.setInverted(true); //So that robot goes straight instead of turning
    rightHoof.setSafetyEnabled(false); // Disables the watchdog timer function off the left Drive Train motors
    leftHoof.setSafetyEnabled(false);  // Disables the watchdog timer function off the right Drive Train motors
  
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (timer.get() < 3) {robotDrive.arcadeDrive(0.6, 0.0, false); } // Drive forward for 3 seconds 
    else if(timer.get() < 10){ robotDrive.arcadeDrive(0, 0.3, false); }// Turns in clockwise direction for 5 seconds
    else { robotDrive.stopMotor(); }  // This stops the motors 
   
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}
  

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {

    

    // Driver Controls USB port 0
    robotDrive.arcadeDrive(filter.calculate(driver.getLeftY()), driver.getRightX()); // This drives the robot.
    if(driver.getAButton() == true){servo.setAngle(0);} else if(driver.getBButton() == true){servo.setAngle(180);} else{servo.setAngle(90);} //This is to test a servo

    // Controller Controls USB port 1
    if(controller.getAButton()==true){coralIntake.set(-0.5);} else if(controller.getBButton()==true){coralIntake.set(0.5);} else{coralIntake.stopMotor();}
    if(controller.getXButton()==true){algaeIntake.set(0.5);} else if(controller.getYButton()==true){algaeIntake.set(-0.5);} else{algaeIntake.stopMotor();}
    algaeArm.set(controller.getRightY()*0.5); // The algae arm goes up and down with the left joystick of the Controller controller.
    climber.set(controller.getLeftY()); 
  }


  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
