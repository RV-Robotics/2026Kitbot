// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Second;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

// Primary Purple Hex: #4B2E83
// Deep, saturated purple — this is the dominant Ridge View color.

// Secondary Lavender Highlight Hex: #A58BCF
// Used in the mane for contrast and depth.

// Black Outline Hex: #000000
// Used for the bold outer edges.

// Dark Gray Hex: #4A4A4A
// Used in shading and interior lines.

// Silver / Light Gray Hex: #C0C0C0
// Used in the mane and accents.

public class LEDs extends SubsystemBase {
private AddressableLED LEDS;
private AddressableLEDBuffer leds;
private AddressableLEDBufferView RSLCopy;
private AddressableLEDBufferView rest;
private AddressableLEDBufferView right;
private AddressableLEDBufferView back;
private AddressableLEDBufferView left;


public LEDs() {
  LEDS = new AddressableLED(0);
  leds = new AddressableLEDBuffer(88);
  RSLCopy = leds.createView(0, 4);
  rest = leds.createView(5, leds.getLength() - 1);
  right = leds.createView(5, 31);
  right = right.reversed();
  back = leds.createView(32, 58);
  left = leds.createView(59, leds.getLength() - 1);

  LEDS.setLength(leds.getLength());
  LEDS.setData(leds);
  LEDS.start();  
}

  @Override
  public void periodic(){
    // This method will be called once per scheduler run
    LEDS.setData(leds);
  }

  public Command ledCommand(){
    return run(() -> {
      Color kRSLOrange = new Color(255, 50, 0);
      Color kRVPurple = new Color(64, 0, 255);
      Color kRVSilver = new Color(32, 32, 24);
      Color allianceColor = (DriverStation.isDSAttached() ? (RobotContainer.getAlliance() == "Red" ? Color.kRed : Color.kBlue) : kRVPurple);
      Color kConnectColor = (DriverStation.isFMSAttached() ? Color.kGreen : (DriverStation.isDSAttached() ? Color.kYellow : Color.kBlack));


      LEDPattern pattern;
      LEDPattern mask;
      
      if (DriverStation.isDisabled()){
        pattern = LEDPattern.solid(kConnectColor);
        pattern.applyTo(RSLCopy);
      } else {
        LEDPattern sync = LEDPattern.solid(kRSLOrange);
        sync.synchronizedBlink(() -> RobotController.getRSLState()).applyTo(RSLCopy);
      }

      if (DriverStation.isDisabled()) {
        // pattern = LEDPattern.steps(wrapMap(allianceColor, Color.kSlateGray, Color.kPurple, Color.kSlateGray, allianceColor));
        // pattern.breathe(Second.of(1)).atBrightness(Percent.of(25)).applyTo(rest);
        pattern = LEDPattern.solid(kRVSilver).blink(Second.of(1)).overlayOn(LEDPattern.solid(kRVPurple));
        pattern.atBrightness(Percent.of(50)).applyTo(right);
        pattern = LEDPattern.steps(wrapMap(Color.kBlack, kConnectColor, Color.kBlack)).overlayOn(LEDPattern.solid(allianceColor).blink(Second.of(0.5)));
        pattern.atBrightness(Percent.of(50)).applyTo(back);
        pattern = LEDPattern.solid(kRVPurple).blink(Second.of(1)).overlayOn(LEDPattern.solid(kRVSilver));
        pattern.atBrightness(Percent.of(50)).applyTo(left);
        
      } else if (DriverStation.isAutonomous() || DriverStation.isAutonomousEnabled()) {
        pattern = LEDPattern.gradient(GradientType.kContinuous, allianceColor, Color.kMediumPurple, Color.kBlack);
        pattern.scrollAtRelativeSpeed(Percent.per(Second).of(15)).atBrightness(Percent.of(50)).applyTo(rest);
      
      } else if (DriverStation.isTeleop() || DriverStation.isTeleopEnabled()) {
        // pattern = LEDPattern.gradient(GradientType.kContinuous, allianceColor, Color.kRed, Color.kPurple);
        // pattern.atBrightness(Percent.of(67)).applyTo(rest);
        pattern = LEDPattern.progressMaskLayer(() -> Math.abs(SmartDashboard.getNumber("Left Volts", 0.0) / 12)).mask(LEDPattern.gradient(GradientType.kDiscontinuous, Color.kRed, Color.kFirstBlue, Color.kMaroon));
        pattern.applyTo(left);
        pattern = LEDPattern.progressMaskLayer(() -> Math.abs(SmartDashboard.getNumber("Right Volts", 0.0) / 12));
        pattern.applyTo(right);
    }
      
    });
  }

  private Map<Number, Color> wrapMap(Color... colors){
    LinkedHashMap<Number, Color> result = new LinkedHashMap<Number, Color>();
    for(int i = 0; i < colors.length; i++){
      result.put((double)i / colors.length, colors[i]);
    }
    return result;
  }
}
