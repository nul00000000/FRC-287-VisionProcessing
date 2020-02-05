package io.github.nul00000000.program;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
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
		Imgproc.threshold(colors.get(0), colors.get(0), 90, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(1), colors.get(1), 90, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(2), colors.get(2), 20, 255, Imgproc.THRESH_BINARY_INV);
		Core.bitwise_and(colors.get(0), colors.get(1), dest);
		Core.bitwise_and(dest, colors.get(2), dest);
		Mat a = new Mat();
		Imgproc.HoughCircles(dest, a, Imgproc.HOUGH_GRADIENT, 1, 10, 100, 30, 1, 30);
		for(int i = 0; i < a.cols(); i++) {
			double[] c = a.get(0, i);
			Imgproc.circle(frame, new Point(c[0],  c[1]), (int) Math.round(c[2]), new Scalar(255, 0, 255), 3, 8, 0);
		}
		Imgproc.cvtColor(dest, dest, Imgproc.COLOR_GRAY2RGB);
		Core.bitwise_and(frame, dest, frame);
		return dest;
	}

}
