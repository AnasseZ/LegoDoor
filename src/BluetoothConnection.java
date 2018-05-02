import java.io.DataInputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class BluetoothConnection {
  
	private static DataInputStream inputStream;
	
	public BluetoothConnection() {
	
		BTConnector BTConnector = (BTConnector) Bluetooth.getNXTCommConnector();
		BTConnection BTConnection = (BTConnection) BTConnector.waitForConnection(3000, NXTConnection.RAW);
	
		inputStream = BTConnection.openDataInputStream();
	}
  
  public int getData() throws IOException {
	  return inputStream.readByte() ;
  }
}


