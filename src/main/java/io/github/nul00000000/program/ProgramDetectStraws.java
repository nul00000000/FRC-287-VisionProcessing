package io.github.nul00000000.program;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ProgramDetectStraws implements Program {
	
	public ProgramDetectStraws() {
		init();
	}

	public void init() {

	}

	public Mat process(Mat frame) {
		Mat dest = new Mat();
		Imgproc.Canny(frame, dest, 50, 200, 3, false);
		return dest;
	}

}
