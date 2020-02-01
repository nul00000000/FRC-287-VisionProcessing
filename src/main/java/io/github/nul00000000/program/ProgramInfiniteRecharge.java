package io.github.nul00000000.program;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

public class ProgramInfiniteRecharge {
		
	private Graphics g;
	private Webcam webcam;
	
	public ProgramInfiniteRecharge() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(160, 90));
		JFrame frame = new JFrame("webcam test");
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		g = panel.getGraphics();
		webcam = Webcam.getDefault();
		System.out.println(Arrays.toString(webcam.getViewSizes()));
		webcam.open();
	}
	
	public void run() {
		try {
			while(true) {
				g.drawImage(webcam.getImage(), 0, 0, null);
			}
		} finally {
			webcam.close();
		}
	}

}
