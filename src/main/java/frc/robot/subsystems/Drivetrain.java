// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  private final SparkMax FL;
  private final SparkMax BL;
  private final SparkMax FR;
  private final SparkMax BR;
  private final DifferentialDrive drive;

  public Drivetrain() {
    FL = new SparkMax(DriveConstants.FL_ID, MotorType.kBrushless);
    BL = new SparkMax(DriveConstants.BL_ID, MotorType.kBrushless);
    FR = new SparkMax(DriveConstants.FR_ID, MotorType.kBrushless);
    BR = new SparkMax(DriveConstants.BR_ID, MotorType.kBrushless);

    drive = new DifferentialDrive(FL, FR);
    drive.setDeadband(0.05);
    FL.setCANTimeout(250);
    BL.setCANTimeout(250);
    FR.setCANTimeout(250);
    BR.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(DriveConstants.DRIVE_CURRENT_LIMIT);

    config.follow(FL);
    BL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    config.follow(FR);
    BR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.disableFollowerMode();
    FR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.inverted(false);
    FL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {}

  public Command driveCurvature(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return runOnce(() -> drive.curvatureDrive(xSpeed.getAsDouble(), zRotation.getAsDouble(), true));
  }
}
