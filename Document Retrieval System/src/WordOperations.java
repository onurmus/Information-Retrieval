import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class WordOperations {
	
	public Set<String> stopWords;	//keeps the set of stopwords  as treeset
	
	/**
	 * constructor
	 */
	public WordOperations(){
		
	}
	
	/**
	 * read stop words from stopwords.txt file
	 * @throws FileNotFoundException
	 */
	public void readStopWords() throws FileNotFoundException{
		
		File myFile = new File("stopwords.txt");
		@SuppressWarnings("resource")
		Scanner scn = new Scanner(myFile);
		stopWords =new TreeSet<String>();
		while(scn.hasNext()){
			String a = scn.next();
			stopWords.add(a);
		}
	}
	
	/**
	 * controls whether a given token is a stop word or not
	 * @param word given token
	 * @return result
	 */
	public boolean isStopWord(String word){
		for(int i=0; i<stopWords.size();i++){
			if(stopWords.contains(word.toLowerCase().trim())){
				return true;
			}
		}
		return false;
	}
	
	public String tokenize(String word){
		//if string contains html codes in it, then remove all 
		while(word.contains("&") && ((word.charAt(word.indexOf("&")+2)+"").equals(";")||(word.charAt(word.indexOf("&")+3)+"").equals(";"))){
			word = word.substring(0,word.indexOf("&"))+ word.substring(word.indexOf(";",word.indexOf("&"))+1,word.length());
		}
		
		//remove all hypens from string and concatanate string
		while(word.contains("-")){
			word = word.substring(0,word.indexOf("-"))+word.substring(word.indexOf("-")+1,word.length());
		}
		
		//if the last char of string not a letter then remove it
		while(word.length()> 0 && !Character.isLetter(word.charAt(word.length()-1)) && !Character.isDigit(word.charAt(word.length()-1))){
			word = word.substring(0,word.length()-1);
		}
		
		//if the first char of string not a letter then remove it
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
	
	/**
	 * for stemming a token. Uses porter stemmer
	 * @param word token
	 * @return
	 */
	public String stemm(String word){
		Stemmer s = new Stemmer();
		for(int i =0 ; i<word.length(); i++){
			s.add(word.charAt(i));
		}
		s.stem();
		return s.toString();
		
	}
	
}
