// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.RobotController;

import static edu.wpi.first.units.Units.Percent;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Dimensionless;

public class LEDs extends SubsystemBase {
private AddressableLED m_led;
private AddressableLEDBuffer m_ledBuffer;
private AddressableLEDBufferView m_left;
private AddressableLEDBufferView m_right;

public LEDs() {
  m_led = new AddressableLED(0);
  m_ledBuffer = new AddressableLEDBuffer(120);
  m_left = m_ledBuffer.createView(0, 29);
  m_right = m_ledBuffer.createView(30, 59).reversed();

  m_led.setLength(m_ledBuffer.getLength());
  m_led.setData(m_ledBuffer);
  m_led.start();  
}

  @Override
  public void periodic(){
    // This method will be called once per scheduler run
    m_led.setData(m_ledBuffer);
  }

  public Command rainbow(){
    return runOnce(() -> {
      LEDPattern m_rainbow = LEDPattern.rainbow(255, 128);
      LEDPattern m_scrollingRainbow = m_rainbow.scrollAtAbsoluteSpeed(Units.MetersPerSecond.of(1), Units.Meters.of(1.0/60)).atBrightness(Percent.of(40));
      m_scrollingRainbow.applyTo(m_ledBuffer);
    }).until(() -> DriverStation.isTeleopEnabled()).ignoringDisable(true);
  }

  // public Command allianceIndicatorCommand(){
  //   return runOnce(() -> {
  //     AddressableLEDBufferView station =   
  //     LEDPattern allianceIndicator = LEDPattern.rainbow(255, 128);
  //     LEDPattern m_scrollingRainbow = m_rainbow.scrollAtAbsoluteSpeed(Units.MetersPerSecond.of(1), Units.Meters.of(1.0/60));
  //     m_scrollingRainbow.applyTo(m_ledBuffer);
  //   }).ignoringDisable(true).repeatedly();
  // }
}
