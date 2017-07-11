package cn.style.address;


import java.util.Comparator;

public class UploadPhoneComparator implements Comparator<UploadPhone> {
	public int compare(UploadPhone o1, UploadPhone o2) {
		if ("#".equals(o2.getSortLetters())) {
			return -1;// o1 < o2
		} else if ("#".equals(o1.getSortLetters())) {
			return 1;// o1 > o2
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
