package dataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;

import CrawlerSYS.entity.CrawlerReturnEntity;
import CrawlerSYS.node.Dispose;
import CrawlerSYS.utils.StringHelper;

import dao.ShowDao;
import dao.impl.ShowDaoImpl;

import entity.LocationEntity;
import entity.ShowEntity;

public class showFromPiao  implements Dispose{
	private Logger logger = Logger.getLogger(this.getClass());  
	//抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//    //用户数量
//    private static int num = 0,index=1,pnum=1;
////    private ShowDao dao = new ShowDaoImpl();
//    
//	@Override
//	public Site getSite() {
//		 return this.site;
//	}
//
//	@Override
//	public void process(Page page) {
//		// TODO Auto-generated method stub
//		if(page.getUrl().regex("http://www\\.piao\\.com/index\\.php*").match()){
////			List<TradeEntity> tList = new ArrayList<TradeEntity>();
//			for (int i = 1; i < 17; i++) {
//				ShowEntity show = new ShowEntity();
//				LocationEntity locat =new LocationEntity();
//				List<String> url=new ArrayList<String>();
//				url.add(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dt/h3/a/@href").get());
//				show.setUrl(url);
//				show.setName(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dt/h3/a/text()").get().trim());
//				System.out.println(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dt/h3/a/text()").get().trim());
//				show.setPicture(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[1]/a/img/@src").get());
////				String year = date[0].split("年", 2)[0];
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
//				try {
//					show.setStartDate(sdf.parse(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dd[1]/text()").get().split(":", 2)[1]));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
//				}
//				locat.setName(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dd[2]/a/text()").get());
//				show.setLocat(locat);
//				show.setMINprice(Integer.parseInt(page.getHtml().xpath("//div[@class='paixu']/ul["+i+"]/li[2]/dl/dd[3]/font/text()").get()));
//				show.setShowtype(index);
////				tList.add(trade);
////				System.out.println(num+++show.getName());
////				logger.info(num+++show.getName());
////			StringHelper.toGBK(trade);
////				if(!dao.save(show))
////					System.out.println("exist");
////					logger.info(show.getName()+"---------exist");
//			}
//			int last = StringHelper.toInt(page.getHtml().xpath("//a[@class='nonce']/text()").get().split("/", 2)[1].trim());
//			if(pnum==1){
//				for (int j = 2; j < last+1; j++) {
//					page.addTargetRequest("http://www.piao.com/index.php?app=search&cate_id=1217&&drop_city=1&page="+j);
//				}
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
