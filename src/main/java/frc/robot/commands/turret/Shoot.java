// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SpindexerSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Shoot extends Command {
  /** Creates a new TurretRPM. */
  TurretLaunchSubsystem turret;
  SpindexerSubsystem spindexerSubsystem;
  double speed;
  public Shoot(double speed, TurretLaunchSubsystem turret, SpindexerSubsystem spindexerSubsystem) {
    this.speed = speed;
    this.turret = turret;
    this.spindexerSubsystem = spindexerSubsystem;
    addRequirements(turret, spindexerSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.revUP(speed);
    spindexerSubsystem.spinUp(1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.revDown();
    spindexerSubsystem.spinDown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
