package dataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import CrawlerSYS.crawler.DefaultConfig;
import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;

import com.alibaba.fastjson.JSONPath;
import com.zaxxer.hikari.HikariDataSource;

import dao.ShowDao;
import dao.impl.ShowDaoImpl;

import entity.LocationEntity;
import entity.ShowEntity;

public class ShowFrom228 
//implements PageProcessor
{
	private Logger logger = Logger.getLogger(this.getClass()); 
	private ShowDao dao;
	private HikariDataSource ds;
	private int task;
	
	public ShowFrom228(){
		ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:mysql://"+DefaultConfig.dbip+"?"+DefaultConfig.dbArgs);
		ds.setUsername(DefaultConfig.user);
		ds.setPassword(DefaultConfig.password);
		dao = new ShowDaoImpl(ds);
		task=1;
	};
	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me()
//    		.addHeader("Host","www.228.com.cn").addHeader("Connection","keep-alive").addHeader("DNT", "1")
//    		.addHeader("Accept","application/json, text/javascript, */*; q=0.01").addHeader("X-Requested-With","XMLHttpRequest")
//    		.addHeader("Accept-Encoding","gzip, deflate, sdch").addHeader("Accept-Language","zh-CN,zh;q=0.01")
//    		.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
//    		.addHeader("Cache-Control","max-age=0").addHeader("Upgrade-Insecure-Requests","1")
//    		.addHeader("Cookie", 
//    				"PHPStat_First_Time_10000001=1475843235450; PHPStat_Cookie_Global_User_Id=_ck16100720271514786744733711936; PHPStat_Return_Time_10000001=1475843235450; PHPStat_Msrc_10000001=%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search; PHPStat_Msrc_Type_10000001=pmf_from_free_search; PHPStat_Main_Website_10000001=_ck16100720271514786744733711936%7C10000001%7C%7C%7C; __jsluid=fb249d6b659b78cbee80888aaca281cf; looyu_id=f438308f8c3e86c2810db98bea8f023d41_20000356%3A5; products=219662415%2C233439537%2C230627026%2C233132029%2C208044547%2C216471470%5E%E4%B8%96%E7%95%8C%E5%B7%A1%E5%9B%9E%E6%BC%94%E5%94%B1%E4%BC%9A%E2%80%94%E5%8D%97%E4%BA%AC%E7%AB%99%EF%BC%885%E6%9C%8820%E6%97%A5%EF%BC%89%3B%E8%AE%A9%E7%88%B1%E9%A3%9E%E7%BF%94%E2%80%94%E5%A4%A7%E5%9E%8B%E6%85%88%E5%96%84%E5%85%AC%E7%9B%8A%E6%BC%94%E5%87%BA%3B%E4%B8%AD%E5%9B%BD%E5%B4%87%E7%A4%BC%E5%AF%8C%E9%BE%99%E5%9B%9B%E5%AD%A3%E5%B0%8F%E9%95%87%20%E7%A7%91%E7%BD%97%E5%A8%9C%E6%97%A5%E8%90%BD%E5%A3%B0%E8%B5%B7%E9%9B%AA%E5%9C%B0%E9%9F%B3%E4%B9%90%E8%8A%82%3B2016%E2%80%942017%E8%B5%9B%E5%AD%A3CBA%E5%AD%A3%E5%90%8E%E8%B5%9B%E9%A6%96%E8%BD%AE%20%E8%BE%BD%E5%AE%81%E6%9C%AC%E9%92%A2VS%E6%B5%99%E6%B1%9F%E5%B9%BF%E5%8E%A6%3B2017%5BA%20CLASSIC%20TOUR%20%E5%AD%A6%E5%8F%8B.%E7%BB%8F%E5%85%B8%5D%E4%B8%96%E7%95%8C%E5%B7%A1%E5%9B%9E%E6%BC%94%E5%94%B1%E4%BC%9A%E2%80%94%E7%A6%8F%E5%B7%9E%E7%AB%99%3B%26%23034%3B%E5%9B%BA%E7%BE%8E%E4%B9%8B%E5%A4%9C%26%23034%3B%E7%9C%8B%E8%A7%81%E6%9C%AA%E6%9D%A5%E6%BC%94%E5%94%B1%E4%BC%9A; NTKF_T2D_CLIENTID=guestTEMP23A0-023A-8EE3-5A68-9F1CA0B4DAE5; SESSION=2c685c42-48af-412a-9c01-5923c5750c50; route_yl=678c16f835b40d619bedc0959d61c89c; OZ_1U_1759=vid=v7f794a4a8d461.0&ctime=1488877157&ltime=1488877141; OZ_1Y_1759=erefer=-&eurl=http%3A//www.228.com.cn/s/yanchanghui/&etime=1488877141&ctime=1488877157&ltime=1488877141&compid=1759")
//    		.setRetryTimes(3).setSleepTime(1000);
    //用户数量
//    private static int num = 0,index=1,pnum=1;
    
//	@Override
//	public Site getSite() {
//		 return this.site;
//	}

	

//	public int getNum() {
//		// TODO Auto-generated method stub
//		return num;
//	}
	
	public void init(int type){
		if(type==1)
			getShow("http://www.228.com.cn/s/yanchanghui/?j=1&p=",1,1);
		else 
		if(type==2)
			getShow("http://www.228.com.cn/s/huajuwutaiju/?j=1&p=",2,1);
		else 
		if(type==3)
			getShow("http://www.228.com.cn/s/yinyuehui/?j=1&p=",3,1);
		else 
		if(type==4)
			getShow("http://www.228.com.cn/s/wudaobalei/?j=1&p=",4,1);
		else 
		if(type==5)
			getShow("http://www.228.com.cn/s/xiquzongyi/?j=1&p=",5,1);
		else 
		if(type==6)
			getShow("http://www.228.com.cn/s/tiyusaishi/?j=1&p=",6,1);
		else 
		if(type>6){
			task=6;
			for (int i = 1; i < 7; i++)
				init(i);
		}
		task--;
		close();
		}
	private void close(){
//		System.out.println(task);
		if(task==0)
			ds.close();
	}
	
	public void getShow(String u,int index,int page){
		String json = WebCrawler.get(u+page,null,null);
//		System.out.println(json);
		int size = (Integer) JSONPath.read(json, "$.pageSize");
		for (int i = 0; i < 10; i++) {
			ShowEntity show = new ShowEntity();
			LocationEntity locat =new LocationEntity();
			List<String> url=new ArrayList<String>();
			show.setName((String)JSONPath.read(json, "$.products["+i+"].name"));
			show.setPicture("http://static.228.cn/"+(String)JSONPath.read(json, "$.products["+i+"].pbigimg"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
			String start = (String)JSONPath.read(json, "$.products["+i+"].begindate"),
					end = (String)JSONPath.read(json, "$.products["+i+"].enddate");
			try {
				if(start!=null)
					show.setStartDate(sdf.parse(start));
				if(end!=null)
					show.setEndDate(sdf.parse(end));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
			}
//			System.out.println(JSONPath.read(json, "$.products["+i+"].minprice"));
			show.setMINprice(StringHelper.toInt((String) JSONPath.read(json, "$.products["+i+"].minprice")));
			show.setMAXprice(StringHelper.toInt((String) JSONPath.read(json, "$.products["+i+"].maxprice")));
			locat.setName((String)JSONPath.read(json, "$.products["+i+"].vname"));
			show.setLocat(locat);
			show.setShowtype(index);
			url.add("http://www.228.com.cn/ticket-"+(String)JSONPath.read(json, "$.products["+i+"].productid")+".html");
			show.setUrl(url);
			if(!dao.save(show))
				logger.info(show.getName()+"---------exist");
		}
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//		}
		if(size>page){
			getShow(u,index,page+1);
		}
	}
}