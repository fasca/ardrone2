package video;

import guiView.VideoPanel;
import drone.Commands;
import drone.Controller;

public class KeepAlive extends Thread {

	private Controller _controller;
	private VideoPanel _panel;
	
	
	public KeepAlive(Controller controller,VideoPanel panel) {
		_controller = controller;
		_panel = panel;
	}
	
	
	public void init(){
		String message;
		message = Commands.configIDS(_controller.getSeq());
		_controller.sendMessage(message);
		message = Commands.configCodec(_controller.getSeq());
		_controller.sendMessage(message);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}	
			
		message = Commands.configIDS(_controller.getSeq());
		_controller.sendMessage(message);
		
		message = Commands.configCameraHorizontal(_controller.getSeq());
		_controller.sendMessage(message);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void run() {
		String message;
		while(true){
			message = Commands.configIDS(_controller.getSeq());
			_controller.sendMessage(message);
			System.out.println("isHORIZONTAL?" + _panel.getIsHorizontal() );
			if(_panel.getIsHorizontal()){
				message = Commands.configCameraHorizontal(_controller.getSeq()); //le vrai msg a envoyer
				//message = Commands.configCameraVertical(_controller.getSeq());
			}
			else{
				message = Commands.configCameraVertical(_controller.getSeq()); //le vrai msg a envoyer
				//message = Commands.configCameraHorizontal(_controller.getSeq());
			}
			_controller.sendMessage(message);
			System.out.println(_controller.getAddr());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
