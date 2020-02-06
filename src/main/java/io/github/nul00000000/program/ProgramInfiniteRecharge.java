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

import io.github.nul00000000.ball.Ball;

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
//		Imgproc.HoughCircles(dest, a, Imgproc.HOUGH_GRADIENT, 1, 10, 100, 30, 20, 100);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(dest, contours, a, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		ArrayList<Ball> balls = new ArrayList<Ball>();
		for(int i = 0; i < contours.size(); i++) {
			if(Imgproc.contourArea(contours.get(i)) > 7) {
				MatOfPoint2f g = new MatOfPoint2f();
				contours.get(i).convertTo(g, CvType.CV_32FC2);
				double f = Imgproc.arcLength(g, true);
				boolean d = false;
				Moments b = Imgproc.moments(contours.get(i));
				Point e = new Point(b.m10 / b.m00,  b.m01 / b.m00);
				for(int j = 0; j < balls.size(); j++) {
					if(balls.get(j).overlaps(e, f)) {
						if(balls.get(j).getRadius() < f) {
							balls.set(j, new Ball(e, f));
						}
						d = true;
						break;
					}
				}
				if(!d) {
					balls.add(new Ball(e, f));
				}
//				Imgproc.drawContours(frame, contours, i, new Scalar(255, 0, 255), 3);
			}
		}
		System.out.println(balls.size());
		Imgproc.cvtColor(dest, dest, Imgproc.COLOR_GRAY2RGB);
		Core.bitwise_and(frame, dest, frame);
		for(Ball p : balls) {
			Imgproc.circle(frame, p.getCenter(), 4, new Scalar(255, 0, 255), -1);
		}
		return dest;
	}

}
