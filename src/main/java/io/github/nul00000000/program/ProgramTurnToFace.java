package io.github.nul00000000.program;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import io.github.nul00000000.jssc.SerialPortListener;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class ProgramTurnToFace implements Program {
	
	private Mat frameGrey = new Mat();
	private MatOfRect faces2 = new MatOfRect();
	private CascadeClassifier faceCascade = new CascadeClassifier();
	private CascadeClassifier faceCascade2 = new CascadeClassifier();
	
	private SerialPort port;
	private SerialPortListener listener;
	
	public ProgramTurnToFace() {
		init();
	}

	public void init() {
		port = new SerialPort(SerialPortList.getPortNames()[0]);
		if(!port.isOpened()) {
			try {
				port.openPort();
				port.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

				port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                        SerialPort.FLOWCONTROL_RTSCTS_OUT);
				listener = new SerialPortListener(port);
				port.addEventListener(listener, SerialPort.MASK_RXCHAR);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		if(!faceCascade.load("C:/OpenCV-4.2.0/opencv/build/etc/lbpcascades/lbpcascade_frontalface.xml")) {
			System.err.println("LBP Face Cascade could not load");
			System.exit(0);
		}
		if(!faceCascade2.load("C:/OpenCV-4.2.0/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml")) {
			System.err.println("Haar Face Cascade could not load");
			System.exit(0);
		}
	}

	public Mat process(Mat frame) {
		Imgproc.cvtColor(frame, frameGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGrey, frameGrey);
		
		faceCascade2.detectMultiScale(frameGrey, faces2);
		Rect largest = new Rect();
		List<Rect> lof = faces2.toList();
		for(Rect face : lof) {
			if(face.width > largest.width) {
				largest.width = face.width;
				largest.x = face.x;
				largest.y = face.y;
			}
			if(face.height > largest.height) {
				largest.height = face.height;
				largest.x = face.x;
				largest.y = face.y;
			}
		}
		Imgproc.rectangle(frame, largest, new Scalar(255, 0, 255));
		try {
			if(!largest.empty()) {
				if(largest.x + largest.width / 2f < 155) {
					port.writeInt(0);
				} else if(largest.x + largest.width / 2f > 165) {
					port.writeInt(2);
				} else {
					port.writeInt(1);
				}
			} else {
				port.writeInt(1);
			}
//			else {
//				port.writeByte((byte) 0);
//			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		return largest.area() > 0 ? frame.submat(largest) : frame;
	}

}
