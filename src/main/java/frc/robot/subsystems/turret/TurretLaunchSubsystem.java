// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.turret;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class TurretLaunchSubsystem extends SubsystemBase {
  /** Creates a new TurretLaunchSubsystem. */
  public SparkMax leftSpark;
  public SparkMax rightSpark;

  public TurretLaunchSubsystem() {
    leftSpark = new SparkMax(Constants.TurretConstants.leftFlywheelSparkID, MotorType.kBrushless);
    rightSpark = new SparkMax(Constants.TurretConstants.rightFlywheelSparkID, MotorType.kBrushless);
    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();
    SparkMaxConfig followerSparkMaxConfig = new SparkMaxConfig();
    basicSparkMaxConfig.smartCurrentLimit(Constants.TurretConstants.launchStall);

    basicSparkMaxConfig.closedLoop.p(Constants.TurretConstants.launchP);
    basicSparkMaxConfig.closedLoop.i(Constants.TurretConstants.launchI);
    basicSparkMaxConfig.closedLoop.d(Constants.TurretConstants.launchD);
    basicSparkMaxConfig.closedLoop.velocityFF(Constants.TurretConstants.launchFF);
    basicSparkMaxConfig.closedLoop.outputRange(0, 1);
    basicSparkMaxConfig.inverted(true);
    followerSparkMaxConfig.smartCurrentLimit(Constants.TurretConstants.launchStall);
    followerSparkMaxConfig.follow(leftSpark, true);
    
    leftSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightSpark.configure(followerSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightSpark.resumeFollowerMode();
  }

  public void revUP(double speed) {
    leftSpark.getClosedLoopController().setSetpoint(speed, ControlType.kVelocity);
  }

  public void controlledRevUP(double distance) {
    leftSpark
    .getClosedLoopController()
    .setSetpoint(
      calculateDistancetoRPM(distance, Constants.TurretConstants.launchEstimationEfficencyFactor
      ), ControlType.kVelocity);
  }

  public void revDown() {
    leftSpark.stopMotor();
  }

  public double calculateDistancetoRPM(double distance, double EF) {
    double rpm = 60 * (
      Math.sqrt(
        (9.81 * (distance/EF))/0.9944) / 0.1595
      ) + 30;

      return rpm;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("TurretRPM", (Math.abs(leftSpark.getEncoder().getVelocity())));
  }
}
