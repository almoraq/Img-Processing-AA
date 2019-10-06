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
    double popGrowthFact = 1;
    int generations = 0;
    double populationFitness = 0;
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
            this.generations += 1;
        }
    }
    
    private void mutatePopulation(){
        
    }
    
    
    
    private void naturalSelection(){
        
    }
    
    private void generatePopulation(){
        //Select two parents from the mating pool, to operate on their genes (color byte numbers)
        //Parent selection uses that specific polygon's fitness to determine an adecuate partner por parent1, as it
        //should find a partner whose fitness is higher or equal than its own fitness number
        //This is achieved by calling the checkPartnerFitness function
        //That way, the population generated will drift more towards a higher overall fitness 
        popGrowthFact = 1.5;
        int targetPopulation = (int) Math.round(population * popGrowthFact);
        //Increases population by popGrowthFact
        for(int polygonsCreated = 0; polygonsCreated < targetPopulation; polygonsCreated++){
            PolygonDNA parent1 = this.matingPool.get((int) Math.random() * (100 + 1));
            PolygonDNA parent2 = this.matingPool.get((int) Math.random() * (100 + 1));
            while(!checkPartnerFitness(parent1, parent2)){
                //Checks if partner2 fitness is higher or equal to partner1's
                //If it isn't, a new partner will be selected
                parent2 = this.matingPool.get((int) Math.random() * (100 + 1));
            }
            //Create child of both polygons
            PolygonDNA child = new PolygonDNA();
            //Select up to what integer the parent's genes will be compared by an xor, and the rest by an and
            int midpoint = (int) Math.round(Math.random() + 1); 
            for(int genesMixed = 0; genesMixed < midpoint; genesMixed++){
                //Mix genes by using an -xor- on the color number bytes
                //set child's genes
                child.colorValueBytes[genesMixed] = parent1.colorValueBytes[genesMixed] ^ parent2.colorValueBytes[genesMixed];
            }
            for(int genesMixed = midpoint; genesMixed < 3-midpoint; genesMixed++){
                //Mix genes by using an -and- on the color number bytes
                //set child's genes
                child.colorValueBytes[genesMixed] = parent1.colorValueBytes[genesMixed] & parent2.colorValueBytes[genesMixed];
            }
            this.population = polygonsCreated;
        }
        this.generations++;
        
        
    }
    
    private void evaluatePopulation(){
        
    }
    
    private boolean checkPartnerFitness(PolygonDNA pPartner1, PolygonDNA pPartner2){
        return pPartner2.calculateFitness() >= pPartner1.calculateFitness();
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


