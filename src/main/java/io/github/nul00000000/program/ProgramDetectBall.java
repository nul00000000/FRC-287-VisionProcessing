package io.github.nul00000000.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ProgramDetectBall implements Program {
	
	private Mat dest;
	private Mat r;
	private Mat g;
	private Mat b;
	private ArrayList<Mat> colors;
	private List<MatOfPoint> contours;
	
	public ProgramDetectBall() {
		init();
	}

	public void init() {
		dest = new Mat();
		r = new Mat();
		g = new Mat();
		b = new Mat();
		colors = new ArrayList<Mat>();
		contours = new ArrayList<MatOfPoint>();
	}

	public Mat process(Mat frame) {
		contours.clear();
		colors.clear();
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
		Core.split(frame, colors);
		Imgproc.threshold(colors.get(0), r, 45, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(0), g, 80, 255, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(colors.get(1), b, 50, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_and(r, g, dest);
		Core.bitwise_and(dest, b, dest);
		
		Imgproc.findContours(dest, contours, r, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		//this commented stuff just masks it with the detection area
//		Imgproc.cvtColor(dest, dest, Imgproc.COLOR_GRAY2RGB);
//		
//		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2RGB);
//		Core.bitwise_and(frame, dest, dest);
		
		Imgproc.cvtColor(frame, dest, Imgproc.COLOR_HSV2RGB);
		
		for(int i = 0; i < contours.size(); i++) {
			double ca = Imgproc.contourArea(contours.get(i));
			if(ca > 300) {
				MatOfPoint2f cir = new MatOfPoint2f();
				contours.get(i).convertTo(cir, CvType.CV_32FC2);
				Point e = new Point();
				float[] radius = new float[1];
				Imgproc.minEnclosingCircle(cir, e, radius);
				if(ca / (radius[0] * radius[0] * Math.PI) > 0.8) {
					Imgproc.circle(dest, e, (int) radius[0], new Scalar(0, 0, 255), -1);
				}
				Imgproc.drawContours(dest, contours, i, new Scalar(0, 150, 0), -1);
			}
		}
		
		return dest;
	}

}
