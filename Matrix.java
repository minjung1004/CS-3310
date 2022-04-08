import java.util.Random;
import java.util.Math;


public class Matrix {
	//Sanity check test
	private static int[][] testA = {
			{2, 0, -1, 6},
			{3, 7, 8, 0},
			{-5, 1, 6, -2},
			{8, 0 ,2, 7}
	};
	
	private static int[][] testB = {
			{0, 1, 6, 3},
			{-2, 8, 7, 1},
			{2, 0, -1, 0},
			{9, 1, 6, -2}
	};
	
	/*answer testA * testB
	 52   9   49   -6
	 2   59   59   16
	 -8   1  -41  -10
	 67  15   88   10
	 */
	

	//----RANDOM MATRIX---------------------------------------------------------------------------------------------------------
	private static int[][] random_test(int n){
		Random random = new Random();
		
		int[][] Random_matrix = new int[n][n];
		for(int i =0; i<n; i++) {
			for(int j=0; j<n; j++) {
				Random_matrix[i][j] = random.nextInt(20);
			}
		}
		return Random_matrix;
	}
	
	
	//-----CLASSICAL-----------------------------------------------------------------------------------------------------------
	private static int[][] Classical(int[][] A, int[][] B){
		//size of the matrix n x n
		int n = A.length;
		//declare the product matrix
		int[][] C = new int[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				for(int k=0; k<n; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;
	}
	
	//------NATIVE DIVIDE AND CONQUER-------------------------------------------------------------------------------------------
	private static int[][] DC(int[][] A, int[][] B){
		//size of the matrix n x n
		int n = A.length;
		//declare the product matrix
		int[][] C = new int[n][n];
		//base case
		if(n==1) {			
			C[0][0]= A[0][0] * B[0][0];
			return C;
		}else {
			//divide
			
			int k = n/2;
			//partition
			int[][] a11 = partition(A, 0 ,k, 0, k);
			int[][] a12 = partition(A, 0 ,k, k, n);
			int[][] a21 = partition(A, k ,n, 0, k);
			int[][] a22 = partition(A, k ,n, k, n);
			
			int[][] b11 = partition(B, 0 ,k, 0, k);
			int[][] b12 = partition(B, 0 ,k, k, n);
			int[][] b21 = partition(B, k ,n, 0, k);
			int[][] b22 = partition(B, k ,n, k, n);

			//recursion
			int[][] x1 = DC(a11,b11);
			int[][] x2 = DC(a12,b21);
			int[][] x3 = DC(a11,b12);
			int[][] x4 = DC(a12,b22);
			int[][] x5 = DC(a21,b11);
			int[][] x6 = DC(a22,b21);
			int[][] x7 = DC(a21,b12);
			int[][] x8 = DC(a22,b22);
			
			int[][] c11 = add(x1,x2);
			int[][] c12 = add(x3,x4);
			int[][] c21 = add(x5,x6);
			int[][] c22 = add(x7,x8);
			
			C = combine(c11,c12,c21,c22);
			
			return C;
		}
	}
	
	//-----STRASSEN-----------------------------------------------------------------------------------------------
	private static int[][] Strassen(int[][] A, int[][] B){
		//size of the matrix n x n
		int n = A.length;
		//declare the product matrix
		int[][] C = new int[n][n];
		//base case
		if(n==1) {
			C[0][0] = A[0][0] * B[0][0];
			return C;
		}else {
			//divide
			int k = n/2;
			
			//partition
			int[][] a11 = partition(A, 0 ,k, 0, k);
			int[][] a12 = partition(A, 0 ,k, k, n);
			int[][] a21 = partition(A, k ,n, 0, k);
			int[][] a22 = partition(A, k ,n, k, n);
			
			int[][] b11 = partition(B, 0 ,k, 0, k);
			int[][] b12 = partition(B, 0 ,k, k, n);
			int[][] b21 = partition(B, k ,n, 0, k);
			int[][] b22 = partition(B, k ,n, k, n);
			
			//recursion
			int[][] p1 = Strassen(a11, sub(b12,b22));
			int[][] p2 = Strassen(add(a11,a12), b22);
			int[][] p3 = Strassen(add(a21, a22), b11);
			int[][] p4 = Strassen(a22, sub(b21,b11));
			int[][] p5 = Strassen(add(a11,a22), add(b11,b22));
			int[][] p6 = Strassen(sub(a12, a22), add(b21,b22));
			int[][] p7 = Strassen(sub(a11,a21), add(b11,b12));
			
			int[][] c11 = add(sub(p4,p2), add(p5,p6));
			int[][] c12 = add(p1,p2);
			int[][] c21 = add(p3,p4);
			int[][] c22 = add(sub(p1,p3), sub(p5,p7));
			
			C = combine(c11,c12,c21,c22);
			return C;
		}
		
	}
	
	//----PARTITION-------------------------------------------------------------------------------------------------------
	
	private static int[][] partition(int[][] matrix, int rowStart, int rowEnd, int colStart, int colEnd){
		//set the size of the matrix
		int n = rowEnd - rowStart;
		//declare the result matrix
		int[][] result = new int[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				result[i][j] = matrix[i+rowStart][j+colStart];
			}
		}
		return result;
	}
	
	//-----ADDITION-----------------------------------------------------------------------------------------------------------
	private static int[][] add(int[][] A, int[][] B){
		//size
		int n = A.length;
		//declare sum matrix
		int[][] sum = new int[n][n];
		//do addition
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				sum[i][j] = A[i][j] + B[i][j];
			}
		}
		return sum;
	}
	
	//------SUBTRACTION------------------------------------------------------------------------------------------------------
	private static int[][] sub(int[][] A, int[][] B){
		//size
		int n = A.length;
		//declare difference matrix
		int[][] diff = new int[n][n];
		//do subtraction
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				diff[i][j] = A[i][j] - B[i][j];
			}
		}
		return diff;
	}
	
	//------PUT TOGETHER THE ALL THE PARTS---------------------------------------------------------------------------------------
	private static int[][] combine(int[][] c11, int[][] c12, int[][] c21, int[][] c22){
		int n = c11.length;
		int new_size = n *2;
		
		int[][] result = new int[new_size][new_size];
		
		for(int i=0; i<new_size; i++) {
			for(int j=0; j<new_size; j++) {
				//Q1
				if(i<n && j<n) {
					result[i][j] = c11[i][j];
				}
				//Q2
				else if(i<n && j>= n) {
					result[i][j] = c12[i][j-n];
				}
				//Q3
				else if(i >=n && j<n) {
					result[i][j] =c21[i-n][j];
				}
				//Q4
				else {
					result[i][j] = c22[i-n][j-n];
				}
			}
		}
		return result;
	}
	//---GET NEXT N  THAT IS POWER OF 2----------------------------------------------------------------------------------------------------------------
	private static int nextPower2(int n) {
		n=n-1;
		int lg = (int)(Math.log(n)/ Math.log(2));
		return 1 << lg +1;	
	}
	
	//---RESIZE THE MATRIX TO BE A NXN POWER OF 2-----------------------------------------
	
	private static int[][] resize(int[][] matrix, int n){
		int x = nextPower2(n);
		int[][] temp = new int[x][x];
		for(int i = 0;i < matrix.length; i++) {
			for(int j=0; j< matrix.length; j++) {
				temp[i][j] = matrix[i][j];
			} 
		}
		return temp;
		
	};
	//----PRINT FINAL MATRIX------------------------------------------------------------------------------------------------------------------
	private static void print(int[][] matrix) {
		int n = matrix.length;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				System.out.print("\t" + matrix[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	//------MAIN--------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		
		//Sanity check:
		System.out.println("Sanity Check:");
		long start = System.currentTimeMillis();
		System.out.println("Classical: ");
		print(Classical(testA, testB));
		long end = System.currentTimeMillis();
		long elapsedTime = end- start;
		System.out.println("Time: "+ elapsedTime+ " ms");
		
		long start1 = System.currentTimeMillis();
		System.out.println("Native DC: ");
		print(DC(testA, testB));
		long end1 = System.currentTimeMillis();
		long elapsedTime1 = end1 - start1;
		System.out.println("Time: "+ elapsedTime1+ " ms");
		
		long start2 = System.currentTimeMillis();
		System.out.println("Strassen: ");
		print(Strassen(testA, testB));
		long end2 = System.currentTimeMillis();
		long elapsedTime2 = end2- start2;
		System.out.println("Time: "+ elapsedTime2+ " ms");
		
		/**
		//Test nxn from 0 to 256
		for(int i=0; i<=16; i++) {
			int[][] testC = resize(random_test(i), i);
			int[][] testD = resize(random_test(i), i);
			System.out.println("Random Classical Test "+i+": ");
			long start3 = System.currentTimeMillis();
		 	print(Classical(testC, testD));
		 	long end3 = System.currentTimeMillis();
		 	long elapsedTime3 = (end3- start3);
		 	System.out.println("Time: "+ elapsedTime3+ " ms");
				 	
			System.out.println("Random DC Test "+i+": ");
		 	long start4 = System.currentTimeMillis();
			print(DC(testC, testD));
			long end4 = System.currentTimeMillis();
		 	long elapsedTime4 = (end4- start4);
		 	System.out.println("Time: "+ elapsedTime4+ " ms");

		 	System.out.println("Random Strassen Test "+i+": ");
			long start5 = System.currentTimeMillis();
		 	print(Strassen(testC, testD));
		 	long end5 = System.currentTimeMillis();
			long elapsedTime5 = (end5- start5);
		 	System.out.println("Time: "+ elapsedTime5 + " ms");
		}
		*/
		/**
		//Test nxn from 256 to 4096
		for(int i=1; i<=10; i++) {
			//testing large set 256x256
			int n = (int) Math.pow(2, i);
			int[][] testC = random_test(n);
			int[][] testD = random_test(n);
			System.out.println("Random Classical Test "+n+": ");
			long start6 = System.currentTimeMillis();
			//print(
			Classical(testC, testD);
			long end6 = System.currentTimeMillis();
			long elapsedTime6 = (end6- start6);
			System.out.println("Time: "+ elapsedTime6+ " ms");
			 	
			System.out.println("Random DC Test "+n+": ");
			long start7 = System.currentTimeMillis();
			//print(
			DC(testC, testD);
			long end7 = System.currentTimeMillis();
			long elapsedTime7 = (end7- start7);
			System.out.println("Time: "+ elapsedTime7+ " ms");

			System.out.println("Random Strassen Test "+n+": ");
			long start8 = System.currentTimeMillis();
			//print(
			Strassen(testC, testD);
			long end8 = System.currentTimeMillis();
			long elapsedTime8 = (end8- start8);
			System.out.println("Time: "+ elapsedTime8 + " ms");
			
			System.out.println();
			}
		*/
		/**
		//Testing phase:
		
		double sum1 = 0, sum2 =0, sum3 =0;
		
		//input n= 2, 4, 8, 16, 32, 64, 128, 256, 512, ..
		//1024 took too long
		for(int i=1; i<100;i++) {
			int n = 1024;
			int[][] testC = random_test(n);
			int[][] testD = random_test(n);
			
			long start6 = System.currentTimeMillis();
			Classical(testC, testD);
			long end6 = System.currentTimeMillis();
			long elapsedTime6 = (end6- start6);


			long start7 = System.currentTimeMillis();
			DC(testC, testD);
			long end7 = System.currentTimeMillis();
			long elapsedTime7 = (end7- start7);

			long start8 = System.currentTimeMillis();
			Strassen(testC, testD);
			long end8 = System.currentTimeMillis();
			long elapsedTime8 = (end8- start8);
			
			sum1 += elapsedTime6;
			sum2 += elapsedTime7;
			sum3 += elapsedTime8;
			//System.out.println("    "+elapsedTime6+"\t\t"+elapsedTime7+"\t    "+elapsedTime8);
			System.out.println("check");
		}
		System.out.println("Testing 1024x1024 matrix w/ random inputs (time in ms) 100x:");
		System.out.println("Average:");
		System.out.println("Classical\tDC\tStrassen");
		*/
		/**
		System.out.println("sum1:"+ sum1);
		System.out.println("sum2:"+ sum2);
		System.out.println("sum3:"+ sum3);
		*/
		/**
		double avg1 = 0, avg2 = 0, avg3 = 0;
		avg1 = sum1/100;
		avg2 = sum2/100;
		avg3 = sum3/100;
		System.out.println("    "+avg1+"        "+avg2+"\t "+avg3);
		*/

	}
}
