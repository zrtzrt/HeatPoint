package dataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;

import CrawlerSYS.crawler.Crawler;
import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.Dispose;
import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;

import dao.ShowDao;
import dao.impl.ShowDaoImpl;

import entity.LocationEntity;
import entity.ShowEntity;

public class ShowFromDaMai  implements 
//PageProcessor , 
Dispose{
	private Logger logger = Logger.getLogger(this.getClass());  
	
//	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me()
////    		.addHeader("Host","www.damai.cn").addHeader("Connection","keep-alive").addHeader("DNT", "1")
////    		.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
////    		.addHeader("Accept-Encoding","gzip, deflate, sdch").addHeader("Accept-Language","zh-CN,zh;q=0.8")
////    		.addHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
////    		.addHeader("Cache-Control","max-age=0").addHeader("Upgrade-Insecure-Requests","1")
////    		.addCookie("PHPStat_Cookie_Global_User_Id","_ck16100720264317717583240338641")
////    		.addCookie("PHPStat_First_Time_10000001", "1475843203638")
////    		.addCookie("PHPStat_Msrc_10000001", "%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search")
////    		.addCookie("PHPStat_Msrc_Type_10000001", "pmf_from_free_search")
////    		.addCookie("PHPStat_Return_Time_10000001", "1475843203638")
////    		.addCookie("cpSTAT_ok_pages", "1").addCookie("cpSTAT_ok_times", "9").addCookie("page_tran_time", "0")
////    		.addCookie("cporder", "ordervalue=bjXlKqMZTofrF%2bliAgYW9F6ZrOVbPR4gNtc15i56OZ0FL%2fQZ%2b1I0eIWnEZgd99zDusexD46dtaR%2b7XnEZzbqFQ%3d%3d")
////    		.addCookie("cporderV2", "bjXlKqMZTofrF%2bliAgYW9F6ZrOVbPR4gNtc15i56OZ0FL%2fQZ%2b1I0eIWnEZgd99zDusexD46dtaR%2b7XnEZzbqFQ%3d%3d")
////    		.addCookie("x_hm_tuid", "cTsaq/iND4YhDdInzGyG7vzdQs1lPdjf5amDKF0XeBpcEJ9ytcSrMpAT7Om50wFu")
//    		.setRetryTimes(3).setSleepTime(1000);
//    //用户数量
//    private static int num = 0;
//    		,index=1,pnum=1;
//    
//	@Override
//	public Site getSite() {
//		 return this.site;
//	}
//
//	@Override
//	public void process(Page page) {
//		// TODO Auto-generated method stub
//		if(page.getUrl().regex("https://www\\.damai\\.cn/projectlist\\.do\\?mcid=*").match()){
//			for (int i = 1; i < 11; i++) {
//				ShowEntity show = new ShowEntity();
//				LocationEntity locat =new LocationEntity();
//				String[] date,price;
//				List<String> url=new ArrayList<String>();
//				url.add(page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/h2/a/@href").get());
//				show.setUrl(url);
//				show.setName(page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/h2/a/text()").get().trim());
//				show.setInfo(page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/p/text()").get());
//				show.setPicture(page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/p/a/img/@src").get());
//				date = page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/p[@class='mt5']/text()").get().split(":", 2)[1].split("-",2);
////				String year = date[0].split("年", 2)[0];
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");//小写的mm表示的是分钟 
//				try {
//					show.setStartDate(sdf.parse(date[0]));
//					if(StringHelper.validateString(date[1]))
//						show.setEndDate(sdf.parse(date[1]));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//    				e.printStackTrace();logger.error("Exception",e);
//				}
//				locat.setName(page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/p[@class='mt5']/span[@class='ml20']/a/text()").get());
//				show.setLocat(locat);
//				price=page.getHtml().xpath("//ul[@id='performList']/li["+i+"]/div/p[3]/span[@class='price-sort'/text()").get().split("-", 2);
//				show.setMINprice(Integer.parseInt(price[0]));
//				show.setMAXprice(Integer.parseInt(price[1]));
//				show.setShowtype(index);
//				System.out.println(num+++show.getName());
//				if(!dao.save(show))
//					System.out.println("exist");
//			}
//			int last = StringHelper.toInt(page.getHtml().xpath("//div[@class='pagination']/span[@class='ml10']/text()").get());
//			if(pnum==1){
//				for (int j = 2; j < last+1; j++) {
//					page.addTargetRequest("https://www.damai.cn/projectlist.do?mcid="+index+"&pageIndex="+j);
//				}
//			}
//			if (pnum==last&&index<6) {
//				index++;
//				page.addTargetRequest("https://www.damai.cn/projectlist.do?mcid="+index);
//			}
//			pnum++;
//			
//		}
//	}
//
//	public int getNum() {
//		// TODO Auto-generated method stub
//		return num;
//	}

	
	public void init(int type){
		try {
			WebCrawler.ignoreSsl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
		}
		List<String> xpath = new ArrayList<String>();
		xpath.add("//ul[@id='performList']/li/div/h2/a/@href");
		xpath.add("//ul[@id='performList']/li/div/h2/a/text()");
		xpath.add("//ul[@id='performList']/li/div/p[1]/text()");
		//*[@id="performList"]/li[2]/div/p[1]
		xpath.add("//ul[@id='performList']/li/p/a/img/@src");
		xpath.add("//ul[@id='performList']/li/div/p[@class='mt5']/text()");
		xpath.add("//ul[@id='performList']/li/div/p[@class='mt5']/span[@class='ml20']/a/text()");
		xpath.add("//ul[@id='performList']/li/div/p[3]/span[@class='price-sort']/text()");
		xpath.add("//div[@class='pagination']/span[@class='ml10']/text()");
		List<String> url = new ArrayList<String>();
		if(type<1)
			return;
		else
		if(type>6){
			for (int i = 1; i < 7; i++) {
				url.add("https://www.damai.cn/projectlist.do?mcid="+i+"&pageIndex=1");
			}
		}else url.add("https://www.damai.cn/projectlist.do?mcid="+type+"&pageIndex=1");
		
		new Crawler(url,xpath).custom(this).limit(5000).run();
	}
	
	@Override
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,HikariDataSource ds) {
//		List<ShowEntity> sl = new ArrayList<ShowEntity>();
		ShowDao dao = new ShowDaoImpl(ds);
		for (int i = 1; i < back.getRes().get(0).size(); i++) {
			ShowEntity show = new ShowEntity();
			LocationEntity locat =new LocationEntity();
			String[] date,price;
			List<String> url=new ArrayList<String>();
			url.add("https:"+back.getRes().get(0).get(i));
			show.setUrl(url);
			show.setName(back.getRes().get(1).get(i).trim().replace('\"', '\''));
			show.setInfo(back.getRes().get(2).get(i).replace('\"', '\''));
			show.setPicture("http:"+back.getRes().get(3).get(i).split("_109_144", 2)[0]+".jpg");
			//pimg.dmcdn.cn/perform/project/1220/122053_n_109_144.jpg small
			//pimg.dmcdn.cn/perform/project/1220/122053_n.jpg         big
			date = back.getRes().get(4).get(i).split("：", 2)[1].split("-",2);
//			String year = date[0].split("年", 2)[0];
			if(StringHelper.isDate(date[0])){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");//小写的mm表示的是分钟 
				try {
					show.setStartDate(sdf.parse(date[0]));
					if(date.length>1&&StringHelper.validateString(date[1]))
						show.setEndDate(sdf.parse(date[1]));
				} catch (ParseException e) {
					SimpleDateFormat sdf2=new SimpleDateFormat("MM.dd");//小写的mm表示的是分钟 
					try {
						show.setEndDate(sdf2.parse(date[1]));
					} catch (ParseException e1) {
						SimpleDateFormat sdf3=new SimpleDateFormat("yy.MM.dd");//小写的mm表示的是分钟 
						try {
							show.setEndDate(sdf3.parse(date[1]));
						} catch (ParseException ex) {
							// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
						}
					}
				}
			}
			locat.setName(back.getRes().get(5).get(i));
			
//			
//			String url2="https://venue.damai.cn/ajax.aspx?_action=Search&keyword="+back.getRes().get(5).get(i);
//			String json = WebCrawler.get(url2,null).split("=", 2)[1];
//			json=json.substring(0, json.length()-1);
////			System.out.println(json);
//			locat.setLongitude((Double) JSONPath.read(json, "$.l[0].x"));
//			locat.setLatitude((Double) JSONPath.read(json, "$.l[0].y"));
//			locat.setDetail((String) JSONPath.read(json, "$.l[0].d"));
			
			
			show.setLocat(locat);
			price=back.getRes().get(6).get(i).split("-", 2);
			show.setMINprice(StringHelper.toInt(price[0]));
			if(price.length>1)
				show.setMAXprice(StringHelper.toInt(price[1]));
			String u = back.getUrl();
			show.setShowtype(Integer.parseInt(u.replaceAll(".+mcid=(\\d).+", "$1")));
//			tList.add(trade);
//			System.out.println(num+++show.getName());
//			logger.info(num+++show.getName());
//		StringHelper.toGBK(trade);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
			}
			if(!dao.save(show))
//				System.out.println(show.getName()+"----------------exist");
				logger.info(show.getName()+"----------------exist");
		}
		int last = StringHelper.toInt(back.getRes().get(7).get(0));
		String page = back.getUrl().split("=", 3)[2];
		if(page.equals("1")){
//			System.out.println("page=1,new url="+last);
			for (int j = 2; j < last+1; j++) {
				back.getLink().add(back.getUrl().substring(0, back.getUrl().length()-page.length())+j);
			}
		}
//		System.out.println(back.getLink());
//		logger.info(back.getLink());
		return back;
	}

	@Override
	public boolean saveProcess(String[] res) {
		// TODO Auto-generated method stub
		return false;
	}
}
