package io.github.nul00000000.program;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ProgramCircle implements Program {

	public ProgramCircle() {
		init();
	}
	
	public void init() {
		
	}

	public Mat process(Mat frame) {
		Mat dest = new Mat();
		Imgproc.cvtColor(frame, dest, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		Mat circles = new Mat();
		Imgproc.HoughCircles(dest, circles, Imgproc.HOUGH_GRADIENT, 1, 100, 100, 50, 30, -1);
		for(int i = 0; i < circles.cols(); i++) {
			double[] a = circles.get(0, i);
			Imgproc.circle(frame, new Point(a[0], a[1]), 4, new Scalar(0, 0, 255), -1);
		}
		return dest;
	}

}
