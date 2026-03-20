  package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class USSensors extends SubsystemBase{
    private final DigitalOutput frontTrig, backTrig;
    private final AnalogInput FREcho, FLEcho, BREcho, BLEcho;
    private double FRRange, FLRange, BRRange, BLRange = 0.0;

    private double voltageScaleFactor;
    protected String sensorUnit;

    public USSensors() {
        FREcho = new AnalogInput(0);
        FLEcho = new AnalogInput(1);
        BREcho = new AnalogInput(2);
        BLEcho = new AnalogInput(3);

        frontTrig = new DigitalOutput(0);
        backTrig = new DigitalOutput(1);
        frontTrig.set(false);
        backTrig.set(false);
    }

    public void periodic(){
        voltageScaleFactor = 5 / RobotController.getVoltage5V();

        sensorUnit = SmartDashboard.getBoolean("Inches", true) ? "Inch" : "CM";
        SmartDashboard.putBoolean("Front Enabled", frontTrig.get());
        SmartDashboard.putNumber("Front Avg Range", sensorUnit.equals("Inch") ? getAvgDistanceInches(1) : getAvgDistanceCM(1));
        SmartDashboard.putNumberArray("Front Ranges", sensorUnit.equals("Inch") ? getDistancesInches(1) : getDistancesCM(1));
        SmartDashboard.putBoolean("Back Enabled", backTrig.get());
        SmartDashboard.putNumber("Back Avg Range", sensorUnit.equals("Inch") ? getAvgDistanceInches(-1) : getAvgDistanceCM(-1));
        SmartDashboard.putNumberArray("Back Ranges", sensorUnit.equals("Inch") ? getDistancesInches(1) : getDistancesCM(1));
    }

    public Command enableSensors(){
        return runOnce(()->{
            frontTrig.set(true);
            backTrig.set(true);
        });
    }

    public Command disableSensors(){
        return runOnce(() -> {
            frontTrig.set(false);
            backTrig.set(false);
        });
    }

    public Command setSensors(int sensors, boolean value){
        switch (sensors){
            case 1:
                return runOnce(() -> frontTrig.set(value));
            case -1:
                return runOnce(() -> backTrig.set(value));
            default:
                return runOnce(() -> {
                    frontTrig.set(value);
                    backTrig.set(value);
                });
        }
    }

    public double getAvgDistanceCM(int sensors){
        switch (sensors) {
            case 1:
                FRRange = FREcho.getValue() * voltageScaleFactor * 0.125;
                FLRange = FLEcho.getValue() * voltageScaleFactor * 0.125;
                return (FRRange + FLRange) / 2;
            case -1:
                BRRange = BREcho.getValue() * voltageScaleFactor * 0.125;
                BLRange = BLEcho.getValue() * voltageScaleFactor * 0.125;
                return (BRRange + BLRange) / 2;      
            default:
                return -1.0;
        }
    }

    public double getAvgDistanceInches(int sensors){
        return getAvgDistanceCM(sensors) / 2.54;
    }

    public double[] getDistancesCM(int sensors){
        switch (sensors) {
            case 1:
                FRRange = FREcho.getValue() * voltageScaleFactor * 0.125;
                FLRange = FLEcho.getValue() * voltageScaleFactor * 0.125;
                return new double[] {FRRange, FLRange};
            case -1:
                BRRange = BREcho.getValue() * voltageScaleFactor * 0.125;
                BLRange = BLEcho.getValue() * voltageScaleFactor * 0.125;
                return new double[] {BRRange, BLRange};    
            default:
                return new double[] {-1.0, -1.0};
        }
    }

    public double[] getDistancesInches(int sensors){
        double[] result = getDistancesCM(sensors);
        result[0] /= 2.54; result[1] /= 2.54;
        return result;
    }

    public double getAlignmentError(int sensors){
        double[] sensorValues = sensorUnit.equals("Inch") ? getDistancesInches(sensors) : getDistancesCM(sensors);
        return sensorValues[0] - sensorValues[1];
    }
}
