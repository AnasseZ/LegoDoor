package Model;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Motor {

	public RegulatedMotor realMotor;
	
	public Motor() {
		 realMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		 realMotor.setSpeed(30);
	}
	
	public void forward() {
		realMotor.forward();
	}
	
	public void backward() {
		realMotor.backward();
	}
	
	public void stop() {
		realMotor.stop();
	}
}
