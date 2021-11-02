import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class GetCapture {
	
	private Mat matrix;
	VideoCapture capture = new VideoCapture(0);
	Processing proc = new Processing();
	double hmin, smin, vmin, hmax, smax, vmax;
	
	public WritableImage getFrame() {
		return(frame(capture));
	}

	public WritableImage frame(VideoCapture capture)
	   {
		   WritableImage WritableImage = null;
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
				   Mat original = new Mat();
				   matrix.copyTo(original);
				   matrix = proc.makeHSV(matrix);
				   matrix = proc.theresholding(matrix, hmin, smin, vmin, hmax, smax, vmax);
				   matrix = proc.findBoundary(matrix);
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
		           WritableImage = SwingFXUtils.toFXImage(image, null);
		         }
		     }
		  return WritableImage;
	   }
	
	public Mat getMatrix()
	{
		return matrix;
	}
	
	public void setMatrix(Mat matrix)
	{
		this.matrix = matrix;
	}
	
	   
	public WritableImage getVideo() 
	{
		WritableImage writableImage = null;
		writableImage = frame(capture);
	    return writableImage;

	}
}
