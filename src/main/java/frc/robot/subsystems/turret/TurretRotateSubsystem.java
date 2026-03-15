// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.turret;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkSoftLimit.SoftLimitDirection;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class TurretRotateSubsystem extends SubsystemBase {
  public SparkMax rotateSpark;
  DigitalInput limitSwitch = new DigitalInput(Constants.TurretConstants.limitSwitchID);

  /** Creates a new TurretSubsystem. */
  public TurretRotateSubsystem() {   
    rotateSpark = new SparkMax(Constants.TurretConstants.rotateSparkID, MotorType.kBrushless);

    SparkMaxConfig basicSparkMaxConfig = new SparkMaxConfig();

    basicSparkMaxConfig.smartCurrentLimit(Constants.TurretConstants.turnStall);
    basicSparkMaxConfig.encoder.positionConversionFactor(Constants.TurretConstants.turnConversionFactor);

    SoftLimitConfig softLimitConfig = new SoftLimitConfig();
    softLimitConfig.forwardSoftLimit(Constants.TurretConstants.maxRotationAmount);
    softLimitConfig.reverseSoftLimit(-Constants.TurretConstants.maxRotationAmount);

    softLimitConfig.forwardSoftLimitEnabled(Constants.TurretConstants.softLimitsEnabled);
    softLimitConfig.reverseSoftLimitEnabled(Constants.TurretConstants.softLimitsEnabled);
    basicSparkMaxConfig.closedLoop.p(Constants.TurretConstants.rotateP);
    basicSparkMaxConfig.closedLoop.i(Constants.TurretConstants.rotateI);
    basicSparkMaxConfig.closedLoop.d(Constants.TurretConstants.rotateD);
    basicSparkMaxConfig.closedLoop.outputRange(
      -Constants.TurretConstants.maxRotationAmount, Constants.TurretConstants.maxRotationAmount
      );

    basicSparkMaxConfig.apply(softLimitConfig);
    rotateSpark.configure(basicSparkMaxConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setAngle(double degrees) {
    rotateSpark.getClosedLoopController().setSetpoint(degrees / 360, ControlType.kPosition);
  }

  public double getTurretGoal(Pose2d robotPose2d, Pose2d goalPoint) {
    double x = goalPoint.getX() - robotPose2d.getX();
    double y = goalPoint.getY() - robotPose2d.getY();

    /*Goal angle is what the robot would need to turn too,
     do not use this, use turretGoal*/
    double goalAngle = Math.atan(y / x) * (360/ (2*Math.PI) );
    if (robotPose2d.getX() > goalPoint.getX())
      goalAngle += 180;

    double turretGoal = goalAngle - robotPose2d.getRotation().getDegrees();

    if(turretGoal < -180) 
      turretGoal += 360;

      if(turretGoal > 180) 
      turretGoal -= 360;
    return turretGoal;
  }

  public void linearRotate(double speed) {
    rotateSpark.set(speed);
  }

  public void stopRotate() {
    rotateSpark.stopMotor();
  }

  public double getTurretAngle() {
    return rotateSpark.getEncoder().getPosition() * 360;
  }

  @Override
  public void periodic() {
    if(!limitSwitch.get()) {
        rotateSpark.getEncoder().setPosition(0);
    }

    SmartDashboard.putNumber("Turret Angle", (getTurretAngle()));
    SmartDashboard.putBoolean("TurretLimit", limitSwitch.get());
  }
}
