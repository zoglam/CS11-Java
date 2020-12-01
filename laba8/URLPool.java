import java.util.LinkedList;

public class URLPool {
	public int maxSize;
	private int counter;
	private LinkedList<URLDepthPair> waitingList;
	private LinkedList<URLDepthPair> visitedList;

	public URLPool(int maxSize) {
		this.counter = 0;
		this.maxSize = maxSize;
		this.waitingList = new LinkedList<URLDepthPair>();
		this.visitedList = new LinkedList<URLDepthPair>();
	}

	public synchronized int getCounter() {
		return counter;
	}

	public synchronized void printPair() {
		for (URLDepthPair i : visitedList) {
			System.out.println(i.getURL() + " " + i.getDepth());
		}
	}

	public synchronized boolean isContainsList(LinkedList<URLDepthPair> obj, String str) {
		for (URLDepthPair i : obj) {
			if (i.getURL().equals(str)) {
				return true;
			}
		}
		return false;
	}

	public void put(URLDepthPair obj) {
		synchronized (waitingList) {
			boolean isContains = false;
			isContains = isContainsList(waitingList, obj.getURL());
			if (!isContains) {
				isContains = isContainsList(visitedList, obj.getURL());
			}
			if (!isContains) {
				if (obj.getDepth() < maxSize) {
					waitingList.addLast(obj);
					waitingList.notify();
				}
				visitedList.addLast(obj);
			}
		}
	}

	public URLDepthPair get() {
		URLDepthPair item = null;
		synchronized (this) {

			while (waitingList.size() == 0) {
				counter++;
				try {
					wait();
				} catch (Exception e) {
					System.err.println(e);
				}
				counter--;
			}

			item = waitingList.removeFirst();
		}
		return item;
	}
}
