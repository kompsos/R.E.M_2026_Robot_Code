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
import frc.robot.subsystems.turret.TurretRotateSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretFerry extends Command {
  /** Creates a new CompleteAutoShootCommand. */
  SwerveSubsystem swerve;
  TurretLaunchSubsystem turretLaunch;
  TurretRotateSubsystem turretRotate;
  SpindexerSubsystem spindexerSubsystem;

  public TurretFerry(SwerveSubsystem swerve, TurretLaunchSubsystem turretLaunch, TurretRotateSubsystem turretRotateSubsystem, SpindexerSubsystem spindexerSubsystem) {
    this.swerve = swerve;
    this.turretLaunch = turretLaunch;
    this.turretRotate = turretRotateSubsystem;
    this.spindexerSubsystem = spindexerSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
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
      goalPose2d = new Pose2d(1.528, 4.015, new Rotation2d(0));        
    } else {
      goalPose2d = new Pose2d(15.242, 4.015, new Rotation2d(0));   
    }
    
    double goalRotation = turretRotate.getTurretGoal(swerve.getEstimatedPose(), goalPose2d);

    double goalRPM = turretLaunch.calculateDistancetoRPM(MathTools.calculateDistance2Points(swerve.getEstimatedPose(), goalPose2d), 0.465);
    turretLaunch.revUP(goalRPM);
      
    turretRotate.setAngle(goalRotation);

    if((turretLaunch.leftSpark.getEncoder().getVelocity() >= goalRPM - 150)) {
      spindexerSubsystem.spinUp(1);
    } else {
      spindexerSubsystem.spinDown();
    }


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    spindexerSubsystem.spinDown();
    turretLaunch.revDown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
