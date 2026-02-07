// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.swerve.HomeTrajectory;
import frc.robot.commands.swerve.Reset;
import frc.robot.commands.swerve.SwerveJoystick;
import frc.robot.commands.turret.TurretRPM;
import frc.robot.commands.turret.TurretRotate;
import frc.robot.constants.Constants;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.constants.Constants.OIConstants;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;
import frc.robot.subsystems.turret.TurretRotateSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
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
  public final static PhotonSubsystem photonSubsystem = new PhotonSubsystem(swerveSubsystem);
  public final static TurretRotateSubsystem turretRotateSubsystem = new TurretRotateSubsystem();
  public final static TurretLaunchSubsystem turretLaunchSubsystem = new TurretLaunchSubsystem();

  public RobotContainer() {
    configureBindings();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  private void configureBindings() {
    // Operator Controls
    new Trigger(() -> m_operatorController.getRightX() > 0.0)
        .whileTrue(new TurretRotate(turretRotateSubsystem, -0.5));
            new Trigger(() -> m_operatorController.getRightX() < 0.0)
        .whileTrue(new TurretRotate(turretRotateSubsystem, 0.5));

    m_operatorController.rightTrigger().whileTrue(new TurretRPM(-0.7, turretLaunchSubsystem));
    m_operatorController.leftTrigger().whileTrue(new TurretRPM(0.7, turretLaunchSubsystem));

    // Driver Controls
    m_driverController.a().onTrue(new Reset(swerveSubsystem, photonSubsystem).withTimeout(0.1));
    m_driverController.b().whileTrue(new HomeTrajectory(swerveSubsystem));

    swerveSubsystem.setDefaultCommand(new SwerveJoystick(
        swerveSubsystem,
        () -> driverJoystick.getRawAxis(OIConstants.kDriverYAxis), // Forward/Back
        () -> driverJoystick.getRawAxis(OIConstants.kDriverXAxis), // Left/Right
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverRotAxis),
        () -> DrivetrainConstants.SwerveConstants.fieldOriented));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
