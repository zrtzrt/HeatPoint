package dataSource;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import out.Output;

import com.zaxxer.hikari.HikariDataSource;

import CrawlerSYS.crawler.Crawler;
import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.CrawlerServer;
import CrawlerSYS.node.Dispose;
import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;

import dao.TradeDao;
import dao.impl.TradeDaoImpl;

import entity.LocationEntity;
import entity.TradeEntity;

public class TradeFromEShow  implements  Dispose{
	private static Logger logger = Logger.getLogger(TradeFromEShow.class);  
	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//    //用户数量
//    private static int num = 0;
//    
//	@Override
//	public Site getSite() {
//		 return this.site;
//	}
//
//	@Override
//	public void process(Page page) {
//		// TODO Auto-generated method stub
//		if(page.getUrl().regex("http://www\\.eshow365\\.com/zhanhui/html/*").match()){
//			TradeEntity trade = new TradeEntity();
//			LocationEntity locat =new LocationEntity();
//			String[] date;
//			List<String> url=new ArrayList<String>();
//			url.add(page.getUrl().get());
//			trade.setUrl(url);
//			trade.setName(page.getHtml().xpath("//div[@class='zhmaincontent']/h1/text()").get().trim().replace("(","（").replace(")", "）"));
//			trade.setEn_name(page.getHtml().xpath("//div[@class='zhxxcontent']/div/p/strong/text()").get());
//			String p = page.getHtml().xpath("//div[@class='zhtitlepic']/img/@src").get();
//			if(p.startsWith("http"))
//				trade.setPicture(p);
//			else trade.setPicture("http://"+page.getUrl().get().split("/")[2]+p);
//			date = page.getHtml().xpath("//div[@class='zhxxcontent']/p[1]/text()").get().split("：", 2)[1].split("---", 2);
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");//小写的mm表示的是分钟 
//			try {
//				trade.setStartDate(sdf.parse(date[0]));
//				trade.setEndDate(sdf.parse(date[1]));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//			}
//			String guan = page.getHtml().xpath("//div[@class='zhxxcontent']/p[2]/a[1]/text()").get();
//			if(guan!=null)
//				locat.setName(guan.replace("(","（").replace(")", "）"));
//			else
//				locat.setName(page.getHtml().xpath("//div[@class='zhxxcontent']/p[2]/text()").get().split("：", 2)[1].trim().replaceAll("\\u00A0",""));
//			locat.setDetail(page.getHtml().xpath("//div[@class='zhxxcontent']/p[2]/text()").get().split("：", 2)[1].trim().replaceAll("\\u00A0",""));
//			trade.setLocat(locat);
//			trade.setIndustry(page.getHtml().xpath("//div[@class='zhxxcontent']/p[3]/a/text()").get());
////			trade.setHot(Integer.parseInt(page.getHtml().xpath("//span[@id='txtClicks']/text()").get()));
//			String hoturl="http://www.eshow365.com/ZhanHui/ajax/UpdateClickByEshowNo.ashx?id="+
//						page.getUrl().get().split("/html/", 2)[1].split("_0", 2)[0];
////			System.out.println(hoturl);
//			String hot = WebCrawler.get(hoturl, null,null);
////			System.out.println(hot);
//			trade.setHot(StringHelper.toInt(hot));
//			for (int i = 4; i < 12; i++) {
//				mean(page.getHtml().xpath("//div[@class='zhxxcontent']/p["+i+"]/text()").get(),trade);
//			}
////			System.out.println(num+++trade.getName());
////			logger.info(num+++trade.getName());
////			StringHelper.toGBK(trade);
////			if(!dao.save(trade))
////				System.out.println("exist");
//				logger.info(trade.getName()+"---------exist");
//		}else 
////			if(page.getUrl().regex("http://www\\.eshow365\\.com/zhanhui/0-0-0-\\d+/0/").match()){
////			int last=Integer.parseInt(page.getHtml().xpath("//li[@class='nextlast']/a[2]/@rel").get());
////			for (int i = 1; i < last+1; i++) {
////				page.addTargetRequest("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
////						+StringHelper.getStringTime("yyyy/MM/dd")+"&page="+i);
////			}
////			//page.addTargetRequests(page.getHtml().xpath("//div[@class='searchlistcon']").links().all());
////		}else 
//			if(page.getUrl().regex("http://www\\.eshow365\\.com/zhanhui/Ajax/AjaxSearcherV3*").match()){
//			page.addTargetRequests(page.getHtml().xpath("//form").links().all());
//			if(page.getUrl().get().endsWith("=1")){
//				int last=StringHelper.toInt(page.getHtml().xpath("//select[@class='pagetiao']/option[1]").get().split("/", 2)[1]);
//				for (int i = 2; i < last+1; i++) {
//					page.addTargetRequest("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
//							+StringHelper.getStringTime("yyyy/MM/dd")+"&page="+i);
//				}
//			}
//				
//			//*[@id="pagestr"]/li[5]/select/option[1]
//		}
//	}
	
	void mean(String t,TradeEntity trade){
//		System.out.println(t);
		if(t==null)
			return;
		String[] info = t.split("：", 2);
		if(info[1]==null){
			return;
		}else 
		if(info[0].trim().equals("展会城市")){
			trade.getLocat().setCity(info[1].split("|", 2)[0]);
		}else 
		if(info[0].trim().equals("主办单位")){
			trade.setHost(info[1].trim().replace("(","（").replace(")", "）").replaceAll(" ", ",")
				.replaceAll("，", ",").replaceAll("、", ",").replaceAll("·", ","));
		}else
		if(info[0].trim().equals("展会面积")){
			trade.setArea(StringHelper.toInt(info[1]));
		}else
		if(info[0].trim().equals("所用展厅")){
			trade.setUsed(info[1]);
		}else
		if(info[0].trim().equals("举办届数")){
			trade.setTimes(StringHelper.toInt(info[1]));
		}else
		if(info[0].trim().equals("举办周期")){
			trade.setFrequency(info[1]);
		}
//		else if(info[0].trim().equals("参观费用")){
//			trade.setCondition(info[1]);
//		}
//		if(info[0].equalsIgnoreCase("�ٰ�ʱ��")){
//			data = info[1].split("---", 2);
//			trade.setStartDate(new Date(data[0]));
//			trade.setEndDate(new Date(data[1]));
//		}
	}

//	public static int getNum() {
//		return num;
//	}
	
//	public static void main(String[] args) {
//		long startTime ,endTime;
//		System.out.println("========小爬虫【启动】喽！=========");
//		startTime = new Date().getTime();
//		Spider.create(new tradeFromEShow()).addUrl("http://www.eshow365.com/zhanhui/0-0-0-"+StringHelper.getStringTime("yyyyMMdd")+"/0/").thread(5).run();
////		Spider.create(new tradeFromHZH()).addUrl("http://www.haozhanhui.com/zhanlanjihua/").thread(5).run();
////		Spider.create(new tradeFromCnena()).addUrl("http://www.cnena.com/showroom/list-htm-fid-1.html").thread(2).run();
////		Spider.create(new showFromDaMai()).addUrl("https://www.damai.cn/projectlist.do?mcid=1").thread(2).run();
////		Spider.create(new showFrom228()).addUrl("http://www.228.com.cn/s/yanchanghui/?j=1&p=1").thread(2).run();
////		test.getShowFrom228();
////		test.getShowFromDaMai();
//		endTime = new Date().getTime();
//		System.out.println("========小爬虫【结束】喽！=========");
//		System.out.println("一共爬到"+num+"个！用时为："+(endTime-startTime)/1000+"s");
//	}
	public void init(){
		List<String> xpath = new ArrayList<String>();
		xpath.add("//div[@class='zhmaincontent']/h1/text()");
		xpath.add("//div[@class='zhxxcontent']/div/p/strong/text()");
		xpath.add("//div[@class='zhtitlepic']/img/@src");
//		xpath.add("//span[@id='txtClicks']/text()");
		xpath.add("//div[@class='zhxxcontent']/p[1]/text()");
		xpath.add("//div[@class='zhxxcontent']/p[2]/a[1]/text()");
		xpath.add("//div[@class='zhxxcontent']/p[2]/text()");
		xpath.add("//div[@class='zhxxcontent']/p[3]/a/text()");
		
		for (int i = 4; i < 12; i++) {
			xpath.add("//div[@class='zhxxcontent']/p["+i+"]/text()");
		}
		
		List<String> url = new ArrayList<String>();
		url.add("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
				+StringHelper.getStringTime("yyyy/MM/dd")+"&page=1");
//		url.add("http://www.eshow365.com/zhanhui/0-0-0-"+StringHelper.getStringTime("yyyyMMdd")+"/0/");
//		List<String> xpath2 = new ArrayList<String>();
//		xpath2.add("//li[@class='nextlast']/a[2]/@rel");
//		xpath2.add("//div[@class='searchlistcon']/a/@href");
		List<String> xpath3 = new ArrayList<String>();
		xpath3.add("//div[@class='sslist']/p[@class='zhtitle']/a/@href");
		xpath3.add("//select[@class='pagetiao']/option[1]");
		//*[@id="from1"]/div[5]/p[1]/a
		
		new Crawler(url,xpath).custom(this)
//		.exception("http://www\\.eshow365\\.com/zhanhui/html/*", xpath)
//		.exception("http://www\\.eshow365\\.com/zhanhui/0-0-0-\\d+/0/", xpath2)
		.exception("http://www\\.eshow365\\.com/zhanhui/Ajax/AjaxSearcherV3.+", xpath3).run();
	}

	@Override
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,HikariDataSource ds) {
		TradeDao dao = new TradeDaoImpl(ds);
		if(back.getUrl().matches("http://www\\.eshow365\\.com/zhanhui/html/.+")){
			TradeEntity trade = new TradeEntity();
			LocationEntity locat =new LocationEntity();
			String[] date;
			List<String> url=new ArrayList<String>();
			url.add(back.getUrl());
			trade.setUrl(url);
			if(back.getRes().size()==0){
				System.err.println("url:"+url.get(0)+"res=null");
				logger.error("url:"+url.get(0)+"res=null");
				return back;
			}else if(back.getRes().get(0).size()==0){
				System.err.println("url:"+url.get(0)+"name=null");
				logger.error("url:"+url.get(0)+"name=null");
				return back;
			}
			trade.setName(back.getRes().get(0).get(0).trim().replace("(","（").replace(")", "）"));
			trade.setEn_name(back.getRes().get(1).get(0));
			String webroot = "http://"+back.getUrl().split("/")[2];
			trade.setPicture(webroot+back.getRes().get(2).get(0));
			date = back.getRes().get(3).get(0).split("：", 2)[1].split("---", 2);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");//小写的mm表示的是分钟 
			try {
				trade.setStartDate(sdf.parse(date[0]));
				trade.setEndDate(sdf.parse(date[1]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
			}
			String hoturl="http://www.eshow365.com/ZhanHui/ajax/UpdateClickByEshowNo.ashx?id="+
					back.getUrl().split("/html/", 2)[1].split("_0", 2)[0];
//			System.out.println(hoturl);
			String hot = WebCrawler.get(hoturl, null,null);
			trade.setHot(StringHelper.toInt(hot));
			List<String> gum = back.getRes().get(4);
			if(gum.size()>0){
				locat.setName(gum.get(0).trim().replace("(","（").replace(")", "）").replaceAll("\\u00A0",""));
				locat.setDetail(back.getRes().get(5).get(0).split("：", 2)[1].trim().replaceAll("\\u00A0",""));
			}
			else
				locat.setName(back.getRes().get(5).get(0).split("：", 2)[1].replace("(","（").replace(")", "）"));
			trade.setLocat(locat);
			trade.setIndustry(back.getRes().get(6).get(0));
			for (int i = 7; i < 15; i++) {
				if(back.getRes().get(i).size()>0)
					mean(back.getRes().get(i).get(0),trade);
			}
//			System.out.println(num+++trade.getName());
//			logger.info(num+++trade.getName());
//			StringHelper.toGBK(trade);
			if(!dao.save(trade))
//				System.out.println("exist");
				logger.info(trade.getName()+"---------exist");
//		}else if(back.getUrl().matches("http://www\\.eshow365\\.com/zhanhui/0-0-0-\\d+/0/")){
//				int last=Integer.parseInt(back.getRes().get(0).get(0));
//				for (int i = 2; i < last+1; i++) {
//					back.getLink().add("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
//							+StringHelper.getStringTime("yyyy/MM/dd")+"&page="+i);
//				}
//				back.getLink().addAll(back.getRes().get(1));
		}else{
//			System.out.println(back.getRes().get(0));
			if(back.getRes().size()!=0)
				back.getLink().addAll(back.getRes().get(0));
//			System.out.println(back.getRes().get(1).get(0));
			if(back.getUrl().endsWith("=1")){
				int last=StringHelper.toInt(back.getRes().get(1).get(0).split("/", 2)[1]);
				for (int i = 2; i < last+1; i++) {
					back.getLink().add("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
							+StringHelper.getStringTime("yyyy/MM/dd")+"&page="+i);
				}
			}
		}
		return back;
	}

	public static void main(String[] args) {
		
////		Spider.create(new tradeFromEShow()).addUrl("http://www.eshow365.com/zhanhui/0-0-0-"+StringHelper.getStringTime("yyyyMMdd")+"/0/").thread(3).run();
////		http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime=2017%2F4%2F24&page=2&s=0.07158046253022055
//		Spider.create(new tradeFromEShow()).addUrl("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime=2017%2F4%2F24&page=1").thread(3).run();
//		Spider.create(new tradeFromHZH()).addUrl("http://www.haozhanhui.com/zhanlanjihua/").thread(2).run();
//	    Spider.create(new tradeFromCnena()).addUrl("http://www.cnena.com/showroom/list-htm-fid-1.html").thread(2).run();
//		new WeatherAlarm().init();
////		new tradeFromEShow().init();
		
//		try {
//			WebCrawler.ignoreSsl();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//		}
//		
//		new CrawlerServer(6545).start();
//		long startTime ,endTime;
//		System.out.println("爬虫已启动");
//		startTime = new Date().getTime();
//		new TradeFromCnena().init();
//		new TradeFromEShow().init();
//		new ShowFromDaMai().init(7);
//		new ShowFrom228().init(7);
//		new MeetingFromHDJ().init();
//		endTime = new Date().getTime();
//		System.out.println("ShowFromDaMai已完成，用时"+(endTime-startTime)/1000+"s");
		
		new AllSiteCrawler().run();
		
//		System.out.println(StringHelper.similarity("'岳来越快乐'2017岳云鹏相声专场—太原站", "'岳来越快乐'2017岳云鹏相声专场—成都站"));
//		System.out.println("http://www.eshow365.com/zhanhui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
//					.matches("http://www\\.eshow365\\.com/zhanhui/Ajax/AjaxSearcherV3.+"));
//		String json = WebCrawler.get("http://www.228.com.cn/s/yanchanghui/?j=1&p=1",null);

//		System.out.println("a.b  c ".replaceAll(" ", ""));
//		String s = "<script type='text/javascript'>aaaa</SCript>bbbbbbbbb<SCRIPT type='text/javascript' />ccccc<SCRIPT type='text/javascript' >mm</SCRIPT>";  
//        s = s.replaceAll("(?i)(<SCRIPT)[\\s\\S]*?((</SCRIPT>)|(/>))", "");//忽略大小写的正则  
//        System.out.println(s);
//		System.out.println("a\"u\"to".replaceAll("\"", "'"));
//		File file = new File("/outputFile"+new Date().toString().split(":", 2)[0]+".xls");
//		try {
//			file.createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();logger.error("Exception",e);
//		}
//		new Output("xlsx","C:\\Users\\51953_000\\Workspaces\\MyEclipse 10\\HeatPoint\\WebRoot\\WEB-INF\\outputFile");
//		String url = "https://venue.damai.cn/ajax.aspx?_action=Search&keyword=南京";
//		String json = WebCrawler.get(url,null,null);
//		System.out.println(json);
		
	}

	@Override
	public boolean saveProcess(String[] res) {
		// TODO Auto-generated method stub
		return false;
	}

}
