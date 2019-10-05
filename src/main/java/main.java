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
        CaseImage currImage = new CaseImage();
        currImage.setupImage(inputFileName);
        int targetPixelsTested = 6;
        int pWidth = currImage.getWidth()-1;
        int pHeight = currImage.getHeight()-1;
        int imageSectionAmount = 1024/8;
        int pWidthDif = pWidth-imageSectionAmount;
        int pHeightDif = pWidth-imageSectionAmount;
        
        currImage.testPixelsArea(pWidth-pWidthDif, pWidth, pHeight-pHeightDif, pHeight, currImage, targetPixelsTested);
        //return pImage.getPixelValuesMatrix();
    }
}
