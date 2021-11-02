import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class GetCapture {
	
	private Mat matrix;
	private Mat theresh;
	private Mat boundaries;
	VideoCapture capture = new VideoCapture(0);
	Processing proc = new Processing();
	double hmin, smin, vmin, hmax, smax, vmax;
	
	public WritableImage getFrame(int i) {
		return(frame(capture).get(i));
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
				   Mat original = new Mat();
				   matrix.copyTo(original);
				   matrix = proc.makeHSV(matrix);
				   matrix = proc.theresholding(matrix, hmin, smin, vmin, hmax, smax, vmax);
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
	
	   
	//public WritableImage getVideo() 
	//{
	//	WritableImage writableImage = null;
	//	writableImage = frame(capture);
	//    return writableImage;

	//}
}
