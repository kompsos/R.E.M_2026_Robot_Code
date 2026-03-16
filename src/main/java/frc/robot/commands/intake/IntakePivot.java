// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakePivotSubsystem;
import frc.robot.subsystems.intake.IntakePivotSubsystem.pivotScenarios;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakePivot extends Command {
  /** Creates a new IntakePivot. */
  private pivotScenarios mode;
  private pivotScenarios oldMode;
  private IntakePivotSubsystem intakePivotSubsystem;
  public IntakePivot(pivotScenarios modeScenarios, IntakePivotSubsystem intakePivotSubsystem) {
    this.mode = modeScenarios;
    this.intakePivotSubsystem = intakePivotSubsystem;
    oldMode = intakePivotSubsystem.mode;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakePivotSubsystem.updateMode(mode);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakePivotSubsystem.updateMode(oldMode);;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
