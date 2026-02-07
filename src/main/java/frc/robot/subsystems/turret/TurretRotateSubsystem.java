// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.turret;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.TurretConstants;

public class TurretRotateSubsystem extends SubsystemBase {
  SparkMax rotateSpark;

  /** Creates a new TurretSubsystem. */
  public TurretRotateSubsystem() {
    rotateSpark = new SparkMax(TurretConstants.MotorConstants.rotateSparkID, MotorType.kBrushless);

    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(TurretConstants.MotorConstants.rotateSparkID);
    basicSparkMaxConfig.encoder.positionConversionFactor(0.25 * 0.19);
    rotateSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void rotate(double speed) {
      rotateSpark.set(speed);
  }

  public void stopRotate() {
    rotateSpark.stopMotor();
  }

  public double getTurretAngle() {
    return rotateSpark.getEncoder().getPosition() * 360;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Turret Angle", (getTurretAngle()));
  }
}
