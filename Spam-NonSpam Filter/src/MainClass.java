import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;


public class MainClass {
	
	private static Map<String,TreeSet<Posting>> invertedIndex; //corpus
	private static	ArrayList<TreeMap<String,Double>> documentList;	//document vector that keeps word's normalized tf-idf scores
		
	private static ArrayList<WordTfIdf> sumOfWordWeightsForSpam;	//keeps sum of tf-idf scores of words in spam training set
	private static ArrayList<WordTfIdf> sumOfWordWeightsForLegitimate;	//keeps sum of tf-idf scores of words in legitimate training set
	
	private static TreeMap<String,Double> centroidOfSpam;	//for keeping centroid of spam mails for rocchio algorithm
	private static TreeMap<String,Double> centroidOfLegitimate;	//for keeping centroid of legitimate mails for rocchio algorithm
	
	private static TreeMap<String,Double> mailVectorForSpam;	//to keep given mail's vector whose words' idf scores taken from spam training set
	private static TreeMap<String,Double> mailVectorForLegitimate;//to keep given mail's vector whose words' idf scores taken from legitimate training set
	
	
	static int tp = 0;	//True positives
	static int tn = 0;	//True negatives
	static int fp = 0;	//false positives
	static int fn = 0;	//false negatives
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		Tokenizer tkn = new Tokenizer(); //initialize tokenizer
		
		/** this function is reading corpus from pre-prepared corpus.txt file
		 * default this part used
		 * **/
		readCorpus();
		tkn.setInvertedIndex(invertedIndex);	
		
		
		/** this functions are for re reading corpus from training set  and writing to file
		 * but it is used for just first time taking corpus
		 * */
		/*
		tkn.read();
		tkn.printMap(tkn.getInvertedIndex());
		invertedIndex = tkn.getInvertedIndex();
		*/
		
		tkn.run();//for taking documents vector
		documentList = tkn.getDocumentList();//get documents vector

		computeTfIdfSumsOfWords();//compute sum of tf-idf sums of words
		
		/**
		 * this part is for taking folder path from user
		 */
		/*
		String pathOfFolder = args[0];
		ArrayList<File> testFileList = new ArrayList<File>(tkn.getFileList(pathOfFolder));
		*/
		
		/**
		 * for calling rocchio algorithm for files given by user
		 */
		
		/*
		 calculateCentrois();//to calculate centroids of spam and legitimate documents
		
		for(int i = 0; i< testFileList.size(); i++){
			int docId = i;
			String filePath = testFileList.get(i).getName();
			
			File file = new File(testFileList.get(i).getAbsolutePath());
			Tokenizer tkn2 = new Tokenizer();
			tkn2.readGivenFile(file, 5000);
			
			computeGetMailVector(tkn2.getInvertedIndex());
			
			rocchioAlgorithm(filePath,docId);
		}*/
		
		/**
		 * for calling kNN algorithm for files given by user
		 */
		/*for(int i = 0; i< testFileList.size(); i++){
			int docId = i;
			String filePath = testFileList.get(i).getName();
			
			File file = new File(filePath);
			Tokenizer tkn2 = new Tokenizer();
			tkn2.readGivenFile(file, 5000);
			
			computeGetMailVector(tkn.getInvertedIndex());
			
			int k = 0; 
			kNNAlgorithm(filePath, k,docId);
		}*/
		
		
		//for reading test set and classifying mail inside folders
		ArrayList<File> givenFileList = new ArrayList<File>(tkn.getFileList("test/spam"));
		givenFileList.addAll(new ArrayList<File>(tkn.getFileList("test/legitimate")));
		
		getMail(givenFileList);
		
	}
	
	/**
	 *  to take given mail list and calling algorithmic operations for it
	 * @param givenFileList
	 * @throws FileNotFoundException
	 */
	private static void getMail(ArrayList<File> givenFileList) throws FileNotFoundException{
		
		for(int i = 0; i< givenFileList.size(); i++){//for each given file

			int docId = i;//this will be used for calculaing tp, tn,fp,fn values for test set. 
			String filePath = givenFileList.get(i).getName();
			
			File file = new File(givenFileList.get(i).getAbsolutePath());
			Tokenizer tkn = new Tokenizer();
			tkn.readGivenFile(file, 5000);	//read given file and get its inverted index
			
			
			computeGetMailVector(tkn.getInvertedIndex());//convert its inverted index to document vector
			
			//for rocchio algorithm
			//rocchioAlgorithm(filePath,docId);
			
			//for kNN algorithm for k =1
			//kNNAlgorithm(filePath, 1,docId);
			
			//for kNN algorithm for k =3
			//kNNAlgorithm(filePath, 3,docId);
			
			//for kNN algorithm for k =5
			//kNNAlgorithm(filePath, 5,docId);
			
			//for kNN algorithm for k =7
			//kNNAlgorithm(filePath, 7,docId);

			//for kNN algorithm for k =9
			kNNAlgorithm(filePath, 9,docId);
		}
		
		System.out.println("tp,tn,fp,fn : "+ tp + "\t"+tn+ "\t"+fp+ "\t"+fn);
		double precision = (double)tp/(tp+fp);
		double recall = (double)tp/(tp+fn);
		double f_measure = (double)2*precision*recall/(precision+recall);
		System.out.println("precision: "+ precision + " recall: " + recall + " f_measure: "+f_measure);

	}
	
	/**
	 * to read corpus from corpus file
	 * @throws Exception	invalid type of corpus exception
	 */
	private static void readCorpus() throws Exception{
		invertedIndex = new TreeMap<String,TreeSet<Posting>>();
		Scanner scn = new Scanner(new File("corpus.txt"));
		
		while(scn.hasNextLine()){//for each term
			if(!scn.hasNext()){
				break;
			}
			String term = scn.next();
			int docFreq = scn.nextInt();
			
			TreeSet<Posting> postingList = new TreeSet<Posting>();
			
			
			for(int i =0; i< docFreq; i++){//for each posting for term
				String small = scn.next();
				if(!small.equals("<")){
					throw new Exception("upps there is an error while reading posting");
				}
				
				int docId = scn.nextInt();
				String coma = scn.next();
				if(!coma.equals(",")){
					throw new Exception("upps there is an error while reading posting");
				}
				
				int termFreq = scn.nextInt();
				
				String greater = scn.next();
				if(!greater.equals(">")){
					throw new Exception("upps there is an error while reading posting greater : " + greater);
				}
				
				Posting post = new Posting();
				post.setDocId(docId);
				post.setTermFrequency(termFreq);
				
				postingList.add(post);
				
			}
			
			invertedIndex.put(term, postingList);//put term with positng list to the invertedindex
		}
		scn.close();
	}


	/**
	 * to compute tf-idf sums of words
	 */
	private static void computeTfIdfSumsOfWords(){
		sumOfWordWeightsForSpam = new ArrayList<WordTfIdf>();
		sumOfWordWeightsForLegitimate = new ArrayList<WordTfIdf>();
		
		ArrayList<String> keySet = new ArrayList<String>(invertedIndex.keySet());
		
		//initialize Lists
		for(int k = 0; k<keySet.size(); k++){
			WordTfIdf wdIdf = new WordTfIdf();
			sumOfWordWeightsForLegitimate.add(wdIdf);
			sumOfWordWeightsForSpam.add(wdIdf);
		}
		
		
		
		//for spam mail documents
		for(int i = 0; i<documentList.size()/2; i++){
			for(int k = 0; k<keySet.size(); k++){
				double tf_idfSum = sumOfWordWeightsForSpam.get(k).getTf_idf();
				TreeMap<String, Double> map = documentList.get(i);//get vector of current document
				double tf_idfDocWord = map.get(keySet.get(k));
				double sum =  tf_idfSum + tf_idfDocWord;
				WordTfIdf wdIdf = new WordTfIdf();
				wdIdf.setTf_idf(sum);
				wdIdf.setWord(keySet.get(k));
				sumOfWordWeightsForSpam.set(k, wdIdf);	//add words tf-idf score
			}
		}
		
		//for legitimate mail documents
		for(int i = documentList.size()/2; i<documentList.size(); i++){
			for(int k = 0; k<keySet.size(); k++){
				double sum = sumOfWordWeightsForLegitimate.get(k).getTf_idf() + documentList.get(i).get(keySet.get(k));//incrase words tf idf score with the tf idf score of it in current document
				WordTfIdf wdIdf = new WordTfIdf();
				wdIdf.setTf_idf(sum);
				wdIdf.setWord(keySet.get(k));
				sumOfWordWeightsForLegitimate.set(k, wdIdf);
			}
		}
		
		//this part is for printing tf-idf total scores of top 20 words
		/*
		Collections.sort(sumOfWordWeightsForSpam);
		Collections.sort(sumOfWordWeightsForLegitimate);
		
		System.out.println("top 20 spam words");
		for(int i = 0; i<20; i++){
			System.out.println(i + " inci spam word : " + sumOfWordWeightsForSpam.get(i).getWord()+ " with score : "+ sumOfWordWeightsForSpam.get(i).getTf_idf());
		}
		
		System.out.println("top 20 legitimate words");
		for(int i = 0; i<20; i++){
			System.out.println(i + " inci legitimate word : " + sumOfWordWeightsForLegitimate.get(i).getWord()+ " with score : "+ sumOfWordWeightsForLegitimate.get(i).getTf_idf());
		}*/
	}
	
	/**
	 * to calculate  centroids of spam and legitimate documetns
	 */
	private static void calculateCentrois(){
		
		centroidOfSpam = new TreeMap<String,Double>();
		centroidOfLegitimate = new TreeMap<String,Double>();
		
		for(int i=0; i< sumOfWordWeightsForSpam.size();i++){//for each spam word divide its total idf value by 240
			WordTfIdf wd = sumOfWordWeightsForSpam.get(i);
			centroidOfSpam.put(wd.getWord(), wd.getTf_idf()/240);
		}
		
		for(int i=0; i< sumOfWordWeightsForLegitimate.size();i++){//for each legitimate word divide its total idf value by 240
			WordTfIdf wd = sumOfWordWeightsForLegitimate.get(i);
			centroidOfLegitimate.put(wd.getWord(), wd.getTf_idf()/240);
		}
	}

	/**
	 * creates mail vectors for given file
	 * @param corpusOfMail
	 */
	private static void computeGetMailVector(Map<String,TreeSet<Posting>> corpusOfMail){
		//initialize vectors
		mailVectorForSpam = new TreeMap<String,Double>();
		mailVectorForLegitimate = new TreeMap<String,Double>();
		
		TreeMap<String, Double> newVector = new TreeMap<String, Double>();
		ArrayList<String> keySet = new ArrayList<String>(invertedIndex.keySet());
		for(int k = 0; k<keySet.size(); k++){//put each word in cospus to vectors with tf-idf score 0
			newVector.put(keySet.get(k), (double)0);
		}
		
		mailVectorForSpam = newVector;
		mailVectorForLegitimate = newVector;
		
		for(Map.Entry<String, TreeSet<Posting>> entry : corpusOfMail.entrySet()){//for each entry in corpus of mail
			String word = entry.getKey();
			
			TreeSet<Posting> docPosting = entry.getValue();
			int termFreq = docPosting.first().getTermFrequency();
			
			double idfForSpam = 0.0;
			double idfForLeg = 0.0;
			if(invertedIndex.containsKey(word)){//if it is in vocabulary then calculate word's tf idf score
				TreeSet<Posting> postingListOfVocWord = invertedIndex.get(word);
				Tokenizer tkn= new Tokenizer();
				int numOfDocSpam = tkn.getNumOfDoc(postingListOfVocWord, 1); //get number of spam doc containing word
				int numOfDocLeg = tkn.getNumOfDoc(postingListOfVocWord, 2); //get number of legitimate doc containing word
				if(numOfDocSpam != 0)	idfForSpam = Math.log10(240/numOfDocSpam);//for spam
				if(numOfDocLeg != 0)	idfForLeg = Math.log10(240/numOfDocLeg);//for leg
				
				//put word to list
				mailVectorForSpam.put(word, termFreq*idfForSpam);
				mailVectorForLegitimate.put(word, termFreq*idfForLeg);
			}
			
			
			
			
		}
	}

	/**
	 * roccihio algorithm
	 * @param filePath	file name of given file
	 * @param docId id of tested document
	 */
	private static void rocchioAlgorithm(String filePath,int docId){
		
		//control similarity and find nearest centroid
		if(getSimilarity(centroidOfSpam,mailVectorForSpam)>getSimilarity(centroidOfLegitimate,mailVectorForLegitimate)){
			System.out.println(filePath + " spam");
			if(docId<240){//if file spam
				tp++;	//then true positive
			}else{
				fp++;	//then false positive
			}
		}else{
			System.out.println(filePath + " legitimate");
			if(docId < 240){
				fn++;	//then false negative
			}else{//if file legitimate
				tn++;	//then true positive
			}
		}
	}
	
	/**
	 * kNN algorithm
	 * @param filePath	file name
	 * @param k	algorithms parameter
	 * @param docId	document id
	 */
	private static void kNNAlgorithm(String filePath,int k,int docId){
		ArrayList<KnnResultClass> resultList  = new ArrayList<KnnResultClass>();//keeps the similarity of document vectors to vector of given mail
		
		for(int i  = 0; i<documentList.size()/2; i++){//for each document in spam set, calculate similarity and put it to list
			double similarity = getSimilarity(documentList.get(i), mailVectorForSpam);
			resultList.add(new KnnResultClass(filePath, 1, similarity));
		}
		
		for(int i  = documentList.size()/2; i<documentList.size(); i++){//for each document in spam set, calculate similarity and put it to list
			double similarity = getSimilarity(documentList.get(i), mailVectorForLegitimate);
			resultList.add(new KnnResultClass(filePath, 2, similarity));
		}
		
		Collections.sort(resultList);//sort list so that we can take top k documents
		
		int totalType = 0;//decrese for spam mails, increase for legitimate mails
		for(int i =0 ; i<k;i++){//for top k documents
			if(resultList.get(i).getType() == 1) totalType --;//spam
			if(resultList.get(i).getType() == 2) totalType ++;//legitimate
		}
		
		
		if(totalType < 0){//spam
			System.out.println(filePath + " spam");
			if(docId<240){//if file spam
				tp++;
			}else{
				fp++;
			}
		}else{//legitimate
			System.out.println(filePath + " legitimate");
			if(docId < 240){
				fn++;
			}else{//if file legitimate
				tn++;
			}
		}
		
	}

	/**
	 * to get similarity between two vectors
	 * @param classCentroid	document vector token from training set
	 * @param givenDoc	given document vector
	 * @return	similarty
	 */
	private static double getSimilarity(TreeMap<String,Double> classCentroid,TreeMap<String,Double> givenDoc){
		double similarity = 0;
		
		ArrayList<String> words = new ArrayList<String>(classCentroid.keySet());
		
		for(int i = 0; i< words.size(); i++){//for each word in corpus
			similarity += classCentroid.get(words.get(i))*givenDoc.get(words.get(i));//get similarity by multiplying normalized tf-idf scores
		}
		
		return similarity;
	}
}

