// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.math.MathContext;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;

/** Add your docs here. */
public class MathTools {
    public static double calculateDistance2Points(Pose2d point1, Pose2d point2) {
        return Math.sqrt(
            Math.pow((point2.getX() - point1.getX()), 2) +
            Math.pow((point2.getY() - point1.getY()), 2)
            );
    }
}
