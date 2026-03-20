package frc.robot;

import java.util.Optional;

// import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.cscore.UsbCamera;
// import edu.wpi.first.cscore.VideoMode;
// import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import frc.robot.Constants.ControllerConstants;
// import frc.robot.commands.Autos;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.FuelSystem;

 public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();
  private final FuelSystem fuelsub = new FuelSystem();
  // Start automatic capture of the first USB camera
  // private final UsbCamera camera;
  

  private final CommandGenericHID driveCon = new CommandGenericHID(ControllerConstants.drivePort);
  private final CommandGenericHID opCon = new CommandGenericHID(ControllerConstants.controlPort);
  // next commit text US Sensors + Hub Lock
  public RobotContainer() {
    configureBindings();
    // camera = CameraServer.startAutomaticCapture();
    // camera.setVideoMode(PixelFormat.kYUYV, 320, 240 , 15);

    SmartDashboard.putBoolean("Use Inches", true);
    SmartDashboard.putString("Alliance Color", getAlliance());
  }
 
  private void configureBindings() {
    drivetrain.setDefaultCommand(drivetrain.driveCurvatureCommand(() -> driveCon.getRawAxis(ControllerConstants.axisRX), () -> driveCon.getRawAxis(ControllerConstants.axisLY)));
    fuelsub.setDefaultCommand(fuelsub.fuelCommand(() -> opCon.getRawAxis(ControllerConstants.axisLT), () -> opCon.getRawAxis(ControllerConstants.axisRT)));
    opCon.button(ControllerConstants.buttonX).whileTrue(fuelsub.fuelOutCommand(() -> 0.75));
    opCon.button(ControllerConstants.buttonB).whileTrue(fuelsub.fuelOutCommand(() -> 0.00));
  }

  public String getAlliance(){
    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent()) {
      if (ally.get() == Alliance.Red) {
        return "RED";
      }
      if (ally.get() == Alliance.Blue) {
        return "BLUE";
      }
    }
    return "NONE";
  }

  public Command getAutonomousCommand() {
    return drivetrain.driveCurvatureCommand(() -> 0, () -> -0.75).withTimeout(1);
  }

}
