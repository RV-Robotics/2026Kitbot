// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FuelConstants;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class FuelSystem extends SubsystemBase {
  private final SparkMax roller;
  private final SparkMax feeder;

  public FuelSystem() {
    roller = new SparkMax(FuelConstants.ROLLER_MOTOR_ID, MotorType.kBrushed);
    feeder = new SparkMax(FuelConstants.FEEDER_MOTOR_ID, MotorType.kBrushed);

    roller.setCANTimeout(250);
    feeder.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(FuelConstants.ROLLER_MOTOR_CURRENT_LIMIT);
    roller.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    config.smartCurrentLimit(FuelConstants.FEEDER_MOTOR_CURRENT_LIMIT);
    feeder.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {}

  public Command fuelCommand(String path) {
    switch (path.toUpperCase()) {
      case "IS": // Intake
        return runOnce(() -> {
          roller.set(-0.8);
          Timer.delay(0.25);
          feeder.set(-1.0);
        });

      case "SO": // Shoot
        return runOnce(() -> {
          roller.set(-1.0);
          Timer.delay(0.25);
          feeder.set(1.0);
        });

      case "SI": // Outake
        return runOnce(() -> {
          roller.set(0.8);
          Timer.delay(0.25);
          feeder.set(1.0);
        });
        
      default:
        return runOnce(() -> {
          feeder.set(0);
          roller.set(0);
        });
    }
  }
}


