package guiView;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import guiListener.KeyboardDrone;
import guiModel.ConsoleModel;
import drone.Controller;

public class DronePanelGUI extends JPanel {
	
	public static final int WIDTH = 640;
	private static final int HEIGHT	= 650;
	
	public DronePanelGUI(KeyboardDrone k, ConsoleModel model,
			Controller controller) {
		
		this.setLayout(new BorderLayout());
		this.setSize(WIDTH,HEIGHT);
		

		ModelPanel modelPanel = new ModelPanel(k,model);
		VideoPanel videoPanel = new VideoPanel(controller,modelPanel);
		
		this.add(videoPanel, BorderLayout.NORTH);
		this.add(modelPanel, BorderLayout.SOUTH);
		
		
	}

}