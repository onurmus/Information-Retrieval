import java.util.Comparator;

/**
 * This class for keeping kNN algorithm results for a document.
 * it keeps document name, type and similarity amount of document to given tested document
 * It is comparator is its similarity because for the top k similar elements decided using similariy field
 * @author OnurM
 *
 */
public class KnnResultClass implements Comparator<KnnResultClass>,Comparable<KnnResultClass>{
	private String docName;
	private int type; //1: for spam and 2: for legitimate
	private Double similarity;
	
	public KnnResultClass(String docName, int type,Double similarity ){
		this.docName = docName;
		this.type = type;
		this.similarity = similarity;
	}
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public int compareTo(KnnResultClass arg0) {
		// TODO Auto-generated method stub
		return this.compare(this, arg0);
	}

	public int compare(KnnResultClass arg0, KnnResultClass arg1) {
		// TODO Auto-generated method stub
		Double sim1 = arg0.getSimilarity();
		Double sim2 = arg1.getSimilarity();

		if (sim1 < sim2) return 1;
        if (sim1 > sim2) return -1;
        return 0;
	}
	
	
	
}
