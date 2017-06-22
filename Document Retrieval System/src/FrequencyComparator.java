import java.util.Comparator;


/**
 * this class used as frequency comparator
 * when we want to order list according to term frequencies, it is used
 * @author OnurM
 *
 */
public 	class FrequencyComparator implements Comparator<TokenClass>{

	public int compare(TokenClass arg0, TokenClass arg1) {
		// TODO Auto-generated method stub
		Integer freq1 = arg0.getFrequency();
		Integer freq2 = arg1.getFrequency();
		return -freq1.compareTo(freq2);
	}
	
}
