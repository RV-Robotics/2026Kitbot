// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class ControllerConstants {
    public static final int kDriverControllerPort = 0;

    public static final int driveLX = 0;
    public static final int driveLY = 1;
    public static final int driveLT = 2;
    public static final int driveRT = 3;
    public static final int driveRX = 4;
    public static final int driveRY = 5;
    
    public static final int driveA = 1;
    public static final int driveB = 2;
    public static final int driveX = 3;
    public static final int driveY = 4;
    public static final int driveLB = 5;
    public static final int driveRB = 6;
    public static final int driveBack = 7;
    public static final int driveStart = 8;
  }

  public static class DriveConstants {
    public static final int FL_ID = 1;
    public static final int BL_ID = 3;
    public static final int FR_ID = 2;
    public static final int BR_ID = 4;
    public static final int DRIVE_CURRENT_LIMIT = 20;

  }

  public static class FuelConstants {
    public static final int ROLLER_MOTOR_ID = 5;
    public static final int FEEDER_MOTOR_ID = 6;
    public static final int ROLLER_MOTOR_CURRENT_LIMIT = 20;
    public static final int FEEDER_MOTOR_CURRENT_LIMIT = 20;

  }

}
