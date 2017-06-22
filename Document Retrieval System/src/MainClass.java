import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainClass {
	
	private static Map<TokenClass,Set<Integer>> invertedIndex; //inverted index that is taken from corpus.txt file
	
	/**
	 *  main method of the project
	 * @param args
	 * @throws FileNotFoundException when not found files
	 */
	public static void main(String[] args) throws FileNotFoundException {
		try {
			readCorpus();	//firstly read the inverted index from corpus.txt file
			AppUI window = new AppUI();	//initialize user interface
			window.open();	//open user interface
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * for reading corpus to inverted index
	 * @throws FileNotFoundException when not found file
	 */
	public static void readCorpus() throws FileNotFoundException{
		Scanner scn = new Scanner(new File("corpus.txt"));	
		invertedIndex = new TreeMap<TokenClass,Set<Integer>>(); //initalize inverted index as treemap for sorted version
		//read data from corpus.txt file line by line. each line is a dictionary,frequency of term and regarding posting list
		while(scn.hasNextLine()){
			if(!scn.hasNext()){
				break;
			}
			String term = scn.next();
			int freq = scn.nextInt();
			TokenClass tk = new TokenClass();
			tk.setToken(term);
			tk.setFrequency(freq);
			Set<Integer> docIds = new TreeSet<Integer>();	//doc ids should be a treeset so that they can be in order
			for(int i=0;i<freq; i++){
				Integer docId = scn.nextInt();
				docIds.add(docId);
			}
			invertedIndex.put(tk, docIds);
		}
		scn.close();
	}

	/**
	 * when user enter query and searches this method is called
	 * @param query	the query user enters
	 * @throws FileNotFoundException	e
	 */
	public static Set<Integer> getTheResult(String query) throws FileNotFoundException{
		ArrayList<String> tokens = new ArrayList<String>();	//token list that is taken from user query
		int typeOfQuery = -1; //0: for the "and" queries, 1: for "or" queries
		if(query.toLowerCase().contains("and") && query.toLowerCase().contains("or")){//if query contains both of "or" and "and" then skip that
			return null;
		}else if(query.toLowerCase().contains("and")){	//if query contains just "and" operator
			typeOfQuery = 0;
			tokens = new ArrayList<String>(Arrays.asList(query.split("AND")));	//take tokens to tokenlist

		}else if(query.toLowerCase().contains("or")){	//if query contains just "or" operator
			typeOfQuery = 1;
			tokens = new ArrayList<String>(Arrays.asList(query.split("OR")));	//take tokens to tokenlist

		}else{//assuming this is a search for just for a word
			typeOfQuery = 0;
			tokens.add(query.trim());
		}
		
		WordOperations wo = new WordOperations();	//initialize WordOperations for tokenization and linguistic operations 
		wo.readStopWords();	//read stop words
		for(int i=0; i<tokens.size(); i++){	//for each token
			String token = tokens.get(i);
			
			token = wo.tokenize(token);	//tokenize it
			token = token.toLowerCase();	//case-folding
			if(wo.isStopWord(token)){	// if it is stop word then remove it
				tokens.remove(i);
				i--;
				continue;
			}
			token = wo.stemm(token);	//stemm the word
			if(token.equals("")){// if token is empty after stemming, remove it
				tokens.remove(i);
				i--;
				continue;
			}
			
			tokens.set(i, token) ;	//update as new token
			
		}
		
		Map<TokenClass,Set<Integer>> searchIndex = new TreeMap<TokenClass,Set<Integer>>();	//this will keep the searched terms as inverted index just for them
		
		for(int i=0; i<tokens.size(); i++){
			String token = tokens.get(i);
									
			TokenClass tk = new TokenClass();
			tk.setToken(token);
			if(invertedIndex.containsKey(tk)){	//if the words contained then add it to searchIndex
				Set<Integer> docIds = invertedIndex.get(tk);
				tk.setFrequency(docIds.size());
				searchIndex.put(tk, docIds);
			}else{	//if the words not contained in corpus then add it to searchIndex
				if(typeOfQuery == 0){ //if it is a "AND" search then return since there is no result
					return null;
				}
			}
		}
		Set<Integer> answers = new TreeSet<Integer>();	//this is the answer that contains document ids
		List<Set<Integer>> docIdsListSet = new ArrayList<Set<Integer>>();	//this will keep the postings list of the searched terms
		for(Map.Entry<TokenClass,Set<Integer>> entry : searchIndex.entrySet()){
			docIdsListSet.add( entry.getValue());
		}
		
		Collections.sort(docIdsListSet, new comp());	//sort the posting list's list according to their length because we start operations from smaller lists
		if(docIdsListSet.size() > 1){	//if there is more then one list then union or intersect them from starting lower length list to upper one
			Set<Integer> entry1 = null;
			for(Set<Integer> entry2: docIdsListSet){
				if(entry1 == null) entry1 = entry2;
				else{
					if(typeOfQuery == 0) entry1 = intersect(entry1,entry2);	//if this is an "and" operation intersect lists
					else if(typeOfQuery == 1) entry1 = union(entry1,entry2); //if this is an "or" operation union lists
				}
			}
			answers = new TreeSet<Integer>(entry1);
		}else if(docIdsListSet.size() == 1){	//if there is just one word then add it directly to answer
			for(Set<Integer> entry: docIdsListSet){
				answers = new TreeSet<Integer>(entry);
			}
		}
		
		return answers;
		
	}
	
	/**
	 * intersect function for two posting lists
	 * @param p1Set	first term's posting list
	 * @param p2Set	second term's posting list
	 * @return	intersection of two sets
	 */
	public static Set<Integer> intersect(Set<Integer> p1Set, Set<Integer> p2Set){
		Set<Integer> answers = new TreeSet<Integer>();	//answer that will be returned. TreeSet chosen for the purpose of ordering
		ArrayList<Integer> p1 = new ArrayList<Integer>(p1Set);	//convert sets to list for iterating easily
		ArrayList<Integer> p2 = new ArrayList<Integer>(p2Set);
	
		int p1Iter = 0;	//index pointer for first list
		int p2Iter = 0;	//index pointer for second list
		
		while(p1Iter < p1.size() && p2Iter < p2.size()){	//while any of the lists not ended
			int docId1 = p1.get(p1Iter);	//take the elements in regarding positions
			int docId2 = p2.get(p2Iter);
			
			if(docId1 == docId2){	//if the document id s are same then add it to answer list and increment pointers
				answers.add(docId1);
				p1Iter++;
				p2Iter++;
			}else{
				if(docId1 < docId2){	//if document id 1 is smaller than 2 then continue on first list  
					p1Iter++; 
				}else{
					p2Iter++;			//if document id 2 is smaller than 1 then continue on second list  
				}
			}
		}
		return answers;
	}
	
	/**
	 * this will union the two sets for "or" operation
	 * @param p1Set	first term's posting list
	 * @param p2Set	second tesm's posting list
	 * @return unionized sets
	 */
	public static Set<Integer> union(Set<Integer> p1Set, Set<Integer> p2Set){
		Set<Integer> answers = new TreeSet<Integer>();	//answer that is returned. by doing treelist, we can avoid same document ids in one set
		ArrayList<Integer> p1 = new ArrayList<Integer>(p1Set);	//convert sets to list for iterating easily
		ArrayList<Integer> p2 = new ArrayList<Integer>(p2Set);
	
		for(int i=0; i< p1.size(); i++){	//add each document id of first term to answer
			answers.add(p1.get(i));
		}
		for(int i=0; i< p2.size(); i++){	//add each document id of first term to answer
			answers.add(p2.get(i));
		}
		return answers;	
	}
	
}

/**
 * comparator class for using frequencies of terms as comparator, by calculating them from their postings lists length
 * @author OnurM
 *	implements Comparator class
 */
class comp implements Comparator<Set<Integer>>{
	public int compare(Set<Integer> arg0, Set<Integer> arg1) {
		// TODO Auto-generated method stub
		Integer freq1 = arg0.size();
		Integer freq2 = arg1.size();
		return -freq1.compareTo(freq2);
	}
}
