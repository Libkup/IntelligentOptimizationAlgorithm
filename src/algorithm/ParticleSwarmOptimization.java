package algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class ParticleSwarmOptimization {
	
	private static Random random;
	private static double maxSpeed = 15;
	private static int N = 10;
	private static double[] positions = new double[N];
	private static double[] speeds = new double[N];
	private static double[] fitness = new double[N];
	private static double[][] selfBestFitness = new double[N][2];
	private static double bestFitness = 0.0;
	private static double bestPosition = 0.0;
	private static double c1 = 2, c2 = 2, w = 0.5, a = 0.5 , b = 0.5;
	private static Writer out;
	public ParticleSwarmOptimization() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException{
		random = new Random(System.currentTimeMillis());
		positions = new double[N];
		speeds = new double[N];
		fitness = new double[N];
		selfBestFitness = new double[N][2];
		File test =new File("D:\\test.txt");
		out = new FileWriter(test);
		initParticles();
		initSpeeds();
		for(int i = 0;i < 1000000; i++){
			calculateFitness();
			updateSelfBestFitness();
			updateGlobalBestFitness();
			updatePositionAndSpeed();
		}
		
//		for(int i = 0;i < positions.length; i++){
			System.out.println(bestFitness + " " + bestPosition);
//		}
		out.close();
	}

	public static void initParticles(){
		for(int i = 0;i < positions.length; i++){
			positions[i] = (random.nextDouble() + random.nextInt(29));
		}
	}
	
	public static void initSpeeds(){
		for(int i = 0;i < speeds.length; i++){
			speeds[i] = (random.nextDouble() + random.nextInt(14));
		}
	}
	
	// f(x) = ln(x^3 + x) + sin(5x) + 4cos(7x)
	public static void calculateFitness(){
		for(int i = 0;i < fitness.length; i++){
			fitness[i] = Math.log(Math.pow(positions[i], 3) + positions[i]) + Math.sin(5*positions[i]) + 4*Math.cos(7*positions[i]);
		}
	}
	
	public static void updateSelfBestFitness(){
		for(int i = 0;i < fitness.length; i++){
			if(fitness[i] > selfBestFitness[i][0]){
				selfBestFitness[i][0] = fitness[i];
				selfBestFitness[i][1] = positions[i];
			}
		}
	}
	
	public static void updateGlobalBestFitness() throws IOException{
		for(int i = 0;i < selfBestFitness.length; i++){
			if(selfBestFitness[i][0] > bestFitness){
				bestFitness = selfBestFitness[i][0];
				bestPosition = selfBestFitness[i][1];
				out.write(String.valueOf(bestFitness)+"	");
			}
		}
		
	}
	
	public static void updatePositionAndSpeed(){
		for(int i = 0;i < speeds.length; i++){
			speeds[i] = w*speeds[i] + c1*a*(selfBestFitness[i][0] - fitness[i]) + c2*b*(bestFitness - fitness[i]);
			if(speeds[i] > maxSpeed)
				speeds[i] = maxSpeed;
			else if( speeds[i] < -maxSpeed)
				speeds[i] = -maxSpeed;
			positions[i] = positions[i] + speeds[i];
			if(positions[i] < 0)
				positions[i] = 0.001;
			else if(positions[i] > 30)
				positions[i] = 30;
		}
	}
}
