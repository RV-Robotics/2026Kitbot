package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FuelConstants;

public class FuelSystem extends SubsystemBase {
  private final SparkMax roller;
  private final SparkMax feeder;
  private final double delayTime = 1.0;

  public FuelSystem() {
    roller = new SparkMax(FuelConstants.ROLLER_MOTOR_ID, MotorType.kBrushless);
    feeder = new SparkMax(FuelConstants.FEEDER_MOTOR_ID, MotorType.kBrushless);

    roller.setCANTimeout(250);
    feeder.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(FuelConstants.ROLLER_CURRENT_LIMIT);
    roller.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    config.smartCurrentLimit(FuelConstants.FEEDER_CURRENT_LIMIT);
    feeder.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {}

  public Command fuelCommand(DoubleSupplier fwd, DoubleSupplier rev) {
    double startTime = Timer.getFPGATimestamp() + delayTime;
    return run(() -> {
      double f = fwd.getAsDouble();
      double b = rev.getAsDouble();
      double rollerSpeed = (3 * f * b) - f - b;
      double feederSpeed = b - f - (b * f);
      roller.set(-rollerSpeed);
      if (Timer.getFPGATimestamp() >= startTime) {
        feeder.set(-feederSpeed);
      }
    });
  }

  public Command fuelOutCommand(DoubleSupplier speed) {
    return run(() -> feeder.set(speed.getAsDouble()));
  }
}


