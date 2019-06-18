package algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class SimulateAnneal {
	private static int[][] distance; // 距离矩阵
	private static int cityNum;		//当前城市的数量
	private static Random random;
	private static int[] way;
	private static int[] bestWay;
	private static int cost;
	private static int bestCost;
	private static int dE;
	private static double k = 7;
	private static double T = 100;
	private static double r = 0.98;
	private static Writer out;
	public SimulateAnneal() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		int[] size = {10,20,40,80,100,120,160,180,200,500};
		File file =new File("D:\\cityMatrix.txt");
		FileReader in =new FileReader(file);
		File test =new File("D:\\test.txt");
		out = new FileWriter(test);
		BufferedReader bf = new BufferedReader(in);
		String line = "";
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
			T = cityNum*5;
			distance = directedGraph;
			cityNum = size[s];
			random = new Random(System.currentTimeMillis());
			way = new int[cityNum];
			bestWay = new int[cityNum];
			init();
			bestCost = cost(way);
			bestWay = way;
			int n = 0;
			while(n < 1000000){
				if(cost(way) < bestCost){
					bestCost = cost(way);
					bestWay = way;
					out.write(bestCost + "	");
				}
				int[] newWay = swap(way);
//				for(int i =0; i < way.length; i++)
//					System.out.print(newWay[i]+" ");
				dE = cost(newWay) - cost(way);
				if(dE < 0){
					way = newWay;
				}else{
//					System.out.println(Math.exp(-dE/(k * T)));
					if(Math.exp(-dE/(k *  T)) > random.nextFloat()){
						way = newWay;
					}
				}
				T = r * T;
				n++;
			}
			
			System.out.println(bestCost);
			out.write("\r\t");
		}
		long endtime = System.currentTimeMillis();
		long costTime = (endtime - begintime);
		System.out.println(costTime+"ms");
		out.close();
	}
	
	public static void init(){
		random = new Random(System.currentTimeMillis());
		int i,j;
		for(i = 0; i < cityNum;){
			way[i] = random.nextInt(cityNum);
			for(j = 0; j < i ; j++){
				if(way[j] == way[i])
					break;
			}
			if(j == i){
				i++;
			}	
		}
	}
	
	public static int cost(int[] way){
		int cost = 0;
		for(int i = 0; i < way.length - 1 ; i++){
			cost += distance[way[i]][way[i+1]];
		}
		cost += distance[way[way.length-1]][way[0]];
		return cost;
	}
	
	public static int[] swap(int[] way){
		int[] tempWay = way.clone();
		random = new Random(System.currentTimeMillis());
		int first,second;
		first = random.nextInt(cityNum);
		second = random.nextInt(cityNum);
		while(second == first)
			second = random.nextInt(cityNum);
		int temp = tempWay[first];
		tempWay[first] = tempWay[second];
		tempWay[second] = temp;
		return tempWay;
	}
}
