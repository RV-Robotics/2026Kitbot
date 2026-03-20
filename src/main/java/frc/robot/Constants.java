package frc.robot;

public final class Constants {

  public static class ControllerConstants {
    public static final int drivePort = 0;
    public static final int controlPort = 1;

    public static final int axisLX = 0;
    public static final int axisLY = 1;
    public static final int axisLT = 2;
    public static final int axisRT = 3;
    public static final int axisRX = 4;
    public static final int axisRY = 5;

    public static final int buttonA = 1;
    public static final int buttonB = 2;
    public static final int buttonX = 3;
    public static final int buttonY = 4;
    public static final int buttonLB = 5;
    public static final int buttonRB = 6;
    public static final int buttonBack = 7;
    public static final int buttonStart = 8;
  }


  public static class DriveConstants {
    public static final int FL_ID = 1;
    public static final int BL_ID = 3;
    public static final int FR_ID = 2;
    public static final int BR_ID = 4;
    public static final int DRIVE_CURRENT_LIMIT = 20;
    public static final double maxDriveOutput = 1;
    public static final double maxTurnOutput = 1;
  };

  public static class FuelConstants {
    public static final int ROLLER_MOTOR_ID = 5;
    public static final int FEEDER_MOTOR_ID = 6;
    public static final int ROLLER_CURRENT_LIMIT = 27;
    public static final int FEEDER_CURRENT_LIMIT = 20;
  }
}
