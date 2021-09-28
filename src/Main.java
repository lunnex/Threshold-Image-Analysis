import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;


/*Создаем отдельный поток для работы камеры*/
class CamRealization implements Runnable{
	WritableImage writableImage = null;
	Text text = new Text("hgvgg"); 
	int i = 0;

	public void run(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Загружаем библтотеку
	    VideoCapture camera = new VideoCapture(0);
		   
		if(!camera.isOpened()) {
			System.out.println("Проблемы с камерой");
	    }
	    else 
	    {
	    	Mat frame = new Mat();
	    	while(true) {
	    		if (camera.read(frame)) {
	    			BufferedImage image = new BufferedImage(frame.width(), frame.height(), BufferedImage.TYPE_3BYTE_BGR);
	    			writableImage = SwingFXUtils.toFXImage(image, null);
	    			System.out.println("Captured Frame Width " + frame.width() + " Height " + frame.height());
	    			text.setText(String.valueOf(i));
	    			i++;
	    		}
	    	}
	    }
		   camera.release();
	}
}

public class Main extends Application{
	CamRealization camRealization = new CamRealization();
	
//public void run() {
		
	//}

   @Override
   public void start(Stage stage) throws FileNotFoundException, IOException {
	   
	   CamRealization camRealization = new CamRealization();
	   Thread camThread = new Thread(camRealization);
	   camThread.start();
	   
	   try {
		   ImageView imageView = new ImageView(camRealization.writableImage);
		   imageView.setFitHeight(600);
		   imageView.setFitWidth(400);
		   
	   }
	   catch (Exception e) {
		   
	   }
	   

	   
	   camRealization.text.setLayoutY(80);    // установка положения надписи по оси Y
	   camRealization.text.setLayoutX(80);  
	   
	   Group root = new Group(camRealization.text);
	   Scene scene = new Scene(root);
	   stage.setTitle("POWER OF JAVAFX & OPENCV");
	   stage.setWidth(600);
	   stage.setHeight(400);
	   stage.setScene(scene);
	   stage.show();
	   
   }
      

   public static void main(String args[]) {
	   

       
       
	   launch(args);
   }
}