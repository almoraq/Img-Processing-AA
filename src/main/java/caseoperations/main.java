/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caseoperations;

import imageanalysis.CaseImage;
import java.util.ArrayList;

/**
 *
 * @author amora
 */
public class main {
    public static void main(String[] args) throws Exception{
        String inputFile = "Images/guacamaya.jpeg";
        int coordDivisions = 4;
        CaseImage testedImage = new CaseImage(inputFile, coordDivisions);
        testedImage.performInitialTests();
        for(int i=0; i<300; i++){
            testedImage.performAditionalTests();
        }
        
        //testedImage.setImageMetadataMatrix(imageMetadataMatrix);
        System.out.println("Colored pixels tested in image: "+testedImage.getTotalColoredPixels()+"\tWhite pixels tested: "+testedImage.getTotalWhitePixels());
        testedImage.printMatrix(testedImage.getPixelMetadataArray());
    }
}
