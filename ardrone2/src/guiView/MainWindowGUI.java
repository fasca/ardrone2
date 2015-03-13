package guiView;

import guiListener.KeyboardDrone;
import guiModel.ConsoleModel;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import drone.Controller;


public class MainWindowGUI extends JFrame{
	private static final int WIDTH 	= 640;
	private static final int HEIGHT	= 650;
	private static final String TITLE = "Prototyp Traking Landing"; 
		
	public MainWindowGUI(KeyboardDrone k, ConsoleModel model, Controller controller){
		
		this.setTitle(TITLE);
		this.setSize(WIDTH,HEIGHT);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout());
		
		DronePanelGUI dronePanel = new DronePanelGUI(k, model, controller);
		
		this.getContentPane().add(dronePanel);
		
		this.setVisible(true);
		
	}
}