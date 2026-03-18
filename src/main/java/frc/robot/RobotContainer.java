// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.endgame.endgameHooks;
import frc.robot.commands.endgame.endgamePivot;
import frc.robot.commands.intake.IntakeBalls;
import frc.robot.commands.intake.IntakePivot;
import frc.robot.commands.intake.Throwup;
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
  //Controls
  public static Joystick driverJoystick = new Joystick(Constants.OperatorConstants.kDriverControllerPort);
  public static Joystick operatorJoystick = new Joystick(Constants.OIConstants.kOperatorControllerPort);
  public static CommandXboxController m_driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
  public static CommandXboxController m_operatorController = new CommandXboxController(OIConstants.kOperatorControllerPort);
  
  public static SwerveSubsystem swerveSubsystem;
  public static TurretRotateSubsystem turretRotateSubsystem;
  public static TurretLaunchSubsystem turretLaunchSubsystem;
  public static SpindexerSubsystem spindexerSubsystem;
  public static IntakeSubsystem intakeSubsystem;
  public static IntakePivotSubsystem intakePivotSubsystem;
  public static EndGameSubsystem endGameSubsystem;
  private SendableChooser<Command> autoChooser;

    /*
   * Intaking balls is a negative speed value, Intake pivoting is positive for up negative for down
   * Check directions of endgame when get to it
   */

  public RobotContainer() {
    swerveSubsystem = new SwerveSubsystem(DrivetrainConstants.ChasisConstants.pidgeonGyro);
    intakeSubsystem = new IntakeSubsystem();
    intakePivotSubsystem = new IntakePivotSubsystem();
    spindexerSubsystem = new SpindexerSubsystem();
    turretRotateSubsystem = new TurretRotateSubsystem();
    turretLaunchSubsystem = new TurretLaunchSubsystem();
    endGameSubsystem = new EndGameSubsystem();
    autoChooser = AutoBuilder.buildAutoChooser();
    
    SmartDashboard.putData("Auto Chooser", autoChooser);

    NamedCommands.registerCommand("intake", new IntakeBalls(Constants.speedModes.intake, intakeSubsystem));
    NamedCommands.registerCommand("intakeUp", new IntakePivot(Constants.speedModes.activeIntakeUp, intakePivotSubsystem).withTimeout(1.125));
    NamedCommands.registerCommand("intakeDown", new IntakePivot(Constants.speedModes.activeIntakeDown, intakePivotSubsystem).withTimeout(1.125));
    NamedCommands.registerCommand("endgameDown", new endgamePivot(Constants.speedModes.activeIntakeDown, endGameSubsystem).withTimeout(3.5));

    NamedCommands.registerCommand("autoShoot", Commands.parallel(
        new EstimatedShoot(turretLaunchSubsystem, spindexerSubsystem, swerveSubsystem),
        new TurretTracker(turretRotateSubsystem, swerveSubsystem),
        new IntakePivot(Constants.speedModes.manualIntakeUp, intakePivotSubsystem)));

    configureBindings();
  }


  private void configureBindings() {
    // Operator Controls

    // Manual Turret Control
    new Trigger(() -> Math.abs(m_operatorController.getRightX()) > 0.0)
        .whileTrue(new ManualJoystickTurretRotate(turretRotateSubsystem));

    // Manual Shooting Override
    m_operatorController.rightBumper().whileTrue(new Shoot(3500, turretLaunchSubsystem, spindexerSubsystem));

    // Manual Intake Controls
    m_operatorController.leftBumper().whileTrue(new IntakePivot(0.1875, intakePivotSubsystem));
    m_operatorController.rightBumper().whileTrue(new IntakePivot(-0.1875, intakePivotSubsystem));

    // Automatic Shooting
    m_operatorController.rightTrigger().whileTrue(Commands.parallel(
        new EstimatedShoot(turretLaunchSubsystem, spindexerSubsystem, swerveSubsystem),
        new TurretTracker(turretRotateSubsystem, swerveSubsystem),
        new IntakePivot(-0.125, intakePivotSubsystem)));

    // Reset Odometry
    m_operatorController.a().onTrue(new Reset(swerveSubsystem).withTimeout(0.1));

    // Hooks
    m_operatorController.y().whileTrue(new endgameHooks(0.125, endGameSubsystem));
    m_operatorController.b().whileTrue(new endgameHooks(-0.125, endGameSubsystem));
    // Driver Controls

    // Endgame
    m_driverController.povUp().whileTrue(new endgamePivot(1, endGameSubsystem));
    m_driverController.povDown().whileTrue(new endgamePivot(-1, endGameSubsystem));

    // Go to 0,0
    m_driverController.b().whileTrue(new HomeTrajectory(swerveSubsystem));

    // Intake
    m_driverController.rightTrigger().whileTrue(new IntakeBalls(1, intakeSubsystem));
    m_driverController.leftTrigger().whileTrue(new Throwup(turretLaunchSubsystem, intakeSubsystem, spindexerSubsystem));

    // Intake Pivot
    m_driverController.leftBumper()
        .onTrue(new IntakePivot(0.25, intakePivotSubsystem).withTimeout(1.25));
    m_driverController.rightBumper()
        .onTrue(new IntakePivot(-0.25, intakePivotSubsystem).withTimeout(1.25));

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
