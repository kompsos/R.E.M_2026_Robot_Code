// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.endgame;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class endGame extends SubsystemBase {
  public SparkMax elevatorLeftSpark;
  public SparkMax elevatorRightSpark;
  public SparkMax elevatorHookSpark;

  /** Creates a new endgame. */
  public endGame() {
    elevator
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
