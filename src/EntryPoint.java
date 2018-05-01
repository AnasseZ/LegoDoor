import Model.Controller;
import Model.StateDoor;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * Entry point for the EV3 block 
 * @author anassezougarh
 *
 */
public class EntryPoint {
 
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		LCD.drawString("DomoDoor.", 0, 4);
		Delay.msDelay(1000);
		int action = 0;
		
		// Starting the controller with the initial State
		Controller controller = new Controller(StateDoor.CLOSED);
		boolean running = true;
		
		BluetoothConnection bc = new BluetoothConnection();
		
		while(running) {
			action = bc.getData();
			action = 0; // recupérer depuis la télécommande android
			switch (action) {
			case 1:
				controller.open();
				break;
			case 2:
				controller.close();
				break;
			case 3:
				running = false;
				controller.quit();
				break;
			default:
				break;
			}
		}
		
		LCD.drawString("Bye bye.", 0, 4);
	}

}
