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
	
	public ProgramFaceFinder() {
		init();//edit
	}
	
	public void init() {
		if(!faceCascade.load("C:/OpenCV-4.2.0/opencv/build/etc/lbpcascades/lbpcascade_frontalface.xml")) {
			System.err.println("LBP Face Cascade could not load");
			System.exit(0);
		}
		if(!faceCascade2.load("C:/OpenCV-4.2.0/opencv/build/etc/haarcascades/haarcascade_frontalface_alt.xml")) {
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
		Imgproc.rectangle(frame, largest, new Scalar(255, 0, 255));
		
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
		return largest.area() > 0 ? frame.submat(largest) : frame;
	}

}
