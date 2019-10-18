package imageanalysis;
import java.io.File; 
import java.util.*;
import java.io.IOException; 
import java.awt.image.*; 
import java.lang.reflect.Array;
import javax.imageio.ImageIO; 
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author amora
 */
public class CaseImage {
    
    int width;
    int height;
    BufferedImage image;
    int arrayLength = 8;
    ArrayList<Integer[]> pixelValuesMatrix = new ArrayList<Integer[]>();
    ArrayList<Double[]> imageMetadataMatrix = new ArrayList<Double[]>();
    boolean areaIsWhite = false;
    int minX = 0;
    int minY = 0;
    int maxX = width;
    int maxY = height;
    String inputFileName = "Images/squirtle.jpg";


    public CaseImage(){
        
    }
    public static void main(String args[])throws IOException 
    { 
        
        CaseImage image = new CaseImage();
        image.setupImage(image.getInputFileName());
        int targetPixelsTested = 200;
        int pWidth = image.getWidth()-1;
        int pHeight = image.getHeight()-1;
        int imageSectionAmount = 1024/8;
        int pWidthDif = pWidth-imageSectionAmount;
        int pHeightDif = pWidth-imageSectionAmount;
        int xCoord = 200;
        int maxXCoord = xCoord+150;
        int yCoord = 300;
        int maxYCoord = yCoord+150;
        image.testPixelsArea(xCoord, maxXCoord, yCoord, maxYCoord, image, targetPixelsTested);
        ArrayList<Integer[]> pixelMatrix = image.getPixelValuesMatrix();
        System.out.println("Pixel values:\nRed|Green|Blue| X1 | X2 | Y1 | Y2 ");
        for(int rows = 0; rows<pixelMatrix.size(); rows++){
                for(int columns = 0; columns < pixelMatrix.get(rows).length; columns++){
                    System.out.print(pixelMatrix.get(rows)[columns] + "\t|");
                }
                System.out.println();
            }
        image.obtainImageMatadata();
        ArrayList<Double[]> metadata = image.getImageMetadataMatrix();
        
        System.out.println("Red|Green|Blue|Occurrence");
        for(int rows = 0; rows<metadata.size(); rows++){
                for(int columns = 0; columns < 4; columns++){
                    System.out.print(metadata.get(rows)[columns] + "\t|");
                }
                System.out.println();
            }    
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
    
    public Integer[] obtainPixelValues(int pX, int pY, BufferedImage pImage){
        int arraySize = 7;
        BufferedImage currImageData = pImage;
        int pixelValue = currImageData.getRGB(pX, pY);//obtaining the RGB value, stored as a single number
        Integer[] pixelValuesArray = new Integer[arraySize];
        //RBG values of the pixel are "extracted" from the pixel value for easier handling
        //int alphaValue = (pixelValue>>24) & 0xff;
        int redValue = (pixelValue>>16) & 0xff;
        int greenValue = (pixelValue>>8) & 0xff;
        int blueValue = (pixelValue) & 0xff;
        
        //The values are added to an array, to be returned by the function
        //pixelValuesArray.add(0, alphaValue);
        pixelValuesArray[0] = redValue;
        pixelValuesArray[1] = greenValue;
        pixelValuesArray[2] = blueValue;
        
        return pixelValuesArray;
    }
    
    public void testPixelsArea(int pXCoord, int pXCoordMax, int pYCoord, int pYCoordMax, CaseImage pCaseImage, int pTotalTests){
        ArrayList<Integer> anchor = new ArrayList<Integer>();
        anchor.add((pXCoordMax-pXCoord)/2);//X coordinate of the anchor
        anchor.add((pYCoordMax-pYCoord)/2);//Y coordinate of the anchor
        int whitePixels = 0;
        for(int pixelsTested = 0; pixelsTested<pTotalTests; pixelsTested++){
            int XCoordDif = pXCoordMax-pXCoord;
            int YCoordDif = pYCoordMax-pYCoord;
            
            /*this.anchor.set(0, (pXCoordMax-pXCoord)/2); 
            this.anchor.set(1, (pYCoordMax-pYCoord)/2);*/
            //this.anchor[1] = (pYCoordMax-pYCoord)/2; 
            //Obtaining random coordinates for testing the pixel, limited by the max and min x,y of the section that is tested
            //Anchor values are used to steer the random elements into the colored pixels of the section
            int randomNum = ThreadLocalRandom.current().nextInt(anchor.get(0)-XCoordDif, anchor.get(0)+XCoordDif + 1);
            int pixelX = Math.abs(randomNum);
            randomNum = ThreadLocalRandom.current().nextInt(anchor.get(1)-YCoordDif, anchor.get(1)+YCoordDif + 1);
            int pixelY = Math.abs(randomNum);
            
            //Obtaining and saving the RGB values of the tested pixels. Values are stored inside the matrix
            
            Integer[] pixelValues = pCaseImage.obtainPixelValues(pixelX, pixelY, pCaseImage.getImage());
            if(!pixelIsWhite(pixelValues)){
                System.out.println("Pixel is not white!");
                pCaseImage.registerPixelValues(pixelValues, pixelsTested);
                System.out.println("Anchor: "+anchor.get(0)+","+anchor.get(1)+"\nX: "+pixelX+"\tY: "+pixelY);
                //this.anchor[0] = 100;
                anchor.set(0, pixelX);
                anchor.set(1, pixelY);
                //this.pixelValuesMatrix.add(new Integer[8]);
                pixelValues[3] = pXCoord;
                pixelValues[4] = pXCoordMax;
                pixelValues[5] = pYCoord;
                pixelValues[6] = pYCoordMax;
                this.pixelValuesMatrix.add(pixelValues);
            }
            else{
                System.out.println("Found a white pixel!");
                whitePixels++;
                /*int randomValue = ThreadLocalRandom.current().nextInt(-20, 20 + 1);
                anchor.set(0, anchor.get(0)+randomValue);*/
            }
            
            //Checking if the section is full of white pixels according to tests
            //If it is, the matrix is updated to remove that entry (and its RGB values)
            if(this.isAreaIsWhite()){
                int currMaxIndex = this.pixelValuesMatrix.size();
                //this.pixelValuesMatrix.add(new Integer[8]);
                //System.out.println("Area is white. Curr index: "+currMaxIndex);
                //this.pixelValuesMatrix.remove(currMaxIndex);
                //this.updateMatrix(pixelsTested-whiteAreas);
                
                this.setAreaIsWhite(false);
            }
            //If its not, the coordinates are added to their respective field in the matrix
            else{
                
                /*this.pixelValuesMatrix.add(new Integer[8]);
                this.pixelValuesMatrix.get(currIndex)[3] = pXCoord;
                this.pixelValuesMatrix.get(currIndex)[4] = pYCoord;
                this.pixelValuesMatrix.get(currIndex)[5] = pXCoordMax;
                this.pixelValuesMatrix.get(currIndex)[6] = pYCoordMax;*/
                /*pixelValuesMatrix[pixelsTested].add(0, pXCoord);
                pixelValuesMatrix[pixelsTested].add(1, pYCoord);
                pixelValuesMatrix[pixelsTested].add(2, pXCoordMax);
                pixelValuesMatrix[pixelsTested].add(3, pYCoordMax);
                */
            }
        }
        System.out.println("Test on pixels has finished.\nColored pixels: "+ (pTotalTests-whitePixels)+"\nWhite pixels: "+whitePixels);
    }
    
    public boolean pixelIsWhite(Integer[] pPixelValues){
        return pPixelValues[0]==255 && pPixelValues[1]==255 && pPixelValues[2]==255;
    }
    
    public void registerPixelValues(Integer[] pPixelValues, int pTestedPixels){
        /*
        Starting x,y | Ending x,y | Total Red | Total Green | Total Blue | Pixels Tested
        0,0 | 127,127 | Sum of red in tested pixels | " green | " blue " | number of tests
        */
        int whitePixelsTested = 0; //counter to evaluate is a section is perceived to be blank
        //int avgAlphaValue = 0;
        //avgValues to obtain the average color of a section
        int avgRedValue = 1;
        int avgGreenValue = 1;
        int avgBlueValue = 1;
        int coloredPixels = 0;
        int testsPerformed;
        
        for (testsPerformed = 0; testsPerformed < pTestedPixels; testsPerformed++){
            //System.out.println("Pixel RBG:\nRed: "+pPixelValues.get(1)+"\nGreen: "+pPixelValues.get(2)+"\nBlue: "+pPixelValues.get(3));
            //Checking if pixel is white, comparing its Alpha, Red, Green and Blue values
            if(pPixelValues[0] == 255 && pPixelValues[1] == 255 && pPixelValues[2] == 255){
                System.out.println("This is a white pixel!");
                whitePixelsTested++;
            }
            else{
                //If pixel is not white, the RGB values are added to the average. That way, the white pixels don't affect the overall color data
                //avgAlphaValue += pPixelValues.get(0);
                coloredPixels++;
                avgRedValue += pPixelValues[0];
                avgGreenValue += pPixelValues[1];
                avgBlueValue = pPixelValues[2];
            }
        }
        
        //this.printMatrix(this.pixelValuesMatrix);
        //Checks to see if the whole section is full of white pixels (by random tests) and sets the boolean to true if so
        if(whitePixelsTested == testsPerformed){
            //System.out.println("Lol this whole area is whiter than Ann Coulter's fanbase");
            this.setAreaIsWhite(true);
        }
        else{
            this.setAreaIsWhite(false);
            //avgAlphaValue = avgAlphaValue/(pTestedPixels-whitePixelsTested);
            //Obtaining the average RGBs once the tests have ended for that matrix entry
            avgRedValue = Math.round(avgRedValue/coloredPixels);
            avgGreenValue = Math.round(avgGreenValue/coloredPixels);
            avgBlueValue = Math.round(avgBlueValue/coloredPixels);
            //Adding the RBG average values to the matrix
            int currIndex = this.pixelValuesMatrix.size();
            pPixelValues[0] = avgRedValue;
            pPixelValues[1] = avgGreenValue;
            pPixelValues[2] = avgBlueValue;
            pPixelValues[3] = pTestedPixels;
            //this.pixelValuesMatrix.add(pPixelValues);
            /*this.pixelValuesMatrix.get(currIndex).add(avgRedValue);
            this.pixelValuesMatrix.get(currIndex).add(avgGreenValue);
            this.pixelValuesMatrix.get(currIndex).add(avgBlueValue);
            this.pixelValuesMatrix.get(currIndex).add(pTestedPixels);
            //pPixelValuesMatrix[pTestedPixels].set(4, avgRedValue);
            */
        }
        
    }
    
    public void obtainImageMatadata(){
        //Fills matrix with pixel color values from different sections, modifies them in order of priority (for easier evaluation by the user
        //and modifies the metadata matrix with those values and their correspondent percentage of occurrence in the image
        //No coordinates are saved in this matrix, only color values and their occurrence
        /*
        Matrix:
        Red | Green | Blue | Occurrence
        200 |  152  |  55  |   0.32 
        */
        //1. Read the pixel values matrix and add each new value into the image metadata matrix.
        //  If color is repeated, add 1 to the occurrence counter for the matrix, which will later transform into a percentage after the
        //  pixelValuesMatrix is finished being analyzed.
        //2. "Normalize" the values, by grouping similar colors together (color variance of +-5 in any of its RGB values
        //3. Rearrange the metadata matrix by the highest to lowest color occurrence
        //  This will also be useful later, to create the genetic pool with those values
        int matrixRows = 0;
        for(int rows = 0; rows < this.pixelValuesMatrix.size(); rows++){
            int redValue = this.pixelValuesMatrix.get(rows)[0];
            //int redValue = this.pixelValuesMatrix[rows].get(0);
            int greenValue = this.pixelValuesMatrix.get(rows)[1];
            //int greenValue = this.pixelValuesMatrix[rows].get(1);
            int blueValue = this.pixelValuesMatrix.get(rows)[2];
            //int blueValue = this.pixelValuesMatrix[rows].get(2);
            int foundMatrixIndex = compareMatrixRGB(redValue, greenValue, blueValue);
            //if entry's values are not already in the matrix, add to the structure in the corresponding index (matrixRows) and add 1 to the occurrence
            //check if element is in metadata already
            if(foundMatrixIndex != 0){
                this.imageMetadataMatrix.get(foundMatrixIndex)[3]+=1;//Adding 1 to the occurrence
                System.out.println("Color is in matrix. Modifying occurrence value in: "+(foundMatrixIndex));
                //colorOccurrence+=1;
                //this.imageMetadataMatrix.get(foundMatrixIndex)[3] = colorOccurrence;
                //this.imageMetadataMatrix.get(foundMatrixIndex).set(3, colorOccurrence); 
                /*this.imageMetadataMatrix.get(foundMatrixIndex).add(((double) redValue)); //Setting the red value
                this.imageMetadataMatrix.get(foundMatrixIndex).add(((double) greenValue)); //Setting the green value
                this.imageMetadataMatrix.get(foundMatrixIndex).add(((double) blueValue)); //Setting the blue value
                */
            }
            //else, add element to the metadata matrix
            else{
               //this.imageMetadataMatrix[matrixRows].set(3, this.imageMetadataMatrix[matrixRows].get(3)+1); 
               System.out.println("Color is not in matrix. Adding element to row: "+matrixRows);
               this.imageMetadataMatrix.add(new Double[4]);
               System.out.println("Red value in this instance: "+redValue+"\nGreen value in this instance: "+greenValue+"\nBlue value in this instance: "+blueValue);
               this.imageMetadataMatrix.get(matrixRows)[0] =  (double)redValue;
               this.imageMetadataMatrix.get(matrixRows)[1] = (double)greenValue;
               this.imageMetadataMatrix.get(matrixRows)[2] = (double)blueValue;
               this.imageMetadataMatrix.get(matrixRows)[3] = 1.00;
               //this.pixelValuesMatrix.get(currIndex).add(avgRedValue);
               //this.imageMetadataMatrix.add(matrixRows, element);
               
               matrixRows++;//Adding 1 to the occurrence
            }
            //call to function to update occurrence values
            //updateOcurrenceValues(matrixRows);
            
            //call to function to rearrange matrix in decreasing occurrence order
        }
    }
    
    public int compareMatrixRGB(int pRed, int pGreen, int pBlue){
        //rearranges matrix elements in decreasing order by occurence value
        for(int rows = 0; rows < this.imageMetadataMatrix.size(); rows++){
            if(this.imageMetadataMatrix.get(rows)[0] <= pRed+8 && this.imageMetadataMatrix.get(rows)[0] >= pRed-8){
                if(this.imageMetadataMatrix.get(rows)[0] <= pGreen+8 && this.imageMetadataMatrix.get(rows)[0]>= pGreen-8){
                    if(this.imageMetadataMatrix.get(rows)[0] <= pBlue+8 && this.imageMetadataMatrix.get(rows)[0]>= pBlue-8){
                        return rows;
                    }
                }
            }
        }
        return 0;
    }
    
    public void updateOcurrenceValues(int pMatrixRows){
        for(int rows = 0; rows < pMatrixRows; rows++){
            
            //this.imageMetadataMatrix.get(rows)[3] = this.imageMetadataMatrix.get(rows)[3]/pMatrixRows);
        }
    }
    
    public void printMatrix(ArrayList<ArrayList<Integer>> matrix){
        for(int rows = 0; rows<matrix.get(rows).size(); rows++){
            for(int columns = 0; columns<8;columns++){
                System.out.print(matrix.get(rows).get(columns)+"\t");
            }
        }
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

    public boolean isAreaIsWhite() {
        return areaIsWhite;
    }

    public void setAreaIsWhite(boolean areaIsWhite) {
        this.areaIsWhite = areaIsWhite;
    }
    
    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
    
    public ArrayList<Double[]> getImageMetadataMatrix() {
        return imageMetadataMatrix;
    }

    public void setImageMetadataMatrix(ArrayList<Double[]> imageMetadataMatrix) {
        this.imageMetadataMatrix = imageMetadataMatrix;
    }
    
    public ArrayList<Integer[]> getPixelValuesMatrix() {
        return pixelValuesMatrix;
    }

    public void setPixelValuesMatrix(ArrayList<Integer[]> pixelValuesMatrix) {
        this.pixelValuesMatrix = pixelValuesMatrix;
    }
    

}//class ends here