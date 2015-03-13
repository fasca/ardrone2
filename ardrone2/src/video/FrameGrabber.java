package video;

import guiView.VideoPanel;

import java.awt.image.BufferedImage;

import drone.Controller;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.swresample;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class FrameGrabber extends Thread{

	private VideoPanel _panel;
	private IplImage _iplImage;
	private BufferedImage _camImg;
	private FFmpegFrameGrabber _fg;
	
	public FrameGrabber(Controller controller, VideoPanel panel){
		Loader.load(swresample.class);
		_panel = panel;
		_fg = new FFmpegFrameGrabber("tcp://"+controller.getAddr()+":5555");
	}
	
	
	public void run(){
		//int i = 0;
		try {
			_fg.start();
			
			Thread.sleep(1000);
			
			while(true){
		/*		++i;
				
				if(i==60){
					System.out.println("restart");
					_fg.restart();
				}
		*/
				_iplImage = _fg.grab();
				_camImg = _iplImage.getBufferedImage();
				_panel.setSize(640,360);
				_panel.setCam(_camImg);
				_panel.repaint();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
