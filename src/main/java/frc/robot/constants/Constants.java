// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class DataLoggingConstants {
    public static Field2d estimatedField = new Field2d();
  }

  public static class VisionConstants {
    public static String backCamera = "limelight-royal";
    public static double maxAllowedAmbiguity = 0.15;
    public static AprilTagFields fieldLayout = AprilTagFields.k2026RebuiltWelded;
  }

  public static final class OIConstants {
    public static final int kDriverYAxis = 1;
    public static final int kDriverXAxis = 0;
    public static final int kDriverRotAxis = 4;
    public static final int kDriverFieldOrientedButtonIdx = 5;
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final double kDeadband = 0.06;
  }

  public static class EndgameConstants {
    public static int leftElevatorMotorSparkID = 16;
    public static int rightElevatorMotorSparkID = 32;
    public static int elevatorHookMotorSparkID = 0;

    public static int elevatorHooksStall = 10;
    public static int elevatorStallPeak = 40; //Divided between both pulling motors
  }

  public static class IntakeConstants {
     public static final int intakePivotSparkID = 40;
     public static final int intakeRollerSparkID = 30;
     public static final int rollerStall = 20;
     public static final int pivotStall = 40;
  }

  public static class TurretConstants {
    public static final int limitSwitchID = 0;
    public static final int turnStall = 5;
    public static final int shootStall = 50;
    public static final int launchStall = 25;
    public static final int rotateSparkID = 62;
    public static final int leftFlywheelSparkID = 8;
    public static final int rightFlywheelSparkID = 14;
    public static final double turnConversionFactor = (0.04 * 0.1323);
    public static final double maxRotationAmount = 1;
    public static final boolean softLimitsEnabled = true;

    public static final double rotateP = 90;
    public static final double rotateI = 0;
    public static final double rotateD = 0;

    public static final double launchP = 0.000005;
    public static final double launchI = 0;
    public static final double launchD = 0;
    public static final double launchFF = 0.0021703;
    public static final double launchEstimationEfficencyFactor = 0.8;
  }
}
