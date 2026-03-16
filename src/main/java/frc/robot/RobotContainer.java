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
import frc.robot.commands.turret.EstimatedShoot;
import frc.robot.commands.turret.Shoot;
import frc.robot.commands.turret.ManualJoystickTurretRotate;
import frc.robot.commands.turret.TurretTracker;
import frc.robot.constants.Constants;
import frc.robot.constants.DrivetrainConstants;
import frc.robot.constants.Constants.OIConstants;
import frc.robot.subsystems.SpindexerSubsystem;
import frc.robot.subsystems.endgame.EndGameSubsystem;
import frc.robot.subsystems.intake.IntakePivotSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.IntakePivotSubsystem.pivotScenarios;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.turret.TurretLaunchSubsystem;
import frc.robot.subsystems.turret.TurretRotateSubsystem;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
  public final static Joystick driverJoystick = new Joystick(Constants.OperatorConstants.kDriverControllerPort);
  public final static Joystick operatorJoystick = new Joystick(Constants.OIConstants.kOperatorControllerPort);
  public final static CommandXboxController m_driverController = new CommandXboxController(
      OIConstants.kDriverControllerPort);
  public final static CommandXboxController m_operatorController = new CommandXboxController(
      OIConstants.kOperatorControllerPort);
  public final static SwerveSubsystem swerveSubsystem = new SwerveSubsystem(
      DrivetrainConstants.ChasisConstants.pidgeonGyro);
  public final static TurretRotateSubsystem turretRotateSubsystem = new TurretRotateSubsystem();
  public final static TurretLaunchSubsystem turretLaunchSubsystem = new TurretLaunchSubsystem();
  public final static SpindexerSubsystem spindexerSubsystem = new SpindexerSubsystem();
  public final static IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public final static IntakePivotSubsystem intakePivotSubsystem = new IntakePivotSubsystem();
  public final static EndGameSubsystem endGameSubsystem = new EndGameSubsystem();
  private final SendableChooser<Command> autoChooser;

  // Manual
  Command joystickTurret = new ManualJoystickTurretRotate(turretRotateSubsystem);
  Command manualShot = new Shoot(3500, turretLaunchSubsystem, spindexerSubsystem);
  Command intake = new IntakeBalls(1, intakeSubsystem);
  Command outtake = new Throwup(turretLaunchSubsystem, intakeSubsystem, spindexerSubsystem);

  // Automated
  Command cycleShot = Commands.parallel(
      new EstimatedShoot(turretLaunchSubsystem, spindexerSubsystem, swerveSubsystem),
      new TurretTracker(turretRotateSubsystem, swerveSubsystem),
      new IntakePivot(pivotScenarios.idleUp, intakePivotSubsystem));

  Command intakeUp = new IntakePivot(pivotScenarios.activeUp, intakePivotSubsystem);
  Command intakeDown = new IntakePivot(pivotScenarios.activeDown, intakePivotSubsystem);
  Command endgameUp = new endgamePivot(-1, endGameSubsystem);
  Command endgameDown = new endgamePivot(1, endGameSubsystem);

  public RobotContainer() {
    configureBindings();
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    NamedCommands.registerCommand("Intake Down", intakeDown.withTimeout(1.125));
    NamedCommands.registerCommand("Intake Up", intakeUp.withTimeout(1.125));
    NamedCommands.registerCommand("Endgame Down", endgameDown.withTimeout(3.5));
    NamedCommands.registerCommand("Shoot Targeting", cycleShot);
  }

  private void configureBindings() {
    // Operator Controls

    // Manual Turret Control
    new Trigger(() -> Math.abs(m_operatorController.getRightX()) > 0.0).whileTrue(joystickTurret);

    // Manual Shooting Override
    m_operatorController.rightTrigger().whileTrue(manualShot);

    // Manual Intake Controls
    m_operatorController.leftBumper().onTrue(intakeUp);
    m_operatorController.leftTrigger().onTrue(intakeDown);

    // Automatic Shooting
    m_operatorController.rightBumper().whileTrue(cycleShot);

    // Reset Odometry
    m_operatorController.a().onTrue(new Reset(swerveSubsystem).withTimeout(0.1));

    // Driver Controls

    // Endgame
    m_driverController.povUp().whileTrue(endgameUp);
    m_driverController.povDown().whileTrue(endgameDown);

    // Go to 0,0
    m_driverController.b().whileTrue(new HomeTrajectory(swerveSubsystem));

    // Intake
    m_driverController.rightTrigger().whileTrue(intake);
    m_driverController.leftTrigger().whileTrue(outtake);

    // Intake Pivot
    m_driverController.leftBumper()
        .onTrue(intakeUp.withTimeout(1.25));
    m_driverController.rightBumper()
        .onTrue(intakeDown.withTimeout(1.25));

    // Driving
    swerveSubsystem.setDefaultCommand(new SwerveJoystick(
        swerveSubsystem,
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverYAxis), // Forward/Back
        () -> -driverJoystick.getRawAxis(OIConstants.kDriverXAxis), // Left/Right
        () -> driverJoystick.getRawAxis(OIConstants.kDriverRotAxis),
        () -> DrivetrainConstants.SwerveConstants.fieldOriented));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
