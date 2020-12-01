import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {
	private String url;
	private int depth;

	public URLDepthPair(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	@Override
	public String toString() {
		return (url + " " + depth);
	}

	public String hostNameString() throws MalformedURLException {
		try {
			return new URL(url).getHost();
		} catch (Exception e) {
			return null;
		}
	}

	public String pathNameString() throws MalformedURLException {
		try {
			return new URL(url).getPath();
		} catch (Exception e) {
			return null;
		}
	}

	public int getDepth() {
		return depth;
	}

	public String getURL() {
		return url;
	}

}
