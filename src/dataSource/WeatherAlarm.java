package dataSource;

import CrawlerSYS.utils.DBHelper;
import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;
import com.alibaba.fastjson.JSONPath;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherAlarm {
	private Logger logger = Logger.getLogger(this.getClass()); 
	public void init(){
		Connection con = DBHelper.getConn();
//		DBHelper.insert("DELETE FROM weatheralarm");
		try {
			con.createStatement().executeUpdate("DELETE FROM weatheralarm");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();logger.error("Exception",e);
		}
		String[] tl = new String[]{"台风","暴雨","暴雪","寒潮","大风","沙尘暴","高温","干旱","雷电","冰雹","霜冻","大雾","霾","道路结冰"};
		String[] tl9=new String[]{"寒冷","灰霾","雷雨大风","森林火险","降温","道路冰雪","干热风","空气重污染","低温"};
		String[] cl=new String[]{"蓝色","黄色","橙色","红色","白色"};
		String[] lable=new String[]{"locat","file","type","color"};
		Map<String,String> header=new HashMap<String,String>();
		header.put("Referer","http://www.weather.com.cn/alarm/warninglist.shtml");
		String json = WebCrawler.get("http://product.weather.com.cn/alarm/grepalarm.php?areaid=[0-9]{5,7}",header,"UTF-8").split("=", 2)[1];
		json=json.substring(0, json.length()-1);
		System.out.println(json);
		List<List<String>> data = (List<List<String>>) JSONPath.read(json, "$.data");
		for (int i = 0; i < data.size(); i++) {
			String city = data.get(i).get(0);
			String file = data.get(i).get(1);
			String info = file.split("-", 3)[2];
			int index = Integer.parseInt(info.substring(0, 2))-1;
			String type;
			if(index>20)
				type = tl9[index%10];
			else type = tl[index];
			String color = cl[Integer.parseInt(info.substring(2,4))-1];
			String[] d=new String[]{city,file,type,color};
			try {
				con.createStatement().executeUpdate(StringHelper.sqlINSERT("weatheralarm", lable,d));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error("Exception",e);
			}
//			DBHelper.insert(StringHelper.sqlINSERT("weatheralarm", lable,d));
		}
		DBHelper.closeConn();
	}
}
