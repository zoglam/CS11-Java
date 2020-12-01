import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

	public static int lim = 0;
	public static LinkedList<URLDepthPair> waitingList = new LinkedList<URLDepthPair>();
	public static LinkedList<URLDepthPair> visitedList = new LinkedList<URLDepthPair>();

	public static void urlSearch(URLDepthPair linkAndDepth)
			throws UnknownHostException, MalformedURLException, IOException {
		try (Socket mySocket = new Socket(linkAndDepth.hostNameString(), 80)) {
			mySocket.setSoTimeout(5000);
			try (PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true)) {
				out.println("GET " + linkAndDepth.pathNameString() + " HTTP/1.1");
				out.println("Host: " + linkAndDepth.hostNameString());
				out.println("Connection: close");
				out.println();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()))) {
					String line;
					while ((line = in.readLine()) != null) {
						int nextDepth = linkAndDepth.getDepth() + 1;
						String pattRez = patternSearch(line);
						boolean isContains = false;
						if (pattRez != null && nextDepth < lim) {
							isContains = isContainsList(waitingList, pattRez);
							if (!isContains) {
								isContains = isContainsList(visitedList, pattRez);
							}
							if (!isContains) {
								waitingList.add(new URLDepthPair(pattRez, nextDepth));
							}
						}
					}
				}
			}
		}
	}

	public static boolean isContainsList(LinkedList<URLDepthPair> obj, String str) {
		for (URLDepthPair i : obj) {
			if (i.getURL().equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static String patternSearch(String line) {
		// href=\"(http[^\"]+)\"
		Pattern patt = Pattern.compile("href=\"http[^\"]+\"");
		Matcher match = patt.matcher(line);
		if (match.find()) {
			return (line.substring(match.start() + 6, match.end() - 1));
		} else
			return null;
	}

	public static void main(String[] args) throws Exception, IOException {

		if (args.length != 2) {
			throw new Exception("Ïðàâèëà âûçîâà: java Crawler <URL> <ãëóáèíà ïîèñêà>");
		}

//		int lim = 0;

		try {
			// Parse the string argument into an integer value.
			lim = Integer.parseInt(args[1]);
		} catch (NumberFormatException nfe) {
			// The first argument isn't a valid integer. Print
			// an error message, then exit with an error code.
			System.out.println("Sec argument must be an integer.");
			System.exit(1);
		}

		if (!args[0].substring(0, 7).equals("http://")) {
			throw new Exception("First arg must be a link");
		}

//		LinkedList<URLDepthPair> waitingList = new LinkedList<URLDepthPair>();
//		LinkedList<URLDepthPair> visitedList = new LinkedList<URLDepthPair>();

		waitingList.add(new URLDepthPair(args[0], 0));

		while (!waitingList.isEmpty()) {
			URLDepthPair nextPair = waitingList.poll();
			urlSearch(nextPair);
			visitedList.add(nextPair);
		}

		for (URLDepthPair i : visitedList) {
			System.out.println(i);
		}

	}
}
