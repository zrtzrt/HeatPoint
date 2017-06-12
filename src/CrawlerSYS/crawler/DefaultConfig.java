package CrawlerSYS.crawler;

public class DefaultConfig {
	public static final String dbip = "127.0.0.1:3306/heatpoint";
	public static final String dbArgs = "useUnicode=true&characterEncoding=UTF-8";
	public static final String user = "root";
	public static final String password = "root";
	public static final String then = "show";
	public static final int urlLimit = 30;
	public static final int thread = 10;
	public static final int allLimit = 5000;
	public static final int sleepTime = 0;
	public static final int serverPort = 6545;
	public static final int socketPort = 6546;
	public static final int lableSize = 200;
	public static final int urlSize = 50;
	public static final int titleSize = 100;
	public static final int textSize = 10000;
	public static final String[] node = {"127.0.0.1:6543"};
}
