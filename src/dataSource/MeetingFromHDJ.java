package dataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;

import dao.ShowDao;
import dao.impl.ShowDaoImpl;

import entity.LocationEntity;
import entity.ShowEntity;

import CrawlerSYS.crawler.Crawler;
import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.Dispose;
import CrawlerSYS.utils.StringHelper;

public class MeetingFromHDJ implements Dispose {
	private Logger logger = Logger.getLogger(this.getClass());  
	public void init(){
		List<String> xpathd = new ArrayList<String>();
		xpathd.add("//div[@class='all_events']/div/div/a/@href");
		xpathd.add("//div[@class='all_events']/div/div/p[2]/span[2]/text()");
		xpathd.add("//div[@class='pagination']/ul/li/a/text()");
		xpathd.add("//div[@class='pagination']/ul/li/a/@href");
		List<String> xpath = new ArrayList<String>();
		xpath.add("//h1[@class='media-heading']/a/text()");
		xpath.add("//div[@class='media-left']/a/img/@src");
		xpath.add("//div[@class='all_info']/span/text()");
		xpath.add("//div[@class='all_info']/span[2]/a/text()");
		xpath.add("//tbody/tr[@class='t_body']/@data-price");
//		xpath.add("//div[@class='new_content']/p[2]/text()");
		List<String> url = new ArrayList<String>();
		for (int i = new Date().getMonth()+1; i < 13; i++) {
			if(i>9)
				url.add("https://www.huodongjia.com/"+i+"/");
			else url.add("https://www.huodongjia.com/0"+i+"/");
		}
//		url.add("https://www.huodongjia.com/07/");
		
		new Crawler(url,xpathd)
			.exception("https://www\\.huodongjia\\.com/event-\\d+.html", xpath)
			.custom(this).limit(5000).run();
	}

	@Override
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,HikariDataSource ds) {
		if(back.getUrl().matches("https://www\\.huodongjia\\.com/event-\\d+.html")){
			ShowDao dao = new ShowDaoImpl(ds);
			ShowEntity show = new ShowEntity();
			LocationEntity locat =new LocationEntity();
			String[] date;
			List<String> url=new ArrayList<String>();
			url.add(back.getUrl());
			show.setUrl(url);
			show.setName(back.getRes().get(0).get(0).trim().replace('\"', '\''));
			show.setPicture(back.getRes().get(1).get(0).split("!", 2)[0]);
			date = back.getRes().get(2).get(0).split("：", 2)[1].split("至",2);
//		String year = date[0].split("年", 2)[0];
			if(StringHelper.isDate(date[0])){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//小写的mm表示的是分钟 
				try {
					show.setStartDate(sdf.parse(date[0]));
					if(date.length>1&&StringHelper.validateString(date[1]))
						show.setEndDate(sdf.parse(date[1]));
				} catch (ParseException e) {
e.printStackTrace();logger.error("Exception",e);
				}
			}
			if(back.getRes().get(3).get(1)!=null&&back.getRes().get(3).get(1).length()>0){
				locat.setCity(back.getRes().get(3).get(0).trim());
				locat.setName(back.getRes().get(3).get(1).replaceAll("\\u00A0","").trim());
				locat.setDetail(back.getRes().get(2).get(1).split("：", 2)[1].replaceAll("\\u00A0","").trim());
			}else{
				locat.setName(back.getRes().get(3).get(0));
			}
			show.setLocat(locat);
			if(back.getRes().get(4).size()>0){
				show.setMINprice(StringHelper.toInt(back.getRes().get(4).get(0)));
				show.setMAXprice(StringHelper.toInt(back.getRes().get(4).get(back.getRes().get(4).size()-1)));
			}
			show.setShowtype(7);
//			show.setInfo(back.getRes().get(5).get(0).replace('\"', '\'').trim().replaceAll("\\u00A0",""));
			if(!dao.save(show))
//				System.out.println(show.getName()+"----------------exist");
				logger.info(show.getName()+"----------------exist");
		}else{
			for (int i = 0; i < back.getRes().get(0).size(); i++) {
				if("已结束".equals(back.getRes().get(1).get(i)))
					back.getRes().get(0).remove(i--);
				back.getRes().get(0).set(i, "https://www.huodongjia.com"+back.getRes().get(0).get(i));
			}
			
			back.getLink().addAll(back.getRes().get(0));
			if(back.getRes().get(2).size()>0)
				if("下一页".equals(back.getRes().get(2).get(back.getRes().get(2).size()-1)))
					back.getLink().add("https://www.huodongjia.com"+back.getRes().get(3).get(back.getRes().get(2).size()-1));
		}
		return back;
	}

	@Override
	public boolean saveProcess(String[] res) {
		// TODO Auto-generated method stub
		return false;
	}
}
