import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
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
	
	public Mat theresholding(Mat NonTheresholded, double hmin, double smin,  double vmin, double hmax, double smax, double vmax)
	{	
		double hLow = hmin;
		double sLow = 255 * smin;
		double vLow = 255 * vmin;
		double hHigh = hmax;
		double sHigh = 255 * smax;
		double vHigh = 255 * vmax;
		
		//System.out.println(hLow + " " + sLow + " " + vLow + " " + hHigh + " " + sHigh + " " + vHigh);
		Mat theresholded = new Mat();
 		Mat kernel = new Mat(new Size(5, 5), CvType.CV_8U, new Scalar(255));
 		 
 		Core.inRange(NonTheresholded, new Scalar(hLow, sLow, vLow, 0), new Scalar(hHigh, sHigh, vHigh, 0), theresholded);
		Imgproc.morphologyEx(theresholded, theresholded, 2, kernel);
		//System.out.println(theresholded.channels() + " " + theresholded.depth());
		
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
		List<MatOfPoint> biggestContours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> biggestContour = new ArrayList<MatOfPoint>();
		Mat hierachy = new Mat();
		
		Mat dst = new Mat(mat.rows(), mat.cols(), mat.type());
        mat.copyTo(dst);


		Imgproc.findContours(dst, matOfPoint, hierachy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		double contourArea = 0;
		for (int i = 0; i < matOfPoint.size(); i++) {
			if (Imgproc.contourArea(matOfPoint.get(i)) > contourArea) {
				biggestContours.add(matOfPoint.get(i));
				contourArea = Imgproc.contourArea(matOfPoint.get(i));
			}
		}
		try {
			biggestContour.add(biggestContours.get(biggestContours.size()-1));
		}
		catch (Exception e) {
			System.out.println("Контуры не найдены");
		}
		//System.out.println(qqq.channels() + " " + qqq.depth());
		return biggestContour;
	}
	
	public Mat contour(Mat image, List<MatOfPoint>matOfCountors) {
		Imgproc.drawContours(image, matOfCountors, -1, new Scalar(1,0,0), 5);
		return image;
	}

}
