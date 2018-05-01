package Model;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;


/**
 * 
 * Controller gérant la gestion de la porte.
 * Nous utilisons uniquement une seul des portes, donc nous avons 2 capteurs et un moteur.
 * @author anassezougarh
 *
 */
public class Controller {
	
	public StateDoor statedoor;
	public StateDoor previousStateDoor;
	
	public Motor leftMotor;
	public Motor rightMotor;

	public EV3TouchSensor frontLeftSensor;
	public EV3TouchSensor sideLeftSensor;
	
	public EV3TouchSensor frontRightSensor;
	public EV3TouchSensor sideRightSensor;
	
	public Controller(StateDoor statedoor) {
		this.statedoor = statedoor;
		
		frontLeftSensor= new EV3TouchSensor(SensorPort.S1);
		sideLeftSensor= new EV3TouchSensor(SensorPort.S2);
		
		frontRightSensor= new EV3TouchSensor(SensorPort.S3);
		sideRightSensor= new EV3TouchSensor(SensorPort.S4);
		
		leftMotor = new Motor();
		rightMotor = new Motor();
		
		// Synchronisation des 2 moteurs
		RegulatedMotor synchronizedMotors[] = new RegulatedMotor[2];
		synchronizedMotors[0] = leftMotor.realMotor;
		rightMotor.realMotor.synchronizeWith(synchronizedMotors);
	}
	
	
	public void open() throws InterruptedException {	
		setCurrentState(StateDoor.OPENING);
		
		if (stopOpening()) {		
			stopDoors();
			setCurrentState(StateDoor.PAUSED);
		}
		
		if(tryingToOpen()) {
			openDoors();
			setCurrentState(StateDoor.OPENED);
		}
		
		if(alReadyOpened()) {
			LCD.drawString("Déjà ouvert!", 0, 4);
			setCurrentState(StateDoor.OPENED);
		}
	}
	
	public void close() {
		setCurrentState(StateDoor.CLOSING);
		
		if (stopClosing()) {		
			stopDoors();
			setCurrentState(StateDoor.PAUSED);
		}
		
		if(tryingToClose()) {
			closeDoors();
			setCurrentState(StateDoor.CLOSED);
		}
		
		if(alReadyClosed()) {
			LCD.drawString("Déjà fermé!", 0, 4);
			setCurrentState(StateDoor.CLOSED);
		}
	}

	// Lorsque la personne veut quitter l'application, on ferme les portes.
	public void quit() {
		if (statedoor == StateDoor.OPENED || statedoor == StateDoor.PAUSED)  {
			setCurrentState(StateDoor.CLOSING);
			closeDoors();
			setCurrentState(StateDoor.CLOSED);
		}
	}
	
	/**
	 * Réaffectation des nouveaux états selon l'action de l'user et l'état actuel des portes
	 * @param newState
	 */
	private void setCurrentState(StateDoor newState) {
		this.previousStateDoor = this.statedoor;
		this.statedoor = newState;
	}
	
	public boolean stopOpening() {
		return statedoor == StateDoor.OPENING 
				&& previousStateDoor == StateDoor.OPENING;
	}
	
	public boolean tryingToOpen() {
		return statedoor == StateDoor.OPENING 
				&& (previousStateDoor == StateDoor.CLOSED || previousStateDoor == StateDoor.PAUSED);
	}
	
	public boolean stopClosing() {
		return statedoor == StateDoor.CLOSING 
				&& previousStateDoor == StateDoor.CLOSING;
	}
	
	public boolean tryingToClose() {
		return statedoor == StateDoor.CLOSING 
				&& (previousStateDoor == StateDoor.OPENED || previousStateDoor == StateDoor.PAUSED);
	}
	
	public boolean alReadyOpened() {
		return statedoor == StateDoor.OPENING 
				&& previousStateDoor == StateDoor.OPENED; 
	}
	
	public boolean alReadyClosed() {
		return statedoor == StateDoor.CLOSING 
				&& previousStateDoor == StateDoor.CLOSED;
	}
	
	// Mouvances des portes
	public void openDoors() {
		rightMotor.realMotor.startSynchronization();
		rightMotor.forward();
		leftMotor.forward();
		rightMotor.realMotor.endSynchronization();
	}
	
	public void stopDoors() {
		rightMotor.realMotor.startSynchronization();
		rightMotor.stop();
		leftMotor.stop();
		rightMotor.realMotor.endSynchronization();
	}

	public void closeDoors() {
		rightMotor.realMotor.startSynchronization();
		rightMotor.forward();
		leftMotor.forward();
		rightMotor.realMotor.endSynchronization();
	}
}
