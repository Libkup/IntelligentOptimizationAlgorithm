package citys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Reader {

	public Reader() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException{
		int[] size = {10,20,40,80,100,120,160,180,200,500};
		File file =new File("D:\\cityMatrix.txt");
		FileReader in =new FileReader(file);
		BufferedReader bf = new BufferedReader(in);
		String line = "";
		//读取每个规模的数据
		for(int i = 0; i < 10; i++){
			int[][] matrix = new int[size[i]][size[i]]; 
			int l = 0;//每个规模的每一行
			while ((line = bf.readLine()) != null) {
				if(line.equals(""))
					break;
				else{
					String[] splited = line.split("\\s+");//这样写就可以了
						for(int k = 0; k < size[i]; k++){
							matrix[l][k] = Integer.valueOf(splited[k]);
						}
				}
				l++; 
			}
			
			for(int j = 0; j < size[i]; j++){
				for(int k = 0; k < size[i]; k++){
					System.out.print(matrix[j][k]+" ");
//					matrix[j][k] = Integer.valueOf(splited[k]);
				}
				System.out.println();
			}
			System.out.println();
		}
		
	}
}
