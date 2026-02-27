// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import static edu.wpi.first.units.Units.Degree;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.turret.TurretRotateSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretPIDRotate extends Command {
  private double angle;
  private TurretRotateSubsystem turret;
  /** Creates a new TurretPIDRotate. */
  public TurretPIDRotate(TurretRotateSubsystem turret, double degrees) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.turret = turret;
    this.angle = degrees;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.setAngle(angle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
