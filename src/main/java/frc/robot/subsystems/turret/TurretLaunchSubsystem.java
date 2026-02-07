// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.turret;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretLaunchSubsystem extends SubsystemBase {
  /** Creates a new TurretLaunchSubsystem. */
  SparkMax leftSpark;
  SparkMax rightSpark;

  public TurretLaunchSubsystem() {
    leftSpark = new SparkMax(8, MotorType.kBrushless);
    rightSpark = new SparkMax(14, MotorType.kBrushless);
    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(30);
    leftSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void revUP(double speed) {
    leftSpark.set(speed);
    rightSpark.set(-speed);
  }

  public void revDown() {
    leftSpark.stopMotor();
    rightSpark.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
