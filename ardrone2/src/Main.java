import guiListener.KeyboardDrone;
import guiModel.ConsoleModel;
import drone.Controller;


import guiView.MainWindowGUI;


public class Main {
	
	public static void main (String[] args) throws InterruptedException{
	
		Controller controller; 		
		KeyboardDrone keyboard;
		ConsoleModel model;

		model = new ConsoleModel("AR-DRONE");
		controller = new Controller("192.168.1.1", 5556, "127.0.0.1", 7000,model);
		keyboard = new KeyboardDrone(controller, model);
			
		new MainWindowGUI(keyboard, model, controller);
	}	
}

