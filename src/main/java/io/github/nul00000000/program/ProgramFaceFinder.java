package io.github.nul00000000.program;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class ProgramFaceFinder implements Program {
	
	private Mat frameGrey = new Mat();
//	private MatOfRect faces = new MatOfRect();
	private MatOfRect faces2 = new MatOfRect();
	private CascadeClassifier faceCascade = new CascadeClassifier();
	private CascadeClassifier faceCascade2 = new CascadeClassifier();
	
	private Rect last = null;
	private int ticksSinceUpdate = 10000;
	
	public ProgramFaceFinder() {
		init();//edit
	}
	
	public void init() {
		if(!faceCascade.load("C:/Users/Admin/Downloads/opencv/build/etc/lbpcascades/lbpcascade_frontalface.xml")) {
			System.err.println("LBP Face Cascade could not load");
			System.exit(0);
		}
		if(!faceCascade2.load("C:/Users/Admin/Downloads/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml")) {
			System.err.println("Haar Face Cascade could not load");
			System.exit(0);
		}
	}
	
	public Mat process(Mat frame) {
		Imgproc.cvtColor(frame, frameGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frameGrey, frameGrey);
		
//		faceCascade.detectMultiScale(frameGrey, faces);
		faceCascade2.detectMultiScale(frameGrey, faces2);
//		List<Rect> lof = faces.toList();
		Rect largest = new Rect();
//		for(Rect face : lof) {
//			if(face.width > largest.width) {
//				largest.width = face.width;
//				largest.x = face.x;
//				largest.y = face.y;
//			}
//			if(face.height > largest.height) {
//				largest.height = face.height;
//				largest.x = face.x;
//				largest.y = face.y;
//			}
//		}
		List<Rect> lof = faces2.toList();
		if(lof.size() > 0) {
			for(Rect face : lof) {
				if(face.width > largest.width) {
					largest.width = face.width;
					largest.x = face.x;
					largest.y = face.y;
				}
				if(face.height > largest.height) {
					largest.height = face.height;
					largest.x = face.x;
					largest.y = face.y;
				}
			}
			if(ticksSinceUpdate > 100 || Math.abs(largest.area() - last.area()) / last.area() < 0.2) {
				last = largest;
			}
		}
		if(last != null && last.area() > 0) {
			Imgproc.rectangle(frame, last, new Scalar(255, 0, 255));
			Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
			return frame.submat(last);
		} else {
			Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
			return frame;
		}
	}

}
