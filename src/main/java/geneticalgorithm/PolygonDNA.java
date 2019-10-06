/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

/**
 *
 * @author amora
 */
public class PolygonDNA {
    int redValue;
    int greenValue;
    int blueValue;
    byte colorValueBytes[];
    
    public PolygonDNA(int pRedValue, int pGreenValue, int pBlueValue){
        this.redValue = pRedValue;
        this.greenValue = pGreenValue;
        this.blueValue = pBlueValue;
        colorValueBytes = new byte[3];
        byte redValueByte = (byte)(this.redValue-128);
        byte greenValueByte = (byte)(this.greenValue-128);
        byte blueValueByte = (byte)(this.blueValue-128);
        colorValueBytes[0] = redValueByte;
        colorValueBytes[1] = greenValueByte;
        colorValueBytes[2] = blueValueByte;
        System.out.println("Red byte: "+redValueByte+"\tGreen byte: "+greenValueByte+"\tBlue byte: "+blueValueByte);
        
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }
    
}
