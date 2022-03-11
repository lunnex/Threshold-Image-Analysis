import java.io.FileNotFoundException;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;



public class Main extends Application {
	
   @Override
   public void start(Stage stage) throws FileNotFoundException, IOException {
	   
	// Загружаем библиотеку
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	   
	   
      // Захват снимка с камеры
	   Processing proc = new Processing();
	   GetCapture gp = new GetCapture();

	   ImageView imageView = new ImageView("blob.jpg");
	   ImageView imageTheresh = new ImageView("blob.jpg");
	   ImageView imageBoundaries = new ImageView("blob.jpg");
	   Slider sliderHMin1 = new Slider(0,99,15);
	   Slider sliderSMin1 = new Slider(0,99,4);
	   Slider sliderVMin1 = new Slider(0,1,0.3);
	   Slider sliderHMax1 = new Slider(0,360,182);
	   Slider sliderSMax1 = new Slider(0,1,0.23);
	   Slider sliderVMax1 = new Slider(0,1,1);
	   
	   Slider sliderHMin2 = new Slider(0,360,94);
	   Slider sliderSMin2 = new Slider(0,1,0);
	   Slider sliderVMin2 = new Slider(0,1,0.3);
	   Slider sliderHMax2 = new Slider(0,360,182);
	   Slider sliderSMax2 = new Slider(0,1,0.23);
	   Slider sliderVMax2 = new Slider(0,1,1);
	   
	   Label hminLabel = new Label("Hmin");
	   Label sminLabel = new Label("Smin");
	   Label vminLabel = new Label("Vmin");
	   Label hmaxLabel = new Label("Hmax");
	   Label smaxLabel = new Label("Smax");
	   Label vmaxLabel = new Label("Vmax");
	   
	   Label timeStamp = new Label();
	   
	   timeStamp.setLayoutX(10);
	   timeStamp.setLayoutY(10);
	   
	   timeStamp.setTextFill(Color.rgb(0, 255, 0));
	   timeStamp.setFont(new Font("Courier New", 20));
	   
	   Label framesPerSecond = new Label();
	   framesPerSecond.setLayoutX(10);
	   framesPerSecond.setLayoutY(30);
	   
	   framesPerSecond.setTextFill(Color.rgb(0, 255, 0));
	   framesPerSecond.setFont(new Font("Courier New", 20));
	   
	   sliderHMin1.setLayoutX(700);
	   sliderHMin1.setLayoutY(500);
	   hminLabel.setLayoutX(670);
	   hminLabel.setLayoutY(500);
	   
	   sliderHMin2.setLayoutX(700);
	   sliderHMin2.setLayoutY(525);
	   
	   sliderSMin1.setLayoutX(700);
	   sliderSMin1.setLayoutY(550);
	   sminLabel.setLayoutX(670);
	   sminLabel.setLayoutY(550);
	   
	   sliderSMin2.setLayoutX(700);
	   sliderSMin2.setLayoutY(675);
	   
	   sliderVMin1.setLayoutX(700);
	   sliderVMin1.setLayoutY(600);
	   vminLabel.setLayoutX(670);
	   vminLabel.setLayoutY(600);
	   
	   sliderVMin2.setLayoutX(700);
	   sliderVMin2.setLayoutY(625);
	   
	   
	   
	   
	   
	   
	   sliderHMax1.setLayoutX(900);
	   sliderHMax1.setLayoutY(500);
	   hmaxLabel.setLayoutX(870);
	   hmaxLabel.setLayoutY(500);
	   
	   sliderHMax2.setLayoutX(900);
	   sliderHMax2.setLayoutY(525);
	   
	   sliderSMax1.setLayoutX(900);
	   sliderSMax1.setLayoutY(550);
	   smaxLabel.setLayoutX(870);
	   smaxLabel.setLayoutY(550);
	   
	   sliderSMax2.setLayoutX(900);
	   sliderSMax2.setLayoutY(575);
	   
	   sliderVMax1.setLayoutX(900);
	   sliderVMax1.setLayoutY(600);
	   vmaxLabel.setLayoutX(870);
	   vmaxLabel.setLayoutY(600);
	   
	   sliderVMax2.setLayoutX(900);
	   sliderVMax2.setLayoutY(825);
	   
	   imageView.setLayoutX(0);
	   imageView.setLayoutY(0);
	   
	   imageTheresh.setLayoutX(0);
	   imageTheresh.setLayoutY(490);
	   
	   imageBoundaries.setLayoutX(650);
	   imageBoundaries.setLayoutY(0);
	   


	   
	   Thread mainview = new Thread(() -> {
		   while(true) 
		   {
			  
			   long startTime = System.currentTimeMillis();
			   
			   gp.hmin1 = (int)sliderHMin1.getValue();
			   gp.smin1 = (int)sliderSMin1.getValue();
			   /*gp.vmin1 = sliderVMin1.getValue();
			   gp.hmax1 = sliderHMax1.getValue();
			   gp.smax1 = sliderSMax1.getValue();
			   gp.vmax1 = sliderVMax1.getValue();
			   
			   gp.hmin2 = sliderHMin2.getValue();
			   gp.smin2 = sliderSMin2.getValue();
			   gp.vmin2 = sliderVMin2.getValue();
			   gp.hmax2 = sliderHMax2.getValue();
			   gp.smax2 = sliderSMax2.getValue();
			   gp.vmax2 = sliderVMax2.getValue();*/
			   WritableImage writableImage = gp.getFrame(0);

			   imageView.setImage(writableImage);

			   
			   long endTime = System.currentTimeMillis();
			   
			   
			   Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				    	Date date = new Date();
						timeStamp.setText(date.toString());
						framesPerSecond.setText(String.valueOf(1000/(endTime - startTime) + " fps"));
				    }
				});
			   
			   
		   }
	   });
	   
	   
	   Thread bottomview = new Thread(() -> {
		   while(true) 
		   {
			   WritableImage writableImageTheresh = gp.getFrame(1);
			   imageTheresh.setImage(writableImageTheresh);
		   }
	   });
	   
	   Thread rightview = new Thread(() -> {
		   while(true) 
		   {

			   WritableImage writableImageBoundaries = gp.getFrame(2);

			   imageBoundaries.setImage(writableImageBoundaries);
   
		   }
	   });
	   
	   
      // Размеры imageView
      imageView.setFitHeight(480);
      imageView.setFitWidth(640);
      
      imageTheresh.setFitHeight(480);
      imageTheresh.setFitWidth(640);
      
      imageBoundaries.setFitHeight(480);
      imageBoundaries.setFitWidth(640);


      // Создаём объект Group
      Group root = new Group(imageView, imageTheresh, imageBoundaries, sliderHMin1, sliderSMin1, sliderVMin1, sliderHMax1, sliderSMax1, sliderVMax1,
    		  hminLabel, sminLabel, vminLabel, hmaxLabel, smaxLabel, vmaxLabel, timeStamp, framesPerSecond);

      

      // Создаём сцену
      Scene scene = new Scene(root, 1290, 980);
      

      // Заголовок окна
      stage.setTitle("JavaFX + OpenCV");

      // Добавляем сцену
      stage.setScene(scene);

      // Показываем сцену
      stage.show();
      
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	    @Override
    	    public void handle(WindowEvent event) {
    	    	mainview.stop();
    	        bottomview.stop();
    	        rightview.stop();
    	    	stage.close();
    	        //event.consume();
    	    }
    	});
      
      // Поток, получающий изображение с камеры
      
      mainview.start();
      bottomview.start();
      rightview.start();
      

   }

   public static void main(String args[]) {
      launch(args);
   }
}