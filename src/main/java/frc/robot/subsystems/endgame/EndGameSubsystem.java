// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endgame;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class EndGameSubsystem extends SubsystemBase {
  public SparkMax elevatorLeftSpark;
  public SparkMax elevatorRightSpark;
  public SparkMax elevatorHookSpark;


  /** Creates a new endgame. */
  public EndGameSubsystem() {
    elevatorLeftSpark = new SparkMax(Constants.EndgameConstants.leftElevatorMotorSparkID, MotorType.kBrushless);
    elevatorRightSpark = new SparkMax(Constants.EndgameConstants.rightElevatorMotorSparkID, MotorType.kBrushless);
    elevatorHookSpark = new SparkMax(Constants.EndgameConstants.elevatorHookMotorSparkID, MotorType.kBrushless);

    SparkMaxConfig masterElevatorConfig = new SparkMaxConfig();
    masterElevatorConfig.smartCurrentLimit(Constants.EndgameConstants.elevatorStallPeak);

    elevatorRightSpark.configure(masterElevatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    elevatorLeftSpark.configure(masterElevatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void moveElevator(double set) {
    elevatorRightSpark.set(set);
    elevatorLeftSpark.set(-set);
  }

  public void stopElevator() {
    elevatorRightSpark.stopMotor();

  }

  @Override
  public void periodic() {

  }
}
