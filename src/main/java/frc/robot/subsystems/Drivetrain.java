package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.studica.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {

  private final AHRS navx;
  private final SparkMax FL;
  private final SparkMax BL;
  private final SparkMax FR;
  private final SparkMax BR;
  private final RelativeEncoder leftEncoder;
  private final RelativeEncoder rightEncoder;
  private final DifferentialDrive drive;

  private final DifferentialDriveOdometry driveOdometry;
  private final DifferentialDriveKinematics driveKinematics;
  private final SimpleMotorFeedforward driveFeedForward;

  private final PIDController leftPID;
  private final PIDController rightPID;
  private final PIDController drivePID;
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
    config.smartCurrentLimit(DriveConstants.MAX_DRIVE_CURRENT);
    config.idleMode(IdleMode.kBrake);
    config.follow(FL);
    BL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.follow(FR);
    BR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.disableFollowerMode();
    FR.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    config.inverted(false);
    FL.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    leftEncoder = FL.getEncoder();
    rightEncoder = FR.getEncoder();
    drive = new DifferentialDrive(FL, FR);
    drive.setSafetyEnabled(true);

    navx = new AHRS(AHRS.NavXComType.kI2C);
    
    driveKinematics = new DifferentialDriveKinematics(DriveConstants.TRACK_WIDTH_METERS);
    driveOdometry = new DifferentialDriveOdometry(
      Rotation2d.fromDegrees(getHeading()),
      getLeftDistanceMeters(),
      getRightDistanceMeters()
    );
    driveFeedForward = new SimpleMotorFeedforward(0.22,2.2,0.2);

    leftPID = new PIDController(0.3, 0, 0);
    leftPID.setTolerance(0.05);
    rightPID = new PIDController(0.3, 0, 0);
    rightPID.setTolerance(0.05);
    drivePID = new PIDController(0.9, 0, 0);
    drivePID.setTolerance(0.05);
    turnPID = new PIDController(0.01, 0, 0.0008);
    turnPID.enableContinuousInput(-180, 180);
    turnPID.setTolerance(2.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Heading", getHeading());
    SmartDashboard.putNumber("Left Encoder", getLeftEncoder());
    SmartDashboard.putNumber("Right Encoder", getRightEncoder());
    SmartDashboard.putNumber("Left Distance", getLeftDistanceMeters());
    SmartDashboard.putNumber("Right Distance", getRightDistanceMeters());
    SmartDashboard.putNumber("Left Speed", getLeftVelocityMetersPerSecond());
    SmartDashboard.putNumber("Right Speed", getRightVelocityMetersPerSecond());
    SmartDashboard.putNumber("Left Volts", FL.getAppliedOutput() * 12);
    SmartDashboard.putNumber("Right Volts", FR.getAppliedOutput() * 12);
    
    driveOdometry.update(
        Rotation2d.fromDegrees(getHeading()),
        getLeftDistanceMeters(),
        getRightDistanceMeters()
    );
  }

  public Command resetEncoders(){
      return run(() -> {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
      });
  }

  public double getLeftEncoder(){
    return leftEncoder.getPosition();
  }

  public double getRightEncoder(){
    return rightEncoder.getPosition();
  }

  public double getEncoder(){
    return (getLeftEncoder() + getRightEncoder()) / 2;
  }
  
  public double getLeftDistanceMeters(){
    return leftEncoder.getPosition() * DriveConstants.METERS_PER_ROTATION;
  }

  public double getRightDistanceMeters(){
    return -rightEncoder.getPosition() * DriveConstants.METERS_PER_ROTATION;
  }

  public double getDistanceMeters(){
    return (getLeftDistanceMeters() + getRightDistanceMeters()) / 2;
  }

  public double getLeftVelocityMetersPerSecond(){
    return (leftEncoder.getVelocity() / 60.0) * DriveConstants.METERS_PER_ROTATION;
  }

  public double getRightVelocityMetersPerSecond(){
    return (rightEncoder.getVelocity() / 60.0) * DriveConstants.METERS_PER_ROTATION;
  }

  public double getVelocityMetersPerSecond(){
    return (getLeftVelocityMetersPerSecond() + getRightVelocityMetersPerSecond()) / 2;
  }

  public double getHeading() {
    return navx.getYaw();
  }

  public Pose2d getPose() {
    return driveOdometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    driveOdometry.resetPosition(
        Rotation2d.fromDegrees(getHeading()),
        0,
        0,
        pose
    );
  }

  public Command driveCurvatureCommandClosedLoop(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return run(() -> {
        double fwd = MathUtil.applyDeadband(xSpeed.getAsDouble() * DriveConstants.MAX_DRIVE_SPEED, 0.05);
        double turn = MathUtil.applyDeadband(zRotation.getAsDouble() * DriveConstants.MAX_TURN_SPEED, 0.05);
        DifferentialDriveWheelSpeeds wheelSpeeds = driveKinematics.toWheelSpeeds(new ChassisSpeeds(fwd, 0, turn));

        double FFLeft = driveFeedForward.calculate(wheelSpeeds.leftMetersPerSecond);
        double FFRight = driveFeedForward.calculate(wheelSpeeds.rightMetersPerSecond);
        double PIDLeft = leftPID.calculate(getLeftVelocityMetersPerSecond(), wheelSpeeds.leftMetersPerSecond);
        double PIDRight = rightPID.calculate(getRightVelocityMetersPerSecond(), wheelSpeeds.rightMetersPerSecond);
        if (Math.abs(fwd) == 0 && Math.abs(turn) == 0){
          FL.setVoltage(0);
          FR.setVoltage(0);
        } else {
          FL.setVoltage(FFLeft + PIDLeft);
          FR.setVoltage(FFRight + PIDRight);
        }
        
        drive.feed();
    });
  }

  public Command moveMeters(DoubleSupplier targetMeters) {
    return runOnce(() -> {
      drivePID.setSetpoint(getDistanceMeters() + targetMeters.getAsDouble());
      turnPID.setSetpoint(getHeading());
    }).andThen(run(() -> {
      double fwd = drivePID.calculate(getDistanceMeters());
      // double turn = turnPID.calculate(getHeading());
      DifferentialDriveWheelSpeeds wheelSpeeds = driveKinematics.toWheelSpeeds(new ChassisSpeeds(fwd, 0, 0));
      
      double FFLeft = driveFeedForward.calculate(wheelSpeeds.leftMetersPerSecond);
      double FFRight = driveFeedForward.calculate(wheelSpeeds.rightMetersPerSecond);
      double PIDLeft = leftPID.calculate(getLeftVelocityMetersPerSecond(), wheelSpeeds.leftMetersPerSecond);
      double PIDRight = rightPID.calculate(getRightVelocityMetersPerSecond(), wheelSpeeds.rightMetersPerSecond);
      
      FL.setVoltage(MathUtil.clamp(FFLeft + PIDLeft, -12, 12));
      FR.setVoltage(-MathUtil.clamp(FFRight + PIDRight, -12, 12));
      drive.feed();
    }).until(() -> drivePID.atSetpoint()));
  } 

  public Command turnToAngle(DoubleSupplier targetAngle){
    turnPID.setSetpoint(targetAngle.getAsDouble());
    return run(() -> {
      double turn = turnPID.calculate(getHeading());
      DifferentialDriveWheelSpeeds wheelSpeeds = driveKinematics.toWheelSpeeds(new ChassisSpeeds(0, 0, turn));
      
      double FFLeft = driveFeedForward.calculate(wheelSpeeds.leftMetersPerSecond);
      double FFRight = driveFeedForward.calculate(wheelSpeeds.rightMetersPerSecond);
      double PIDLeft = leftPID.calculate(getLeftVelocityMetersPerSecond(), wheelSpeeds.leftMetersPerSecond);
      double PIDRight = rightPID.calculate(getRightVelocityMetersPerSecond(), wheelSpeeds.rightMetersPerSecond);
      
      FL.setVoltage(MathUtil.clamp(FFLeft + PIDLeft, -12, 12));
      FR.setVoltage(MathUtil.clamp(FFRight + PIDRight, -12, 12));
      drive.feed();
    }).until(() -> turnPID.atSetpoint());
  }
}
