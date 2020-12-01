import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTask implements Runnable {

	private URLPool p1;

	public CrawlerTask(URLPool p) {
		this.p1 = p;
	}

	public static String patternSearch(String line) {
		Pattern patt = Pattern.compile("href=\"http[^\"]+\"");
		Matcher match = patt.matcher(line);
		if (match.find()) {
			return (line.substring(match.start() + 6, match.end() - 1));
		} else
			return null;
	}

	public void getContent(URLDepthPair obj) throws Exception {
		try (Socket mySocket = new Socket(obj.hostNameString(), 80)) {
			mySocket.setSoTimeout(5000);
			try (PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true)) {
				out.println("GET " + obj.pathNameString() + " HTTP/1.1");
				out.println("Host: " + obj.hostNameString());
				out.println("Connection: close");
				out.println();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()))) {
					String line;
					while ((line = in.readLine()) != null) {
						int newDepth = obj.getDepth() + 1;
						String findedUrl = patternSearch(line);
						if (findedUrl != null) {
							p1.put(new URLDepthPair(findedUrl, newDepth));
						}
					}
				}
			}
		}

	}

	@Override
	public void run() {
		while (true) {
			URLDepthPair pair = p1.get();
			try {
				getContent(pair);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

}
