/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageanalysis;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author amora
 */
public class ImageSection {
    BufferedImage imageSample;
    BufferedImage fullImageCopy;
    ArrayList<PixelData> pixelsArray = new ArrayList<>();
    ArrayList<Integer[]> sectionValuesMatrix = new ArrayList<>();
    int minX, minY;
    int maxX, maxY;
    Integer[] anchor = new Integer[2];
    int whitePixels = 0;
    int coloredPixels = 0;
    int xRange;
    int yRange;
    int consecutiveWhitePixels = 0;
    int consecutiveWhitePixelsLimit = 6;
    int testingRange;
    int totalTests;
    int minTests = totalTests / 4;
    boolean isWhite = false;

    
    public ImageSection(int pMinX, int pMinY, int pMaxX, int pMaxY, int pTotalTests, BufferedImage pImageSample, BufferedImage pFullImageCopy){
        this.minX = pMinX;
        this.minY = pMinY;
        this.maxX = pMaxX;
        this.maxY = pMaxY;
        this.totalTests = pTotalTests;
        this.imageSample = pImageSample;
        this.fullImageCopy = pFullImageCopy;
        this.xRange = (this.maxX-this.minX);
        this.yRange = (this.maxY-this.minY);
    }
    public void testPixelsArea(){
        //System.out.println("Testing pixel area...");
        this.anchor[0] = this.xRange/2+this.minX;//X coordinate of the anchor
        this.anchor[1] = this.yRange/2+this.minY;
        this.testingRange = this.xRange/2;
        //System.out.println("Anchor x,y: "+this.anchor[0]+","+this.anchor[1]);
        for(int pixelsTested = 0; pixelsTested < this.totalTests; pixelsTested++){
            //Obtaining random coordinates for testing the pixel, limited by the max and min x,y of the section that is tested
            //Anchor values are used to steer the random elements into the colored pixels of the section
            //X and Y values picked are limited by the size of the section, so no values outside of them can be used
            int randomNum = ThreadLocalRandom.current().nextInt(this.anchor[0]-this.testingRange, this.anchor[0]+(this.testingRange));
            //int randomNum = random.nextInt(this.testingRange + this.testingRange) - this.testingRange; 
            while(!(this.anchor[0]+randomNum+minX < this.maxX || this.anchor[0]+randomNum+minX > this.minX)){
                randomNum = ThreadLocalRandom.current().nextInt(this.anchor[0]-this.testingRange, this.anchor[0]+(this.testingRange));
                //randomNum = ThreadLocalRandom.current().nextInt(1-(this.testingRange), testingRange);
            }
            //int pixelX = Math.abs(randomNum)+minX;
            int pixelX = Math.abs(randomNum);
            while(!(this.anchor[1]+randomNum < this.maxY || this.anchor[1]+randomNum > this.minY)){
                randomNum = ThreadLocalRandom.current().nextInt(this.anchor[1]-this.testingRange, this.anchor[1]+(this.testingRange));
                //randomNum = ThreadLocalRandom.current().nextInt(1-(this.testingRange), testingRange);
                //randomNum = ThreadLocalRandom.current().nextInt(this.anchor[1]-this.yRange/2, this.anchor[1]+(this.yRange/2) + 1);
                //System.out.println("Selected y: "+randomNum);
            }
            int pixelY = Math.abs(randomNum);
            //int pixelY = Math.abs(randomNum)+minY;
            //System.out.println("Testing pixel x,y:" +pixelX+","+pixelY);
            //Obtaining and saving the RGB values of the tested pixels. Values are stored inside the matrix
            //If pixel is white, values are not stored.
            //this.fullImageCopy.setRGB(pixelX, pixelY, 000000);
            this.obtainPixelValues(pixelX, pixelY);
            
            //Extra condition to stop section analysis if all pixels have been white after a certain amount of tests
            if(this.whitePixels == pixelsTested && pixelsTested > this.totalTests/4){
                this.isWhite = true;
                break;
            }
        }
        System.out.println("Test on pixels has finished.\nColored pixels: "+this.coloredPixels+"\nWhite pixels: "+this.whitePixels);
    }
    
    public void obtainPixelValues(int pX, int pY){
        //Obtains and evaluates the pixel values
        //If a pixel is white, it doesnt add it to the matrix
        
        int pixelValue = this.imageSample.getRGB(pX, pY);//obtaining the ARGB value, stored as a single number
        PixelData currPixel = new PixelData(pixelValue);
        currPixel.roundARGBValues();
        //ARBG values of the pixel are "extracted" from the pixel value for easier handling
        if(!currPixel.checkIfWhite()){
            System.out.println("Colored pixel values: "+currPixel.getAlphaValue()+", "+currPixel.getRedValue()+", "+currPixel.getGreenValue()+", "+currPixel.getBlueValue());
            this.anchor[0] = pX;
            this.anchor[1] = pY;
            if(this.testingRange>this.xRange/5){
                this.testingRange-=3;
            }
            
            //Pixel is not white, the values are added to the class matrix
            //Check if values are already in the matrix. If they are, occurrence is increased

            if(this.pixelsArray.contains(currPixel)){
                currPixel.setOccurrence(currPixel.getOccurrence()+1);
            }
            else{
                this.pixelsArray.add(currPixel);
            }
            this.coloredPixels += 1;
            /*this.anchor[0] -= 2;
            this.anchor[1] -= 2;*/
        }
        else{
            this.whitePixels += 1;
            this.consecutiveWhitePixels += 1;
            if(this.consecutiveWhitePixels>=this.consecutiveWhitePixelsLimit){
                this.anchor[0] = this.xRange/2+this.minX;//X coordinate of the anchor
                this.anchor[1] = this.yRange/2+this.minY;
                this.testingRange = this.xRange/2;
            }
            
            //System.out.println("White pixel found.");
        }
        
    }
    
    public BufferedImage getImageSample() {
        return imageSample;
    }

    public void setImageSample(BufferedImage imageSample) {
        this.imageSample = imageSample;
    }

    public ArrayList<PixelData> getPixelValuesArray() {
        return pixelsArray;
    }

    public void setPixelValuesArray(ArrayList<PixelData> pixelValuesArray) {
        this.pixelsArray = pixelValuesArray;
    }

    public ArrayList<Integer[]> getPixelValuesMatrix() {
        return sectionValuesMatrix;
    }

    public void setPixelValuesMatrix(ArrayList<Integer[]> pixelValuesMatrix) {
        this.sectionValuesMatrix = pixelValuesMatrix;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public Integer[] getAnchor() {
        return anchor;
    }

    public void setAnchor(Integer[] anchor) {
        this.anchor = anchor;
    }

    public int getWhitePixels() {
        return whitePixels;
    }

    public void setWhitePixels(int whitePixels) {
        this.whitePixels = whitePixels;
    }

    public int getColoredPixels() {
        return coloredPixels;
    }

    public void setColoredPixels(int coloredPixels) {
        this.coloredPixels = coloredPixels;
    }

    public int getxRange() {
        return xRange;
    }

    public void setxRange(int xRange) {
        this.xRange = xRange;
    }

    public int getyRange() {
        return yRange;
    }

    public void setyRange(int yRange) {
        this.yRange = yRange;
    }
    
    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public boolean isIsWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }
}
