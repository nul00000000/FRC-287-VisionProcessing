package io.github.nul00000000;

import java.io.PrintStream;

import io.github.nul00000000.program.ProgramBadFaceTrack;

public class Main {
	
	public static final byte INFINITE_RECHARGE = 0;
	public static final byte TURTLE_STRAW_SCREAM = 1;
	public static final byte FACE_FINDER = 2;
	public static final byte BAD_FACE_TRACK = 3;
	
	private byte program;
		
	public Main(String[] args) {
		System.load("C:/OpenCV-4.2.0/opencv/build/java/x64/opencv_java420.dll");
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		if(args.length < 1) {
			System.out.println("No program selected, defaulting to 0 (INFINITE_RECHARGE)");
			program = BAD_FACE_TRACK;
		} else if(args[0].equals("list")) {
			this.printList(System.out);
		} else {
			try {
				program = Byte.parseByte(args[0]);
			} catch(NumberFormatException e) {
				System.err.println("First argument must be program ID.");
				this.printList(System.err);
			}
		}
		switch(program) {
		case INFINITE_RECHARGE:
			break;
		case BAD_FACE_TRACK:
			new ProgramBadFaceTrack().run();
			break;
		default:
			break;
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
