// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.FuelSystem;
import frc.robot.subsystems.USSensors;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public final class Autos {
  
  public static Command drive(Drivetrain drivetrain, double speed, double rotation) {
    return Commands.run(() -> drivetrain.driveCurvatureCommand(() -> speed, () -> rotation), drivetrain);
  }

  public static Command alignDrive(Drivetrain drivetrain, USSensors usSensor, double angle) {
    return Commands.sequence();
  }


  // public static Command shootFuel(FuelSystem subsystem, double fwd, double rev) {
  //   return Commands.sequence(subsystem.fuelCommand(() -> fwd, () -> rev));
  // }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
