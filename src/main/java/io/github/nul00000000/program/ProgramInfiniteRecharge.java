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

public class ProgramInfiniteRecharge implements Program {
	
	private Mat dest;
	private Mat a;
	private Mat h;
	private Mat k;
	private ArrayList<Mat> colors;
	private List<MatOfPoint> contours;
	
	public ProgramInfiniteRecharge() {
		init();
	}

	public void init() {
		dest = new Mat();
		a = new Mat();
		h = new Mat();
		k = new Mat();
		colors = new ArrayList<Mat>();
		contours = new ArrayList<MatOfPoint>();
	}

	public Mat process(Mat frame) {
		contours.clear();
		colors.clear();
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
		Core.split(frame, colors);
		Imgproc.threshold(colors.get(0), a, 85, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(0), h, 100, 255, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(colors.get(1), k, 80, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_and(a, h, dest);
		Core.bitwise_and(dest, k, dest);
		Imgproc.findContours(dest, contours, a, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		dest.setTo(new Scalar(0));
		for(int i = 0; i < contours.size(); i++) {
			double ca = Imgproc.contourArea(contours.get(i));
			if(ca > 300) {
				MatOfPoint2f cir = new MatOfPoint2f();
				contours.get(i).convertTo(cir, CvType.CV_32FC2);
				Point e = new Point();
				float[] radius = new float[1];
				System.out.println(Arrays.toString(radius));
				Imgproc.minEnclosingCircle(cir, e, radius);
				if(ca / (radius[0] * radius[0] * Math.PI) > 0.8) {
//					System.out.println(ca / (radius[0] * radius[0] * Math.PI));
					Imgproc.circle(frame, e, (int) radius[0], new Scalar(255, 0, 0), 3);
				}
				Imgproc.drawContours(dest, contours, i, new Scalar(255), 2);
			}
		}
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2RGB);
//		Core.bitwise_and(frame, dest, frame);
		return dest;
	}

}
