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
    //
    public static final double WHEEL_DIAMETER = 0.1524; // 6" wheels
    public static final double GEAR_RATIO = 10.71; // AM14U6 default
    public static final double METERS_PER_ROTATION = (Math.PI * WHEEL_DIAMETER) / GEAR_RATIO;
    public static final double TRACK_WIDTH_METERS = 0.546;

    //
    public static final int FL_ID = 1;
    public static final int BL_ID = 3;
    public static final int FR_ID = 2;
    public static final int BR_ID = 4;

    //
    public static final int MAX_DRIVE_CURRENT = 20;
    public static final double MAX_DRIVE_SPEED = 12;
    public static final double MAX_TURN_SPEED = 16;
  };


  public static class FuelConstants {
    //
    public static final int INTAKE_MOTOR_ID = 5;
    public static final int MAX_INTAKE_CURRENT = 20;
    public static final double MAX_INTAKE_SPEED = 1.0;
    //
    public static final int FEEDER_MOTOR_ID = 6;
    public static final int MAX_FEEDER_CURRENT = 20;
    public static final double MAX_FEEDER_SPEED = 1;
    //
    public static final int SHOOTER_MOTOR_ID = 7;
    public static final int MAX_SHOOTER_CURRENT = 35;
    public static final double MAX_SHOOTER_SPEED = 0.675;
  }
}
