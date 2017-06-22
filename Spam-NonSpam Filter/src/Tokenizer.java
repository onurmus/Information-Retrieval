import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * this class is designed for taking files, creating corpus, creating vector of documents, and doing tokenization operation
 * @author OnurM
 *
 */
public class Tokenizer {

	private static int NUM_OF_SPAM_MAIL = 240;	//total number of training spam mails
	private static int NUM_OF_LEGITIMATE_MAIL = 240;	//total number of training legitimate mails
	
	//to take the invertedIntex of training set.
	//it keeps words in the format=>  word : { <doc1, termFrequency1>, <doc2,termFrequency1>}
	private Map<String,TreeSet<Posting>> invertedIndex;	
	
	//assign each file name to a document Id.
	//id<240 is spam and id>240 legitimate
	//because we read firstly spam mails
	private Map<String,Integer> fileToDocId;
	
	//keeps documents as vector of terms and their tf-idf values
	//it keeps document in format: {<word1:tf-idf1>, <word2:tf-idf2>,.. }
	private	ArrayList<TreeMap<String,Double>> documentList;
	
	/**
	 * constructor. Initialize invertedIndex and fileToDocId list
	 */
	public Tokenizer(){
		fileToDocId = new HashMap<String,Integer>();
		invertedIndex = new TreeMap<String,TreeSet<Posting>>();
	}
	

	/**
	 * gets files of type txt in a given folder
	 * @param path path of given folder
	 * @return files to be read
	 * @throws FileNotFoundException file not found exception
	 */
	public List<File> getFileList(String path) throws FileNotFoundException{
		File folder = new File(path);
		List<File> fileList = new ArrayList<File>();	//list of files that is ended with txt
		for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile()) {
	            
	            if(fileEntry.getName().endsWith(".txt")){	//for all files that is ended with txt
	            	fileList.add(fileEntry);
	            }
	        }
	    }
		
		return fileList;
	}
	
	/**
	 * this method is used as main method for calculating document vector and tf idf scores inside documents' words
	 * @throws FileNotFoundException e
	 */
	public void run() throws FileNotFoundException{
		initializeDocVector();
		computeTfIdfScores();
		normalizeDocVectors();
	}
	
	/**
	 * to initialize document list. It create a table whose rows are words and columns are documents 
	 */
	private void initializeDocVector(){
		documentList = new ArrayList<TreeMap<String,Double>>();
		for(int i = 0; i < 480; i++){	//for each document
			TreeMap<String, Double> newVector = new TreeMap<String, Double>();
			ArrayList<String> keySet = new ArrayList<String>(invertedIndex.keySet());	//take words in corpus
			for(int k = 0; k<keySet.size(); k++){	//for each word in corpus index
				newVector.put(keySet.get(k), (double)0);
			}
			
 			documentList.add(newVector);
		}
	}
	
	/**
	 * this will call readGivenFile method for all files in trainig set
	 * @throws FileNotFoundException 
	 */
	public void read() throws FileNotFoundException{
		List<File> spamFileList = new ArrayList<File>(getFileList("training/spam"));	//list of spam mails
		List<File> legitimateFileList = new ArrayList<File>(getFileList("training/legitimate"));//list of legitimate mails
		
		List<File> fileList = new ArrayList<File>();
		fileList.addAll(spamFileList);
		fileList.addAll(legitimateFileList);
		
		int docId = 0;//document id of documents. it starts with zero and incremented. so that id<240 becomes spam documents and >240 legitimate ones
		for(File myFile : fileList){//foreach file
			readGivenFile(myFile,docId);
			docId++;
		}
	}
	
	/**
	 * This reads given file and takes all terms one by one and gives term to tokenization operations
	 * @param myFile given file
	 * @param docId	given file determined doc id
	 * @throws FileNotFoundException exception
	 */
	public void readGivenFile(File myFile,int docId) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scn = new Scanner(myFile);
		scn.next(); //subject:
		
		fileToDocId.put(myFile.getName(), docId);//keep document name and corresponding document id
		
		int lastDocId = docId;	//keep the info of what new id is read
		
		while(scn.hasNext()){	//for each new term
			String newTerm = scn.next();
			
			tokenizeTheTerm(newTerm,lastDocId);	//add taken list of words to token list

		}
		
	}
	
	/**
	 * to tokenize and add the token to tokenList
	 * @param term	a new term
	 * @param documentId	the current document id	
	 */
	private void tokenizeTheTerm(String term,int documentId){
		String candidateWord = term;
		candidateWord = tokenize(candidateWord);	//tokenize it
		if(!candidateWord.trim().equals("")){	//if not empty add it to list
			candidateWord = candidateWord.toLowerCase(); //case fold
			if(!candidateWord.equals("")){
				addToCorpus(candidateWord,documentId);
			}
			
		}
	}
	
	/**
	 * this adds given word to corpus
	 * @param term given term
	 * @param docId	given document id
	 */
	private void addToCorpus(String term,int docId){
		if(invertedIndex.containsKey(term)){//if term is in corpus
			Set<Posting> postingList = invertedIndex.get(term);	//get posting list 
			
			Posting tempPost = new Posting();	//create a temp post to control whether posting list contains current doc or not
			tempPost.setDocId(docId);
			tempPost.setTermFrequency(0);
			
			if(postingList.contains(tempPost)){ //if posting list contains current docId
				Iterator<Posting> it = postingList.iterator();
				Posting current;
				while(it.hasNext() ) {	//iterate over posting list
					current = it.next();
					if(current.getDocId() == docId){ //find document and then increase term frequency for document by one
						int occurences = current.getTermFrequency();
						occurences++;
						current.setTermFrequency(occurences);
					}
	
				}
			}else{	//if document is new then add it to posting set
				Posting newPosting = new Posting();
				newPosting.setDocId(docId);
				newPosting.setTermFrequency(1);
				postingList.add(newPosting);
			}

		}else{//if term is not in vocabulary add it to vocabulary
			TreeSet<Posting> postList = new TreeSet<Posting>();//post list for new term
			Posting posting = new Posting(); //posting for new document
			posting.setDocId(docId);	
			posting.setTermFrequency(1);
			postList.add(posting);	//add posting to posting list
			invertedIndex.put(term, postList);	//insert to map
		}
	}
		

	/**
	 * for printing corpus to the corpus.txt 
	 * @param corpus	the inverted index
	 * print each term with the form:  term {postings list: <docId: term frequency>}
	 * @throws FileNotFoundException
	 */
	public void printMap(Map<String,TreeSet<Posting>> corpus) throws FileNotFoundException{
		PrintStream out = new PrintStream(new File("corpus.txt"));
		for(Map.Entry<String, TreeSet<Posting>> entry : corpus.entrySet()){
			String term = entry.getKey();
			out.print(term +" "+entry.getValue().size()+" ");
			for(Posting posting : entry.getValue()){
				out.print("< "+ posting.getDocId() + " , "+ posting.getTermFrequency() + " > ");
			}
			out.print("\n");
		}
		out.close();
	}
	

	/**
	 * to compute tf idf scored of words in documents
	 */
	private void computeTfIdfScores(){
		ArrayList<String> keySet = new ArrayList<String>(invertedIndex.keySet());//take all words in corpus
		
		for(int k = 0; k<keySet.size(); k++){//for each word in inverted intex
			String word = keySet.get(k);
			TreeSet<Posting> postingList = invertedIndex.get(word);	//get posting list
			
			
			//iterate over posting list of word. so that for each document assign the word tf idf scores in it
			Iterator<Posting> it = postingList.iterator(); 
			Posting current;
			while(it.hasNext() ) {	
				current = it.next();
				
				double tf_idf = 0;
				
				double tf = current.getTermFrequency();	//get term frequency of word
				
				double idf = 0;
				
				
				if(tf != 0){//if term frequency is 0 then tf_idf 0 in any way, so do not calculate idf
					if(current.getDocId()<NUM_OF_SPAM_MAIL){//if term is in spam mail
						idf = NUM_OF_SPAM_MAIL / getNumOfDoc(postingList,1);
					}else{//if term is in legitimate mail
						idf = NUM_OF_LEGITIMATE_MAIL / getNumOfDoc(postingList,2);
					}
					
					if (tf > 0){//if tf 0 then we cannot take log
						tf = 1 + Math.log10(tf);
					}
					
					if(idf > 0){//if idf 0 then we cannot take log
						idf = Math.log10(idf);
					}
					
					tf_idf = tf*idf;
				}
				
				documentList.get(current.getDocId()).put(word, tf_idf);//put tf idf score of word to current document's given word

			}

		}
		
	}
	
	/**
	 * to normalize the document vector
	 */
	private void normalizeDocVectors(){
		for(int i = 0; i< documentList.size(); i++){//for each document vector in document vector list
			double totalSquaredSum = 0;
			TreeMap<String,Double> vector = documentList.get(i); //get its words and tf-idf scores
			for(Map.Entry<String, Double> entry : vector.entrySet()){//calculate total squared sum of all words' tf-idf scores
				totalSquaredSum += Math.pow(entry.getValue(), 2); 
			}
			totalSquaredSum = Math.sqrt(totalSquaredSum);//take square root of sum
			for(Map.Entry<String, Double> entry : vector.entrySet()){//update each word's tf-idf score with normalized score
				entry.setValue(entry.getValue()/totalSquaredSum);
			}
		}
	}
	
	/**
	 * this method gives the number of document in a posting list with a given document type
	 * @param postingList given document list
	 * @param type 1: spam and 2: legitimate
	 * @return
	 */
	public int getNumOfDoc(TreeSet<Posting> postingList,int type){
		int numberOfSpam = 0;
		int numberOfLegitimate = 0;
		
		Iterator<Posting> it = postingList.iterator();//iterate over posting lsit
		Posting current;
		while(it.hasNext() ) {	
			current = it.next();
			if(current.getDocId() < NUM_OF_SPAM_MAIL){ //if doc id < 240 then this is spam mail doc
				numberOfSpam++;
			}else{
				numberOfLegitimate++;
			}

		}
		
		if(type == 1){//get spam mails
			return numberOfSpam;
		}else{
			return numberOfLegitimate;
		}
	}
	
	
	/**
	 * this method is for tokenize the token
	 * @param word token
	 * @return tokenized token
	 */
	public String tokenize(String word){
		//remove all hypens from string and concatanate string
		while(word.contains("-")){
			word = word.substring(0,word.indexOf("-"))+word.substring(word.indexOf("-")+1,word.length());
		}
		
		//if the last char of string not a letter or number then remove it
		while(word.length()> 0 && !Character.isLetter(word.charAt(word.length()-1)) && !Character.isDigit(word.charAt(word.length()-1))){
			word = word.substring(0,word.length()-1);
		}
		
		//if the first char of string not a letter or number then remove it
		while(word.length()> 1 && !Character.isLetter(word.charAt(0)) && !Character.isDigit(word.charAt(0))){
			word = word.substring(1,word.length());
		}
		
		//this part is for Clitics 
		if(word.contains("'")){
			if((word.indexOf("'") - word.length())<3 ){ // if it is like I'm, or dog's
				word = word.substring(0,word.indexOf("'"));
			}
		}
				
		return word;	
	}
	
	public Map<String, TreeSet<Posting>> getInvertedIndex() {
		return invertedIndex;
	}

	public void setInvertedIndex(Map<String, TreeSet<Posting>> invertedIndex) {
		this.invertedIndex = invertedIndex;
	}

	public ArrayList<TreeMap<String, Double>> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(ArrayList<TreeMap<String, Double>> documentList) {
		this.documentList = documentList;
	}
	
}
