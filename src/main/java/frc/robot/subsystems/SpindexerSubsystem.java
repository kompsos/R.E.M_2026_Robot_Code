// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.sim.TalonFXSimState.MotorType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SpindexerSubsystem extends SubsystemBase {
  /** Creates a new SpindexerSubsystem. */
  public SparkMax kicker550;
  public SparkMax indexer;
  public SpindexerSubsystem() {
    kicker550 = new SparkMax(5, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);
    indexer = new SparkMax(2, com.revrobotics.spark.SparkLowLevel.MotorType.kBrushless);
    SparkMaxConfig config = new SparkMaxConfig();
    config.smartCurrentLimit(40);
    indexer.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    kicker550.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void spinUp(double speed) {
    kicker550.set(-speed);
    indexer.set(-speed / 4);
  }

  public void spinDown() {
    kicker550.stopMotor();
    indexer.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("TubeRPM", Math.abs(kicker550.getEncoder().getVelocity()/5));
    SmartDashboard.putNumber("IndexerRPM", Math.abs(indexer.getEncoder().getVelocity()/9));

    SmartDashboard.putNumber("TubeAmperage", kicker550.getOutputCurrent());
    SmartDashboard.putNumber("IndexerAmperage", indexer.getOutputCurrent());
  }
}
