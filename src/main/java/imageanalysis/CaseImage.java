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
            
    public static void main(String args[])throws IOException 
    { 
        String inputFileName = "Images/vacationMario.jpg";
        CaseImage currImage = new CaseImage();
        currImage.setupImage(inputFileName);
        int targetPixelsTested = 6;
        int pWidth = currImage.getWidth()-1;
        int pHeight = currImage.getHeight()-1;
        for(int pixelsTested = 0; pixelsTested<targetPixelsTested; pixelsTested++){
            ArrayList<Integer> pixelValues = currImage.obtainPixelValues(pWidth, pHeight, currImage.getImage());
            int alphaValue = pixelValues.get(0);
            int redValue = pixelValues.get(1);
            int greenValue = pixelValues.get(2);
            int blueValue = pixelValues.get(3);
            System.out.println("Pixel RBG for x="+(pWidth)+" and y="+(pHeight)+":\nAlpha: "+alphaValue+
                                        "\nRed: "+redValue+"\nGreen: "+greenValue+"\nBlue: "+blueValue);
            if(alphaValue == 255 && redValue == 255 && greenValue == 255 && blueValue == 255){
                System.out.println("This is a white pixel!");
            }
            pWidth = pWidth/2;
            pHeight = pHeight/2;
        }
    }//main() ends here 
    
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
}//class ends here

