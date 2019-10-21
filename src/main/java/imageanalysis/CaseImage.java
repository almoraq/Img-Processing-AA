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
import java.util.concurrent.ThreadLocalRandom;
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
    int targetTests = 52;
    int totalCoordDivisions;
    ArrayList<PixelData> pixelMetadataArray = new ArrayList<>();
    ArrayList<ImageSection> imageSectionsArray = new ArrayList<>();
    ArrayList<ImageSection> entireSectionsArray = new ArrayList<>();
    //ArrayList<Integer[]> imageMetadataMatrix = new ArrayList<>();
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

    public void performInitialTests(){
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
                    this.imageSectionsArray.add(currSection);
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
        this.sectionsArraySetup();
    }
    
    
    public void performAditionalTests(){
        //Get random element from the big sections array
        //Perform tests on that area, using that areas's coordinates as min and max xy
        //Redistribute the big sections array to accomodate new section pixelDistribution
        int randomNum = ThreadLocalRandom.current().nextInt(0, this.entireSectionsArray.size());
        ImageSection currSection = this.entireSectionsArray.get(randomNum);
        int minX = currSection.getMinX();
        int maxX = currSection.getMaxX();
        int minY = currSection.getMinY();
        int maxY = currSection.getMaxY();
        System.out.println("Testing in coordinates:\n x  ,  y\n"+minX+" , "+minY+"\n"+maxX+" , "+maxY);

        currSection.testPixelsArea();
        //this.imageSectionsArray.add(currSection);
        this.addImageMetadata(currSection.getPixelValuesArray());
        this.totalColoredPixels+=currSection.getColoredPixels();
        this.totalWhitePixels+=currSection.getWhitePixels();
            
        
        this.updateMetadataArray();
        //System.out.println("Total colored pixels: "+this.totalColoredPixels+"\nTotal white pixels: "+this.totalWhitePixels);
        //this.sectionsArraySetup();
        this.rearrangeSectionsArray();
    }
    
    public void sectionsArraySetup(){
        for(int sectionsAdded = 0; sectionsAdded < this.totalSections; sectionsAdded++){
            int counter = 0;
            while(counter < 10){
                this.entireSectionsArray.add(this.imageSectionsArray.get(sectionsAdded));
                counter += 1;
            }
        }
    }
    
    public void rearrangeSectionsArray(){
        int totalAdditions = this.totalSections*10;
        ArrayList<ImageSection> sectionsArrayCopy = this.imageSectionsArray;
        //Maybe sort the elements from lowest to highest pixelDistribution values??
        for(int sectionsAdded=0; sectionsAdded < sectionsArrayCopy.size(); sectionsAdded++){
            //Add sections in proportion to their pixelDistribution value. 
            //The higher the value the more elements of this section are added.
            this.entireSectionsArray.add(this.imageSectionsArray.get(sectionsAdded));
            int sectionPixelDis = this.imageSectionsArray.get(sectionsAdded).getPixelDistribution();
            totalAdditions-=1;
            while(sectionPixelDis-3>0){
                this.entireSectionsArray.add(this.imageSectionsArray.get(sectionsAdded));
                sectionPixelDis-=3;
                totalAdditions-=1;
            }
        }
        for(int additionsLeft = 0; additionsLeft < totalAdditions; additionsLeft++){
            int randomNum = ThreadLocalRandom.current().nextInt(0, this.imageSectionsArray.size());
            this.entireSectionsArray.add(this.imageSectionsArray.get(randomNum));
        }
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
    

    public ArrayList<PixelData> getPixelMetadataArray() {
        return pixelMetadataArray;
    }

    public void setPixelMetadataArray(ArrayList<PixelData> pixelMetadataArray) {
        this.pixelMetadataArray = pixelMetadataArray;
    }

    public int getTotalColoredPixels() {
        return totalColoredPixels;
    }

    public void setTotalColoredPixels(int totalColoredPixels) {
        this.totalColoredPixels = totalColoredPixels;
    }

    public int getTotalWhitePixels() {
        return totalWhitePixels;
    }

    public void setTotalWhitePixels(int totalWhitePixels) {
        this.totalWhitePixels = totalWhitePixels;
    }
}
