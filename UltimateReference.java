/*
  
  _____       _           _____                         __ _  _   ___   ___  
 |  __ \     | |         |  __ \                       / /| || | / _ \ / _ \ 
 | |__) |___ | |__   ___ | |__) |__ _ _ __ ___  ___   / /_| || || (_) | (_) |
 |  _  // _ \| '_ \ / _ \|  _  // _` | '_ ` _ \/ __| | '_ \__   _> _ < > _ < 
 | | \ \ (_) | |_) | (_) | | \ \ (_| | | | | | \__ \ | (_) | | || (_) | (_) |
 |_|  \_\___/|_.__/ \___/|_|  \_\__,_|_| |_| |_|___/  \___/  |_| \___/ \___/ 
                                                                             
  This code is meant to serve as the ultimate reference guide for the RoboRam coders
  This includes all of the code that we used this year and then some on top of it
  With this you should have a good idea of how to construct code for all your future robots.
  Good luck to all of yall and Go Rams!
                                                    
 */

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
// The two lines of code above are default and just have to be a part of the code

// All of the import lines below are all of the objects that we use on the robot.




/*
 We use a timer, a camera, an Xbox controller, Differential drive which does drivetrain math, Talon motor controllers,
 Sendable chooser to pick from different autonomous functions, smartdashboard so that we can display information on the 
 driver station, Spark max motor controller controlled by PWM, an analog sensor which is an ultrasonic sensor, a double 
 solenoid used which is pneumatics, and the control mode and TalonSRX objects to use the phoenix TalonSRX motor controller.
*/
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


// This line of code right here is the start of the class that we work with called robot.
public class Robot extends TimedRobot {

// The space below is where objects should be created


private AnalogInput m_US = new AnalogInput(0);

  // Sendable Chooser stuff (The names of these objects are what pop up when you pick the autonomous options in the driver station)
  private static final String leftAuto = "Left Side Auto";
  private static final String rightAuto = "Right Side Auto";
  private static final String midAuto = "Middle Auto";
  private static final String escape = "Drive Out";
  private String autoSelected;
  private final SendableChooser<String> chooser = new SendableChooser<>();

  // Motor Controllers 
  private final PWMSparkMax rightHoof = new PWMSparkMax(0);        // Right Drive Train      PWM port 0
  private final PWMSparkMax leftHoof = new PWMSparkMax(1);         // Left Drive Train       PWM port 1
  private final Talon coralIntake = new Talon(2);                  // Coral Intake           PWM port 2
  private final Talon algaeArm = new Talon(3);                     // Algae Arm              PWM port 3
  private final Talon algaeIntake = new Talon(4);                  // Algae Intake           PWM port 4
  private final Talon algaeDislodger = new Talon(5);               // Algae Dislodger        PWM port 5
  private final TalonSRX talon = new TalonSRX(0);             // talon                  CAN ID   0 

  // In order to use the Talon SRX, you must download the CTR library from the vendor dependancies tab in WPILIB VSCODE
  // You also have to download the phoenix X tuner application, set the proper CAN ID, and make sure the firmware of the TalonSRX is up to date.


  // Pneumatics
  private final DoubleSolenoid climber = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  // Xbox Controllers
  private final XboxController driver = new XboxController(0);        // Driver Controller      USB port 0
  private final XboxController controller = new XboxController(1);    // Controller Controller  USB port 1

  //Initializing drivetrain and timer.
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftHoof::set, rightHoof::set);    // Research should be done on how the DifferentialDrive object works
  private final Timer timer = new Timer();

  // This line of code is a constructor for the robot class and runs after all of the objects are created
  
  public Robot() {
    // In this method, if objects need some sort of specification, here is where it would be done

    CameraServer.startAutomaticCapture();      // Camera start
    rightHoof.setInverted(true);    // We invert one side of the drive train so that robot goes straight instead of turning
    rightHoof.setSafetyEnabled(false); // Disables the watchdog timer function off the left Drive Train motors
    leftHoof.setSafetyEnabled(false);  // Disables the watchdog timer function off the right Drive Train motors
  

    // Sendable Chooser stuff
    chooser.setDefaultOption("Drive Out", escape);
    chooser.addOption("Middle Auto", midAuto);
    chooser.addOption("Left Side Coral", leftAuto);
    chooser.addOption("Right Side Coral", rightAuto);
    SmartDashboard.putData("Auto choices", chooser);
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    // The timer starts counting when the robot is turned on. 
    // We want to measure the amount of time that has passed in autonomous so we reset the timer at the beggining of the autonomous period
    timer.restart();
    autoSelected = chooser.getSelected();
    System.out.println("Auto selected: " + autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    // This block of code defines what each specific autonomous function will do.
    switch (autoSelected) {
      case leftAuto:
        if (timer.get() < 1.3) {robotDrive.arcadeDrive(0.4, 0.0, false); }      // Drives Forward
        else if(timer.get() < 1.8){ robotDrive.arcadeDrive(0, -0.3, false); }             // Turns right 
        else if(timer.get() < 4.7){ robotDrive.arcadeDrive(0.3,0,false);}       // Slowly drives forward
        else if(timer.get() < 5){ coralIntake.set(-0.5);}                                                     // Shoots Coral
        else { robotDrive.stopMotor(); coralIntake.stopMotor(); }                                             // Stops Motors
        break;
      case rightAuto:
        if (timer.get() < 1.3) {robotDrive.arcadeDrive(0.4, 0.0, false); }      // Drive forward
        else if(timer.get() < 1.8){ robotDrive.arcadeDrive(0, 0.3, false); }    // Turns left
        else if(timer.get() < 4.7){ robotDrive.arcadeDrive(0.3,0,false);}       // Slowly drives forward
        else if(timer.get() < 5){ coralIntake.set(-0.5);}                                                     // Shoots Coral
        else { robotDrive.stopMotor(); coralIntake.stopMotor(); }                                             // Stops Motors
        break;
      case midAuto:
        if (timer.get() < 3) {robotDrive.arcadeDrive(0.3,0,false);}             // Drives Forward
        else if (timer.get() < 3.25){ coralIntake.set(-0.4); }                                                // Shoots Coral
        else { robotDrive.stopMotor(); coralIntake.stopMotor(); }                                             // Stops Motor
        break;
      case escape:
      default: 
        if(timer.get()< 10){ robotDrive.stopMotor(); }         // Drives Forward
        else if (timer.get() < 11.5){ robotDrive.arcadeDrive(0.4, 0,false);}                                                                        // Stops Motor
    }
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}
  
  
  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    // This is where the the majority of the code is
    // This method is called periodically when the driver is in control of the robot. 

    // The sensor gives a distance in millimeters from objects in front of it.
    double sensorValue = m_US.getVoltage();
    final double scaleFactor = 1/(5./1024.);
    double distance = 5*sensorValue*scaleFactor;
    SmartDashboard.putNumber("Distance", distance);

    // Driver Controls USB port 0
    robotDrive.arcadeDrive(driver.getLeftY(), driver.getRightX()); // This drives the robot using the differential drive class and the left Y axis and the right X axis of the driver controller.
    if(driver.getAButton()){coralIntake.set(-0.4);} else if(driver.getBButton()){coralIntake.set(0.4);} else{coralIntake.stopMotor();} // If the A button is pressed, the coral shoots coral. If the B button is pressed, it backspins the coral. If no button is pressed nothing happens.
    if (driver.getXButton()){algaeDislodger.set(1);} else if(driver.getYButton()){algaeDislodger.set(-1);} else{algaeDislodger.stopMotor();} // If the X button is pressed, the algae dislodger goes out. iF the Y button is pressed, the algae dislodger goes in. Nothing happens with no button press.
    
    
    // Controller Controls USB port 1
    talon.set(ControlMode.PercentOutput, controller.getRightY());  // This sets the motor controller talon to output a speed based on the right Y joystick input
    if(controller.getXButton()==true){algaeIntake.set(0.5);} else if(controller.getYButton()==true){algaeIntake.set(-0.5);} else{algaeIntake.stopMotor();} // If the controller's X button is pressed, the algae intake intakes algae. If the Y button is pressed, the algae intake shoots out algae. If nothing is pressed it just stops
    algaeArm.set(-controller.getLeftY()*0.75); // This sets the algae arm speed to a value based on the left Y joystick Axis;
    if (controller.getLeftBumperButtonPressed()){climber.set(DoubleSolenoid.Value.kForward);} else if (controller.getRightBumperButtonPressed()){climber.set(DoubleSolenoid.Value.kReverse);} // If the left bumper is pressed, the soleonoid pushes the piston forward. If the right bumper is pressed, the solenoid pushes the piston down.

  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
