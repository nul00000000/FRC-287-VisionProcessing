package io.github.nul00000000.program;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ProgramInfiniteRecharge implements Program {
	
	public ProgramInfiniteRecharge() {
		init();
	}

	public void init() {
		
	}

	public Mat process(Mat frame) {
		Mat dest = new Mat();
		ArrayList<Mat> colors = new ArrayList<Mat>();
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
		Core.split(frame, colors);
		Mat a = new Mat();
		Mat h = new Mat();
		Mat k = new Mat();
		Imgproc.threshold(colors.get(0), a, 85, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(colors.get(0), h, 100, 255, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(colors.get(1), k, 80, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_and(a, h, dest);
		Core.bitwise_and(dest, k, dest);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(dest, contours, a, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		dest.setTo(new Scalar(0));
		for(int i = 0; i < contours.size(); i++) {
			if(Imgproc.contourArea(contours.get(i)) > 300) {
				MatOfPoint2f cir = new MatOfPoint2f();
				contours.get(0).convertTo(cir, CvType.CV_32FC2);
				Point e = new Point();
				float[] radius = new float[1];
				Imgproc.minEnclosingCircle(cir, e, radius);
				Imgproc.drawContours(dest, contours, i, new Scalar(255), 2);
				Imgproc.circle(dest, e, 4, new Scalar(128), -1);
			}
		}
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2RGB);
//		Core.bitwise_and(frame, dest, frame);
		return dest;
	}

}
