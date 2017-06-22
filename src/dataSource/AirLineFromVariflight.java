package dataSource;

import java.util.Date;

import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;

public class AirLineFromVariflight {

	public static String getAirLine(String ap, Date time) {
		String json = WebCrawler.get("http://map.variflight.com/api?dep="+ap
				+"&isStop=0&isShare=1&isConnect=0&isExtend=1&isAll=0&isAll_ctry=CN&queryDate="
				+StringHelper.sqlDate(time)+"&dataType=1",null,null);
		// TODO Auto-generated method stub
		return json;
	}
	
	public static String getDetail(String dep, String arr, Date time) {
		String json = WebCrawler.get("http://map.variflight.com/api?arr="+arr+"&dep="+dep
				+"&isStop=0&isShare=1&isConnect=0&isExtend=1&isAll=0&isAll_ctry=CN&queryDate="
				+StringHelper.sqlDate(time)+"&dataType=4",null,null);
		// TODO Auto-generated method stub
		return json;
	}
//	public static void main(String[] args) {
//		String json = "";
//		JSONPath.
////YYC:["-114.01055","51.131393","卡尔加里",3,"CA"]
//	}
	
}
