/*     2/9/2025
       This is the main robot code
       This has a live feed camera
       This has arcade drive using an Xbox Controller
       This has an multiple autonomous functions

       Driver Controller Controls
        -Left Joystick Y controls forward backward
        -Right Joystick X controls left and right
        - A button and B button controls Coral Intake

      Controller Controls 
        -Right Joystick Y controls climber
        -Left Joystick Y controls Algae arm
*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

//import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.can.TalonSRX;



public class Robot extends TimedRobot {

private AnalogInput m_US = new AnalogInput(0);

  // Sendable Chooser stuff
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
  //private final Talon climber = new Talon(5);                      // Climber arm            PWM port 5
  private final Talon algaeDislodger = new Talon(5);
  //private final TalonSRX algaeDislodger = new TalonSRX(4);

  // Pneumatics
  private final DoubleSolenoid climber = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  // Xbox Controllers
  private final XboxController driver = new XboxController(0);        // Driver Controller      USB port 0
  private final XboxController controller = new XboxController(1);    // Controller Controller  USB port 1

  //Initializing drivetrain and timer.
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftHoof::set, rightHoof::set);
  private final Timer timer = new Timer();

  
  public Robot() {
    
    CameraServer.startAutomaticCapture();      // Camera start
    rightHoof.setInverted(true);    //So that robot goes straight instead of turning
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
    timer.restart();
    autoSelected = chooser.getSelected();
    System.out.println("Auto selected: " + autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

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

      /*
         This is a base for rewriting the autonomous to drive straight
         if (timer.get() < 3) {rightHoof.set(0.3); leftHoof.set(0.3);}
         else if(timer.get()< 3.25 ) {rigtHoof.stopMotor(); leftHoof.stopMotor(); coralIntake.set(-0.3);}
         else {coralIntake.stopMotor();}
       */
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

    double sensorValue = m_US.getVoltage();
    final double scaleFactor = 1/(5./1024.);
    double distance = 5*sensorValue*scaleFactor;
    SmartDashboard.putNumber("Distance", distance);
    // Driver Controls USB port 0    .set(ControlMode.PercentOutput, driver.getLeftBumperButton())b

    robotDrive.arcadeDrive(driver.getLeftY(), driver.getRightX()); // This drives the robot.
    if(driver.getAButton()){coralIntake.set(-0.4);}
    else if(driver.getBButton()){coralIntake.set(0.4);}
    else{coralIntake.stopMotor();}
    if (driver.getXButton()){algaeDislodger.set(1);}
    else if(driver.getYButton()){algaeDislodger.set(-1);}
    else{algaeDislodger.stopMotor();}
    
    
    // Controller Controls USB port 1

    if(controller.getXButton()==true){algaeIntake.set(0.5);}
    else if(controller.getYButton()==true){algaeIntake.set(-0.5);}
    else{algaeIntake.stopMotor();}
    algaeArm.set(-controller.getLeftY()*0.75); // The algae arm goes up and down with the left joystick of the Controller controller.

    if (controller.getLeftBumperButtonPressed()){climber.set(DoubleSolenoid.Value.kForward);}
    else if (controller.getRightBumperButtonPressed()){climber.set(DoubleSolenoid.Value.kReverse);}
    //algaeDislodger.set(-controller.getRightY());
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
