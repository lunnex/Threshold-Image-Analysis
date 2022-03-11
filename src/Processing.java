import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
//import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;



//import static org.opencv.imgproc.Imgproc.

public class Processing {
	
	public Mat makeHSV(Mat NonHSVMatr)
	{
		Mat HSVMatr = new Mat();
		Imgproc.cvtColor(NonHSVMatr, HSVMatr, Imgproc.COLOR_BGR2HSV);
		return HSVMatr;
	}
	
	public Mat theresholding(Mat NonTheresholded, int hmin, int smin,  double vmin, double hmax, double smax, double vmax)
	{	
		double hLow = hmin;
		double sLow = 255 * smin;
		double vLow = 255 * vmin;
		double hHigh = hmax;
		double sHigh = 255 * smax;
		double vHigh = 255 * vmax;
		
		//System.out.println(hLow + " " + sLow + " " + vLow + " " + hHigh + " " + sHigh + " " + vHigh);
		Mat theresholded = new Mat();
 		Mat kernel = new Mat(new Size(10, 10), CvType.CV_8U, new Scalar(255));
 		
 		 Mat mask = new Mat();
 		//NonTheresholded.copyTo(mask);
 		
 		 
 		Imgproc.cvtColor(NonTheresholded, NonTheresholded, Imgproc.COLOR_BGR2GRAY);
 		NonTheresholded.copyTo(mask);
 		
 		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
 		 Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

 		 Imgproc.erode(mask, NonTheresholded, erodeElement);
 		 Imgproc.erode(mask, NonTheresholded, erodeElement);

 		 Imgproc.dilate(mask, NonTheresholded, dilateElement);
 		 Imgproc.dilate(mask, NonTheresholded, dilateElement);
 		//Imgproc.dilate(mask, mask, kernel);
 		Core.subtract(NonTheresholded, mask, NonTheresholded);
 		
 		
 		

 		Imgproc.adaptiveThreshold(NonTheresholded,theresholded,255,  Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, hmin, smin);

 		//Imgproc.dilate(theresholded, theresholded, kernel);
 		 //Imgproc.erode(theresholded, theresholded, kernel);

 		
		
		return theresholded;
	}
	
	public Mat findBoundary(Mat mat)
	{
		Mat edges = new Mat();
		Imgproc.Canny(mat, edges, 80, 200);
		
		return edges;
	}
	
	public List<MatOfPoint> findContour(Mat mat)
	{
		List<MatOfPoint> matOfPoint = new ArrayList<MatOfPoint>();
		List<MatOfPoint> ListForBiggestCoutour = new ArrayList<MatOfPoint>();
		MatOfPoint biggestContour = null; 
		Mat hierachy = new Mat();
		
		Mat dst = new Mat(mat.rows(), mat.cols(), mat.type());
        mat.copyTo(dst);
        
       


		Imgproc.findContours(dst, matOfPoint, hierachy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_KCOS);
		
		
		
		double smallestArea = 0;
		for(MatOfPoint contour : matOfPoint) {
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), false) > smallestArea) {
				smallestArea = Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), false);
				biggestContour = contour;
			}
		}
		if (biggestContour != null) {
			ListForBiggestCoutour.add(biggestContour);
		}
		else {
		}
		


		
		return ListForBiggestCoutour;
	}
	
	public Mat contour(Mat image, List<MatOfPoint>matOfCountors) {
		try {
			Rect rectangle = Imgproc.boundingRect(matOfCountors.get(0));
			Imgproc.rectangle(image, new Point(rectangle.x, rectangle.y),
					 new Point(rectangle.x + rectangle.width - 1, rectangle.y + rectangle.height - 1),
					 new Scalar(0,255,0)); 
			Imgproc.drawContours(image, matOfCountors, -1, new Scalar(255,0,0), 5);
		}
		catch (Exception e) {
		}
		return image;
	}

}
