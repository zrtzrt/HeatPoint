package dataSource;

import CrawlerSYS.crawler.Crawler;
import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.Dispose;
import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;
import com.zaxxer.hikari.HikariDataSource;
import dao.TradeDao;
import dao.impl.TradeDaoImpl;
import entity.LocationEntity;
import entity.TradeEntity;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TradeFromCnena  implements Dispose{
	private Logger logger = Logger.getLogger(this.getClass());  
	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//    //用户数量
//    private static int num = 0,index=1,pnum=1;
//    private tradeDao dao = new tradeDaoImpl();
//    
//	@Override
//	public Site getSite() {
//		 return this.site;
//	}
//
//	@Override
//	public void process(Page page) {
//		// TODO Auto-generated method stub
//		if(page.getUrl().regex("http://www\\.cnena\\.com/showroom/list-htm-fid-*").match()){
////			List<TradeEntity> tList = new ArrayList<TradeEntity>();
//			String industry = page.getHtml().xpath("//span[@class='biaoti']/a[3]/text()").get().split("/", 2)[0].substring(1);
//			for (int i = 2; i < 10; i++) {
//				TradeEntity trade = new TradeEntity();
//				LocationEntity locat =new LocationEntity();
//				String[] date;
//				List<String> url=new ArrayList<String>();
//				//div[@class='srpnel']/table[2]/tbody/tr/td[2]/a
//				url.add(page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[2]/a/@href").get());
//				trade.setUrl(url);
//				trade.setName(page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[2]/a/text()").get().trim().replace("(","（").replace(")", "）"));
//				trade.setPicture(page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[1]/center/a/img/@src").get());
////				String info = page.getHtml().xpath("//div[@class='area-sub']/div[1]/div/p/text()").get().trim();
////				String host = page.getHtml().xpath("//div[@class='area-sub']/div[2]/div/p/text()").get().trim();
//				date = page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[3]/center/text()").get().split("至", 2);
////				String year = date[0].split("年", 2)[0];
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
//				try {
//					trade.setStartDate(sdf.parse(date[0].trim().replaceAll("\\u00A0","")));
//					trade.setEndDate(sdf.parse(date[1].trim().replaceAll("\\u00A0","")));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//				}
//				locat.setName(page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[4]/a/text()").get().replace("(","（").replace(")", "）").replaceAll("博览", "展览"));
//				trade.setLocat(locat);
////				trade.setHost(page.getHtml().xpath("//div[@class='exhinfo_center']/ul/li[3]/a/text()").get());
//				trade.setIndustry(industry);
//				trade.setHot(StringHelper.toInt(page.getHtml().xpath("//div[@class='srpnel']/table["+i+"]/tbody/tr/td[5]/text()").get()));
//				
////				tList.add(trade);
//				
////			for (int i = 4; i < 12; i++) {
////				mean(page.getHtml().xpath("//div[@class='zhxxcontent']/p["+i+"]/text()").get(),trade);
////			}
////				System.out.println(num+++trade.getName());
////				logger.info(num+++trade.getName());
////			StringHelper.toGBK(trade);
//				if(!dao.save(trade))
////					System.out.println("exist");
//					logger.info(trade.getName()+"---------exist");
//			}
//			int last = Integer.parseInt(page.getHtml().xpath("//div[@class='page']/a[12]/text()").get().split("/", 3)[1]);
//			if(pnum==1){
//				for (int j = 2; j < last+1; j++) {
//					page.addTargetRequest("http://www.cnena.com/showroom/list-htm-fid-"+index+"-page-"+j+".html");
//				}
//			}
//			if (pnum==last&&index<24) {
//				index++;
//				page.addTargetRequest("http://www.cnena.com/showroom/list-htm-fid-"+index+".html");
//			}
//			pnum++;
//			
//		}
//	}
	public void init(){
		List<String> xpath = new ArrayList<String>();
		xpath.add("//span[@class='biaoti']/a[3]/text()");//industry
		xpath.add("//table[@class='gridtable']/tbody/tr/td[2]/a/@href");//url
		xpath.add("//table[@class='gridtable']/tbody/tr/td[2]/a/text()");//name
		xpath.add("//table[@class='gridtable']/tbody/tr/td[1]/center/a/img/@src");//pic
		xpath.add("//table[@class='gridtable']/tbody/tr/td[3]/center/text()");//date
		xpath.add("//table[@class='gridtable']/tbody/tr/td[4]/a/text()");//locat
		xpath.add("//table[@class='gridtable']/tbody/tr/td[5]/text()");//hot
		xpath.add("//div[@class='page']/a[12]/text()");//last
		
		List<String> url = new ArrayList<String>();
		for (int i = 1; i < 24; i++) {
			url.add("http://www.cnena.com/showroom/list-htm-fid-"+i+".html");
		}
		
		new Crawler(url,xpath).custom(this).run();
		
	}

	@Override
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,
			HikariDataSource ds) {
		TradeDao dao = new TradeDaoImpl(ds);
		
		String industry = back.getRes().get(0).get(0).split("/", 2)[0].substring(1);
		
		for (int i = 0; i < back.getRes().get(1).size(); i++) {
			TradeEntity trade = new TradeEntity();
			LocationEntity locat =new LocationEntity();
			String[] date;
			List<String> url=new ArrayList<String>();
			if(back.getRes().size()==0){
				System.err.println("url:"+url.get(0)+"res=null");
				logger.error("url:"+url.get(0)+"res=null");
				return back;
			}else if(back.getRes().get(0).size()==0){
				System.err.println("url:"+url.get(0)+"url=null");
				logger.error("url:"+url.get(0)+"url=null");
				return back;
			}
			url.add("http://www.cnena.com/showroom/"+back.getRes().get(1).get(i));
			trade.setUrl(url);
			trade.setName(back.getRes().get(2).get(i).trim().replace("(","（").replace(")", "）"));
			trade.setPicture(back.getRes().get(3).get(i));
			date = back.getRes().get(4).get(i).split("至", 2);
//			String year = date[0].split("年", 2)[0];
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
			try {
				trade.setStartDate(sdf.parse(date[0].trim().replaceAll("\\u00A0","")));
				trade.setEndDate(sdf.parse(date[1].trim().replaceAll("\\u00A0","")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
			}
			locat.setName(back.getRes().get(5).get(i).replace("(","（").replace(")", "）").replaceAll("博览", "展览"));
			trade.setLocat(locat);
			trade.setIndustry(industry);
			trade.setHot(StringHelper.toInt(back.getRes().get(6).get(i)));
			
			List<String> xpath = new ArrayList<String>();
			xpath.add("//div[@class='area-sub']/div[2]/div[2]/p/text()");//industry
			xpath.add("//div[@class='area-main']/div[4]/p/text()");//url
			List<List<String>> res = null;
			try {
				res = WebCrawler.getByXpath(WebCrawler.getConnect(url.get(0), null, null).get(), xpath, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error("Exception",e);
			}
			trade.setHost(res.get(0).get(0).split("展会信息发布平台", 2)[0].replaceAll(" ", ""));
			trade.setUsed(res.get(1).get(0).replaceAll(" ", ""));
			
			if(dao.save(trade))
				logger.info(trade.getName()+"---------exist");
		}
		
		String newU = back.getUrl().substring(0, back.getUrl().length()-5);
		if(newU.endsWith("list-htm-fid-[0~9]+")){
			int last = StringHelper.toInt(back.getRes().get(7).get(0));
			for (int i = 0; i < last+1; i++) {
				back.getLink().add(newU+"-page-"+i+".html");
			}
		}
		
		return back;
	}

	@Override
	public boolean saveProcess(String[] res) {
		// TODO Auto-generated method stub
		return false;
	}
	

}