// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.USSensors;

public class LockToHub extends Command {
    private final Drivetrain drivetrain;
    private final USSensors sensors;

    private final PIDController gyroPID;
    private final PIDController alignPID;
    private final PIDController distancePID;

    public LockToHub(Drivetrain drivetrain, USSensors sensors, double targetHeading, double targetDistanceCM) {
        this.drivetrain = drivetrain;
        this.sensors = sensors;
        addRequirements(drivetrain);

        gyroPID = new PIDController(0.02, 0, 0);
        gyroPID.enableContinuousInput(-180, 180);
        gyroPID.setSetpoint(targetHeading);
        gyroPID.setTolerance(2);

        alignPID = new PIDController(0.02, 0, 0);
        alignPID.setSetpoint(0);
        alignPID.setTolerance(2);

        distancePID = new PIDController(0.02, 0, 0);
        distancePID.setSetpoint(targetDistanceCM);
        distancePID.setTolerance(2);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double turnGyro = gyroPID.calculate(drivetrain.getHeading());
        double errorAlign = sensors.getAlignmentError(-1);
        double turnAlign = alignPID.calculate(errorAlign);
        double forward = distancePID.calculate(sensors.getAvgDistanceCM(-1));

        drivetrain.driveCurvatureCommand(() -> forward, () -> turnGyro + turnAlign);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.driveCurvatureCommand(() -> 0, () -> 0);
    }
    
    @Override
    public boolean isFinished() {
        return gyroPID.atSetpoint() && alignPID.atSetpoint() && distancePID.atSetpoint();
    }
}

