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
        testedImage.performTests();
        //testedImage.setImageMetadataMatrix(imageMetadataMatrix);
        testedImage.printMatrix(testedImage.getPixelMetadataArray());
    }
}
