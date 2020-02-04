package io.github.nul00000000.program;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ProgramInfiniteRecharge implements Program {
	
	public ProgramInfiniteRecharge() {
		init();
	}

	public void init() {
		
	}

	public Mat process(Mat frame) {
		Mat dest = new Mat();
		ArrayList<Mat> colors = new ArrayList<Mat>();
		Core.split(frame, colors);
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		Imgproc.threshold(colors.get(0), colors.get(0), 128, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(1), colors.get(1), 128, 255, 0);
		Core.bitwise_and(colors.get(0), colors.get(1), dest);
		Imgproc.cvtColor(dest, dest, Imgproc.COLOR_GRAY2RGB);
		Core.bitwise_and(frame, dest, frame);
		return dest;
	}

}
