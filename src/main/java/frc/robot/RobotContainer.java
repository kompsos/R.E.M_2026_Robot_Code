// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.HomeTrajectory;
import frc.robot.commands.Reset;
import frc.robot.commands.SwerveJoystick;
import frc.robot.commands.TurretRPM;
import frc.robot.commands.TurretRotate;
import frc.robot.constants.Constants;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.constants.Constants.OIConstants;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.TurretSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  public final static Joystick driverJoystick = new Joystick(Constants.OperatorConstants.kDriverControllerPort);
  public final static CommandXboxController m_driverController = new CommandXboxController(
      OIConstants.kDriverControllerPort);
  private final SendableChooser<Command> autoChooser;
  public final static SwerveSubsystem swerveSubsystem = new SwerveSubsystem(
      DrivetrainConstants.ChasisConstants.pidgeonGyro);
  public final static PhotonSubsystem photonSubsystem = new PhotonSubsystem(swerveSubsystem);
  public final static TurretSubsystem turretSubsystem = new TurretSubsystem();

  public RobotContainer() {
    configureBindings();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

  }

  private void configureBindings() {

    m_driverController.a().onTrue(new Reset(swerveSubsystem, photonSubsystem).withTimeout(0.1));
    m_driverController.b().whileTrue(new HomeTrajectory(swerveSubsystem));
    m_driverController.povLeft().whileTrue(new TurretRotate(0.0625, turretSubsystem));
    m_driverController.povRight().whileTrue(new TurretRotate(-0.0625, turretSubsystem));
    m_driverController.x().whileTrue(new TurretRPM(-1, turretSubsystem));
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
