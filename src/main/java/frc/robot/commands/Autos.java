// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.FuelSystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public final class Autos {

  public static Command hubShoot(Drivetrain drivetrain, FuelSystem fuelsys) {
    return drivetrain.resetEncoders().withTimeout(0.25)
      .andThen(fuelsys.runShooter(() -> -0.625).withTimeout(0.01))
      .andThen(drivetrain.moveMeters(() -> 1.2192).raceWith(fuelsys.runIntake(() -> -1)))
      .andThen(fuelsys.runFeeder(() -> 1))
    ;
  }

  public static Command none(){
    return new InstantCommand();
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
