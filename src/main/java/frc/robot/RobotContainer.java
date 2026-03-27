package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.Autos;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.FuelSystem;
import frc.robot.subsystems.LEDs;

 public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();
  private final FuelSystem fuelsys = new FuelSystem();
  private final LEDs leds = new LEDs();

  private SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  private final CommandGenericHID driveCon = new CommandGenericHID(ControllerConstants.drivePort);
  private final CommandGenericHID opCon = new CommandGenericHID(ControllerConstants.controlPort);

  public RobotContainer() {
    configureBindings();

    SmartDashboard.putBoolean("Use Inches", true);
    SmartDashboard.putString("Alliance Color", getAlliance());
  }
 
  private void configureBindings() {
    autoChooser.addOption("None", Autos.none());
    autoChooser.addOption("Full Auto", Autos.hubShoot(drivetrain, fuelsys));
    autoChooser.setDefaultOption("Full Auto", Autos.hubShoot(drivetrain, fuelsys));
    SmartDashboard.putData(autoChooser);

    drivetrain.setDefaultCommand(drivetrain.driveCurvatureCommandClosedLoop(() -> driveCon.getRawAxis(ControllerConstants.axisRX), () -> driveCon.getRawAxis(ControllerConstants.axisLY)));
    // driveCon.button(ControllerConstants.buttonA).onTrue(drivetrain.resetEncoders().ignoringDisable(true));
    // driveCon.povLeft().onTrue(drivetrain.turnToAngle(() -> 45));
    // driveCon.povRight().onTrue(drivetrain.turnToAngle(() -> -45));
    
    fuelsys.setDefaultCommand(fuelsys.fuelCommand(() -> opCon.getRawAxis(ControllerConstants.axisLT), () -> opCon.getRawAxis(ControllerConstants.axisRT)));
    
    leds.setDefaultCommand(leds.ledCommand().ignoringDisable(true));
  }

  public static String getAlliance(){
    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent()) {
      if (ally.get() == Alliance.Red) {
        return "Red";
      }
      if (ally.get() == Alliance.Blue) {
        return "Blue";
      }
    }
    return "NONE";
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

}
