package io.github.nul00000000.jssc;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortListener implements SerialPortEventListener {
	
	public static final int CODE_RECEIVED = 1;
	
	private SerialPort port;
	public boolean ready = true;
//	private ArrayList<Integer> buf;
	
	public SerialPortListener(SerialPort port) {
		this.port = port;
//		buf = new ArrayList<Integer>();
	}

	public void serialEvent(SerialPortEvent e) {
        if(e.isRXCHAR() && e.getEventValue() > 0) {
            try {
            	int[] a = port.readIntArray(e.getEventValue());
            	if(a[0] == 1) {
            		ready = true;
            	}
            }
            catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
    }

}
