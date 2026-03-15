// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Throwup;
import frc.robot.commands.endgamePivot;
import frc.robot.commands.intake.IntakeBalls;
import frc.robot.commands.intake.IntakePivot;
import frc.robot.commands.swerve.HomeTrajectory;
import frc.robot.commands.swerve.Reset;
import frc.robot.commands.swerve.SwerveJoystick;
import frc.robot.commands.turret.Shoot;
import frc.robot.commands.turret.TurretPIDRotate;
import frc.robot.commands.turret.TurretRotate;
import frc.robot.constants.Constants;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.constants.Constants.OIConstants;
import frc.robot.subsystems.SpindexerSubsystem;
import frc.robot.subsystems.endgame.EndGameSubsystem;
import frc.robot.subsystems.intake.IntakePivotSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;
import frc.robot.subsystems.turret.TurretRotateSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
  public final static Joystick driverJoystick = new Joystick(Constants.OperatorConstants.kDriverControllerPort);
  public final static Joystick operatorJoystick = new Joystick(Constants.OIConstants.kOperatorControllerPort);
  public final static CommandXboxController m_driverController = new CommandXboxController(
      OIConstants.kDriverControllerPort);
  public final static CommandXboxController m_operatorController = new CommandXboxController(
      OIConstants.kOperatorControllerPort);
  private final SendableChooser<Command> autoChooser;
  public final static SwerveSubsystem swerveSubsystem = new SwerveSubsystem(
      DrivetrainConstants.ChasisConstants.pidgeonGyro);
  // public final static PhotonSubsystem photonSubsystem = new
  // PhotonSubsystem(swerveSubsystem);
  public final static TurretRotateSubsystem turretRotateSubsystem = new TurretRotateSubsystem();
  public final static TurretLaunchSubsystem turretLaunchSubsystem = new TurretLaunchSubsystem();
  public final static SpindexerSubsystem spindexerSubsystem = new SpindexerSubsystem();
  public final static IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public final static IntakePivotSubsystem intakePivotSubsystem = new IntakePivotSubsystem();
  public final static EndGameSubsystem endGameSubsystem = new EndGameSubsystem();

  public RobotContainer() {
    configureBindings();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  private void configureBindings() {
    // Operator Controls
    // Manual Turret Control
    new Trigger(() -> m_operatorController.getRightX() > 0.0)
        .whileTrue(new TurretRotate(turretRotateSubsystem));
    new Trigger(() -> m_operatorController.getRightX() < 0.0)
        .whileTrue(new TurretRotate(turretRotateSubsystem));

    // Shoot Turret
    //m_operatorController.rightTrigger().whileTrue(new Shoot(2000, turretLaunchSubsystem, spindexerSubsystem));

    Pose2d goalPose2d;
    if(DriverStation.getAlliance().get() == Alliance.Blue) {
      goalPose2d = new Pose2d(4.620, 4.015, new Rotation2d(0));        
    } else {
      goalPose2d = new Pose2d(11.920, 4.015, new Rotation2d(0));   
    }
    
    /*m_operatorController.rightTrigger().whileTrue(new Shoot(turretLaunchSubsystem.calculateDistancetoRPM(
      MathTools.calculateDistance2Points(swerveSubsystem.getEstimatedPose(), goalPose2d)
    , 0), turretLaunchSubsystem, spindexerSubsystem));*/

    m_driverController.rightTrigger().whileTrue(new Shoot(2500, turretLaunchSubsystem, spindexerSubsystem));
    
    m_operatorController.leftTrigger()
        .whileTrue(new Throwup(turretLaunchSubsystem, intakeSubsystem, spindexerSubsystem));

    // PID Turret Control
    
    /*m_operatorController.leftBumper().whileTrue(
        new TurretPIDRotate(turretRotateSubsystem,
            turretRotateSubsystem.getTurretGoal(RobotContainer.swerveSubsystem.getEstimatedPose(),
                goalPose2d)));*/
                

    m_operatorController.leftBumper().whileTrue(new TurretPIDRotate(turretRotateSubsystem, 90));
    m_operatorController.rightBumper().whileTrue(new TurretPIDRotate(turretRotateSubsystem, -90));
    m_operatorController.a().whileTrue(new TurretPIDRotate(turretRotateSubsystem, 0));

    // Driver Controls

    // Endgame
    m_driverController.povUp().whileTrue(new endgamePivot(1, endGameSubsystem));
    m_driverController.povDown().whileTrue(new endgamePivot(-1, endGameSubsystem));

    // Reset Odometry
    m_driverController.a().onTrue(new Reset(swerveSubsystem).withTimeout(0.1));

    // Go to 0,0
    m_driverController.b().whileTrue(new HomeTrajectory(swerveSubsystem));

    // Intake
    m_driverController.leftTrigger().whileTrue(new IntakeBalls(1, intakeSubsystem));

    // Intake Pivot
    m_driverController.leftBumper().onTrue(new IntakePivot(0.125, intakePivotSubsystem).withTimeout(1.25));
    m_driverController.rightBumper().onTrue(new IntakePivot(-0.125, intakePivotSubsystem).withTimeout(1.25));

    // Driving
    swerveSubsystem.setDefaultCommand(new SwerveJoystick(
        swerveSubsystem,
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverYAxis), // Forward/Back
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverXAxis), // Left/Right
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverRotAxis),
        () -> DrivetrainConstants.SwerveConstants.fieldOriented));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
