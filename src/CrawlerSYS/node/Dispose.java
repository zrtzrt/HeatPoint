package CrawlerSYS.node;

import CrawlerSYS.entity.CrawlerReturnEntity;
import com.zaxxer.hikari.HikariDataSource;

public interface Dispose {
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,HikariDataSource ds);
	public boolean saveProcess(String[] res);
}
