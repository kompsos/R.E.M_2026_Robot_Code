// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SpindexerSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Throwup extends Command {
  /** Creates a new Throwup. */
  TurretLaunchSubsystem turretLaunchSubsystem;
  IntakeSubsystem intakeSubsystem;
  SpindexerSubsystem spindexerSubsystem;

  public Throwup(TurretLaunchSubsystem turretLaunchSubsystem, IntakeSubsystem intakeSubsystem, SpindexerSubsystem spindexerSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.turretLaunchSubsystem = turretLaunchSubsystem;
    this.intakeSubsystem = intakeSubsystem;
    this.spindexerSubsystem = spindexerSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turretLaunchSubsystem.leftSpark.set(0.25);
    spindexerSubsystem.kicker550.set(0.25);
    spindexerSubsystem.indexer.set(0.25);
    intakeSubsystem.IntakeRollerSpark.set(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turretLaunchSubsystem.leftSpark.stopMotor();
    turretLaunchSubsystem.rightSpark.stopMotor();
    spindexerSubsystem.kicker550.stopMotor();
    spindexerSubsystem.indexer.stopMotor();
    intakeSubsystem.IntakeRollerSpark.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
