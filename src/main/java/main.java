/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import imageanalysis.CaseImage;
import java.util.ArrayList;
/**
 *
 * @author amora
 */
public class main {
    public static void testEntireImage(CaseImage pImage, int pTotalTests){
        String inputFileName = "Images/squirtle.jpg";
        CaseImage image = new CaseImage();
        image.setupImage(inputFileName);
        int targetPixelsTested = 6;
        int pWidth = image.getWidth()-1;
        int pHeight = image.getHeight()-1;
        int imageSectionAmount = 1024/8;
        int pWidthDif = pWidth-imageSectionAmount;
        int pHeightDif = pWidth-imageSectionAmount;
        image.testPixelsArea(pWidth-pWidthDif, pWidth, pHeight-pHeightDif, pHeight, image, targetPixelsTested);
        
        image.obtainImageMatadata();
        ArrayList<Double>[] metadata = image.getImageMetadataMatrix();
        
        
        for(int row = 0; row<metadata.length; row++){
                for(int column = 0; column < 4; column++){
                    System.out.print(metadata[row].get(column) + "\t//");
                }
                System.out.println();
            }    
            
        //return pImage.getPixelValuesMatrix();
    }
}
