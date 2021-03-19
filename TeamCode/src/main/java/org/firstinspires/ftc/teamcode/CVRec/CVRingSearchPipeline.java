package org.firstinspires.ftc.teamcode.CVRec;

import android.util.Log;

import org.firstinspires.ftc.teamcode.skills.Geometry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class CVRingSearchPipeline extends CVPipelineBase {

    private ArrayList<CVRoi> regions = new ArrayList<>();
    private Mat YCrCb = new Mat();
    private Mat Cb = new Mat();
    private int numRegions;

    static final Point REGION_ANCHOR_POINT = new Point(150,100);
    static final int REGION_WIDTH = 40;
    static final int REGION_HEIGHT = 60;

    static final int SINGLE_MAX = 118;

    private static String TAG = "CVRingSearchPipeline";


    public CVRingSearchPipeline(int resX, int resY){
        super(resX, resY);
        this.numRegions = getResolutionX()/REGION_WIDTH * getResolutionY()/REGION_HEIGHT;
    }

    private void buildRegions(){
        int numCols = getResolutionX()/REGION_WIDTH;
        Point intake = new Point(getResolutionX()/2, getResolutionY());

        int startX = 0;
        int startY = 0;
        Point pointA = new Point(startX, startY);
        Point pointB = new Point(REGION_WIDTH, REGION_HEIGHT);
        int colIndex = 0;
        int rowIndex = 1;
        for(int i = 0; i < numRegions; i++){
            Rect r = new Rect(pointA, pointB);
            Log.d(TAG, String.format("Rectangle %d. A: %.2f x %.2f  B:  %.2f x %.2f    ", i, pointA.x, pointA.y, pointB.x, pointB.y));
            Point center = new Point(pointA.x + REGION_WIDTH/2, pointA.y + REGION_HEIGHT/2);
            double distancePixels = Geometry.getDistance(intake.x, intake.y, center.x, center.y);
            double catet = center.x - intake.x;
            boolean clockwise = catet > 0;
            double angleDegrees = Math.toDegrees(Math.asin(Math.abs(catet)/distancePixels));
            CVRoi roi = new CVRoi();
            roi.setIndex(i);
            roi.setAngle(angleDegrees);
            roi.setDistance(distancePixels);
            roi.setInput(Cb.submat(r));
            roi.setClockwise(clockwise);
            regions.add(roi);

            colIndex++;
            if (colIndex >= numCols){
                colIndex = 0;
                rowIndex++;
            }
            //next region
            pointA = new Point(startX + REGION_WIDTH*colIndex, startY + REGION_HEIGHT*(rowIndex - 1));
            pointB = new Point(startX + REGION_WIDTH*(colIndex+1), startY + REGION_HEIGHT*(rowIndex));
        }
    }

    @Override
    public void init(Mat input) {
        try {
            inputToCb(input);
            buildRegions();
        }
        catch (Exception ex){
            Log.e(TAG, "Error initializing the pipeline", ex);
        }
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCb(input);
        for(int i = 0; i < regions.size(); i++) {
            CVRoi roi = this.regions.get(i);
            double mean = (int) Core.mean(roi.getInput()).val[0];

           if(mean < SINGLE_MAX && !getTargets().contains(roi)) {
               Log.d(TAG, String.format("Adding Roi: %d to targets", i));
               getTargets().add(roi);
            }
        }

        return input;
    }

    private void inputToCb(Mat input)
    {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cb, 2);
    }
}
