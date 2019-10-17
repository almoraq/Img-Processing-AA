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
    int targetTests = 15;
    int totalCoordDivisions;
    ArrayList<Integer[]> imageMetadataMatrix = new ArrayList<>();
    int totalSections;
    int sectionXDistance;
    int sectionYDistance;
    Integer[] highestLeftestCoord = {0,0};
    Integer[] lowestRightestCoord = {0,0};
    
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
                    System.out.println("Testing in coordinates: x1,y1: "+minX+","+minY+" x2,y2: "+maxX+","+maxY);
                    ImageSection currSection = new ImageSection(minX, minY, maxX, maxY, this.targetTests, this.image, this.imageCopy);
                    
                    currSection.testPixelsArea();
                    this.addImageMetadata(currSection.getPixelValuesMatrix());
                    minX += this.sectionXDistance;
                    maxX += this.sectionXDistance;
                }
            }
            minY += this.sectionYDistance;
            maxY += this.sectionYDistance;
            minX = 0;
            maxX = minX + this.sectionXDistance;
        }
        this.updateMetadataMatrix();
    }
    
    public void addImageMetadata(ArrayList<Integer[]> pPixelValuesMatrix){
        int pixelMatrixSize = pPixelValuesMatrix.size();
        for(int matrixRows = 0; matrixRows < pixelMatrixSize; matrixRows++){
            this.imageMetadataMatrix.add(pPixelValuesMatrix.get(matrixRows));
        }
    }
    
    public void updateMetadataMatrix(){ 
        ArrayList<Integer[]> newMetadataMatrix = new ArrayList<>();
        for(int rows = 0; rows < this.imageMetadataMatrix.size(); rows++){
            Integer[] element = this.imageMetadataMatrix.get(rows);
            if(!(newMetadataMatrix.contains(element))){
                newMetadataMatrix.add(element);
                //sets occurrence to 1
                newMetadataMatrix.get(rows)[4] = 1;
            }
            else{
                //adds 1 to occurrence
                newMetadataMatrix.get(rows)[4] += 1;
            }
        }
        this.imageMetadataMatrix = newMetadataMatrix;
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
    
    public void printMatrix(ArrayList<Integer[]> pMatrix){
        System.out.println("Pixel values:\nAlpha|Red|Green|Blue|Occurence| ");
        for(int rows = 0; rows < pMatrix.size(); rows++){
            for(int columns = 0; columns < pMatrix.get(rows).length; columns++){
                System.out.println(pMatrix.get(rows)[columns] + "\t|");
                System.out.println("fuck");
            }
            System.out.println();
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
}
