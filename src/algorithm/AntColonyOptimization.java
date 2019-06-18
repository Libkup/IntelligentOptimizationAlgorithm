package algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class AntColonyOptimization {

	private static int[][] distance; // 距离矩阵
	private static int cityNum;		//当前城市的数量
	private static double[][] pheromones; //信息素
	private static Random random;
	
	private static int[] allow;	//禁忌表
	
	private static int[][] bestWay;
	private static int[][] currentWay;
	private static int[] bestCost;
	private static int[] currentCost;
	private static int Q = 100;	//信息素总量
	private static int N = 100;	//迭代次数
	private static int k = 10; 	//蚂蚁数量
	private static double a = 2.5 , b = 2.5;
	private static double p = 0.25;	
	private static Writer out;
	private static int currentBest = Integer.MAX_VALUE;
	public AntColonyOptimization() {
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		int[] size = {10,20,40,80,100,120,160,180,200,500};
		File file =new File("D:\\cityMatrix.txt");
		FileReader in =new FileReader(file);
		File test =new File("D:\\test.txt");
		out = new FileWriter(test);
		BufferedReader bf = new BufferedReader(in);
		String line = "";
		random = new Random(System.currentTimeMillis());
		long begintime = System.currentTimeMillis();
		for(int s = 0; s < 10; s++){
			int[][] directedGraph = new int[size[s]][size[s]]; 
			int l = 0;//每个规模的每一行
			while ((line = bf.readLine()) != null) {
				if(line.equals(""))
					break;
				else{
					String[] splited = line.split("\\s+");
						for(int k = 0; k < size[s]; k++){
							directedGraph[l][k] = Integer.valueOf(splited[k]);
						}
				}
				l++; 
			}
			distance = directedGraph;
			cityNum = size[s];
			currentWay = new int[k][cityNum];
			bestWay = new int[k][cityNum];
			currentCost = new int[k];
			bestCost = new int[k];
			for(int i = 0;i < bestCost.length; i++)
				bestCost[i] = Integer.MAX_VALUE;
			initPheromones();
			for(int n = 0;n < N; n++){
//				if(n < N/2)
//					p = 0.85;
//				else
//					p = 0.15;
				for(int i = 0;i < k; i++){
					int position = random.nextInt(cityNum);
					initAllow(position);
					currentWay[i][0] = position;
					for(int j = 1;j < cityNum; j++){
						int count = 0;
						for(int c = 0;c < allow.length;c++)
							if(allow[c] != -1)
								count ++;
						double[] p = caculateP(position,count);
						double rand = random.nextDouble();
						int m = 0;
						for(int c = 0;c < count; c++){
							if(rand < p[c]){
								m = c;
								break;
							}
						}
						int x = 0,y = 0;
						for(;x < allow.length; x++){
							if(allow[x] == -1)
								continue;
							if(y == m)
								break;
							y++;
						}
						currentWay[i][j] = allow[x];
						position = allow[x];
						allow[x] = -1;
					}
					currentCost[i] = caculateCost(currentWay[i]);
				}
//				updatePheromonesCycle(currentWay,currentCost);
//				updatePheromonesQuantitySelf1(currentWay,currentCost);
				updatePheromonesQuantitySelf2(currentWay,currentCost);
//				updatePheromonesQuantity(currentWay);
//				updatePheromonesDensity(currentWay);
				updateBestCost(currentCost,bestCost);
				int best = Integer.MAX_VALUE;
				for(int z = 0; z < bestCost.length ; z++){
					if(bestCost[z] < best)
						best = bestCost[z];
				}
				if(best < currentBest){
					currentBest = best;
					out.write(currentBest + "	");
				}
			}
			int best = Integer.MAX_VALUE;
			for(int z = 0; z < bestCost.length ; z++){
				if(bestCost[z] < best)
					best = bestCost[z];
			}
			currentBest = Integer.MAX_VALUE;
			out.write("\r\n");
			System.out.println(best);
		}
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime);
		System.out.println(costTime+"ms");
		out.close();
	}
	
	public static void initPheromones(){
		pheromones = new double[cityNum][cityNum];
		for(int i = 0; i < cityNum; i++)
			for(int j = 0; j < cityNum; j++){
				pheromones[i][j] = 1;
			}
	}
	
	public static void initAllow(int position){
		allow = new int[cityNum-1];
		int temp = random.nextInt(cityNum);
		while(temp == position)
			temp = random.nextInt(cityNum);
		allow[0] = temp;
		for(int i = 1; i < cityNum-1;){
			temp = random.nextInt(cityNum);
			int j;
			for(j = 0; j < i; j++){
				if(allow[j] == temp || position == temp)
					break;
			}
			if(j == i){
				allow[i] = temp;
				i++;
			}	
		}	
	}
	
	public static double[] caculateP(int position,int count){
		
		double[] p = new double[count];
		double sum = 0;
		for(int i = 0;i < allow.length;i++)
			if(allow[i] == -1)
				continue;
			else
				sum += Math.pow(pheromones[position][allow[i]], a) + Math.pow((1/distance[position][allow[i]]), b);
		
		boolean flag = true;
		int j = 0;
		for(int i = 0;i < allow.length;i++){
			if(allow[i] == -1)
				continue;
			else{
				if(flag){
					p[j] = (Math.pow(pheromones[position][allow[i]], a) + Math.pow((1/distance[position][allow[i]]), b))/sum;
					flag = false;
					j++;
				}else{
					p[j] = p[j-1] + (Math.pow(pheromones[position][allow[i]], a) + Math.pow((1/distance[position][allow[i]]), b))/sum;
					j++;
				}
			}
		}		
		return p;
	}
	

	public static int caculateCost(int[] way){
		int cost = 0;
		for(int i = 0;i < way.length - 1; i++){
			cost = cost + distance[way[i]][way[i+1]];
		}
		cost = cost + distance[way[way.length-1]][way[0]];
		return cost;
	}
	
	public static void updatePheromonesCycle(int[][] ways, int[] L){
		for(int i = 0; i < pheromones.length; i++){
			for(int j = 0; j < pheromones[i].length; j++){
				pheromones[i][j] = (1-p)*pheromones[i][j];
			}
		}
		for(int i = 0; i < ways.length; i++){
			for(int j = 0; j < ways[i].length-1; j++){
				pheromones[ways[i][j]][ways[i][j+1]] = pheromones[ways[i][j]][ways[i][j+1]] + Q/L[i];
			}
			pheromones[ways[i][ways[i].length-1]][ways[i][0]] = pheromones[ways[i][ways[i].length-1]][ways[i][0]] + Q/L[i];
		}
	}
	
	public static void updatePheromonesQuantity(int[][] ways){
		for(int i = 0; i < pheromones.length; i++){
			for(int j = 0; j < pheromones[i].length; j++){
				pheromones[i][j] = (1-p)*pheromones[i][j];
			}
		}
		for(int i = 0; i < ways.length; i++){
			for(int j = 0; j < ways[i].length-1; j++){
				pheromones[ways[i][j]][ways[i][j+1]] = pheromones[ways[i][j]][ways[i][j+1]] + Q/distance[ways[i][j]][ways[i][j+1]];
			}
			pheromones[ways[i][ways[i].length-1]][ways[i][0]] = pheromones[ways[i][ways[i].length-1]][ways[i][0]] + Q/distance[ways[i][ways[i].length-1]][ways[i][0]];
		}
	}
	
	public static void updatePheromonesQuantitySelf1(int[][] ways, int[] L){
		for(int i = 0; i < ways.length; i++){
			for(int j = 0; j < ways[i].length-1; j++){
				pheromones[ways[i][j]][ways[i][j+1]] = (1-p)*pheromones[ways[i][j]][ways[i][j+1]] + Q/distance[ways[i][j]][ways[i][j+1]];
			}
			pheromones[ways[i][ways[i].length-1]][ways[i][0]] = (1-p)*pheromones[ways[i][ways[i].length-1]][ways[i][0]] + Q/distance[ways[i][ways[i].length-1]][ways[i][0]];
		}
	}
	
	public static void updatePheromonesQuantitySelf2(int[][] ways, int[] L){
		for(int i = 0; i < ways.length; i++){
			for(int j = 0; j < ways[i].length-1; j++){
				pheromones[ways[i][j]][ways[i][j+1]] = (1-p)*pheromones[ways[i][j]][ways[i][j+1]] + p*Q/distance[ways[i][j]][ways[i][j+1]];
			}
			pheromones[ways[i][ways[i].length-1]][ways[i][0]] = (1-p)*pheromones[ways[i][ways[i].length-1]][ways[i][0]] + p*Q/distance[ways[i][ways[i].length-1]][ways[i][0]];
		}
	}
	
	public static void updatePheromonesDensity(int[][] ways){
		for(int i = 0; i < pheromones.length; i++){
			for(int j = 0; j < pheromones[i].length; j++){
				pheromones[i][j] = (1-p)*pheromones[i][j];
			}
		}
		for(int i = 0; i < ways.length; i++){
			for(int j = 0; j < ways[i].length-1; j++){
				pheromones[ways[i][j]][ways[i][j+1]] = pheromones[ways[i][j]][ways[i][j+1]] + Q;
			}
			pheromones[ways[i][ways[i].length-1]][ways[i][0]] = pheromones[ways[i][ways[i].length-1]][ways[i][0]] + Q;
		}
	}
	
	public static void printPheromones(double[][] pheromones2){
		for(int i = 0; i < pheromones2.length; i++){
			for(int j = 0; j < pheromones2[i].length; j++){
				System.out.printf("%#.2f ",pheromones2[i][j] );
			}
			System.out.println();
		}
	}
	
	public static void updateBestCost(int[] currentCost,int[] bestCost){
		for(int i = 0;i < bestCost.length;i ++)
			if(currentCost[i] < bestCost[i])
				bestCost[i] = currentCost[i];
	}
}
