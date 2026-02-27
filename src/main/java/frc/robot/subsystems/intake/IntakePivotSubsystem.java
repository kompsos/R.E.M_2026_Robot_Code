// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.TurretConstants;

public class IntakePivotSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  public SparkMax pivotSpark;

  public IntakePivotSubsystem() {    
    pivotSpark = new SparkMax(40, MotorType.kBrushless);

    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(40);
    basicSparkMaxConfig.encoder.positionConversionFactor(0.04 * 0.158);

    SoftLimitConfig softLimitConfig = new SoftLimitConfig();
    
    softLimitConfig.forwardSoftLimitEnabled(true);
    softLimitConfig.reverseSoftLimitEnabled(true);

    basicSparkMaxConfig.apply(softLimitConfig);
    pivotSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    pivotSpark.getEncoder().setPosition(0);
  }

  public void rotate(double speed) {
    pivotSpark.set(speed);
  }

  public void stopRotate() {
    pivotSpark.stopMotor();
  }
}
