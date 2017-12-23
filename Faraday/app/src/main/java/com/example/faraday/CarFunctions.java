package com.example.faraday;

/**
 * Created by Mukul Gosavi on 11/22/2017.
 */

public class CarFunctions {

    /*
    *
    *
    * */

    private static int followTrack = 0;

    public static int rPiNo = 1, auto_mode = 1;
    public static int twoCarsCrashFlag = 0, extTwoCarsCrashFlag = 0;
    public static float carWidth = 0, carHeight = 0, trackWidth = 0;

    private static float vertexXMin = 0, vertexXMax = 0, vertexYMin = 0, vertexYMax = 0;
    private static float vertexXMin_1 = 0, vertexXMax_1 = 0, vertexYMin_1 = 0, vertexYMax_1 = 0;
    private static float vertexXMin_2 = 0, vertexXMax_2 = 0, vertexYMin_2 = 0, vertexYMax_2 = 0;
    public static float vertexXMinExt = 0, vertexXMaxExt = 0, vertexYMinExt = 0, vertexYMaxExt = 0, extW, extH;
    public static float vertexXMinExt_1 = 0, vertexXMaxExt_1 = 0, vertexYMinExt_1 = 0, vertexYMaxExt_1 = 0;
    public static float vertexXMinExt_2 = 0, vertexXMaxExt_2 = 0, vertexYMinExt_2 = 0, vertexYMaxExt_2 = 0;
    private static float trackTop = 0, trackBottom = 0, trackLeft = 0, trackRight = 0;
    private static float boundTop_OM, boundBottom_OM, boundLeft_OM, boundRight_OM;             // Outer Boundaries of 1st track (Outermost)
    private static float boundTop_I1O2, boundBottom_I1O2, boundLeft_I1O2, boundRight_I1O2;     // Inner Boundaries of 1st track or Outer Boundaries of 2nd track
    private static float boundTop_I2O3, boundBottom_I2O3, boundLeft_I2O3, boundRight_I2O3;     // Inner Boundaries of 2nd track or Outer Boundaries of 3rd track
    private static float boundTop_IM, boundBottom_IM, boundLeft_IM, boundRight_IM;             // Inner Boundaries of 3rd track (Innermost)

    public static float initPosX = 0, initPosY = 0, carAngle = 0;

    public static synchronized boolean CheckBounds() {

        CarTrackInit();
        FindCarVertices();

        //bounds are derived by trial & error

        boundTop_OM = trackTop  + 4;
        boundBottom_OM = trackBottom;
        boundLeft_OM = trackLeft + 5;
        boundRight_OM = trackRight - 4;

        boundTop_I1O2 = boundTop_OM + trackWidth/10 - 5;
        boundBottom_I1O2 = boundBottom_OM - trackWidth/10;
        boundLeft_I1O2 = boundLeft_OM + trackWidth/10 - 8;
        boundRight_I1O2 = boundRight_OM - trackWidth/10 + 5;

        boundTop_I2O3 = boundTop_I1O2 + trackWidth/10;
        boundBottom_I2O3 = boundBottom_I1O2 - trackWidth/10;
        boundLeft_I2O3 = boundLeft_I1O2 + trackWidth/10;
        boundRight_I2O3 = boundRight_I1O2 - trackWidth/10 + 5;

        boundTop_IM = boundTop_I2O3 + trackWidth/10;
        boundBottom_IM = boundBottom_I2O3 - trackWidth/10;
        boundLeft_IM = boundLeft_I2O3 + trackWidth/10;
        boundRight_IM = boundRight_I2O3 - trackWidth/10 + 5;

        if(followTrack == 0 || followTrack > 4) {
            if((vertexYMin > boundTop_OM) && (vertexYMax < boundBottom_OM) && (vertexXMin > boundLeft_OM) && (vertexXMax < boundRight_OM)) {
                return true;
            }
        }
        else if(followTrack == 1) {
            if((vertexYMin > boundTop_OM) && (vertexYMax < boundBottom_OM) && (vertexXMin > boundLeft_OM) && (vertexXMax < boundRight_OM)) {
                if((vertexYMax < boundTop_I1O2) || (vertexYMin > boundBottom_I1O2) || (vertexXMax < boundLeft_I1O2) || (vertexXMin > boundRight_I1O2)) {
                    return true;
                }
            }
        }
        else if(followTrack == 2) {
            if((vertexYMin > boundTop_I1O2) && (vertexYMax < boundBottom_I1O2) && (vertexXMin > boundLeft_I1O2) && (vertexXMax < boundRight_I1O2)) {
                if((vertexYMax < boundTop_I2O3) || (vertexYMin > boundBottom_I2O3) || (vertexXMax < boundLeft_I2O3) || (vertexXMin > boundRight_I2O3)) {
                    return true;
                }
            }
        }
        else if(followTrack == 3) {
            if((vertexYMin > boundTop_I2O3) && (vertexYMax < boundBottom_I2O3) && (vertexXMin > boundLeft_I2O3) && (vertexXMax < boundRight_I2O3)) {
                if((vertexYMax < boundTop_IM) || (vertexYMin > boundBottom_IM) || (vertexXMax < boundLeft_IM) || (vertexXMin > boundRight_IM)) {
                    return true;
                }
            }
        }
        else if(followTrack == 4) {
            if((vertexYMin > boundTop_OM) && (vertexYMax < boundBottom_OM) && (vertexXMin > boundLeft_OM) && (vertexXMax < boundRight_OM)) {
                if((vertexYMax < boundTop_IM) || (vertexYMin > boundBottom_IM) || (vertexXMax < boundLeft_IM) || (vertexXMin > boundRight_IM)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static synchronized boolean CheckCarCrash() {

        FindCarVertices();
        FindCarVertices_1();

        // Check if Car 1 crashes with my car
        if(!(((vertexYMin < vertexYMax_1 && vertexYMax < vertexYMin_1) || (vertexYMin > vertexYMax_1 && vertexYMax > vertexYMin_1)) ||
                ((vertexXMin < vertexXMax_1 && vertexXMax < vertexXMin_1) || (vertexXMin > vertexXMax_1 && vertexXMax > vertexXMin_1)))) {
            twoCarsCrashFlag = 1;
            return false;
        }

        FindCarVertices_2();
        // Check if Car 2 crashes with my car
        if(!(((vertexYMin < vertexYMax_2 && vertexYMax < vertexYMin_2) || (vertexYMin > vertexYMax_2 && vertexYMax > vertexYMin_2)) ||
                ((vertexXMin < vertexXMax_2 && vertexXMax < vertexXMin_2) || (vertexXMin > vertexXMax_2 && vertexXMax > vertexXMin_2)))) {
            twoCarsCrashFlag = 2;
            return false;
        }

        return true;
    }


    public static synchronized boolean PreventCheckCarCrash() {

        FindCarVertices();
        FindCarVertices_1();


        // Check if Car 1 crashes with my car
        if(!(((vertexYMinExt < vertexYMaxExt_1 && vertexYMaxExt < vertexYMinExt_1) || (vertexYMinExt > vertexYMaxExt_1 && vertexYMaxExt > vertexYMinExt_1)) ||
                ((vertexXMinExt < vertexXMaxExt_1 && vertexXMaxExt < vertexXMinExt_1) || (vertexXMinExt > vertexXMaxExt_1 && vertexXMaxExt > vertexXMinExt_1)))) {
            extTwoCarsCrashFlag = 1;
            return false;
        }

        FindCarVertices_2();
        // Check if Car 2 crashes with my car
        if(!(((vertexYMinExt < vertexYMaxExt_2 && vertexYMaxExt < vertexYMinExt_2) || (vertexYMinExt > vertexYMaxExt_2 && vertexYMaxExt > vertexYMinExt_2)) ||
                ((vertexXMinExt < vertexXMaxExt_2 && vertexXMaxExt < vertexXMinExt_2) || (vertexXMinExt > vertexXMaxExt_2 && vertexXMaxExt > vertexXMinExt_2)))) {
            extTwoCarsCrashFlag = 2;
            return false;
        }

        return true;
    }


    public static synchronized void CarTrackInit() {
        initPosX = CarTrackSim.car_img.getX();
        initPosY = CarTrackSim.car_img.getY();
        carAngle = CarTrackSim.car_img.getRotation();
        trackTop = CarTrackSim.track_img.getTop();
        trackBottom = CarTrackSim.track_img.getBottom();
        trackLeft = CarTrackSim.track_img.getLeft();
        trackRight = CarTrackSim.track_img.getRight();
        carWidth = CarTrackSim.car_img.getWidth();
        carHeight = CarTrackSim.car_img.getHeight();
        trackWidth = CarTrackSim.track_img.getWidth();
    }


    private static synchronized void FindCarVertices() {
        float frontLeftX, frontLeftY, frontRightX, frontRightY;
        float bottomLeftX, bottomLeftY, bottomRightX, bottomRightY;

        double carAngleLocal = Math.toRadians(CarTrackSim.car_img.getRotation());

        double wCos = carWidth * Math.cos(Math.toRadians(carAngleLocal));
        double wSin = carWidth * Math.sin(Math.toRadians(carAngleLocal));
        double hCos = carHeight * Math.cos(Math.toRadians(carAngleLocal));
        double hSin = carHeight * Math.sin(Math.toRadians(carAngleLocal));

        frontLeftX = CarTrackSim.car_img.getX();
        frontLeftY = CarTrackSim.car_img.getY();
        frontRightX = (float) (CarTrackSim.car_img.getX() + wCos);
        frontRightY = (float) (CarTrackSim.car_img.getY() + wSin);
        bottomLeftX = (float) (CarTrackSim.car_img.getX() - hSin);
        bottomLeftY = (float) (CarTrackSim.car_img.getY() + hCos);
        bottomRightX = (float) (CarTrackSim.car_img.getX() + wCos - hSin);
        bottomRightY = (float) (CarTrackSim.car_img.getY() + wSin + hCos);

        extH = carHeight/2;
        extW = carWidth/2;

        vertexXMin = Math.min(Math.min(frontLeftX, frontRightX), Math.min(bottomLeftX, bottomRightX));
        vertexXMinExt = vertexXMin - extW;
        vertexXMax = Math.max(Math.max(frontLeftX, frontRightX), Math.max(bottomLeftX, bottomRightX));
        vertexXMaxExt = vertexXMax + extW;
        vertexYMin = Math.min(Math.min(frontLeftY, frontRightY), Math.min(bottomLeftY, bottomRightY));
        vertexYMinExt = vertexYMin - extH;
        vertexYMax = Math.max(Math.max(frontLeftY, frontRightY), Math.max(bottomLeftY, bottomRightY));
        vertexYMaxExt = vertexYMax + extH;
    }


    private static synchronized void FindCarVertices_1() {
        float frontLeftX_1, frontLeftY_1, frontRightX_1, frontRightY_1;
        float bottomLeftX_1, bottomLeftY_1, bottomRightX_1, bottomRightY_1;

        double carAngleLocal_1 = Math.toRadians(CarTrackSim.car_img_1.getRotation());

        double wCos_1 = carWidth * Math.cos(Math.toRadians(carAngleLocal_1));
        double wSin_1 = carWidth * Math.sin(Math.toRadians(carAngleLocal_1));
        double hCos_1 = carHeight * Math.cos(Math.toRadians(carAngleLocal_1));
        double hSin_1 = carHeight * Math.sin(Math.toRadians(carAngleLocal_1));

        frontLeftX_1 = CarTrackSim.car_img_1.getX();
        frontLeftY_1 = CarTrackSim.car_img_1.getY();
        frontRightX_1 = (float) (CarTrackSim.car_img_1.getX() + wCos_1);
        frontRightY_1 = (float) (CarTrackSim.car_img_1.getY() + wSin_1);
        bottomLeftX_1 = (float) (CarTrackSim.car_img_1.getX() - hSin_1);
        bottomLeftY_1 = (float) (CarTrackSim.car_img_1.getY() + hCos_1);
        bottomRightX_1 = (float) (CarTrackSim.car_img_1.getX() + wCos_1 - hSin_1);
        bottomRightY_1 = (float) (CarTrackSim.car_img_1.getY() + wSin_1 + hCos_1);

        vertexXMin_1 = Math.min(Math.min(frontLeftX_1, frontRightX_1), Math.min(bottomLeftX_1, bottomRightX_1));
        vertexXMinExt_1 = vertexXMin_1 - extW;
        vertexXMax_1 = Math.max(Math.max(frontLeftX_1, frontRightX_1), Math.max(bottomLeftX_1, bottomRightX_1));
        vertexXMaxExt_1 = vertexXMax_1 + extW;
        vertexYMin_1 = Math.min(Math.min(frontLeftY_1, frontRightY_1), Math.min(bottomLeftY_1, bottomRightY_1));
        vertexYMinExt_1 = vertexYMin_1 - extH;
        vertexYMax_1 = Math.max(Math.max(frontLeftY_1, frontRightY_1), Math.max(bottomLeftY_1, bottomRightY_1));
        vertexYMaxExt_1 = vertexYMax_1 + extH;

    }


    private static synchronized void FindCarVertices_2() {
        float frontLeftX_2, frontLeftY_2, frontRightX_2, frontRightY_2;
        float bottomLeftX_2, bottomLeftY_2, bottomRightX_2, bottomRightY_2;

        double carAngleLocal_2 = Math.toRadians(CarTrackSim.car_img_2.getRotation());

        double wCos_2 = carWidth * Math.cos(Math.toRadians(carAngleLocal_2));
        double wSin_2 = carWidth * Math.sin(Math.toRadians(carAngleLocal_2));
        double hCos_2 = carHeight * Math.cos(Math.toRadians(carAngleLocal_2));
        double hSin_2 = carHeight * Math.sin(Math.toRadians(carAngleLocal_2));

        frontLeftX_2 = CarTrackSim.car_img_2.getX();
        frontLeftY_2 = CarTrackSim.car_img_2.getY();
        frontRightX_2 = (float) (CarTrackSim.car_img_2.getX() + wCos_2);
        frontRightY_2 = (float) (CarTrackSim.car_img_2.getY() + wSin_2);
        bottomLeftX_2 = (float) (CarTrackSim.car_img_2.getX() - hSin_2);
        bottomLeftY_2 = (float) (CarTrackSim.car_img_2.getY() + hCos_2);
        bottomRightX_2 = (float) (CarTrackSim.car_img_2.getX() + wCos_2 - hSin_2);
        bottomRightY_2 = (float) (CarTrackSim.car_img_2.getY() + wSin_2 + hCos_2);

        vertexXMin_2 = Math.min(Math.min(frontLeftX_2, frontRightX_2), Math.min(bottomLeftX_2, bottomRightX_2));
        vertexXMinExt_2 = vertexXMin_2 - extW;
        vertexXMax_2 = Math.max(Math.max(frontLeftX_2, frontRightX_2), Math.max(bottomLeftX_2, bottomRightX_2));
        vertexXMaxExt_2 = vertexXMax_2 + extW;
        vertexYMin_2 = Math.min(Math.min(frontLeftY_2, frontRightY_2), Math.min(bottomLeftY_2, bottomRightY_2));
        vertexYMinExt_2 = vertexYMin_2 - extH;
        vertexYMax_2 = Math.max(Math.max(frontLeftY_2, frontRightY_2), Math.max(bottomLeftY_2, bottomRightY_2));
        vertexYMaxExt_2 = vertexYMax_2 + extH;
    }


}

