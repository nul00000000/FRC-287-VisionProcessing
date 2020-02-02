package io.github.nul00000000.program;

import org.opencv.core.Mat;

public interface Program {
	
	public void init();
	public Mat process(Mat frame, Mat dest);

}
