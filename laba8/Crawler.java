import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

	private int threads;
	private URLPool p;

	public Crawler(String url, int depth, int threads) {
		this.threads = threads;
		p = new URLPool(depth);
		p.put(new URLDepthPair(url, 0));
	}

	private void runTask() {
		for (int i = 0; i < threads; i++) {
			CrawlerTask crawler = new CrawlerTask(p);
			Thread thread = new Thread(crawler);
			thread.start();
		}

		while (p.getCounter() != threads) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("»гнорирование непредвиденного прерывани€ - " + e.getMessage());
			}
		}
		p.printPair();
	}

	public static void main(String[] args) throws Exception, IOException {

		if (args.length != 3) {
			throw new Exception("Correct format: java Crawler <URL> <depth> <threads>");
		}
		int maxSize = 0;
		int numOfThreads = 0;
		try {
			maxSize = Integer.parseInt(args[1]);
			numOfThreads = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
			System.err.println("Sec argument must be an integer.");
			System.exit(1);
		}

		if (!args[0].substring(0, 7).equals("http://")) {
			throw new Exception("First arg must be a link");
		}
		Crawler crawler = new Crawler(args[0], maxSize, numOfThreads);
		crawler.runTask();
		System.exit(1);
	}
}
