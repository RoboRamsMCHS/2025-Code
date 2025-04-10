package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

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
    private XboxController driver;   // Xbox Controller plugged into DriverStation Port 0
    private Joystick controller = new Joystick(1);            // Joystic Controller plugged into DriverStation port 1

    // PWM Motor controllers
    private final Talon motor1 = new Talon(0);                    // Plugged into PWM port 0
    private PWMSparkMax motor2 = new PWMSparkMax(1);        // Plugged into PWM port 1
  
    // CAN Motor controllers
    private TalonSRX motor3 = new TalonSRX(0);         // Can ID # 0
    private VictorSPX motor4 = new VictorSPX(1);       // Can ID # 1

    // Timer 
    private final Timer timer = new Timer();

  public Robot() {

    // Camera Implementation
    CameraServer.startAutomaticCapture();
  
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
    // This line of code sets motor1 to the position of the Right Y joytick axis of the xbox controller
    motor1.set(-driver.getRightY());
    
    // This line of code sets a motor to go full forward when the 7 button of the flighstickis pressed, full reverse when the 8 button of the flightstick is pressed, and stopped when neither button is pressed.
    if(controller.getRawButton(7)){motor2.set(1);} else if (controller.getRawButton(8)){motor2.set(-1);} else {motor2.stopMotor();}

    // This line of code sets a motor to the Y axis of the flight Stick
    motor3.set(ControlMode.PercentOutput, -controller.getY());

    // This line of code sets a motor to full throttle when the B button is pressed, full reverse when the A button is pressed, and stopped when neither button is pressed.
    if(driver.getBButton()){motor4.set(ControlMode.PercentOutput, 1);} else if (driver.getAButton()){motor4.set(ControlMode.PercentOutput, -1);} else{motor4.set(ControlMode.PercentOutput, 0);}
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
