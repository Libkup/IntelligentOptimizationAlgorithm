package citys;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class City {

	public City() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException{
		int[] size = {10,20,40,80,100,120,160,180,200,500};
		Random rand = new Random();
		
		
		File file =new File("D:\\cityMatrix.txt");
		Writer out =new FileWriter(file);
		for(int i = 0; i < 10;i++){
			int[][] matrix = new int[size[i]][size[i]]; 
			for(int j = 0; j < size[i]; j++){
				for(int k = 0; k < size[i]; k++){
					if(j == k ){
						matrix[j][k] = 0;
					}else if (k > j){
						int cost = rand.nextInt();
						cost = rand.nextInt(100); //生成0-100以内的随机数
						cost = (int)(Math.random() * 100); //0-100以内的随机数，用Matn.random()方式 
						while(cost == 0){
							cost = (int)(Math.random() * 100);
						}
						matrix[j][k] = cost;
						matrix[k][j] = cost;
					}
				}
			}
			
			
			for(int j = 0; j < size[i]; j++){
				String data= "";
				for(int k = 0; k < size[i]; k++){
					data += matrix[j][k] + " ";
				}
				out.write(data);
				out.write("\r\n");
			}
			out.write("\r\n");
		}
		out.close();
		
	}
}
