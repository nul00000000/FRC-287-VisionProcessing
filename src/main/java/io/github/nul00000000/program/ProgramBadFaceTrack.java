package io.github.nul00000000.program;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

public class ProgramBadFaceTrack implements Program {
		
	private Mat hsv_roi = new Mat();
	private Mat mask = new Mat();
	
	private Rect track = new Rect(100, 100, 20, 20);
	private TermCriteria term_crit = new TermCriteria(TermCriteria.EPS | TermCriteria.COUNT, 10, 1);
	
	private Mat hsv = new Mat();
	private Mat dst = new Mat();
	private MatOfFloat range = new MatOfFloat(0 , 256);
	private Mat roi_hist = new Mat();
	private MatOfInt histSize = new MatOfInt(180);
	private MatOfInt channels = new MatOfInt(0);
	
	public ProgramBadFaceTrack(Mat frameStart) {
		Imgproc.cvtColor(new Mat(frameStart, track), hsv_roi, Imgproc.COLOR_BGR2HSV);
		init();
	}
	
	public void init() {
		Core.inRange(hsv_roi, new Scalar(0,  60, 32), new Scalar(180, 255, 255), mask);
		Imgproc.calcHist(Arrays.asList(hsv_roi), channels, mask, roi_hist, histSize, range);
		Core.normalize(roi_hist, roi_hist, 0, 255, Core.NORM_MINMAX);
	}
	
	public Mat process(Mat frame) {
		Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
		Imgproc.calcBackProject(Arrays.asList(hsv), channels, roi_hist, dst, range, 1);
		
		RotatedRect rot_rect = Video.CamShift(dst, track, term_crit);
		
		Point[] points = new Point[4];
		rot_rect.points(points);
		for(int i = 0; i < 4; i++) {
			Imgproc.line(frame, points[i], points[(i + 1) % 4], new Scalar(255, 0, 0), 2);
		}
		
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		
		return new Mat(frame, track);
	}

}
