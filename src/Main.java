import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

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
	   Slider sliderHMin = new Slider(0,360,94);
	   Slider sliderSMin = new Slider(0,1,0);
	   Slider sliderVMin = new Slider(0,1,0.3);
	   Slider sliderHMax = new Slider(0,360,182);
	   Slider sliderSMax = new Slider(0,1,0.23);
	   Slider sliderVMax = new Slider(0,1,1);
	   
	   Label hminLabel = new Label("Hmin");
	   Label sminLabel = new Label("Smin");
	   Label vminLabel = new Label("Vmin");
	   Label hmaxLabel = new Label("Hmax");
	   Label smaxLabel = new Label("Smax");
	   Label vmaxLabel = new Label("Vmax");
	   sliderHMin.setLayoutX(200);
	   sliderHMin.setLayoutY(500);
	   hminLabel.setLayoutX(170);
	   hminLabel.setLayoutY(500);
	   
	   sliderSMin.setLayoutX(200);
	   sliderSMin.setLayoutY(550);
	   sminLabel.setLayoutX(170);
	   sminLabel.setLayoutY(550);
	   
	   sliderVMin.setLayoutX(200);
	   sliderVMin.setLayoutY(600);
	   vminLabel.setLayoutX(170);
	   vminLabel.setLayoutY(600);
	   
	   sliderHMax.setLayoutX(400);
	   sliderHMax.setLayoutY(500);
	   hmaxLabel.setLayoutX(370);
	   hmaxLabel.setLayoutY(500);
	   
	   sliderSMax.setLayoutX(400);
	   sliderSMax.setLayoutY(550);
	   smaxLabel.setLayoutX(370);
	   smaxLabel.setLayoutY(550);
	   
	   sliderVMax.setLayoutX(400);
	   sliderVMax.setLayoutY(600);
	   vmaxLabel.setLayoutX(370);
	   vmaxLabel.setLayoutY(600);
	   
	   Thread th = new Thread(() -> {
		   while(true) 
		   {
			   gp.hmin = sliderHMin.getValue();
			   gp.smin = sliderSMin.getValue();
			   gp.vmin = sliderVMin.getValue();
			   gp.hmax = sliderHMax.getValue();
			   gp.smax = sliderSMax.getValue();
			   gp.vmax = sliderVMax.getValue();
			   WritableImage writableImage = gp.getFrame();
			   imageView.setImage(writableImage);
		   }
	   });
	   
      // Размеры imageView
      imageView.setFitHeight(480);
      imageView.setFitWidth(640);
      


      // Создаём объект Group
      Group root = new Group(imageView, sliderHMin, sliderSMin, sliderVMin, sliderHMax, sliderSMax, sliderVMax,
    		  hminLabel, sminLabel, vminLabel, hmaxLabel, smaxLabel, vmaxLabel);

      

      // Создаём сцену
      Scene scene = new Scene(root, 640, 640);
      

      // Заголовок окна
      stage.setTitle("JavaFX + OpenCV");

      // Добавляем сцену
      stage.setScene(scene);

      // Показываем сцену
      stage.show();
      
      // Поток, получающий изображение с камеры
      th.start();

   }

   public static void main(String args[]) {
      launch(args);
   }
}