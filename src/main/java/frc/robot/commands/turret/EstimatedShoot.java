// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.MathTools;
import frc.robot.subsystems.SpindexerSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class EstimatedShoot extends Command {
  TurretLaunchSubsystem turret;
  SwerveSubsystem swerve;
  double requiredSpeed;
  SpindexerSubsystem spindexerSubsystem;
  public EstimatedShoot(TurretLaunchSubsystem turret, SpindexerSubsystem spindexerSubsystem, SwerveSubsystem swerve) {
    this.turret = turret;
    this.swerve = swerve;
    this.spindexerSubsystem = spindexerSubsystem;
    addRequirements(turret, spindexerSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Pose2d goalPose2d;
    if(DriverStation.getAlliance().get() == Alliance.Blue) {
      goalPose2d = new Pose2d(4.620, 4.015, new Rotation2d(0));        
    } else {
      goalPose2d = new Pose2d(11.920, 4.015, new Rotation2d(0));   
    }
    
    double goalRPM = turret.calculateDistancetoRPM(MathTools.calculateDistance2Points(swerve.getEstimatedPose(), goalPose2d), 0.465);
    turret.revUP(goalRPM);
      
    if(turret.leftSpark.getEncoder().getVelocity() >= goalRPM - 150) {
      spindexerSubsystem.spinUp(1);
    } else {
      spindexerSubsystem.spinDown();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    spindexerSubsystem.spinDown();
    turret.revDown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
