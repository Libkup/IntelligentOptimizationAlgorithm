package algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class TabuSearch {

	private static int[][] distance; // 距离矩阵
	private static int cityNum;		//当前城市的数量
	private static int tabuLength = 2;	//禁忌长度
	private static int candidateSetLength = 10;//候选集合长度
	private static int[] cost;
	private static int[][] candidateSet;
	private static Random random;
	private static int[] way;
	private static int[] bestway;
	private static int best;
	static Queue<String> queue;
	private static Writer out;
	public TabuSearch() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
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
			distance = directedGraph;
			cityNum = size[s];
//			for(int i = 0; i < cityNum; i++){
//				for(int j = 0; j < cityNum; j++)
//					System.out.printf("%-4d", distance[i][j]);
//				System.out.println();
//			}
			
			way = new int[cityNum];
			bestway = new int[cityNum];
			queue = new LinkedList<String>();
			init();
			calculateInit();
//			for(int i = 0; i < cityNum;i++)
//				System.out.print(way[i]+" ");
//			System.out.println();
			
			for(int i = 0; i < 100000; i++){
				twoOPT();
//				for (int k = 0; k < candidateSetLength; k++) {
//					for (int j = 0; j < cityNum; j++) {
//						System.out.print(candidateSet[k][j] + " ");
//					}
//					System.out.println();
//				}
				
				cost = new int[candidateSetLength];
				calculateCost();
//				for (int k = 0; k < candidateSetLength; k++) {
//					System.out.println(cost[k]);
//				}
				if (cost[minCost()] < best) {
//					queue.poll();
//					queue.add(comparison(minCost()));
					if(queue.size() < tabuLength)
						queue.add(comparison(minCost()));
					else{
						queue.poll();
						queue.add(comparison(minCost()));
					}
					bestway = candidateSet[minCost()];
					way = candidateSet[minCost()];
					best  = cost[minCost()];
					out.write(best + "	");
				} else {
					String item = comparison(minCost());
					boolean flag = true;
					while (flag) {
						if (queue.contains(item)) {
							cost[minCost()] = Integer.MAX_VALUE;
							item = comparison(minCost());
						} else {
							flag = false;
						}
					}
					if(queue.size() < tabuLength)
						queue.add(item);
					else{
						queue.poll();
						queue.add(item);
					}
					way = candidateSet[minCost()];
				}
			}
			out.write("\r\n");
			System.out.println(best);
//			for(int i = 0; i < cityNum; i++)
//				System.out.print(bestway[i]+" ");
//			System.out.println();
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
	
	public static void twoOPT(){
		int i,j,first,second,temp;
		candidateSet = new int[candidateSetLength][cityNum];
		for(i = 0; i < candidateSetLength ; i++){
			candidateSet[i] = way.clone();
			first = random.nextInt(cityNum);
			second = random.nextInt(cityNum);
			while(first == second)
				second = random.nextInt(cityNum);
			temp = candidateSet[i][first];
			candidateSet[i][first] = candidateSet[i][second];
			candidateSet[i][second] = temp;
		}
	}
	
	public static void calculateCost(){
		for(int i = 0; i < candidateSetLength ; i++){
			for(int j = 0;j < cityNum-1; j++){
				cost[i] += distance[candidateSet[i][j]][candidateSet[i][j+1]];
			}
			cost[i] += distance[candidateSet[i][cityNum-1]][candidateSet[i][0]];
		}
		
	}
	
	public static void calculateInit(){
		for(int j = 0;j < cityNum-1; j++){
			best += distance[way[j]][way[j+1]];
		}
		best += distance[way[cityNum-1]][0];
	}
	
	public static int minCost(){
		int min = cost[0];
		int minWitch = 0;
		for(int i = 1;i < candidateSetLength;i ++){
			if(cost[i] < min){
				min = cost[i];
				minWitch = i;
			}
		}
		return minWitch;
	}

	public static String comparison(int row){
		int miner=0,maxer=0;
		for(int i = 0; i < cityNum; i++){
			if(candidateSet[row][i] < way[i]){
				miner = candidateSet[row][i];
				maxer = way[i];
				break;
			}else if(candidateSet[row][i] > way[i]){
				miner = way[i];
				maxer = candidateSet[row][i];
				break;
			}
		}
		return miner + "-" + maxer;
	}
}
