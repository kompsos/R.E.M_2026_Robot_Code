// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretSubsystem extends SubsystemBase {
  double speed = 0;
  SparkMax rotateSpark;
  SparkMax leftSpark;
  SparkMax rightSpark;

  /** Creates a new TurretSubsystem. */
  public TurretSubsystem() {
    rotateSpark = new SparkMax(62, MotorType.kBrushless);
    leftSpark = new SparkMax(8, MotorType.kBrushless);
    rightSpark = new SparkMax(14, MotorType.kBrushless);
    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();
    SparkMaxConfig leftSparkMaxConfig = new SparkMaxConfig();
    SparkMaxConfig rightSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(30);
    leftSparkMaxConfig.smartCurrentLimit(30);
    rightSparkMaxConfig.smartCurrentLimit(30);

    rotateSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void rotate(double speed) {
    rotateSpark.set(speed);
  }

  public void revUP(double speed) {
    leftSpark.set(speed);
    rightSpark.set(-speed);
  }

  public void revDown() {
    leftSpark.stopMotor();
    rightSpark.stopMotor();
  }

  public void stopRotate() {
    rotateSpark.stopMotor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
