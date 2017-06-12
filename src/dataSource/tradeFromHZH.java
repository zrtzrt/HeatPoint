package dataSource;

import org.apache.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;

import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.Dispose;

public class tradeFromHZH  implements Dispose{
	private Logger logger = Logger.getLogger(this.getClass());  
	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//    //用户数量
//    private static int num = 0;
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
//		if(page.getUrl().regex("http://www\\.haozhanhui\\.com/exh/exh_index_*").match()){
//			TradeEntity trade = new TradeEntity();
//			LocationEntity locat =new LocationEntity();
//			String[] date;
//			List<String> url=new ArrayList<String>();
//			url.add(page.getUrl().get());
//			trade.setUrl(url);
//			trade.setName(page.getHtml().xpath("//div[@class='exhname']/h1/text()").get().trim().replace("(","（").replace(")", "）"));
//			trade.setPicture(page.getHtml().xpath("//img[@class='exhico']/@src").get());
//			String d = page.getHtml().xpath("//div[@id='exhtime']/text()").get();
//			if(d!=null){
//				date = d.split("~", 2);
//				String year = date[0].split("-", 2)[0];
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
//				try {
//					trade.setStartDate(sdf.parse(date[0]));
//					trade.setEndDate(sdf.parse(year+"-"+date[1]));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//				}
//			}else
//				return;
//			locat.setName(page.getHtml().xpath("//div[@class='exhinfo_center']/ul/li[1]/a/text()").get().replace("(","（").replace(")", "）"));
//			trade.setLocat(locat);
//			String host=page.getHtml().xpath("//div[@class='exhinfo_center']/ul/li[3]/a/text()").get();
//			if(host!=null)
//				trade.setHost(host.trim().replaceAll(" \\(", "\\(").replaceAll(" ", ",").replaceAll("，", ",").replaceAll("、", ",").replaceAll("·", ","));
//			trade.setIndustry(page.getHtml().xpath("//div[@class='crumb']/a/text()").get());
//			trade.setArea(StringHelper.toInt(page.getHtml().xpath("//div[@class='exhinfo_center']/ul/li[2]/strong/text()").get()));
////			for (int i = 4; i < 12; i++) {
////				mean(page.getHtml().xpath("//div[@class='zhxxcontent']/p["+i+"]/text()").get(),trade);
////			}
//			System.out.println(num+++trade.getName());
////			logger.info(num+++trade.getName());
////			StringHelper.toGBK(trade);
//			if(!dao.save(trade))
////				System.out.println("exist");
//				logger.info(trade.getName()+"---------exist");
//		}else if(page.getUrl().regex("http://www\\.haozhanhui\\.com/zhanlanjihua/").match()){
//			 page.addTargetRequests(page.getHtml().xpath("//div[@id='oseexh1']").links().all());
//			 page.addTargetRequests(page.getHtml().xpath("//div[@id='oseexh2']").links().all());
//			 page.addTargetRequests(page.getHtml().xpath("//div[@id='oseexh3']").links().all());
//		}
//	}
//
//	public int getNum() {
//		return num;
//	}

	@Override
	public CrawlerReturnEntity resultDispose(CrawlerReturnEntity back,
			HikariDataSource ds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveProcess(String[] res) {
		// TODO Auto-generated method stub
		return false;
	}

}