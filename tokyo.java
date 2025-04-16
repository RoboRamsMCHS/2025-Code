package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.PneumaticsModuleType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {


  //private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  
  private AnalogInput sensor = new AnalogInput(0);

  // Motor Controllers 

  private final Talon leftSide = new Talon(0);   // PWM port 0
  private final Talon rightSide = new Talon(1);  // PWM port 1

 // private final PWMSparkMax leftSide = new PWMSparkMax(0);    // PWM port 0
 // private final PWMSparkMax rightSide = new PWMSparkMax(1);   // PWM port 1


  // Xbox Controller
  private final XboxController driver = new XboxController(0);   // USB port 0
  //private final DifferentialDrive drive = new DifferentialDrive(leftSide::set, rightSide::set);


  public Robot() {
    //rightSide.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {

    double sensorValue = sensor.getVoltage();
    final double scaleFactor = 1/(5./1024.);
    double distance = 5*sensorValue*scaleFactor;
    SmartDashboard.putNumber("Distance", distance);
  //  if (driver.getLeftBumperButtonPressed()){solenoid.set(DoubleSolenoid.Value.kForward);} else if (driver.getRightBumperButtonPressed()){solenoid.set(DoubleSolenoid.Value.kReverse);} 
  //drive.arcadeDrive(driver.getLeftY(), driver.getRightX());
  double speed = -driver.getLeftY()*0.6;
  double turn = driver.getRightX()*0.3;

  double left = speed + turn;
  double right = speed - turn;

  leftSide.set(-left*1.0);    // to fix different speed sides change 1.0 accordingly
  rightSide.set(right*1.0); // To fix different speed sides change 1.0 accordingly
  
}

}
