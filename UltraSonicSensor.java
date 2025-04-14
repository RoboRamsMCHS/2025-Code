// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
 
  private AnalogInput sensor = new AnalogInput(0);
 // private XboxController driver = new XboxController(0);
  private Talon leftSide = new Talon(0);
  private Talon rightSide = new Talon(1);
  private DifferentialDrive drive = new DifferentialDrive(leftSide :: set, rightSide :: set);
  private XboxController driver = new XboxController(0);

  double distance;
  public Robot() {
    rightSide.setInverted(true);
  }
  @Override
  public void robotPeriodic(){
    double sensorValue = sensor.getVoltage();
    final double scaleFactor = 1/(5./1024.);
    distance = 5*sensorValue*scaleFactor;
    SmartDashboard.putNumber("Distance", distance);
  }
  // At distance 5000, speed should be 1
  // At distance 400, speed should be 0
  @Override 
  public void autonomousPeriodic(){
    //drive.arcadeDrive(1, 0);
    if (distance > 3000){
      drive.arcadeDrive(1, 0);
    }
    else if(distance >2000){
      drive.arcadeDrive(0.5, 0);
    }
    else if(distance > 1000){
      drive.arcadeDrive(0.3, 0);
    }
    else if(distance > 300){
      drive.arcadeDrive(0.1, 0);
    }
    else{
      drive.arcadeDrive(0, 0);
    }
  }
  @Override
  public void teleopPeriodic() {
     drive.arcadeDrive(driver.getLeftY(), driver.getRightX());
  }

}
