/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageanalysis;

/**
 *
 * @author amora
 */
public class PixelData {
    int ARGBValue;
    int alphaValue;
    int redValue;
    int greenValue;
    int blueValue;
    int occurrence = 1;
    boolean isWhite = false;

    public PixelData(int pARGBValue){
        this.ARGBValue = pARGBValue;
        //checkIfWhite();
    }
    
    public void roundARGBValues(){
        this.alphaValue = roundValue((this.ARGBValue>>24) & 0xff, 5);
        this.redValue = roundValue((this.ARGBValue>>16) & 0xff, 5);
        //System.out.println("OG value: "+(this.ARGBValue>>8 & 0xff));
        this.greenValue = roundValue((this.ARGBValue>>8) & 0xff, 5);
        this.blueValue = roundValue((this.ARGBValue) & 0xff, 5);
        //System.out.println("New value: "+this.greenValue);
    }
    
    public boolean checkIfWhite(){
        if(this.alphaValue == 255 && this.redValue == 255 && this.greenValue == 255 && this.blueValue == 255){
            this.isWhite = true;
        }
        return this.isWhite;
    }
    public int roundValue(int pValue, int pRoundValue){
        //return Math.round(pValue/pRoundValue)*pRoundValue;
        return Math.round(pValue/pRoundValue)*pRoundValue;
    }
    
    public int getAlphaValue() {
        return alphaValue;
    }

    public void setAlphaValue(int alphaValue) {
        this.alphaValue = alphaValue;
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

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public boolean isIsWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }
    
}
