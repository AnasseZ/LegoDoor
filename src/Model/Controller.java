package Model;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;


/**
 *
 * Controller gérant la gestion de la porte.
 * @author anassezougarh
 *
 */
public class Controller {

	public StateDoor statedoor;
	public StateDoor previousStateDoor;

	public Motor leftMotor;
	public Motor rightMotor;

	public Sensor frontLeftSensor;
	public Sensor sideLeftSensor;

	public Sensor frontRightSensor;
	public Sensor sideRightSensor;

  public boolean actionFinished;

	public Controller(StateDoor statedoor) {
		this.statedoor = statedoor;
		actionFinished = false;
		
		frontLeftSensor= new Sensor(this, SensorPort.S1);
		sideLeftSensor= new Sensor(this, SensorPort.S2);

		frontRightSensor= new Sensor(this, SensorPort.S3);
		sideRightSensor= new Sensor(this, SensorPort.S4);

		leftMotor = new Motor();
		rightMotor = new Motor();

		// Synchronisation des 2 moteurs
		RegulatedMotor synchronizedMotors[] = new RegulatedMotor[2];
		synchronizedMotors[0] = leftMotor.realMotor;
		rightMotor.realMotor.synchronizeWith(synchronizedMotors);
		
		// Lancement des threads qui s'occuperont de detecter les collisions avec les bords
		frontLeftSensor.start();
		sideLeftSensor.start();
	}


	public void open() throws InterruptedException {
		setCurrentState(StateDoor.OPENING);

		if (stopOpening()) {
			stopDoors();
			setCurrentState(StateDoor.PAUSED);
		}

		if(tryingToOpen()) {
			openDoors();
		    // Tant que pas de collision on continue d'ouvrir la porte
		    actionFinished = false;
		    while(!actionFinished) {
		
		    }
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

    // Tant que pas de collision on continue de fermer la porte
		if(tryingToClose()) {
			closeDoors();
      actionFinished = false;
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
			frontLeftSensor.detecting = false;
			sideLeftSensor.detecting = false;
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

	 public void handleCollision() {
	   stopDoors();
	   actionFinished = true;
	    
	 }
}
