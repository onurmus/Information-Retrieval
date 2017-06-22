import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * this class is for reading the file
 * @author OnurM
 *
 */
public class ReadFile {
	private ArrayList<String> verticesList; //list of vertices names
	private Matrix transitionProbability;	//transition probability matrix
	private String filePath;	
	
	public ReadFile(String filePath){
		verticesList = new ArrayList<String>();
		this.filePath = filePath;
	}
	
	/**
	 * this methods reads the file and put its contents on transition probability matrix
	 * file format assumed as your description.
	 * @throws Exception
	 */
	public void read() throws Exception{
		File fileReader = new File(filePath);
		Scanner scn = new Scanner(fileReader);
		
		String vertices = scn.next();
		if(!vertices.equals("*Vertices")){
			throw new Exception("File format is not suitable");
		}
		
		int numOfvertices = scn.nextInt();//take number oof vertices
		
		transitionProbability = new Matrix(numOfvertices,numOfvertices); //initialize matrix
		
		for(int i = 0; i< numOfvertices; i++){//for each vertex add it to vertices list
			int verticeOrder = scn.nextInt();
			String vertice = scn.next();
			vertice = vertice.substring(1, vertice.length()-1);
			verticesList.add(vertice);
			if(verticesList.size() != verticeOrder){
				throw new Exception("Please give vertice number as ordered");
			}
		}
		
		String edges = scn.next();
		if(!edges.equals("*Edges")){
			throw new Exception("File format is not suitable");
		}
		
		while(scn.hasNextLine()){//take each path between vertices.
			if(scn.hasNextInt()){
				int vertice1 = scn.nextInt();
				int vertice2 = scn.nextInt();
				
				transitionProbability.getData()[vertice1-1][vertice2-1] = 1; //mark related positions in matrix as 1
				transitionProbability.getData()[vertice2-1][vertice1-1] = 1;
			}
		}
		
	}



	public Matrix getTransitionProbability() {
		return transitionProbability;
	}

	public void setTransitionProbability(Matrix transitionProbability) {
		this.transitionProbability = transitionProbability;
	}

	public ArrayList<String> getVerticesList() {
		return verticesList;
	}

	public void setVerticesList(ArrayList<String> verticesList) {
		this.verticesList = verticesList;
	}

	
}
