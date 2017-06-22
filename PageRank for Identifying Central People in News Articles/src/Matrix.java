
final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // copy constructor
    private Matrix(Matrix A) { this(A.data); }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if ((A.data[i][j] - B.data[i][j])>0.00000001) return false; //I assign 10^-8 error for my code for the sake of execution time 
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }


    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.printf("%9.8f ", data[i][j]);
            System.out.println();
        }
    }

    /**
     * this method convert matrix to transition probability matrix
     * @param teleportation teleportation value
     */
    public void convertTransitionProbability(double teleportation){
    	
    	for (int i = 0; i < M; i++) {//for each row
            double[] temp = data[i];	//take row
            double[] newRow = new double[N];	//new row
            int numOfOnesInRow = getNumOfOnes(temp); //get total num of ones in row
            if(numOfOnesInRow == 0){//If a row has no 1, then replace each element by teleportation/N;
            	for(int k = 0; k<N; k++){
            		newRow[k] = (double)teleportation/N;
            	}
            }else{//if row has 1s
            	for(int k = 0; k<N; k++){ // replace each element by teleportation/N
            		newRow[k] = (double)teleportation/N;
            	}
            	for(int k = 0; k<N; k++){// add remaining points 1-teleportation to each element that contains 1
            		if(temp[k] == 1){
            			newRow[k] += (double)(1-teleportation)/numOfOnesInRow; 
            		}
            	}
            }
            data[i] = newRow;//change row with new one
        }
    }
    
    //returns number of ones in a row
    private int getNumOfOnes(double[] row){
    	int total = 0;
    	for(int i = 0; i<row.length; i++){
    		if(row[i] == 1){
    			total++;
    		}
    	}
    	return total;
    }
    
    public double[][] getData(){
    	return this.data;
    }
    
}