package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.studica.frc.AHRS;

import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  private final AHRS navx;
  private final SparkMax FL;
  private final SparkMax BL;
  private final SparkMax FR;
  private final SparkMax BR;
  private final DifferentialDrive drive;

  private final PIDController turnPID;


  public Drivetrain() {
    FL = new SparkMax(DriveConstants.FL_ID, MotorType.kBrushless);
    BL = new SparkMax(DriveConstants.BL_ID, MotorType.kBrushless);
    FR = new SparkMax(DriveConstants.FR_ID, MotorType.kBrushless);
    BR = new SparkMax(DriveConstants.BR_ID, MotorType.kBrushless);

    FL.setCANTimeout(100);
    BL.setCANTimeout(100);
    FR.setCANTimeout(100);
    BR.setCANTimeout(100);

    SparkMaxConfig config = new SparkMaxConfig();
    config.voltageCompensation(12);
    config.smartCurrentLimit(DriveConstants.DRIVE_CURRENT_LIMIT);
    config.idleMode(IdleMode.kBrake);

    config.follow(FL);
    BL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    
    config.follow(FR);
    BR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.disableFollowerMode();
    FR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    config.inverted(false);
    FL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    drive = new DifferentialDrive(FL, FR);
    drive.setDeadband(0.05);
    drive.setSafetyEnabled(true);
    
    navx = new AHRS(AHRS.NavXComType.kI2C);
    navx.reset();

    turnPID = new PIDController(0.02, 0, 0);
    turnPID.enableContinuousInput(-180, 180);
    turnPID.setTolerance(2.0);
  }

  @Override
  public void periodic() {}

  public double getHeading() {
    return navx.getYaw();
  }

  public Command driveCurvatureCommand(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return runOnce(() -> {
      double fwd = Math.copySign(Math.pow(xSpeed.getAsDouble(), 2), xSpeed.getAsDouble()) * DriveConstants.maxDriveOutput;
      double turn = Math.copySign(Math.pow(zRotation.getAsDouble(), 2), zRotation.getAsDouble()) * DriveConstants.maxTurnOutput;
      drive.curvatureDrive(fwd, turn, true);
    });
  }

  public Command turnToAngleCommand(double targetAngle){
    turnPID.setSetpoint(targetAngle);
    double output = turnPID.calculate(getHeading());
    
    return runEnd(() -> driveCurvatureCommand(() -> 0, () -> output),
      () -> driveCurvatureCommand(() -> 0, () -> 0)).until(() -> turnPID.atSetpoint()
    );
  }
}
