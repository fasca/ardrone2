package video;

import guiView.VideoPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import drone.Controller;
import drone.Convert;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;
import org.bytedeco.javacpp.swresample;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;












import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_MEDIAN;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvEqualizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetCentralMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetSpatialMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvMoments;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;



/*

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
		//		++i;
		//		
		//		if(i==60){
		//			System.out.println("restart");
		//			_fg.restart();
		//		}
		//
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

*/


/*
 * 
public void move(float left_right_tilt, float front_back_tilt, float vertical_speed, float angular_speed) throws IOException
{
	cmd_queue.add(new MoveCommand(combinedYawMode, left_right_tilt, front_back_tilt, vertical_speed, angular_speed));
}

 */





public class FrameGrabber extends Thread{

	private VideoPanel _panel;
	private IplImage _iplImage;
	private BufferedImage _camImg;
	private FFmpegFrameGrabber _fg;
	private Controller _contr;
	
	JLabel _jlab = null;
   
  //dopoldne cvscalar=BGR  en format HSV
    static CvScalar hsv_min = cvScalar(29, 160, 40, 0);
    static CvScalar hsv_max = cvScalar(78, 255, 255, 0);
    
//    FlightNavigator nav = new FlightNavigator();--------------------------------------------
    float roll = 0;
    int batt = 0;
    
    
    
    CanvasFrame canvas = new CanvasFrame("Quad Cam Live");
    CanvasFrame path = new CanvasFrame("Detection");
    JPanel jp = new JPanel();
    
    
	public FrameGrabber(Controller controller, VideoPanel panel){
		Loader.load(swresample.class);
		_panel = panel;
		_fg = new FFmpegFrameGrabber("tcp://"+controller.getAddr()+":5555");
		_contr = controller;
		
		path.setContentPane(jp);
        canvas.setVisible(true);
        path.setVisible(true);
        
        
        
        
        JFrame okno = new JFrame("iBird");
		okno.setSize(650, 650);
		okno.setResizable(false);
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		okno.setLocation((dim.width/2-okno.getSize().width/2 ), dim.height/2-okno.getSize().height/2 - 20);
		
		//okno.setExtendedState(JFrame.MAXIMIZED_VERT);
		
		_jlab = new JLabel();
		
		okno.add("Center",_jlab);
        
		okno.setVisible(true);
		
	}
	
	 static IplImage hsvThreshold(IplImage orgImg) {
	        IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);
	        cvCvtColor(orgImg, imgHSV, CV_BGR2HSV);
	        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
	        cvInRangeS(imgHSV, hsv_min, hsv_max, imgThreshold);
	        opencv_core.cvReleaseImage(imgHSV);
	        cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 13,13,0,0);//ajout de 13,0,0
	        return imgThreshold;
	    }
	 
	 private void paint(IplImage img, int posX, int posY) {
	        Graphics g = jp.getGraphics();
	        path.setSize(img.width(), img.height());
	        g.clearRect(0, 0, img.width(), img.height());
	        g.setColor(Color.RED);
	        g.fillOval(posX, posY, 20, 20);
	        g.drawOval(posX, posY, 20, 20);
	        //System.out.println(posX + " , " + posY);

	    }


	public void run(){

		try {
			_fg.start();
			long milis = System.currentTimeMillis();
            int posX = 0;
            int posY = 0;

			Thread.sleep(1000);
			
			while(true){

				_iplImage = _fg.grab();
				
				
				if(_iplImage != null){
					// show image on window
                    cvFlip(_iplImage,_iplImage , 1);// l-r = 90_degrees_steps_anti_clockwise
                    //canvas.showImage(img);
                    IplImage detectThrs = hsvThreshold(_iplImage);//getThresholdImage(img);//getThresholdImage(img);//hsvThreshold(img); //
                    
                    canvas.showImage(detectThrs);
                    
                    CvMoments moments = new CvMoments();
                    cvMoments(detectThrs, moments, 1);
                    //cvtColor
                    double mom10 = cvGetSpatialMoment(moments, 1, 0);
                    double mom01 = cvGetSpatialMoment(moments, 0, 1);
                    double area = cvGetCentralMoment(moments, 0, 0);
                    posX = (int) (mom10 / area);
                    posY = (int) (mom01 / area);
                    // only if its a valid position
                    //if (posX > 0 && posY > 0) {
                        paint(_iplImage, posX, posY);
                    //}
                        
                    
    /*                if(stolp.isTakenOff&&stolp.flightMode.equals("visual")){
                    	nav.navigateRotationBasedOnRedDot(stolp, posX, posY, img.width(), img.height());---------------------------------------
                    	//nav.navigatePitchBasedOnRedDot(stolp, posX, posY, img.width(), img.height());
                    }
    */                
                        
                        
                        
                        int prevPosX, prevPosY;
                    	
                    	boolean isSearching = false;
                    	
                    	boolean isTracking = false;
                    	
                    	long timeToSearch;
                    	
                    	float yawMove = 0;
                    	float pitchMove = 0;
                    	
                    	boolean isMovingForward = false;
                    	
                    	float yawTrackingP = 0.75f;
                    	
                    	float pitchTrackingP = 0.42f;
                    	float pitchTrackingI = 0.0011f;
                        
                    	float pitchE = 0;
                        
                        if(posX>0&&posY>0){
                			
                			int XposRelToCenter = (_iplImage.width()/2)-posX;
                			yawMove = (yawTrackingP * ((float)(XposRelToCenter) / 280.0f));
                			

                			int YposRelToCenter = (_iplImage.height()/2)-posY;
                			pitchE = pitchE + ((float)(YposRelToCenter) / 200.0f);
                			pitchMove = (pitchTrackingP * ((float)(YposRelToCenter) / 200.0f) )  + (pitchTrackingI*pitchE);
                			
                		}else{
                			pitchMove = 0;
                			yawMove = 0;
                		}
                		
                		//if(pitchMove!=0&&yawMove!=0){
                		//	System.out.println("Random: " +  ((Math.abs(pitchMove*2))*yawMove)*10  );
                		//}
                        //move(left_right_tilt,front_back_tilt,vertical_speed,angular_speed)
                        //stolp.letalnik.move(0,pitchMove,0, yawMove);
                        //roll,pitch,throttle,yaw
                		String message = move(0, Convert.convert754(pitchMove), 0, Convert.convert754(yawMove), _contr.getSeq()); 
						_contr.sendMessage(message);
                		
                		//System.out.println("Pitch: " + pitchMove);
                		
                		prevPosX = posX;
                		prevPosY = posY;  
                        
                        
                        
                        
                        
                    milis = System.currentTimeMillis() - milis;
                    if(!(milis > 50))
                    	Thread.sleep(50-milis);
                    
				}
				
				_camImg = _iplImage.getBufferedImage();
				
				_jlab.setIcon(new ImageIcon(scaleImage(_camImg,640,360,Color.black,(int)((float)posX*1.5f), (int)((float)posY*1.5f), roll, batt)));
                
				
				
				
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

	
	
	// fait bouger le drone suivant les angles Roll(L/R), Pitch(F/B),
		// Throttle(Gaz) et Yaw(Angle)
		public static String move(long roll, long pitch, long throttle, long yaw,
				int seq) {
			String ID = "1";
			return "AT*PCMD=" + seq + "," + ID + "," + roll + "," + pitch + ","
					+ throttle + "," + yaw;
		}
	
	
	
	
	public static  BufferedImage scaleImage(BufferedImage img, int width, int height, Color background, int a, int b, float angle, int bt) {
	    int imgWidth = img.getWidth();
	    int imgHeight = img.getHeight();
	    if (imgWidth*height < imgHeight*width) {
	        width = imgWidth*height/imgHeight;
	    } else {
	        height = imgHeight*width/imgWidth;
	    }
	    BufferedImage newImage = new BufferedImage(width, height,
	            BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	    	g.rotate(0);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g.setBackground(background);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	        g.setColor(Color.green);
	        g.drawLine(width/2, 0, width/2, height);
	        g.drawLine(0, height/2, width, height/2);
	        g.setColor(Color.white);
	        g.drawOval(Math.abs(a - width)+1, b+1, 30, 30);
	        g.drawOval(Math.abs(a - width)+1, b+1, 31, 31);
	        g.setColor(Color.white);
	        g.setFont(new Font("Arial", Font.PLAIN, 16));
	        g.drawString("PowPow: "+bt + "%", 5, 35);
	        g.rotate(Math.toRadians(angle), width/2, height/2+10);
	        g.drawLine(width/2-150, height/2-5, width/2 + 150, height/2-5);
	        g.drawLine(width/2-150, height/2+5, width/2 + 150, height/2+5);
	    } finally {
	        g.dispose();
	    }
	    return newImage;
	}
	
	
	
	
	
	
	
	
	
	
}





























































