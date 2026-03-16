// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class IntakePivotSubsystem extends SubsystemBase {
  /** Creates a new IntakePivotSubsystem. */
  public SparkMax pivotSpark;
  public pivotScenarios mode = pivotScenarios.neutral;

  public static enum pivotScenarios {
    idleDown,
    idleUp,
    activeUp,
    activeDown,
    neutral
  }

  public IntakePivotSubsystem() {
    pivotSpark = new SparkMax(Constants.IntakeConstants.intakePivotSparkID, MotorType.kBrushless);

    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(Constants.IntakeConstants.pivotStall);
    pivotSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void updateMode(pivotScenarios mode) {
    switch (mode) {
      case neutral:
        pivotSpark.stopMotor();
        break;
      case idleUp:
        pivotSpark.set(1 / 6);
        break;
      case idleDown:
        pivotSpark.set(-(1 / 6));
        break;
      case activeUp:
        pivotSpark.set(1 / 4);
        break;
      case activeDown:
        pivotSpark.set(-(1 / 4));
        break;
      default:
        pivotSpark.stopMotor();
        break;
    }
  }
}
