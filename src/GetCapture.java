import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Date;

//import org.bytedeco.tesseract.MATRIX;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class GetCapture {
	
	private Mat matrix;
	private Mat theresh;
	private Mat boundaries;
	VideoCapture capture = new VideoCapture(0);
	Processing proc = new Processing();
	int hmin1, smin1, vmin1, hmax1, smax1, vmax1, hmin2, smin2, vmin2, hmax2, smax2, vmax2;
	
	public WritableImage getFrame(int i) {
		return(frame(capture).get(i));
	}
	
	public ArrayList<WritableImage> getFrameArray() {
		//System.out.println("sss");
		return frame(capture);
	}
	
	public String getTime() {
		Date date = new Date();
		return date.toString();
	}
	
	public Mat mergeMats(Mat mat1, Mat mat2) {
		
		try {
		for(int i = 0; i < mat1.cols(); i++) {
			for(int j = 0; j < mat1.rows(); j++) {
				if(mat2.get(i,j)[0] == 255.0) {
					mat1.put(i, j, mat2.get(i,j)[0]);
				}
			}
			
		}
		}
		catch(Exception e) {
			
			//System.out.println(e);
		}
		return mat1;
	}
	
	public ArrayList<WritableImage> frame(VideoCapture capture)
	   {
		ArrayList<WritableImage> images = new ArrayList<WritableImage>();
		   //Processing proc = new Processing();
		// Читаем кадр с камеры
		   Mat matrix = new Mat();
		   capture.read(matrix);

		      // Если камера включена
		   if( capture.isOpened()) 
		   {
		       // Если есть следующий кадр с камеры
			   if (capture.read(matrix)) 
		       {
				   
				   CascadeClassifier face_detector = new CascadeClassifier();
				   String path = "C:\\Program Files\\opencv\\opencv\\sources\\data\\haarcascades\\";
				   String name = "haarcascade_frontalface_alt.xml";
				   
				   if (!face_detector.load(path + name)) {
					   System.out.println("Не удалось загрузить классификатор " + name);
					  
					  } 

				   MatOfRect faces = new MatOfRect();
				   face_detector.detectMultiScale(matrix, faces);
				   for (Rect r : faces.toList()) {
				    Imgproc.rectangle(matrix, new Point(r.x, r.y),
				    new Point(r.x + r.width, r.y + r.height), new Scalar(0,255,0), 2);
				   } 
				   

				   Imgproc.morphologyEx(matrix, matrix, 2, new Mat(new Size(7, 7), CvType.CV_8U, new Scalar(255)));
				   //Imgproc.morphologyEx(matrix, matrix, 2, new Mat(new Size(7, 7), CvType.CV_8U, new Scalar(255)));
		          // Imgproc.medianBlur (matrix, matrix, 5);
		          Imgproc.medianBlur (matrix, matrix, 5);
		          //Imgproc.medianBlur (matrix, matrix, 5);
		          //Imgproc.medianBlur (matrix, matrix, 5);
		          
				   //Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);
				   
				   Mat original = new Mat();
				   //Mat allixuaryMat = new Mat();
				   matrix.copyTo(original);
				   //matrix.copyTo(allixuaryMat);
				   //matrix = proc.makeHSV(matrix);
				   //allixuaryMat = proc.makeHSV(allixuaryMat);
				   matrix = proc.theresholding(matrix, hmin1, smin1, vmin1, hmax1, smax1, vmax1);
				   //allixuaryMat = proc.theresholding(allixuaryMat, hmin2, smin2, vmin2, hmax2, smax2, vmax2);
				   
				   //System.out.println(allixuaryMat.get(0, 0)[0]);
				   //matrix = mergeMats(matrix, allixuaryMat);
				   Mat theresh = new Mat();
				   matrix.copyTo(theresh);
				   matrix = proc.findBoundary(matrix);
				   Mat boundaries = new Mat();
				   matrix.copyTo(boundaries);
				   original = proc.contour(original, proc.findContour(matrix));

		           // Создаем буфферизированное изображение 
		           BufferedImage image = new BufferedImage(original.width(), original.height(), BufferedImage.TYPE_3BYTE_BGR);
		           
		            
		           WritableRaster raster = image.getRaster();
		           DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		           byte[] data = dataBuffer.getData();
		           original.get(0, 0, data);
		           //matrix.get(0, 0, data);
		           this.matrix = matrix;
		            
		            // Создаём Writable Image
		           images.add(0, SwingFXUtils.toFXImage(image, null));
		           
		        // Создаем буфферизированное изображение 
		           BufferedImage thereshimage = new BufferedImage(theresh.width(), theresh.height(), BufferedImage.TYPE_3BYTE_BGR);
		            
		            
		           WritableRaster thereshraster = thereshimage.getRaster();
		           DataBufferByte thereshdataBuffer = (DataBufferByte) thereshraster.getDataBuffer();
		           byte[] thereshdata = thereshdataBuffer.getData();
		           theresh.get(0, 0, thereshdata);
		           //matrix.get(0, 0, data);
		           this.theresh = theresh;
		           
		           images.add(1, SwingFXUtils.toFXImage(thereshimage, null));
		           
		        // Создаем буфферизированное изображение 
		           BufferedImage boudariesimage = new BufferedImage(boundaries.width(), boundaries.height(), BufferedImage.TYPE_3BYTE_BGR);
		            
		            
		           WritableRaster boudariesraster = boudariesimage.getRaster();
		           DataBufferByte boudariesdataBuffer = (DataBufferByte) boudariesraster.getDataBuffer();
		           byte[] boudariesdata = boudariesdataBuffer.getData();
		           boundaries.get(0, 0, boudariesdata);
		           //matrix.get(0, 0, data);
		           this.boundaries = boundaries;
		           
		           
		           images.add(2, SwingFXUtils.toFXImage(boudariesimage, null));
		         }
		     }
		  return images;
	   }
	
	public Mat getMatrix()
	{
		return matrix;
	}
	
	public void setMatrix(Mat matrix)
	{
		this.matrix = matrix;
	}
	
}
