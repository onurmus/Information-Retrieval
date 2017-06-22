import java.util.Comparator;

/**
 * this class is used for two purposes
 * 1) for keeping a term and its frequency
 * 2) for keeping a word and the document id where it belongs
 * @author OnurM
 *
 */
public class TokenClass implements Comparator<TokenClass>,Comparable<TokenClass>{

    private String token;	//token or term
    private int docIdOrFreq;	//document id or frequency of the token
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getDocumentId() {
		return docIdOrFreq;
	}
	public void setDocumentId(int documentId) {
		this.docIdOrFreq = documentId;
	}
	
	public int getFrequency(){
		return docIdOrFreq;
	}
	public void setFrequency(int frequency){
		this.docIdOrFreq = frequency;
	}
	public int compareTo(TokenClass arg0) {
		// TODO Auto-generated method stub
		return this.token.compareTo(arg0.getToken());
	}
	public int compare(TokenClass arg0, TokenClass arg1) { //we compare this class according to token or term strings
		return arg0.compareTo(arg1);
	}
	
}