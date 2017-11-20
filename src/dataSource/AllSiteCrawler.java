package dataSource;

import CrawlerSYS.crawler.DefaultConfig;
import CrawlerSYS.node.CrawlerServer;
import org.apache.log4j.Logger;

import java.util.Date;

public class AllSiteCrawler implements Runnable{
	private Logger logger = Logger.getLogger(this.getClass()); 
//	public AutoExecutor(int h,int m,int s){
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DAY_OF_MONTH, 1);
//		c.set(c.YEAR, c.MONTH, c.DAY_OF_MONTH, h, m, s);
//		long d = c.getTimeInMillis() - System.currentTimeMillis();
//		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(nc,initDelay,1,TimeUnit.DAYS);
//	}
	public void run(){
		CrawlerServer cs = new CrawlerServer(DefaultConfig.serverPort);
		cs.start();
		long startTime ,endTime;
		System.out.println("爬虫已启动");
		logger.info("爬虫已启动");
		startTime = new Date().getTime();
//		new WeatherAlarm().init();
//		endTime = new Date().getTime();
//		System.out.println("天气信息已完成，用时"+(endTime-startTime)/1000+"s");
//		logger.info("天气信息已完成，用时"+(endTime-startTime)/1000+"s");

//		Spider.create(new tradeFromEShow()).addUrl("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
//				+StringHelper.getStringTime("yyyy/MM/dd")+"&page=1").thread(3).run();
//		Spider.create(new tradeFromHZH()).addUrl("http://www.haozhanhui.com/zhanlanjihua/").thread(2).run();
//	    Spider.create(new tradeFromCnena()).addUrl("http://www.cnena.com/showroom/list-htm-fid-1.html").thread(2).run();
		new ShowFromDaMai().init(7);
		endTime = new Date().getTime();
		System.out.println("ShowFromDaMai已完成，用时"+(endTime-startTime)/1000+"s");
		logger.info("ShowFromDaMai已完成，用时"+(endTime-startTime)/1000+"s");
		new MeetingFromHDJ().init();
		endTime = new Date().getTime();
		System.out.println("MeetingFromHDJ已完成，用时"+(endTime-startTime)/1000+"s");
		logger.info("ShowFrom228已完成，用时"+(endTime-startTime)/1000+"s");
		new ShowFrom228().init(7);
		endTime = new Date().getTime();
		System.out.println("爬虫已全部完成，用时"+(endTime-startTime)/1000+"s");
		logger.info("爬虫已全部完成，用时"+(endTime-startTime)/1000+"s");
		new TradeFromEShow().init();
		endTime = new Date().getTime();
		System.out.println("TradeFromEShow已完成，用时"+(endTime-startTime)/1000+"s");
		logger.info("TradeFromEShow已完成，用时"+(endTime-startTime)/1000+"s");
		new TradeFromCnena().init();
		endTime = new Date().getTime();
		System.out.println("TradeFromCnena已完成，用时"+(endTime-startTime)/1000+"s");
		logger.info("TradeFromCnena已完成，用时"+(endTime-startTime)/1000+"s");
		cs.close();
	}
}
