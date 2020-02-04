package io.github.nul00000000;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.DataBufferByte;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import com.github.sarxos.webcam.Webcam;

import io.github.nul00000000.program.Program;
import io.github.nul00000000.program.ProgramBadFaceTrack;
import io.github.nul00000000.program.ProgramFaceFinder;
import io.github.nul00000000.program.ProgramInfiniteRecharge;

public class Main {
	
	public static final int WIDTH = 1280, HEIGHT = 360;
	
	public static final byte INFINITE_RECHARGE = 0;
	public static final byte TURTLE_STRAW_SCREAM = 1;
	public static final byte FACE_FINDER = 2;
	public static final byte BAD_FACE_TRACK = 3;
	
	private byte programID;
	private Program program;
	
	private Webcam webcam;
	private JFrame window;
	private JPanel content;
	private Graphics g;
		
	public Main(String[] args) {
		System.load("C:/OpenCV-4.2.0/opencv/build/java/x64/opencv_java420.dll");
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		if(args.length < 1) {
			System.out.println("No program selected, defaulting to 0 (INFINITE_RECHARGE)");
			programID = INFINITE_RECHARGE;
		} else if(args[0].equals("list")) {
			this.printList(System.out);
		} else {
			try {
				programID = Byte.parseByte(args[0]);
			} catch(NumberFormatException e) {
				System.err.println("First argument must be program ID.");
				this.printList(System.err);
			}
		}
		webcam = Webcam.getDefault();
		content = new JPanel();
		Dimension camDim = new Dimension(320, 180);
		content.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		webcam.setCustomViewSizes(camDim);
		webcam.setViewSize(camDim);
		webcam.open();
		Mat frame = new Mat(camDim.height, camDim.width, CvType.CV_8UC3);
		Mat dest = new Mat();
		window = new JFrame("OpenCV Projects");
		window.pack();
		window.add(content);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		g = content.getGraphics();
		
		switch(programID) {
		case INFINITE_RECHARGE:
			program = new ProgramInfiniteRecharge();
			break;
		case FACE_FINDER:
			program = new ProgramFaceFinder();
			break;
		case BAD_FACE_TRACK:
			frame.put(0, 0, ((DataBufferByte) webcam.getImage().getRaster().getDataBuffer()).getData());
			program = new ProgramBadFaceTrack(frame);
			break;
		default:
			break;
		}
		
		try {
			while(true) {
				frame.put(0, 0, ((DataBufferByte) webcam.getImage().getRaster().getDataBuffer()).getData());
				
				dest = program.process(frame);
				
				g.drawImage(HighGui.toBufferedImage(frame), WIDTH / 2, 0, -WIDTH / 2, HEIGHT, null);
				g.drawImage(HighGui.toBufferedImage(dest), WIDTH, 0, -WIDTH / 2, HEIGHT, null);
			}
		} finally {
			webcam.close();
		}
	}
	
	private void printList(PrintStream stream) {
		stream.println("INFINITE_RECHARGE = " + INFINITE_RECHARGE + "\n" + 
				"TURTLE_STRAW_SCREAM = " + TURTLE_STRAW_SCREAM + "\n" + 
				"FACE_FINDER = " + FACE_FINDER + "\n");
	}
	
	public static void main(String[] args) {
		new Main(args);
	}

}
