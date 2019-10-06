/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;
import imageanalysis.CaseImage;
import java.util.ArrayList;

/**
 *
 * @author amora
 */
public class Population {
    
    ArrayList<PolygonDNA> matingPool;
    ArrayList<PolygonDNA> polygonDNAArray;
    int polygonArraySize = 100;
    int population = 20;
    CaseImage image = new CaseImage();
    ArrayList<Double>[] metadataMatrix = image.getImageMetadataMatrix();
    
    private void generatePolygonArray(){
        for(int polygonsAdded = 0; polygonsAdded < this.getPolygonArraySize(); polygonsAdded++){
            int redValue = (int) Math.round(this.metadataMatrix[polygonsAdded].get(0));
            int greenValue = (int) Math.round(this.metadataMatrix[polygonsAdded].get(1));
            int blueValue = (int) Math.round(this.metadataMatrix[polygonsAdded].get(2));
            PolygonDNA polygon = new PolygonDNA(redValue, greenValue, blueValue);
            for(int instanceOfColor = 0; instanceOfColor < (this.metadataMatrix[3].get(polygonsAdded)*100); instanceOfColor++){
                //Adds as many instances of that polygon as its occurence value * 100 (like it's adding the percentage)
                this.polygonDNAArray.add(polygon);//adds instance of polygon to the end of the list
            }
        }
    }
    
    private void generateMatingPool(){
        for(int polygonsCreated = 0; polygonsCreated < this.getPopulation(); polygonsCreated++){
            //Add random instance of polygon, within the elements of the array to create the starter population array
            int polygonArrayIndex = (int) Math.random() * ((100) + 1);
            PolygonDNA currPolygon = this.polygonDNAArray.get(polygonArrayIndex);
            this.matingPool.add(currPolygon);
        }
    }
    
    private void calculateFitness(){

    }
    
    private void naturalSelection(){
        
    }
    
    private void generatePopulation(){
        
    }
    
    private void evaluatePopulation(){
        
    }
    
    public int getPolygonArraySize() {
        return polygonArraySize;
    }

    public void setPolygonArraySize(int polygonArraySize) {
        this.polygonArraySize = polygonArraySize;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}


