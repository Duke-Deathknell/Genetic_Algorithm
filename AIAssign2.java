package ai.assign2;

/**Michael Alsbergas, 5104112
 * 
 * This program uses a genetic algorithm to solve a self-avoiding walk.
 * To run, just type in the values for each parameter as requested.
 * Remember to subtract 1 from each of the start co-ordinates!
 */

import java.util.Scanner;
import java.util.Random;

//Here's where the global variables are declared.
public class AIAssign2 {
    int popsize;
    int tournysize;
    double crossprob; 
    double mutaprob;
    int maxgen;
    int height;
    int width; 
    int startx;
    int starty;
    long seed;
    
    double bestavg;
    double avgavg;
    
    char[][] problem; 
    int [][] population;
    int [][] population2;
    int [] popfitness;
    
    //This is the main class that runs the whole thing
    public AIAssign2(){
        
        Scanner console = new Scanner(System.in);
        
        System.out.print("Input population size ");
        popsize = console.nextInt();
        
        System.out.print("Input tournament size ");
        tournysize = console.nextInt();
        
        System.out.print("Input crossover chance (%) ");
        crossprob = console.nextDouble();
        
        System.out.print("Input mutation chance (%) ");
        mutaprob = console.nextDouble();
        
        System.out.print("Input max generation size ");
        maxgen = console.nextInt();
        
        System.out.print("Input problem width ");
        width = console.nextInt();
        
        System.out.print("Input problem height ");
        height = console.nextInt();
        
        System.out.print("Input start location x-1 ");
        startx = console.nextInt();
        
        System.out.print("Input start location y-1 ");
        starty = console.nextInt();
        
        System.out.print("Input seed value ");
        seed = console.nextLong();
        
        Random rnd = new Random(seed);
        
        population = new int[popsize][height*width];
        population2 = new int[popsize][height*width];
        popfitness = new int[popsize];
        
        for (int i = 0 ; i<popsize ; i++){
            for (int j = 0 ; j<population[0].length ; j++){
                population[i][j] = (rnd.nextInt(4));
            }
        }
        Print(population);
        
        //the generation loop
        for (int gen = 0 ; gen < maxgen ; gen++){
            
            //fitness of individuals is stored
            int btemp = 0;
            for (int i = 0 ; i < popsize ; i++){
                
                problem = new char[width][height];
                problem[startx][starty] = 'S';
        
                popfitness[i] = Fitness(population[i], problem, startx, starty, 0);
                
                if (popfitness[i] > btemp){ btemp = popfitness[i];}
            }
            bestavg = bestavg + btemp;
            
            String line="";
            int avg=0;
            for (int i = 0 ; i < popsize ; i++){
                 line = line + "[" + popfitness[i] + "] ";  
                 avg = avg + popfitness[i];
            }
            avg = avg/popsize;
            avgavg = avgavg + avg;
            bestavg = bestavg / (gen+1);
            avgavg = avgavg / (gen+1);
            System.out.println("Gen: "+gen + " Fitness: "+ line + " Avg: "+ avg + " BestAvg: " + bestavg + " AvgAvg: " + avgavg);
            bestavg = bestavg * (gen+1);
            avgavg = avgavg * (gen+1);
            
            //Tournament selection
            for (int i = 0 ; i < popsize ; i++){
                int best = 0; 
                int rand = (int)(rnd.nextInt(popsize));
                best = rand;
                
                for (int k = 0 ; k < tournysize ; k++){
                    if (popfitness[rand] > popfitness[best]){
                        best = rand;
                    }
                    
                    rand = (int)(rnd.nextInt(popsize));
                }
                population2[i] = Copy(population[best]); 
            }
            
            //single-focus-flip and block exchange mutations
            for (int i = 0 ; i < popsize-1 ; i++){
                if (crossprob > rnd.nextInt(100)){
                    int temp;                     
                    for (int a = 0 ; a < 5 ; a++){
                        temp = population2[i][a];
                        population2[i][a] = population2[i+1][a];
                        population2[i+1][a] = temp;
                    }
                }
            }
            
            for (int i = 0 ; i < popsize ; i++){
                if (mutaprob > rnd.nextInt(200)){
                    int temp = (int) (rnd.nextInt(population2[0].length));                     
                    population2[i][temp] = (int)(rnd.nextInt(4));
                }
                else if (mutaprob > rnd.nextInt(200)){
                    for (int j = 0 ; j < population2[0].length/2 ; j++){
                        int temp = population2[i][j];
                        population2[i][j] = population2[i][population2[0].length-1-j];
                        population2[i][population2[0].length-1-j] = temp;
                    }
                }
            }
            
            population = population2;
            population2 = new int[popsize][population[0].length];
            
        }
            
        bestavg = bestavg / maxgen;
        avgavg = avgavg / maxgen;
            Print(population);
            System.out.println("Best Avg: " + bestavg + "  Average of Avg: " + avgavg);
    }
    
    //Fitness of given individual is calculated
    private int Fitness(int[] indiv, char[][] sample, int x, int y, int step){
        if (x < 0 || x == sample.length || y < 0 || y == sample[0].length ||
                step == indiv.length){
            return 0;
        }
        
        if (indiv[step] == 0){ //Up
            if (sample[x][y] != '*'){
                sample[x][y] = '*';
                return Fitness(indiv, sample, x, y-1, step+1) +1;
            }
            else {return Fitness(indiv, sample, x, y-1, step+1);
                
            }
        }
        else if (indiv[step] == 1){ //right
            if (sample[x][y] != '*'){
                sample[x][y] = '*';
                return Fitness(indiv, sample, x+1, y, step+1) +1;
            }
            else {return Fitness(indiv, sample, x+1, y, step+1);
                
            }
        }
        else if (indiv[step] == 2){ //down
            if (sample[x][y] != '*'){
                sample[x][y] = '*';
                return Fitness(indiv, sample, x, y+1, step+1) +1;
            }
            else {return Fitness(indiv, sample, x, y+1, step+1);
                
            }
        }
        else { //left
            if (sample[x][y] != '*'){
                sample[x][y] = '*';
                return Fitness(indiv, sample, x-1, y, step+1) +1;
            }
            else {return Fitness(indiv, sample, x-1, y, step+1);
                
            }
        }
    }
    
    //Duplicates an individual
    private int[] Copy(int[] origin){
        int[] temp = new int[origin.length];
        for (int x = 0 ; x < origin.length ; x++){
            temp[x] = origin[x];
        }
        return temp;
    }
    
    //Generates output    
    private void Print(int[][] puzzle){
        String line;
        
        for (int out=0 ; out< puzzle.length ; out++){
            line = "";
            for (int inner=0 ; inner< puzzle[0].length ; inner++){
                line = line +"["+ puzzle[out][inner] +"]";
            }
            System.out.println(line);
        }
    }
    
    public static void main(String[] args) {
        AIAssign2 a = new AIAssign2();
    }
}
