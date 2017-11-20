package dataSource;

import CrawlerSYS.utils.WebCrawler;
import com.alibaba.fastjson.JSONPath;
import dao.LocationDao;
import dao.impl.LocationDaoImpl;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocatFromDaMai {
	private static Logger logger = Logger.getLogger(LocatFromDaMai.class);  
	int id = 0;
	public LocatFromDaMai(int id){
		this.id=id;
	}
	public static String getLocation(int id){
		Map<String,String> header=new HashMap<String,String>();
		header.put("Referer","https://venue.damai.cn/");
		header.put("Host","venue.damai.cn");
		header.put("Connection","keep-alive");
//		header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
		LocationDao dao=new LocationDaoImpl();
		String url="https://venue.damai.cn/ajax.aspx?_action=Search&keyword="+dao.getName(id);
//		System.out.println(url);
		String json = null;
		try {
			json = WebCrawler.getConnect(url,null,null).get().toString()
					.split("=", 2)[1].split(";", 2)[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
		}
//		System.out.println(json);
		logger.info(json);
		Double lng = Double.parseDouble((String) JSONPath.read(json, "$.l[0].x"));
		Double lat = Double.parseDouble((String) JSONPath.read(json, "$.l[0].y"));
//		String det = (String)JSONPath.read(json, "$.l[0].d");
		
//		if(dao.saveL(id, lng, lat))
//			System.out.println("save id, lng, lat"+id+lng+ lat);
//		if(dao.saveD(id, det))
//			System.out.println("save id, det, lat"+id+det);
		return "{\"x\":\""+lng+"\",\"y\":\""+lat+"\"}";
//		return json;
	}
}
