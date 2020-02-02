package io.github.nul00000000.program;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import com.github.sarxos.webcam.Webcam;

public class ProgramBadFaceTrack {
	
	public static final int WIDTH = 1280, HEIGHT = 360;
	
	private Dimension a = new Dimension(320, 180);
	private Mat src;
	private Mat hsv_roi = new Mat();
	private Mat mask = new Mat();
	private Mat roi;
	
	private Webcam webcam;
	
	private Graphics g;
	
	public ProgramBadFaceTrack() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame("webcam test");
		frame.pack();
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		g = panel.getGraphics();
		src = new Mat(a.height, a.width, CvType.CV_8UC3);
		webcam = Webcam.getWebcams().get(0);
		webcam.setCustomViewSizes(a);
		webcam.setViewSize(a);
		webcam.open();
	}
	
	public void run() {
		boolean running = true;
		src.put(0, 0, ((DataBufferByte) webcam.getImage().getRaster().getDataBuffer()).getData());
		Rect track = new Rect(a.width / 2 - 10, a.height / 2 - 10, 20, 20);
		roi = new Mat(src, track);
		Imgproc.cvtColor(roi, hsv_roi, Imgproc.COLOR_BGR2HSV);
		Core.inRange(hsv_roi, new Scalar(0,  60, 32), new Scalar(180, 255, 255), mask);
		
		MatOfFloat range = new MatOfFloat(0 , 256);
		Mat roi_hist = new Mat();
		MatOfInt histSize = new MatOfInt(180);
		MatOfInt channels = new MatOfInt(0);
		Imgproc.calcHist(Arrays.asList(hsv_roi), channels, mask, roi_hist, histSize, range);
		Core.normalize(roi_hist, roi_hist, 0, 255, Core.NORM_MINMAX);
		
		Mat hsv = new Mat();
		Mat dst = new Mat();
		
		Mat tracked = new Mat();
		
		TermCriteria term_crit = new TermCriteria(TermCriteria.EPS | TermCriteria.COUNT, 10, 1);
		
		try {
			while(running) {
				src.put(0, 0, ((DataBufferByte) webcam.getImage().getRaster().getDataBuffer()).getData());
				if(src.empty()) {
					break;
				}
				
				Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
				Imgproc.calcBackProject(Arrays.asList(hsv), channels, roi_hist, dst, range, 1);
				
				RotatedRect rot_rect = Video.CamShift(dst, track, term_crit);
				
				Point[] points = new Point[4];
				rot_rect.points(points);
				for(int i = 0; i < 4; i++) {
					Imgproc.line(src, points[i], points[(i + 1) % 4], new Scalar(255, 0, 0), 2);
				}
				
				Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
				
				tracked = new Mat(src, track);
				
				g.drawImage(HighGui.toBufferedImage(src), WIDTH / 2, 0, -WIDTH / 2, HEIGHT, null);
				g.drawImage(HighGui.toBufferedImage(tracked), WIDTH, 0, -WIDTH / 2, HEIGHT, null);
			}
		} finally {
			webcam.close();
			running = false;
		}
	}

}
