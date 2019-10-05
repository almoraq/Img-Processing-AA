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

    
    
    public static void main(String args[])throws IOException 
    { 
        
        
    }//main() ends here 
    
    
    
    public void setupImage(String pFileName){
        // READ IMAGE 
        try
        { 
            File inputFile = new File(pFileName); //image file path  
             // Reading input file 
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
        int pixelValue = currImageData.getRGB(pWidth, pHeight);
        ArrayList<Integer> pixelValuesArray = new ArrayList<Integer>(arraySize);
        int alphaValue = (pixelValue>>24) & 0xff;
        int redValue = (pixelValue>>16) & 0xff;
        int greenValue = (pixelValue>>8) & 0xff;
        int blueValue = pixelValue & 0xff;
        pixelValuesArray.add(0, alphaValue);
        pixelValuesArray.add(1, redValue);
        pixelValuesArray.add(2, greenValue);
        pixelValuesArray.add(3, blueValue);
        return pixelValuesArray;
    }
    
    public void testPixelsArea(int pXCoord, int pXCoordMax, int pYCoord, int pYCoordMax, CaseImage pCaseImage, int pTotalTests){
        for(int pixelsTested = 0; pixelsTested<pTotalTests; pixelsTested++){
            int pixelX = pXCoord + (int)(Math.random() * ((pXCoordMax - pXCoord) + 1));
            int pixelY = pYCoord + (int)(Math.random() * ((pYCoordMax - pYCoord) + 1));
            
            ArrayList<Integer> pixelValues = pCaseImage.obtainPixelValues(pixelX, pixelY, pCaseImage.getImage());
            pCaseImage.registerPixelValues(pixelValuesMatrix, pixelValues, pixelsTested);
            
            pixelValuesMatrix[pixelsTested].add(0, pXCoord);
            pixelValuesMatrix[pixelsTested].add(1, pYCoord);
            pixelValuesMatrix[pixelsTested].add(2, pXCoordMax);
            pixelValuesMatrix[pixelsTested].add(3, pYCoordMax);
        }
    }
    
    public void registerPixelValues(ArrayList<Integer>[] pPixelValuesMatrix, ArrayList<Integer> pPixelValues,int pTestedPixels){
        /*
        Starting x,y | Ending x,y | Total Red | Total Green | Total Blue | Pixels Tested
        0,0 | 127,127 | Sum of red in tested pixels | " green | " blue " | number of tests
        */
        int whitePixelsTested = 0;
        //int avgAlphaValue = 0;
        int avgRedValue = 0;
        int avgGreenValue = 0;
        int avgBlueValue = 0;
        int testsPerformed;
        
        for (testsPerformed = 0; testsPerformed < pTestedPixels; testsPerformed++){
            System.out.println("Pixel RBG:\nRed: "+pPixelValues.get(1)+"\nGreen: "+pPixelValues.get(2)+"\nBlue: "+pPixelValues.get(3));
            if(pPixelValues.get(0) == 255 && pPixelValues.get(1) == 255 && pPixelValues.get(2) == 255 && pPixelValues.get(3) == 255){
                System.out.println("This is a white pixel!");
                whitePixelsTested++;
            }
            else{
                //avgAlphaValue += pPixelValues.get(0);
                avgRedValue += pPixelValues.get(1);
                avgGreenValue += pPixelValues.get(2);
                avgBlueValue = pPixelValues.get(3);
            }
        }
        //avgAlphaValue = avgAlphaValue/(pTestedPixels-whitePixelsTested);
        avgRedValue = avgRedValue/(pTestedPixels-whitePixelsTested);
        avgGreenValue = avgGreenValue/(pTestedPixels-whitePixelsTested);
        avgBlueValue = avgBlueValue/(pTestedPixels-whitePixelsTested);
        
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
}//class ends here

