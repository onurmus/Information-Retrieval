import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class PageRank {
	
	static Matrix transitionProbability;
	static ArrayList<String> verticesList;
	final static float teleportation = (float) 0.15;
	static Matrix probRowVector;
	static int numOfVertices;
	
	public static void main(String[] args) throws Exception{
		ReadFile readFile = new ReadFile(args[0]);	//take args[0] as filepath 
		readFile.read();	//read file
		transitionProbability = readFile.getTransitionProbability(); //take matrix
		verticesList = readFile.getVerticesList();	//take vertex list
		transitionProbability.convertTransitionProbability(teleportation);	//transform matrix to corresponding to transition probability matrix with transportation
		numOfVertices = transitionProbability.getData().length;	//get number of vertices
		
		probRowVector = new Matrix(1,numOfVertices);	//create probability row matrix x
		initializeProbRowVector();	//initiliaze x
		Matrix oldRowVector = new Matrix(probRowVector.getData());// keep row vector as oldRowVEctor
		
		probRowVector = probRowVector.times(transitionProbability);	//for first time calculate new probability row vector x
		
		while(!oldRowVector.eq(probRowVector)){	//while old row vector not equals new one, continue
			oldRowVector = probRowVector;	//keep new as old
			probRowVector = probRowVector.times(transitionProbability);	//calculate new row vector
			
		}
		
		//create a sorted set with id of edges and their corresponding probabilities. Use probability as comparator
		SortedSet<Map.Entry<Integer, Double>> sortedset = new TreeSet<Map.Entry<Integer, Double>>(
	            new Comparator<Map.Entry<Integer, Double>>() {
	                public int compare(Map.Entry<Integer, Double> e1,Map.Entry<Integer, Double> e2) {
	                    return -1*e1.getValue().compareTo(e2.getValue());
	                }
	            });
		
		SortedMap<Integer, Double> topFifty = new TreeMap<Integer, Double>();//for keeping top 50
		
		for(int i = 0; i< numOfVertices; i++){//add top 50 to topFifty map
			topFifty.put(i, probRowVector.getData()[0][i]);
		}
		
		sortedset.addAll(topFifty.entrySet());//add all items to sortedset so that we sort set automatically and gain top 50
		
		Iterator<Map.Entry<Integer, Double>> it = sortedset.iterator();
		
		int counter = 0;
		while(counter < 50){//print top 50 one
			Map.Entry<Integer, Double> entry = it.next();
			System.out.println((counter+1)+". "+verticesList.get(entry.getKey()));
			counter++;
		}
		
	}
	
	/**
	 * initializes the row vector
	 * assigns 1/numberofvertices equal probability to each vertex
	 */
	private static void initializeProbRowVector(){
		for(int i = 0; i< numOfVertices; i++){
			probRowVector.getData()[0][i]= (double)1/numOfVertices;
		}
	}

}
