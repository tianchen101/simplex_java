// Student : Xinghang Ye
// ID : V00788404
// CSC445 Programming Assignment #2
import java.lang.*;
import java.util.Scanner;

 class array_and_basic {
 // this data structure is the return type of the pivot method
 // and we can based on the return types to renew the array[][] and basic[]
 // for next pivot use.
	double array[][];
	int basic[];
	array_and_basic(double new_array[][],int new_basic[]){
		array = new_array;
		basic = new_basic;

	}
}
class array_and_optimal{
   // this is used as the return type of the function used to optimize the phase 1 problem
   // because we know that the phase1 will return value 0, if it is feasible, and then we will
   // need the information of the matrix to start the phase2
	double array[][];
	double optimal;
	int basic[];
	array_and_optimal(double new_array[][],double new_optimal,int new_basic[]){
		array = new_array;
		optimal = new_optimal;
		basic = new_basic;
	}
}
public class Simplex{
  
 public static int Find_Basic(int basic[],int n,double array[],int m){ // n is the number of elements in basic[]
									// m is the number of elements in array[]
		/*
			This method is used to find the basic variable in each row of the array,which is generated
			from the input, and we will place the found variable to the left hand side of the equation 
		*/

		int i,j;					        
		int flag = -1;
		int position = -1;
	outloop:
		for(i=0;i<m;i++){
			for(j=0;j<n;j++){
			     if(basic[j] == i && ((array[i] < -1*Math.pow(10,-5))||(Math.pow(10,-5)<array[i]))){ // means that we found the position of j
				position = i;
				break outloop;
				}
			}
		}
		return position;		
  }
  public static boolean[] is_in_basis_array(int basic[],int m,int n){
  	/*
		This is used to store the boolean value of each variable in this culculation,
		if the variable xi is in the basis then is_in_basis[i] would be 1
		otherwise it would be flase
  	*/
  			boolean is_in_basis[] = new boolean[m+n+1] ; // to store if a variable is in basis or not
  			int i;
  			is_in_basis[0] = false;
  			for(i=1;i<n+m+1;i++){
  				if(is_basic(basic,m,i))
  					is_in_basis[i] = true;
  				else
  					is_in_basis[i] = false;
  			}
  			return is_in_basis;
  }

  public static boolean is_basic(int basic[],int m,int index){
		/*
			This is used to determine whether or not print out the value of a xi,
			if i is not in basic we can print it out at the right hand side of 
			the equation, if it is in basic set, then we won't print it out
		*/
		boolean flag = false;
		int i;
		for(i = 0;i < m; i++){
			if(basic[i] == index){
				flag = true;
				break;
			}					
		}
		return flag;
	}

	public static int find_ind_row(int n,int m,double array[][],int basic[],int index){
		 /*
			This is used to find the row for next pivot, it will be used after we find the  index, which 
			is the index of the colum to be pivoted next. The basic idea here is that find the coeffiecints of each row
			to see if the coeff is negative and then find the smallet constraint, so we could find the row out, the the row 
			number is the ind_row
		 */
			int ind_row = 0;
			int i,j;
			double max = 0;
			double min = 100000;
			for(i=1;i<m+1;i++){
				if(array[i][index] < 0){
					if( (min > array[i][0]/Math.abs(array[i][index])) && (Math.abs(array[i][index]) > Math.pow(10,-5)) ){
						min = array[i][0]/Math.abs(array[i][index]); //used to find the variable to enter the basis
						ind_row = i;
					}
				}	
			}
		return ind_row;
		}
	public static int find_index(int n,int m,double array[][],int basic[],int pivot_mode_flag){
		/*
			THis is used to find the index of the colum to be pivoted next time, because the max coefficient rule is applied
			Therefore we need to  find the maximum coefficient of each colum in the z-row, after we find the colum out, then
			index = colum number.

		*/
			int i,j;
			int count = 0; // count the number of possitive coefficient in z row!
			double max = Math.pow(10,-5);
			double min = 100000;
			int index = 0;
			double[] maximum_increase = new double[m+n+1]; // for each non-basis variable
			switch(pivot_mode_flag){
				case 1:
						for(i=1;i<m+n+1;i++){
							if(array[0][i] > Math.pow(10,-5)){ //smallest subscript rule
									index = i;
									break;
							}
						}
						break;
				case 2:
						for(i=1;i<m+n+1;i++){ // to find the largest coefficient
								if(max < array[0][i]){
										max = array[0][i];
								   		index = i;
								}
						}
						break;
				case 3:  // this is the largest increase rule
						for(i=0;i<m+n+1;i++){ // initialize the maximum_increase array all to be 0
							maximum_increase[i] = 0;
						}
						for(i=1;i<m+n+1;i++){
							if(array[0][i] > Math.pow(10,-5)){
								if(count == 0){ // the first that we find is possitive coeff
									index = i;
								}
								count = 1+count;
							}
						}
						//System.out.print("count ="+count+"\n");
						if(count == 0){   // if there are no positive coeff,then we should have found the optimal
							return index;
						}
							//break; // means that no coefficient in z row is possitive
						for(i=1;i<m+n+1;i++){ // largest increase rule
							min = 100000 ; // re-initialize it to 0
							if((-1)*Math.pow(10,-5)<array[0][i] && array[0][i]<Math.pow(10,-5)){//means 0
										maximum_increase[i] = 0;
										continue;
							}
							if(array[0][i] > Math.pow(10,-5)){  // so the coeffient of the variable is positive
								for(j=1;j<m+1;j++){//be feasible
									if(array[j][i]<(-1)*Math.pow(10,-5)&&array[j][0]*array[0][i]/((-1)*array[j][i]) < min){ // beacause we need to ensure this problem to 
											min = array[j][0]*array[0][i]/((-1)*array[j][i]);											
									}
								}
								maximum_increase[i] = min;
							}
						}
						//till now, We should have got the increase generated by each variable
						count = 0;
						for(i=1;i<n+m+1;i++){ // find the maximun increase
							if(maximum_increase[i] > Math.pow(10,-5) && maximum_increase[i] > max){//positive value
												max = maximum_increase[i];
												index = i;
							}
						}
						break;
				default: 
						System.out.print("No method_rule is indicated!\n");
						break;
			}
				
		return index;
		}
	public static void print_row_zero(double value){
		/*
		This is used to print the first element of each row in the matrix
		*/
			if(value >= Math.pow(10,-5) && value < 10)
					System.out.printf("%.2f     ",value);
			else if (value > -10 && value < -Math.pow(10,-5))
					System.out.printf("%.2f    ",value);
			else if(value > -100 && value < -10)
					System.out.printf("%.2f   ",value);
			else if(value > -1000 && value < -100)
					System.out.printf("%.2f ",value);
			else if(value >= 10 && value < 100)
					System.out.printf("%.2f    ",value);
			else if(value >= 100 && value < 1000)
					System.out.printf("%.2f   ",value);
			else if(value >= 1000 && value < 10000)
					System.out.printf("%.2f  ",value);
			else if(value > -Math.pow(10,-5) && value < Math.pow(10,-5))
					System.out.printf("%.2f     ",value);
			else 
					System.out.printf("%.2f ",value);

	}
	public static void print_row_variables(double value,int i){
		/*
		This is used to print the variables in each row
		*/
			if(value >= Math.pow(10,-5) && value < 10){
					System.out.print("+");
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"    ");
				}
			else if (value > -10 && value < -Math.pow(10,-5)){
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"    ");
				}
			else if(value > -100 && value < -10){
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"   ");
				}
			else if(value > -1000 && value < -100){
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"  ");
				}
			else if(value >= 10 && value < 100){
					System.out.print("+");
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"   ");
				}
			else if(value > 100 && value < 1000){
					System.out.print("+");
					System.out.printf("%.2f",value);
					System.out.print("x"+i+"  ");
				}
			else if(value > 1000 && value < 10000){
					System.out.print("+");
					System.out.printf("%.2f",value);
					System.out.print("x"+i+" ");
				}
			else if(value > -Math.pow(10,-5) && value <Math.pow(10,-5)){
					double zero = 0.0;
					System.out.print("+");
					System.out.printf("%.2f",zero);
					System.out.printf("x"+i+"    ");
				}
			else {
					System.out.printf("%.2f",value);
					System.out.printf("x"+i+" ");
				}
	}
	public static void matrix_to_equations(int basic[],double array[][],int m,int n,boolean is_in_basis[]){
		/*
		This method is to convert the matrix to equations form, that is from the matrix
		we are using for calculation to the dictionary form, which is identical to what
		the textbook suggested.
		*/
			int i,j;
			int basic_position;
			for(i=1;i<m+1;i++){ // print out the new array
				basic_position = basic[i-1]; // the array basic stores the basic variable in each row
				if(basic_position < 10)
					System.out.print("x"+basic_position+"  =  ");
				else
					System.out.print("x"+basic_position+" =  ");
				for(j=0;j<m+n+1;j++){
					if(j == 0){ //this is the number in the row
						print_row_zero(array[i][j]);
						continue;
					}
					if(j !=basic_position&& !is_in_basis[j]){
						print_row_variables(array[i][j],j);
						}
					}
				System.out.print("\n");
				}
				//System.out.print("\n");
			
			System.out.print("---------------------------------------------------------------------------------\n");
			System.out.print("z   =  ");
			print_row_zero(array[0][0]);
			for(j=1;j<m+n+1;j++){
				if(!is_in_basis[j])
					print_row_variables(array[0][j],j);
		}
			System.out.print("\n\n");

		}
	public static double[][] read_file(Scanner new_scan){
		/*
			This is to read the input, the main call need to pass it a Scanner, and the read_file call 
			will need to read the content of the scanner, and it will read a problem input at a time
		*/
			String mchar,nchar,middle;	
			int i,j;
			nchar = new_scan.next();
			mchar = new_scan.next();
			System.out.println("n="+ nchar);
			System.out.println("m="+ mchar);// debug information
			int n = Integer.parseInt(nchar);
	        int m = Integer.parseInt(mchar);
			double[][] array = new double[m+1][m+n+1];// the 0 numbered row and colum,will be left as zero
			int[] basic = new int[m];
			for(i=0;i<m;i++){
		    	basic[i] = n+i+1; // the initialize information of basic seti
		    	System.out.print(basic[i]+ " ");
			}
			for(i=0;i<m+1;i++){
		  		for(j=0;j<m+n+1;j++){
					array[i][j] = 0; // all initialize to 0
		  		}
			} // end of initialization
			System.out.print("\n");
			for(i=1;i<n+1;i++){
				middle = new_scan.next();
				System.out.print(middle+" ");
				array[0][i] = Double.parseDouble(middle);  //parse the values to double
			} // this is used to store the information of subject function
			System.out.print("\n");
			for(i=1;i<m+1;i++){
		   		for(j=0;j<n+1;j++){
					middle = new_scan.next();
					array[i][j] = Double.parseDouble(middle);
		  			// the non-basic information
				}		
		 	array[i][i+n] = 1; // These are the slack variable
			} // have completed the array
			for(i=1;i<m+1;i++){
		   		for(j=1;j<n+1;j++){
					array[i][j] = 0 - array[i][j]; // convert the value of coefficient of non-basic variable 
		    	}
			}
			boolean is_in_basis[] = is_in_basis_array(basic,m,n);
			matrix_to_equations(basic,array,m,n,is_in_basis);		
		return array;
	}

  public static array_and_basic pivot(double array[][],int basic[],int m,int n,int index,int ind_row){
  		int i,j;
  		boolean positive_flag = false ;
  		int leave_variable = 0;
  		if(array[ind_row][index] < (-1)*Math.pow(10,-5)){ // means that this is negative
  			positive_flag = false ;
  		}
  		if(array[ind_row][index] > Math.pow(10,-5)){
  			positive_flag = true ;
  		}
  		for(i=0;i<m+n+1;i++){
			if(i != index)
					array[ind_row][i] = array[ind_row][i]/Math.abs(array[ind_row][index]);
		}
		if(positive_flag == false){
			array[ind_row][index] = 1; // the cross-point of colum and row to be pivoted
		}
outout:
		for(j=1;j<m+n+1;j++){
				if(-Math.pow(10,-5)<array[ind_row][j]&& array[ind_row][j] <Math.pow(10,-5))// if zero
					continue;			
				for(i=0;i<m;i++){ // this is to use the index to replace the variable originally in basis
					if(basic[i] == j){
						array[ind_row][j] = (-1)*array[ind_row][j]; // pumped it out
						basic[i] = index;
						leave_variable = j;
						System.out.print("\nLeaving variable:"+"x"+j+'\n');
						System.out.print("\nEntering variable:"+"x"+index+"\n\n");
						 break outout;
					}					
				}
		}
		//the ind_row has been modified, and we then need to bring it to other functions to eliminate the xi which 
		// would enter the basic set in next pivoting
		if(positive_flag == true){ // this means the pivot is different,and we need to do some fix to what we've done above
									// this is used only when we need to pivot x0 out of basis
			array[ind_row][j] = 1;
			for(i=1;i<m+n+1;i++){
				if(i != index) {
				array[ind_row][i] = (-1)*array[ind_row][i]; // except for the entering variable, the rest all convert their value 				
				}
			}
			array[ind_row][leave_variable] = -1;

		}
		for(i=0;i<m+1;i++){
				for(j=0;j<m+n+1;j++){
					if(i !=ind_row && j !=index){				
						array[i][j] = array[i][j] + array[i][index]*array[ind_row][j];
					}
				}
				if(i != ind_row)
					array[i][index] = 0;
			}
	array_and_basic new_structure = new array_and_basic(array,basic) ;
	return new_structure; 
    }

    public static array_and_basic init_phase1(double array[][], int basic[],int m, int n){
    	// array is a (m+1)*(m+n+1) double 2D array,storing the 
    	// basic is a (m+1) int 1D array
    	int i,j;
    	double[][] array1 = new double[m+1][m+n+2]; // last row is the objective function
    												  // therefore, after the pivotings we will have the new objective function
		int[] basic1 = new int[m];
		for(i=0;i<m;i++){
			basic1[i] = basic[i];
		}
		for(i=0;i<m+n;i++){ // constructing new objective function to be optimized
			array1[0][i] = 0;
		}
		array1[0][m+n+1] = -1; // -X0
    	// above part is to initialize the first row of the dictionary,that is the subjective function
		for(i=1;i<m+1;i++){
			for(j=0;j<m+n+1;j++){
				//System.out.print("\n??????\n");
				array1[i][j] = array[i][j]; // this part is directly copied from the original dictionary
			}
			array1[i][m+n+1] = 1 ; // + X0	
		}
		//after this we have already got the dictionary, which will be used to determine a feasible solution 
		//or to determine if it is infeasible.
		double min = 0;
		int point_row = 0 ; 
		for(i=1;i<m+1;i++){
				if(min > array1[i][0]){
					min = array[i][0];
					point_row = i;
				}
		}
		//we assume that only when we find that b < 0 , we then will call this function, so for sure 
		//after the loop above, we will get min < 0 and point_row > 0
		for(i=0;i<m;i++){
			if(basic1[i] == point_row + n ) {// this means that we find the basic variable,
											   // and will replace it
				 basic1[i] = n+m+1;
				}
		} // this means that x0 enter the basis
		for(i=0;i<point_row+n;i++){
			array1[point_row][i] =  (-1)*array1[point_row][i];// convert
		}
		// plug the equation we get to other equations in this dictionary
		for(i=0;i<m+1;i++){
			if(i == point_row)
				continue;
			for(j=0;j<n+m+1;j++){
					array1[i][j] = array1[i][j] + (array1[i][n+m+1]/array1[point_row][n+m+1])* array1[point_row][j];
			}
			array1[i][n+m+1] = 0; // set to 0
		}
		//so far, we have made the initialized dictionary.
		array_and_basic My_structure = new array_and_basic(array1,basic1);
		System.out.print("\nafter the initialization\n\n");
		boolean is_in_basis[] = is_in_basis_array(basic1,m,n+1);
		for(i=1;i<m+n+2;i++){
				System.out.print(i+" ");
				System.out.print(is_in_basis[i]+"  ");
		}
		System.out.print("\n");
		matrix_to_equations(basic1,array1,m,n+1,is_in_basis);
		return My_structure;
    }
  public static boolean whether_phase1(double array[][],int m){
  			int i;
  			boolean flag = false;
  			for(i=1;i<m+1;i++){
  				if(array[i][0] <-Math.pow(10,-5)){
  					flag = true;
  					break;
  				}
  			}
  			return flag;

  }
  public static array_and_optimal optimized_phase1(double array[][],int basic[],int m,int n,int pivot_mode_flag){//!!!!!!!!!!!!!!!!!!!!!
  	// will return the value of the optimized subjective function
  	//and the value of the optimized function
  	//!!!!!!!!!!!!!!!! notice here the n is different! , n' = n + 1, bacause we add a new X0 in the dictionary!
  	System.out.print("\nThis is to find the optimal value of the phase1 problem\n");
	int num_pivot = 1 ;
	int i,j;
	while(true){
		int index = find_index(n,m,array,basic,pivot_mode_flag);  // index of colum
		//!!!!!!!!!!!!!!!!!!!!!!
		int ind_row = find_ind_row(n,m,array,basic,index); // index of row
		//!!!!!!!!!!!!!!!!!!!!!!
		if(index == 0){
			System.out.print("\nHave found the optimal solution!\n"); // this means the coefficient of obj function are all negative
			break; // go out of the while loop
		}
		if(ind_row == 0){
			System.out.print("\nThis LP problem is unbounded!\n");
			break;
		}
		//so far, we have found the index and ind_row,then can start pivoting
		array_and_basic My_structure = new array_and_basic(array,basic);
		System.out.print("After "+num_pivot+" Pivot\n");
		num_pivot = num_pivot + 1; // increase the # of pivot by 1	
		My_structure = pivot(array,basic,m,n,index,ind_row);
		array = My_structure.array;
		basic = My_structure.basic;	
		boolean is_in_basis[] = is_in_basis_array(basic,m,n);
		matrix_to_equations(basic,array,m,n,is_in_basis);
	} //end of while
	// check if the X0 is still in the basic, here x0 actually is Xn+m+1
	boolean still_in_flag = false;
	for(i=0;i<m;i++){
		if(basic[i] == m+n){//!!!!!!!!!!! This is very important because now n = n+1
			still_in_flag = true;
			break;
		}
	}
	int point_row =0;
	int point_colum = 0;
	if(array[0][0] < -Math.pow(10,-5)){
		System.out.print("This problem is infeasible\n");
	}
	if(still_in_flag && array[0][0] > -Math.pow(10,-5)){  // the X0 is still in the basis
		System.out.print("\nSo still in basis,and we need to use other variable to pivot it out\n");
		for(i=1;i<m+1;i++){ // to find which row is it
				if(array[i][n+m] == 1){ // if it still in the basis, then only one row will array[i][m+n] == 1
					point_row = i;
					break;
				}
		}
		//find the row!!
		//////////////////////////////
		//find the colum can be used the other way to get. will refine it later
		for(i=1;i<n+m;i++){
			if(array[point_row][i] < (-1)*Math.pow(10,-5)||array[point_row][i] > Math.pow(10,-5)){
				point_colum = i;
				break;
			}
		}
		//find the colum.
		//now need to (1) switch the sign of x0 and the x whose coefficient is not zero
		//(2) for row point_row, all the coefficients need to be divided by Math.abs(array[][])
		// and it seems that we can pivot here!
		pivot(array,basic,m,n,point_colum,point_row);
		boolean is_in_basis[] = is_in_basis_array(basic,m,n);
		matrix_to_equations(basic,array,m,n,is_in_basis);
	}
	array_and_optimal My_structure = new array_and_optimal(array,array[0][0],basic); // we know that array[0][0] stores the optimal value
	return My_structure;
  }

  public static void simplex(int n, int m, double array[][],int basic[],int pivot_mode_flag){	
	int num_pivot = 1; // This used to count the number of pivot
	int basic_position;  //find the position of basic variable in each row
	array_and_basic My_structure = new array_and_basic(array,basic);
	array_and_optimal My_new_structure = new array_and_optimal(array,0,basic);
	double array_row0[] = new double[m+n+1]; // objective function
	int i,j;
	for(i=0;i<m+n+1;i++){
		array_row0[i] = array[0][i]; // copy the objective function
	} 	
	boolean feasible_flag = true;
	if(whether_phase1(array,m) == true){
		My_structure = init_phase1(array,basic,m,n);
		array = My_structure.array;
		basic = My_structure.basic;
		//so far has got the initial feasible dictionary, and will start to process this dictionary.
		//////////////////////////////////////////////////////////////////////////
		My_new_structure = optimized_phase1(array,basic,m,n+1,pivot_mode_flag); // NEED TO NOTICE THE WIDTH & LENGTH OF ARRAY

		if(My_new_structure.optimal < (-1)*Math.pow(10,-5)){ // negative optimized value,means this problem is infeasible
			feasible_flag = false;
			return;	
		}
		///////////////////////////////////////////////////////////////////////
		//otherwise the problem is feasible and we need to solve this problem
		for(i=1;i<m+1;i++){ // this is used to copy the part without X0 and objective function
			for(j=0;j<m+n+1;j++){
				array[i][j] = My_new_structure.array[i][j];
			}
		}
		for(i=0;i<m;i++){
			basic[i] = My_new_structure.basic[i]; // copy the basic information
		}
		// now we need to construct the objective function,because we choose that the first copy the original function
		//so now we need to use the copied objective function to construct the new objective function
		for(i=0;i<m+n+1;i++){ // initialize the dictionary
			array[0][i] = array_row0[i]; 
		}

		for(i=1;i<m+1;i++){ //for each equation, we need to find if the the basic variable in that equation 's coefficient
							// is in the objective function.
			int b_position = Find_Basic(basic,m,array[i],m+n+1);
			if(array_row0[b_position]< (-1)*Math.pow(10,-5)||array_row0[b_position]>Math.pow(10,-5)){
					//means the coefficient in the objective is not zero, then we need to plug this in
					for(j=0;j<m+n+1;j++){
						if(j != b_position){
							array[0][j] = array[0][j]+array[i][j]*(array_row0[b_position]/array[i][b_position]);
						}
					}
					array[0][b_position] = 0;
			}
		}
		//so far , we need have a new dictionary and let's see if it works!
		if(feasible_flag){
			System.out.print("\n\n\nso far, we should have a new LP problem to work with!\n\n\n");
			boolean is_in_basis[] = is_in_basis_array(basic,m,n);
			matrix_to_equations(basic,array,m,n,is_in_basis);
		}
	}
	if(!feasible_flag){
		System.out.print("\n We have found that this problem is infeasible \n");
	}
	while(feasible_flag){
		int index = find_index(n,m,array,basic,pivot_mode_flag);  // index of colum
		int ind_row = find_ind_row(n,m,array,basic,index); // index of row
		// this point require some 
		if(index == 0){
			System.out.print("\nHave found the optimal solution!\n"); // this means the coefficient of obj function are all negative
			boolean is_in_basis[] = is_in_basis_array(basic,m,n);
			System.out.print("\nbasis:\n");
			for(i=1;i<m+1;i++){
				int basic_variable = basic[i-1];
				System.out.print("X"+basic_variable+"=");
				System.out.printf("%.2f  ",array[i][0]);
			}
			System.out.print("\nNon-basis:\n");
			for(i=1;i<m+n+1;i++){
				if(is_in_basis[i] == false){
					double zero = 0.0;
					System.out.print("X"+i+"=");
					System.out.printf("%.2f  ",zero);
				}
			}
			System.out.print("\n\n");
			break; // go out of the while loop
		}
		if(ind_row == 0){
			System.out.print("\nThis LP problem is unbounded!\n");
			break;
		}
		//so far, we have found the index and ind_row,then can start pivoting
		System.out.print("After "+num_pivot+" Pivot\n");
		num_pivot = num_pivot + 1; // increase the # of pivot by 1	
		My_structure = pivot(array,basic,m,n,index,ind_row);
		array = My_structure.array;
		basic = My_structure.basic;	
		boolean is_in_basis1[] = is_in_basis_array(basic,m,n);
		matrix_to_equations(basic,array,m,n,is_in_basis1);
		try{
			Thread.sleep(100);

		}catch(InterruptedException e){
			e.printStackTrace();
		}
	} //end of while

  } // end of simplex() method  

  public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int problem_num = 0; // this used to count the number of problems
	int m = 0,n=0; // initialize them to 0
	int i,j;
	int pivot_mode_flag = 0;
	int mode =  Integer.parseInt(args[0]);
	switch(mode){
		case 1:
			System.out.print("\nThe method is smallest subscript\n");
			pivot_mode_flag = 1;
			break;
		case 2:
			System.out.print("\nThe method is largest coefficient\n");
			pivot_mode_flag = 2;
			break;
		case 3:
			System.out.print("\nThe method is maximum increase\n");
			pivot_mode_flag = 3;
			break;
		default:	
			System.out.print("\nNo method indicated,default method is largest coefficients!\n");
			pivot_mode_flag = 2;
			break;
	}
	while(in.hasNext()){ // if the file is not ended
	    problem_num = problem_num+1;
	    System.out.print("*************problem "+problem_num+"************************************\n");
		double[][] array = null;// the 0 numbered row and colum,will be left as zero
		array = read_file(in);
		m = array.length-1;
		n = array[0].length-m-1;
		int[] basic = new int[m];
		for(i=0;i<m;i++){
		    basic[i] = n+i+1; // the initialize information of basic seti
			}
		System.out.print("\n\n");
	    simplex(n,m,array,basic,pivot_mode_flag); // newly added line
		} //end of while loop	
	} //end of the main call
} // end of Simplex class definition
