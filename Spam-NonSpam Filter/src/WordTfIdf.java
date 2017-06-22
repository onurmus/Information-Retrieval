import java.util.Comparator;


/**
 * this class is designed to keep a word's tf-idf value
 * it has string word field and tf_idf fields as needed
 * @author OnurM
 *
 */
public class WordTfIdf implements Comparator<WordTfIdf>,Comparable<WordTfIdf>{
	private String word;
	
	private Double tf_idf;
	
	public WordTfIdf(){
		word="";
		tf_idf = 0.0;
	}

	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public Double getTf_idf() {
		return tf_idf;
	}

	public void setTf_idf(Double tf_idf) {
		this.tf_idf = tf_idf;
	}


	public int compareTo(WordTfIdf arg0) {
		// TODO Auto-generated method stub
		return this.compare(this, arg0);
	}


	public int compare(WordTfIdf arg0, WordTfIdf arg1) {
		// TODO Auto-generated method stub
		Double freq1 = arg0.getTf_idf();
		Double freq2 = arg1.getTf_idf();

		if (freq1 < freq2) return 1;
        if (freq1 > freq2) return -1;
        return 0;
	}
	
	
}
