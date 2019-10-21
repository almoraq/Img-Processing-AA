/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageanalysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author amora
 */
public class CaseImage {
    BufferedImage image;
    BufferedImage imageCopy;
    String inputFile;
    int width, height;
    int targetTests = 512;
    int totalCoordDivisions;
    ArrayList<PixelData> pixelMetadataArray = new ArrayList<>();
    ArrayList<Integer[]> imageMetadataMatrix = new ArrayList<>();
    int totalSections;
    int sectionXDistance;
    int sectionYDistance;
    Integer[] highestLeftestCoord = {0,0};
    Integer[] lowestRightestCoord = {0,0};
    int totalColoredPixels = 0;
    int totalWhitePixels = 0;
    
    public CaseImage(String pInputFile, int pTotalCoordDivisions){
        this.inputFile = pInputFile;
        this.totalCoordDivisions = pTotalCoordDivisions;
        this.totalSections = totalCoordDivisions*totalCoordDivisions;
        //this.targetTests = (this.width*this.height)/10;
        System.out.println("Target tests: "+this.targetTests);
    }

    public void performTests(){
        this.setupImage();
        System.out.println("Image setup finalized.");
        int minX = 0;
        int minY = 0;
        int maxX = minX + this.sectionXDistance;
        int maxY = minY + this.sectionYDistance;
        
        int subsections = (int) Math.sqrt(this.totalSections);
        //System.out.println("Total subsections: "+subsections);
        for(int imageRows = 0; imageRows < subsections; imageRows++){
            //System.out.println("Miny for row "+imageRows+": "+minY);
            for(int imageColumns = 0; imageColumns < subsections; imageColumns++){
                if(minY <= (this.height-this.sectionYDistance) && minX <= (this.width-this.sectionXDistance) && maxX < this.width+1){
                    System.out.println("Testing in coordinates:\n x  ,  y\n"+minX+" , "+minY+"\n"+maxX+" , "+maxY);
                    ImageSection currSection = new ImageSection(minX, minY, maxX, maxY, this.targetTests, this.image, this.imageCopy);
                    
                    currSection.testPixelsArea();
                    this.addImageMetadata(currSection.getPixelValuesArray());
                    this.totalColoredPixels+=currSection.getColoredPixels();
                    this.totalWhitePixels+=currSection.getWhitePixels();
                    minX += this.sectionXDistance;
                    maxX += this.sectionXDistance;
                    currSection.anchor[0]+=this.sectionXDistance;
                    currSection.anchor[1]+=this.sectionYDistance;
                }
            }
            minY += this.sectionYDistance;
            maxY += this.sectionYDistance;
            minX = 0;
            maxX = minX + this.sectionXDistance;
        }
        this.updateMetadataArray();
        System.out.println("Total colored pixels: "+this.totalColoredPixels+"\nTotal white pixels: "+this.totalWhitePixels);
    }
    
    public void addImageMetadata(ArrayList<PixelData> pPixelValuesArray){
        int pixelArraySize = pPixelValuesArray.size();
        for(int rows = 0; rows < pixelArraySize; rows++){
            PixelData pixelValue = pPixelValuesArray.get(rows);
            if(this.pixelMetadataArray.contains(pixelValue)){
                pixelValue.setOccurrence(pixelValue.getOccurrence()+1);
            }
            else{
                this.pixelMetadataArray.add(pPixelValuesArray.get(0));
            }
            
        }
    }
    
    public void updateMetadataArray(){ 
        ArrayList<PixelData> newMetadataArray = new ArrayList<>();
        for(int rows = 0; rows < this.pixelMetadataArray.size(); rows++){
            PixelData pixel = this.pixelMetadataArray.get(rows);
            if(!(newMetadataArray.contains(pixel))){
                newMetadataArray.add(pixel);
            }
            else{
                //adds 1 to occurrence
                int elementIndex = newMetadataArray.indexOf(pixel);
                int elementOccurrence = newMetadataArray.get(elementIndex).getOccurrence()+1;
                newMetadataArray.get(elementIndex).setOccurrence(elementOccurrence);
            }
        }
        this.pixelMetadataArray = newMetadataArray;
    } 
    
    public void setupImage(){
        //Read the image given by the filename
        try
        { 
            //image file path
            File inputFile = new File(this.inputFile);   
            //Reading input file 
            setImage(ImageIO.read(inputFile)); 
            setWidth(image.getWidth());
            setHeight(image.getHeight());
            System.out.println("Reading complete.");
            this.imageCopy = this.image;
        } 
        catch(IOException e) 
        { 
            System.out.println("Error: "+e); 
        }
        this.sectionXDistance = this.width/this.totalCoordDivisions;
        this.sectionYDistance = this.height/this.totalCoordDivisions;
    }
    
    public void printMatrix(ArrayList<PixelData> pArray){
        System.out.println("Pixel values:\nRed | Green | Blue | Occurrence | ");
        for(int rows = 0; rows < pArray.size(); rows++){
            int redValue = pArray.get(rows).getRedValue();
            int greenValue = pArray.get(rows).getGreenValue();
            int blueValue = pArray.get(rows).getBlueValue();
            int occurrenceValue = pArray.get(rows).getOccurrence();
            System.out.println(redValue+"  \t "+greenValue+"\t "+blueValue+"\t  "+occurrenceValue);
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public ArrayList<Integer[]> getImageMetadataMatrix() {
        return imageMetadataMatrix;
    }

    public void setImageMetadataMatrix(ArrayList<Integer[]> imageMetadataMatrix) {
        this.imageMetadataMatrix = imageMetadataMatrix;
    }

    public ArrayList<PixelData> getPixelMetadataArray() {
        return pixelMetadataArray;
    }

    public void setPixelMetadataArray(ArrayList<PixelData> pixelMetadataArray) {
        this.pixelMetadataArray = pixelMetadataArray;
    }
}
