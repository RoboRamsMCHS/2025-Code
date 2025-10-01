//Goodbye world
// I AM Cassiel Menendez.
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;


// Differential Drive class
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//Controller libraries
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

//Motor Control Library
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

// Camera Library
import edu.wpi.first.cameraserver.CameraServer;

// Timer Library 
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {



  // This space is where you create all of your objects and specify where each of the objects is.
  

    

    // Controllers
    private XboxController driver = new XboxController(0);  // Xbox Controller plugged into DriverStation Port 0
    private Joystick controller = new Joystick(1);          // Joystic Controller plugged into DriverStation port 1

    // PWM Motor controllers
    private Talon oldTalon = new Talon(0);               // Plugged into PWM port 0
    private PWMSparkMax sparkMax = new PWMSparkMax(1);   // Plugged into PWM port 1
    private Talon leftDrive = new Talon(2);              // Plugged into PWM port 3
    private Talon rightDrive = new Talon(3);             // Plugged into PWM port 3
   
    // CAN Motor controllers
    private TalonSRX newTalon = new TalonSRX(0);    // Can ID # 0
    private VictorSPX victor = new VictorSPX(1);    // Can ID # 1

    // Timer 
    private final Timer timer = new Timer();
    
    // Drive Train Math 
    private DifferentialDrive driveTrain = new DifferentialDrive(leftDrive::set, rightDrive::set);


  public Robot() {

    // This is how you start the live feed to the camera
    CameraServer.startAutomaticCapture();
    
    /*  We invert one side of the drive train so that when we
        give both sides a positive value, they work together to
        in one direction instead of turning
    */
    rightDrive.setInverted(true);
  
  }


// The robotPeriodic(){} block of code is called continously while the robot is enabled
  @Override
  public void robotPeriodic() {}

// The autonomousInit() {} method is called once at the beggining of autonomous mode
  @Override
  public void autonomousInit() {
    timer.restart();
  }

// The autonomousPeriodic() {} method is called periodically while in autonomous mode
  @Override
  public void autonomousPeriodic() {}

// The teleopInit() {} method is called once at the beggining of teleoperated mode or when driver controls begin
  @Override
  public void teleopInit() {}

  // The teleopPeriodic() {} method is called periodically while in teleoperated mode or while the driver has control
  @Override
  public void teleopPeriodic() {

    // This line is representative of how you could control the drive train of a skid steer robot.
    driveTrain.arcadeDrive(driver.getLeftY(), -driver.getRightY());

    // These two lines set the speed of a motor based on the position of a certain joystick
    oldTalon.set(-controller.getX());
    newTalon.set(ControlMode.PercentOutput, -controller.getY());

    // These two lines give a motor an output based on if a certain button is pressed or not
    if(driver.getBButton()){victor.set(ControlMode.PercentOutput, 1);} else if (driver.getAButton()){victor.set(ControlMode.PercentOutput, -1);} else{victor.set(ControlMode.PercentOutput, 0);}
    if(controller.getRawButton(7)){sparkMax.set(1);} else if (controller.getRawButton(8)){sparkMax.set(-1);} else {sparkMax.stopMotor();}

  }

  // The disabledInit() {} method is called once at the beggining of disabled mode
  public void disabledInit() {}

   // The disabledPeriodic() {} method is called periodically while the robot is disabled
  @Override
  public void disabledPeriodic() {}

//  The testInit() {} method is called once at the start of test mode
  @Override
  public void testInit() {}

  //  The testPeriodic() {} method is called periodically during test mode
  @Override
  public void testPeriodic() {}


}
