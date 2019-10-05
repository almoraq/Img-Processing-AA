package imageanalysis;
import java.io.File; 
import java.util.*;
import java.io.IOException; 
import java.awt.image.*; 
import javax.imageio.ImageIO; 

/**
 *
 * @author amora
 */
public class CaseImage {
    
    int width;
    int height;
    BufferedImage image;
    int arrayLength = 8;
    ArrayList<Integer>[] pixelValuesMatrix = new ArrayList[arrayLength];
    boolean areaIsWhite = false;
    int minX = 0;
    int minY = 0;
    int maxX = width;
    int maxY = height;

    
    public static void main(String args[])throws IOException 
    { 
        
    }//main() ends here 
    
    public void setupImage(String pFileName){
        //Read the image given by the filename
        try
        { 
            //image file path
            File inputFile = new File(pFileName);   
            //Reading input file 
            setImage(ImageIO.read(inputFile)); 
            setWidth(image.getWidth());
            setHeight(image.getHeight());
            System.out.println("Reading complete.");
        } 
        catch(IOException e) 
        { 
            System.out.println("Error: "+e); 
        }
    }
    
    public ArrayList obtainPixelValues(int pWidth, int pHeight, BufferedImage pImage){
        int arraySize = 4;
        BufferedImage currImageData = pImage;
        int pixelValue = currImageData.getRGB(pWidth, pHeight);//obtaining the RGB value, stored as a single number
        ArrayList<Integer> pixelValuesArray = new ArrayList<Integer>(arraySize);
        //RBG values of the pixel are "extracted" from the pixel value for easier handling
        int alphaValue = (pixelValue>>24) & 0xff;
        int redValue = (pixelValue>>16) & 0xff;
        int greenValue = (pixelValue>>8) & 0xff;
        int blueValue = pixelValue & 0xff;
        //The values are added to an array, to be returned by the function
        pixelValuesArray.add(0, alphaValue);
        pixelValuesArray.add(1, redValue);
        pixelValuesArray.add(2, greenValue);
        pixelValuesArray.add(3, blueValue);
        
        return pixelValuesArray;
    }
    
    public void testPixelsArea(int pXCoord, int pXCoordMax, int pYCoord, int pYCoordMax, CaseImage pCaseImage, int pTotalTests){
        for(int pixelsTested = 0; pixelsTested<pTotalTests; pixelsTested++){
            //Obtaining random coordinates for testing the pixel, limited by the max and min x,y of the section that is tested
            int pixelX = pXCoord + (int)(Math.random() * ((pXCoordMax - pXCoord) + 1));
            int pixelY = pYCoord + (int)(Math.random() * ((pYCoordMax - pYCoord) + 1));
            //Obtaining and saving the RGB values of the tested pixels. Values are stored inside the matrix
            ArrayList<Integer> pixelValues = pCaseImage.obtainPixelValues(pixelX, pixelY, pCaseImage.getImage());
            pCaseImage.registerPixelValues(pCaseImage.getPixelValuesMatrix(), pixelValues, pixelsTested);
            //Checking if the section is full of white pixels according to tests
            //If it is, the matrix is updated to remove that entry (and its RGB values)
            if(this.isAreaIsWhite()){
                this.updateMatrix(pCaseImage.getPixelValuesMatrix(), pixelsTested);
                this.setAreaIsWhite(false);
            }
            //If its not, the coordinates are added to their respective field in the matrix
            else{
                pixelValuesMatrix[pixelsTested].add(0, pXCoord);
                pixelValuesMatrix[pixelsTested].add(1, pYCoord);
                pixelValuesMatrix[pixelsTested].add(2, pXCoordMax);
                pixelValuesMatrix[pixelsTested].add(3, pYCoordMax);
            }
        }
    }
    
    public void obtainMaxMinCoord(ArrayList<Integer>[] pPixelValuesMatrix){
        for(int rows = 0; rows < pPixelValuesMatrix.length; rows++){
            //Fin the highest and lowest x,y coordinates to "crop" the image and get the final dimensions
            if(pPixelValuesMatrix[rows].get(0) < minX){//finding lowest X in the matrix
                minX = pPixelValuesMatrix[rows].get(0);
            }
            if(pPixelValuesMatrix[rows].get(1) > maxX){//finding highest X
                maxX = pPixelValuesMatrix[rows].get(1);
            }
            if(pPixelValuesMatrix[rows].get(2) > minY){//finding lowest Y
                minY = pPixelValuesMatrix[rows].get(2);
            }
            if(pPixelValuesMatrix[rows].get(3) > maxY){//finding highest Y
                maxY = pPixelValuesMatrix[rows].get(3);
            }
        }
    }
    
    public void updateMatrix(ArrayList<Integer>[] pPixelValuesMatrix, int pTestNumber){
        for(int rows = 0; rows<pPixelValuesMatrix.length; rows++){
            pPixelValuesMatrix[pTestNumber] = pPixelValuesMatrix[pTestNumber+1];
            //Delete leftover number function missing
        }
    }
    
    public void registerPixelValues(ArrayList<Integer>[] pPixelValuesMatrix, ArrayList<Integer> pPixelValues,int pTestedPixels){
        /*
        Starting x,y | Ending x,y | Total Red | Total Green | Total Blue | Pixels Tested
        0,0 | 127,127 | Sum of red in tested pixels | " green | " blue " | number of tests
        */
        int whitePixelsTested = 0; //counter to evaluate is a section is perceived to be blank
        //int avgAlphaValue = 0;
        //avgValues to obtain the average color of a section
        int avgRedValue = 0;
        int avgGreenValue = 0;
        int avgBlueValue = 0;
        int testsPerformed;
        
        for (testsPerformed = 0; testsPerformed < pTestedPixels; testsPerformed++){
            System.out.println("Pixel RBG:\nRed: "+pPixelValues.get(1)+"\nGreen: "+pPixelValues.get(2)+"\nBlue: "+pPixelValues.get(3));
            //Checking if pixel is white, comparing its Alpha, Red, Green and Blue values
            if(pPixelValues.get(0) == 255 && pPixelValues.get(1) == 255 && pPixelValues.get(2) == 255 && pPixelValues.get(3) == 255){
                System.out.println("This is a white pixel!");
                whitePixelsTested++;
            }
            else{
                //If pixel is not white, the RGB values are added to the average. That way, the white pixels don't affect the overall color data
                //avgAlphaValue += pPixelValues.get(0);
                avgRedValue += pPixelValues.get(1);
                avgGreenValue += pPixelValues.get(2);
                avgBlueValue = pPixelValues.get(3);
            }
        }
        //Checks to see if the whole section is full of white pixels (by random tests) and sets the boolean to true if so
        if(whitePixelsTested == testsPerformed++){
            System.out.println("Lol this whole area is whiter than Ann Coulter's fanbase");
            this.setAreaIsWhite(true);
        }
        //avgAlphaValue = avgAlphaValue/(pTestedPixels-whitePixelsTested);
        //Obtaining the average RGBs once the tests have ended for that matrix entry
        avgRedValue = avgRedValue/(pTestedPixels-whitePixelsTested);
        avgGreenValue = avgGreenValue/(pTestedPixels-whitePixelsTested);
        avgBlueValue = avgBlueValue/(pTestedPixels-whitePixelsTested);
        //Adding the RBG average values to the matrix
        pPixelValuesMatrix[testsPerformed].add(4, avgRedValue);
        pPixelValuesMatrix[testsPerformed].add(5, avgGreenValue);
        pPixelValuesMatrix[testsPerformed].add(6, avgBlueValue);
        pPixelValuesMatrix[testsPerformed].add(7, pTestedPixels);  
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public int getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    public ArrayList<Integer>[] getPixelValuesMatrix() {
        return pixelValuesMatrix;
    }

    public void setPixelValuesMatrix(ArrayList<Integer>[] pixelValuesMatrix) {
        this.pixelValuesMatrix = pixelValuesMatrix;
    }
    public boolean isAreaIsWhite() {
        return areaIsWhite;
    }

    public void setAreaIsWhite(boolean areaIsWhite) {
        this.areaIsWhite = areaIsWhite;
    }

}//class ends here

