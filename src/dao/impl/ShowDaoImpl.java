package dao.impl;

import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;
import com.alibaba.fastjson.JSONPath;
import com.zaxxer.hikari.HikariDataSource;
import dao.ShowDao;
import entity.ShowEntity;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//import CrawlerSYS.utils.DBHelper;

public class ShowDaoImpl implements ShowDao{
	private Logger logger = Logger.getLogger(this.getClass());  
	private HikariDataSource ds;
	
	public ShowDaoImpl(HikariDataSource ds){
		this.ds=ds;
	}
	@Override
	public boolean save(ShowEntity s) {
		if(s.getStartDate()!=null&&s.getStartDate().before(new Date()))
			return false;
		
		String[] lable,data = null;
		String table;
		boolean res = true;
//				showId=getIdByLocatAndDate(s.getLocat().getName(),s.getStartDate(),s.getEndDate(), s.getName()),
		int showId = 0,locatId = 0,urlid = urlExcit(s.getUrl().get(0));
		if(urlid==-1)
			showId=tradeExcit(s.getName());
		if(showId==-1)
			locatId=locationExcit(s.getLocat().getName(),s.getLocat().getDetail());
//			urlid=urlExcit(s.getUrl().get(0));
		if(locatId==-1){
			String url,json,province = null,city = null,lng = null,lat = null,urlP = null;
			try {
				if(s.getLocat().getName()!=null)
					urlP = URLEncoder.encode(s.getLocat().getName(),"utf-8");
				else if(s.getLocat().getDetail()!=null)
					urlP = URLEncoder.encode(s.getLocat().getDetail(),"utf-8");
				else
					return false;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error("UnsupportedEncodingException",e);
			}
			
			if(s.getUrl().get(0).contains("damai")){
				url="https://venue.damai.cn/ajax.aspx?_action=Search&keyword="+urlP;
				json = WebCrawler.get(url,null,null);
				if(json.contains("=")){
					json = json.split("=", 2)[1];
					json = json.substring(0, json.length()-1);
					lng = (String) JSONPath.read(json, "$.l[0].x");
					lat = (String) JSONPath.read(json, "$.l[0].y");
				}else{
						url = "http://api.map.baidu.com/geocoder/v2/?address="+urlP+
								"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
						json = WebCrawler.get(url,null,null);
						if((Integer) JSONPath.read(json, "$.status")==0){
							lng=((BigDecimal) JSONPath.read(json, "$.result.location.lng")).toString();
							lat=((BigDecimal) JSONPath.read(json, "$.result.location.lat")).toString();
						}
				}
			}else{
				url = "http://api.map.baidu.com/geocoder/v2/?address="+urlP+
						"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
				json = WebCrawler.get(url,null,null);
				if((Integer) JSONPath.read(json, "$.status")==0){
					lng=((BigDecimal) JSONPath.read(json, "$.result.location.lng")).toString();
					lat=((BigDecimal) JSONPath.read(json, "$.result.location.lat")).toString();
				}else if(s.getLocat().getDetail()!=null){
					try {
						urlP = URLEncoder.encode(s.getLocat().getDetail(),"utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();logger.error("UnsupportedEncodingException",e);
					}
					url = "http://api.map.baidu.com/geocoder/v2/?address="+urlP+
							"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
					json = WebCrawler.get(url,null,null);
					if((Integer) JSONPath.read(json, "$.status")==0){
						lng=((BigDecimal) JSONPath.read(json, "$.result.location.lng")).toString();
						lat=((BigDecimal) JSONPath.read(json, "$.result.location.lat")).toString();
					}
				}
			}
			url = "http://api.map.baidu.com/geocoder/v2/?location="+lat+","+lng+
					"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
			json = WebCrawler.get(url,null,null);
			if((Integer) JSONPath.read(json, "$.status")==0){
				province = (String) JSONPath.read(json, "$.result.addressComponent.province");
				city = (String) JSONPath.read(json, "$.result.addressComponent.city");
				s.getLocat().setDetail((String) JSONPath.read(json, "$.result.formatted_address"));
			}
			table="location";
			lable=new String[]{"locatname","city","detail","longitude","latitude","province"};
			data=new String[]{s.getLocat().getName(),city,s.getLocat().getDetail(),lng,lat,province};
//			System.out.println(StringHelper.sqlINSERT(table,lable,data));
			String sql = StringHelper.sqlINSERT(table,lable,data);
			logger.info(sql);
			insert(sql);
			locatId=locationExcit(s.getLocat().getName(),s.getLocat().getDetail());
		}
//			id=getIdByLocatAndDate(t.getLocat().getName(),t.getStartDate(),t.getEndDate(), t.getName());
		if(showId==-1){
			String[] type=new String[]{"演唱会","音乐会","话剧歌剧","舞蹈芭蕾","曲苑杂坛","体育赛事","会议"};
			table="othershow";
			lable=new String[]{"name","picture","startDate","endDate","locat","minprice","maxprice","type","info"};
			data=new String[]{s.getName(),s.getPicture(),StringHelper.sqlDate(s.getStartDate()),StringHelper.sqlDate(s.getEndDate()),
					Integer.toString(locatId),Integer.toString(s.getMINprice()),Integer.toString(s.getMAXprice()),type[s.getShowtype()-1],s.getInfo()};
//			System.out.println(StringHelper.sqlINSERT(table,lable,data));
			String sql = StringHelper.sqlINSERT(table,lable,data);
			logger.info(sql);
			insert(sql);
			showId=tradeExcit(s.getName());
		}else{
			res=false;
		}
		if(urlid==-1){
			table="url";
			lable=new String[]{"url","showid","showtype"};
			data=new String[]{s.getUrl().get(0),Integer.toString(showId),Integer.toString(s.getShowtype())};
			insert(StringHelper.sqlINSERT(table,lable,data));
		}
		return res;
	}

//	public int getIdByLocatAndDate(String locat, Date startDate,
//			Date endDate,String name) {
//		List<String> nameList=new ArrayList<String>(),locatList=new ArrayList<String>();
//		List<Integer> id=new ArrayList<Integer>();
//		
//		String sql="SELECT id,name,locatname FROM Oshow WHERE startdate = '"+StringHelper.sqlDate(startDate)+"'";
//		try {
//			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
//			DBHelper.rs=DBHelper.ps.executeQuery();
//			while(DBHelper.rs.next()){
//				id.add(DBHelper.rs.getInt(1));
//				nameList.add(DBHelper.rs.getString(2));
//				locatList.add(DBHelper.rs.getString(3));
//			}
//			DBHelper.closeConn();
//		} catch (SQLException e) {
//			DBHelper.closeConn();
//e.printStackTrace();logger.error("Exception",e);
//		}
//		String sqlLocat = StringHelper.similarity(locatList,locat,0.7);
//		if(StringHelper.validateString(sqlLocat)){
//			int index = locatList.indexOf(sqlLocat);
//			if(StringHelper.similarity(nameList.get(index),name)>0.8)
//				return id.get(index);
//		}
//		return -1;
//	}
	
	int locationExcit(String locat,String detail){
		int id = -1;
		if(locat==null)
			return id;
		String sql="SELECT locatid FROM location WHERE locatname =\""+locat.replaceAll("\"", "'")+"\"";
		Connection con = null;
			try {
				con = ds.getConnection();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();logger.error("SQLException",e1);
			}
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()){
				id=rs.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
//			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception:"+locat,e);
		try {
			con.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();logger.error("SQLException",e1);
		}
		}
		return id;
	}
	
	int tradeExcit(String name){
		int id = -1;
		if(name==null)
			return id;
		String sql="SELECT id FROM othershow WHERE name =\""+name.replaceAll("\"", "'")+"\"";
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();logger.error("SQLException",e1);
		}
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()){
				id=rs.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
//			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception:"+name,e);
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();logger.error("SQLException",e1);
			}
		}
		return id;
	}
	
	int urlExcit(String name){
		int id = -1;
		String sql="SELECT urlid FROM url WHERE url =\""+name.replaceAll("\"", "'")+"\"";
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();logger.error("SQLException",e1);
		}
		try {
//			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
//			DBHelper.rs=DBHelper.ps.executeQuery();
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()){
				id=rs.getInt(1);
			}
//			DBHelper.closeConn();
			con.close();
		} catch (SQLException e) {
//			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception:"+name,e);
try {
	con.close();
} catch (SQLException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();logger.error("SQLException",e1);
}
		}
		return id;
	}
	
//	void insert(String sql){
//		try {
//			DBHelper.getConn().createStatement().executeUpdate(sql);
//			DBHelper.closeConn();
//		} catch (SQLException e) {
//			DBHelper.closeConn();
//e.printStackTrace();logger.error("Exception:"+sql,e);
//		}
//	}
	
	void insert(String sql){
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();logger.error("SQLException",e1);
		}
		try {
			con.createStatement().executeUpdate(sql);
			con.close();
		} catch (SQLException e) {
e.printStackTrace();logger.error("Exception:"+sql,e);
try {
	con.close();
} catch (SQLException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();logger.error("SQLException",e1);
}
		}
	}
}

