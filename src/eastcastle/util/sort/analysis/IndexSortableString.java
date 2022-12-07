package eastcastle.util.sort.analysis;

import java.util.concurrent.ThreadLocalRandom;

import eastcastle.util.sort.IndexSortable;
import eastcastle.util.sort.StringSortIndexer;

/**
 * This class is for testing of IndexSortable only
 */
public class IndexSortableString implements IndexSortable<IndexSortableString> {
	private final int		sortIndex;
	private final String	s;
	
	public IndexSortableString(String s) {
		this.s = s;
		this.sortIndex = StringSortIndexer.instance.sortIndex(s);
	}

	@Override
	public int compareTo(IndexSortableString o) {
		if (this.sortIndex == o.sortIndex) {
			return this.s.compareTo(o.s);
		} else {
			return this.sortIndex > o.sortIndex ? 1 : -1;
		}
	}

	@Override
	public int sortIndex() {
		return sortIndex;
	}
	
	@Override
	public String toString() {
		return s;
	}

	public static IndexSortable random(int length) {
		byte[]	b;
		
		b = new byte[length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte)('a' + ThreadLocalRandom.current().nextInt(0, 26));
		}
		return new IndexSortableString(new String(b));
	}
	
	public static void main(String[] args) {
		System.out.println(random(4));
	}
}
