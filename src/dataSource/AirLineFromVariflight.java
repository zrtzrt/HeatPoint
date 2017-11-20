package dataSource;

import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AirLineFromVariflight {

	public static String getAirLine(String ap, Date time) {
//		String json = WebCrawler.get("http://map.variflight.com/api?dep="+ap
//				+"&isStop=0&isShare=1&isConnect=0&isExtend=1&isAll=0&isAll_ctry=CN&queryDate="
//				+StringHelper.sqlDate(time)+"&dataType=1",null,null);
		Map<String,String> data = new HashMap<String,String>();
		data.put("queryDate1","");
		data.put("queryDate2","");
		data.put("dep",ap);
		data.put("depType","1");
		data.put("isStop","0");
		data.put("isShare","1");
		data.put("isDomestic","1");
		String json = WebCrawler.post("http://map.variflight.com/__api/SuXAvAQcoWkchQuUUqHN/dr",data,null);
		// queryDate1=&queryDate2=&maker=&union=&ftype=&dep=PEK&depType=1&isStop=0&isShare=1&isDomestic=1
		return json;
	}
	
	public static String getDetail(String dep, String arr, Date time) {
//		String json = WebCrawler.get("http://map.variflight.com/api?arr="+arr+"&dep="+dep
//				+"&isStop=0&isShare=1&isConnect=0&isExtend=1&isAll=0&isAll_ctry=CN&queryDate="
//				+StringHelper.sqlDate(time)+"&dataType=4",null,null);
		Map<String,String> data = new HashMap<String,String>();
		data.put("queryDate1","");
		data.put("queryDate2","");
		data.put("dep",dep);
		data.put("arr",arr);
		data.put("depType","1");
		data.put("isStop","0");
		data.put("isShare","1");
		data.put("isDomestic","1");
		String json = WebCrawler.post("http://map.variflight.com/__api/SuXAvAQcoWkchQuUUqHN/de2",data,null);
		// queryDate1=&queryDate2=&maker=&union=&ftype=&dep=PEK&depType=1&isStop=0&isShare=1&isDomestic=1&data=~&arr=CGO
		return json;
	}
//	public static void main(String[] args) {
//		String json = "";
//		JSONPath.
////YYC:["-114.01055","51.131393","卡尔加里",3,"CA"]
//	}
	
}
