package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.FuelConstants;

public class FuelSystem extends SubsystemBase {
  private final SparkMax shooter;
  private final SparkMax feeder;
  private final SparkMax intake;

  public FuelSystem() {
    shooter = new SparkMax(FuelConstants.SHOOTER_MOTOR_ID, MotorType.kBrushless);
    feeder = new SparkMax(FuelConstants.FEEDER_MOTOR_ID, MotorType.kBrushless);
    intake = new SparkMax(FuelConstants.INTAKE_MOTOR_ID, MotorType.kBrushless);

    shooter.setCANTimeout(250);
    feeder.setCANTimeout(250);
    intake.setCANTimeout(250);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(FuelConstants.MAX_SHOOTER_CURRENT);
    config.idleMode(IdleMode.kBrake);
    shooter.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.smartCurrentLimit(FuelConstants.MAX_FEEDER_CURRENT);
    feeder.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.smartCurrentLimit(FuelConstants.MAX_INTAKE_CURRENT);
    intake.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Speed", intake.getEncoder().getVelocity());
    SmartDashboard.putNumber("Feeder Speed", feeder.getEncoder().getVelocity());
    SmartDashboard.putNumber("Shooter Speed", shooter.getEncoder().getVelocity());
  }

  public Command fuelCommand(DoubleSupplier fwd, DoubleSupplier rev) {
    return run(() -> {
      double f = fwd.getAsDouble();
      double b = rev.getAsDouble();
      double shooterSpeed = (3 * f * b) - f - b;
      double feederSpeed = b - f - (b * f);
      double intakeSpeed = (3 * f * b) - f - b;
      shooter.set(-shooterSpeed * FuelConstants.MAX_SHOOTER_SPEED);
      intake.set(-intakeSpeed * FuelConstants.MAX_INTAKE_SPEED);
      feeder.set(-feederSpeed * FuelConstants.MAX_FEEDER_SPEED);
    });
  }

  public Command runShooter(DoubleSupplier speed) {
    return run(() -> shooter.set(-speed.getAsDouble()));
  }

  public Command runFeeder(DoubleSupplier speed) {
    return run(() -> feeder.set(-speed.getAsDouble()));
  }

  public Command runIntake(DoubleSupplier speed) {
    return run(() -> intake.set(-speed.getAsDouble()));
  }
}


