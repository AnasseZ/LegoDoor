package Model;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class Sensor extends Thread {

  public EV3TouchSensor realSensor;
  public Controller controller;
  public boolean detecting;

  public Sensor(Controller controller, Port port) {
	this.realSensor = new EV3TouchSensor(port);
    this.detecting = true;
    this.controller = controller;
  }
  
  public void run() {
      handleCollision();
  }

  public void handleCollision() {
	  while(detecting) {
		int sampleSize = this.realSensor.sampleSize();
		float[] sample = new float[sampleSize];
		
		this.realSensor.fetchSample(sample,0);
		if (1.0 == sample[0])	this.controller.handleCollision();
    }
  }
}
