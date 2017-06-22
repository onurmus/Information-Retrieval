import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * for taking data from reuters21578 folder and constructing a corpus
 * @author OnurM
 *
 */
public class ReadData extends WordOperations{
	
	private Map<TokenClass,Set<Integer>> invertedIndex;	//to take the invertedIntex for printing to corpus.txt
	private ArrayList<TokenClass> tokenList;	//to take the tokenList
	
	/**
	 * constructor of the class
	 */
	public ReadData(){
		try {
			//firstly read stopwords
			readStopWords();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this will run the linguistic operations and saving corpus to a text file 
	 * @throws FileNotFoundException e
	 */
	public void run() throws FileNotFoundException{
		tokenList = new ArrayList<TokenClass>();	//initialize tokenList
		read();	// read the reuters21578 words into the tokenList
		int ans11a = tokenList.size();
		System.out.println("a) tokens, the corpus contain before stopword removal and stemming "+ ans11a);
		Collections.sort(tokenList);	//sort the tokenList 
		Map<TokenClass,Set<Integer>> beforeProccess = getNumOfUniqueTokens();//before linguistic operations get unique tokens
		int ans11c = beforeProccess.size();
		top20Words(beforeProccess); //top 20 frequent words before stopword removal, stemming, and casefolding?
		System.out.println("c) unique tokens,before stopword removal, stemming, and case-folding: "+ ans11c);
		caseFolding();	//casefold all tokens iin tokenList
		removeAndStemCorpus();	//remove stopwords and stem words
		int ans11b = tokenList.size();
		System.out.println("b) tokens, the corpus contain after stopword removal and stemming: "+ ans11b);
		Collections.sort(tokenList);	//sort the new tokenlist, this will help when taking data to invertedindex
		invertedIndex = getNumOfUniqueTokens();	//get data to invertedindex
		int ans11d = invertedIndex.size();
		System.out.println("d) unique tokens,after stopword removal, stemming, and case-folding: "+ ans11d);
		top20Words(invertedIndex);//top 20 frequent words after stopword removal, stemming, and casefolding?
		printMap(invertedIndex);	//print inverted index to corpus
		
	}
	
	/**
	 * this will read all files in reuters21578
	 * @throws FileNotFoundException if not found reuters21578
	 */
	private void read() throws FileNotFoundException{
		File folder = new File("reuters21578");
		List<File> fileList = new ArrayList<File>();	//list of files that is ended with sgm
		for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile()) {
	            
	            if(fileEntry.getName().endsWith(".sgm")){	//for all files that is ended with sgm
	            	fileList.add(fileEntry);
	            }
	        }
	    }
		for(File myFile : fileList){//foreach file
			@SuppressWarnings("resource")
			Scanner scn = new Scanner(myFile);

			int lastNewId = 0;	//keep the info of what new id is read
			
			boolean isProcessContinue = false;	//for reading the line that has no tags
			boolean isProccessedLine = false;	//determines wheteher line was proccessed
			while(scn.hasNextLine()){	//for each new line
				String newLine = scn.nextLine();
				isProccessedLine = false;
				
				if(newLine.contains("<REUTERS")){	//when a new reuter new comes
					if(newLine.contains("NEWID")){	//this will read current new id
						lastNewId = getNewId(newLine);
						isProccessedLine = true;
					}
				}
				
				if(newLine.contains("<TITLE")){	//when a <title> is in the same line with <text>
						String title = "";
						if(newLine.contains("</TITLE")){	//if title starts and ends in one line
							title = newLine.substring(newLine.indexOf(">",newLine.indexOf("<TITLE"+7))+1, newLine.indexOf("</TITLE"));
							isProcessContinue = false;
						}else{	//if title is kept in multi lines
							isProcessContinue = true;
							isProccessedLine = true;
							title = newLine.substring(newLine.indexOf(">",newLine.indexOf("<TITLE"+7))+1, newLine.length());
						}
						addToTokenList(title,lastNewId);	//add taken list of words to token list
						
				}else if(newLine.contains("</TITLE")){ //if this is end for title
					isProcessContinue = false;
					String title = "";
					title = newLine.substring(0, newLine.indexOf("</TITLE"));
					addToTokenList(title,lastNewId);//add taken list of words to token list
				}
				
				if(newLine.contains("<BODY")){	//if a body starts
					String body = "";
					if(newLine.contains("</BODY")){	//if body consist of one line
						isProcessContinue = false;
						body = newLine.substring( newLine.indexOf(">",newLine.indexOf("<BODY"))+1,newLine.indexOf("</BODY"));
					}else{	//if body consist of multiple lines
						isProcessContinue = true;
						isProccessedLine = true;
						body = newLine.substring(newLine.indexOf(">",newLine.indexOf("<BODY"))+1,newLine.length());
					}
					addToTokenList(body,lastNewId);//add taken list of words to token list
				}else if(newLine.contains("</BODY")){	//if a body is ended
					isProcessContinue = false;
					String body = "";
					body = newLine.substring(0, newLine.indexOf("</BODY"));
					addToTokenList(body,lastNewId);	//add taken list of words to token list
				}
				
				if(isProcessContinue == true && isProccessedLine ==false){//if this is a line with no tag
					addToTokenList(newLine,lastNewId);	//add taken list of words to token list
				}
			}
		}
	}
	
	/**
	 * make all the tokens in tokenList lowercased
	 */
	private void caseFolding(){	
		for(int i = 0; i< tokenList.size(); i++){
			tokenList.get(i).setToken(tokenList.get(i).getToken().toLowerCase());
		}
	}
	
	
	/**
	 * remove stop words and stem the tokens
	 */
	private void removeAndStemCorpus(){
		ArrayList<TokenClass> newtokenList = new ArrayList<TokenClass>(); //new list for keeping terms after linguistic operations
		for(int i=0; i<tokenList.size(); i++){
			String token = tokenList.get(i).getToken();
			if(isStopWord(token)){//if token is stop word 
				continue;	//do not add to new list
			}
			
			String stemmedToken=stemm(token);
			if(stemmedToken.equals("")){// if token is empty after stemming
				continue; //do not add to new list
			}else{
				TokenClass tk = new TokenClass();
				tk.setToken(stemmedToken);
				tk.setDocumentId(tokenList.get(i).getDocumentId());
				newtokenList.add(tk);	//add term to new list
			}
			
		}
		
		tokenList = newtokenList;	//convert tokenlist to new token list

	}

	/**
	 * for getting new id from the line
	 * @param line	containing newid
	 * @return
	 */
	private int getNewId(String line){
		int lastNewId = 0;
		int newIdIndex = line.indexOf("NEWID=\"")+7;
		lastNewId = Integer.parseInt(line.substring(newIdIndex,line.indexOf("\"",newIdIndex)));
		return lastNewId;
	}

	/**
	 * to tokenize and add the token to tokenList
	 * @param wordList	a new line
	 * @param documentId	the current new id	
	 */
	private void addToTokenList(String wordList,int documentId){
		wordList = wordList.trim();	//escape white space characters
		String[] myArr = wordList.split("\\s+");	//split according to white space characters
		for(int i = 0; i< myArr.length; i++){	//for each token
			String candidateWord = myArr[i];
			candidateWord = tokenize(candidateWord);	//tokenize it
			if(!candidateWord.trim().equals("")){	//if not empty add it to list
				TokenClass newTok = new TokenClass();
				newTok.setToken(candidateWord);
				newTok.setDocumentId(documentId);
				tokenList.add(newTok);
			}
		}
	}
	
	/**
	 * to get a sorted and mapped list of tokenList
	 * note that tokenList is sorted before calling that method
	 * @return a treeMap like invertedIndex, actually after linguistic processing it return inverted index
	 */
	private Map<TokenClass,Set<Integer>> getNumOfUniqueTokens(){
		TreeMap<TokenClass,Set<Integer>> map = new TreeMap<TokenClass,Set<Integer>>();	//returned treemap
		for(TokenClass token : tokenList){	//for each token
			boolean isMapContainToken = false;
			
			Map.Entry<TokenClass,Set<Integer>> entry = map.lastEntry();	//for the last entry of map because both map and tokenlist is sorted
			if(entry !=null && entry.getKey().getToken().equals(token.getToken())){//if map contains word
				//get posting list
				Set<Integer> docIds = new TreeSet<Integer>(); //is tree set both for sorting and uniqueness of the document ids
				docIds = entry.getValue();
				docIds.add(token.getDocumentId());
				//get token and create new one with new posting list and incremented frequency
				TokenClass mapToken = entry.getKey();
				mapToken.setFrequency(docIds.size());
				map.put(mapToken, docIds);	//add it to map
				isMapContainToken = true;
				//break;
			}
			
			if(isMapContainToken == false){//if map does not contain the token
				//create a new posting list
				Set<Integer> docIds = new TreeSet<Integer>();
				docIds.add(token.getDocumentId());
				//create a new token with frequency 
				TokenClass mapToken = new TokenClass();
				mapToken.setFrequency(1);
				mapToken.setToken(token.getToken());
				map.put(mapToken, docIds);//add it to map
			}
			
		}
		return map;
	}

	/**
	 * for printing top 
	 * @param corpus most frequent words
	 */
	private void top20Words(Map<TokenClass,Set<Integer>> corpus){
		Set<TokenClass> frequencylist = new TreeSet<TokenClass>(new FrequencyComparator());//we use frequency as document id for TokenClass for counting. frequency list use frequency as comparator
		
		for(Map.Entry<TokenClass, Set<Integer>> entry : corpus.entrySet()){	//for each term in corpus
			TokenClass termWithFreq = entry.getKey();
			frequencylist.add(termWithFreq);	//add term with its frequency to the frequency set which orders according to frequency
		}
		System.out.println("top 20 word");
		@SuppressWarnings("rawtypes")
		Iterator iter = frequencylist.iterator();
		int counter = 1 ;
		while(iter.hasNext() && counter < 21){	//prints the top 20 wordss
			TokenClass tk = (TokenClass) iter.next();
			System.out.println(tk.getToken()+" withfreq: "+tk.getFrequency());
			counter++;
		}
	}

	/**
	 * for printing corpus to the corpus.txt 
	 * @param corpus	the inverted index
	 * print each term with the form:  term frequency {postings list}
	 * @throws FileNotFoundException
	 */
	private void printMap(Map<TokenClass,Set<Integer>> corpus) throws FileNotFoundException{
		PrintStream out = new PrintStream(new File("corpus.txt"));
		for(Map.Entry<TokenClass, Set<Integer>> entry : corpus.entrySet()){
			TokenClass termWithFreq = entry.getKey();
			out.print(termWithFreq.getToken() +" "+termWithFreq.getFrequency()+" ");
			for(Integer docIds : entry.getValue()){
				out.print(docIds + " ");
			}
			out.print("\n");
		}
		out.close();
	}
	
}
