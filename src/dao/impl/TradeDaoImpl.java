package dao.impl;

import CrawlerSYS.utils.StringHelper;
import CrawlerSYS.utils.WebCrawler;
import com.alibaba.fastjson.JSONPath;
import com.zaxxer.hikari.HikariDataSource;
import dao.TradeDao;
import entity.TradeEntity;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

//import CrawlerSYS.utils.DBHelper;

public class TradeDaoImpl implements TradeDao{
	private Logger logger = Logger.getLogger(this.getClass());  
	private HikariDataSource ds;
	
	public TradeDaoImpl(HikariDataSource ds){
		this.ds = ds;
	}
	
	@Override
	public boolean save(TradeEntity t) {
		if(t.getStartDate().before(new Date()))
			return false;
		String[] lable,data = null;
		String table;
		boolean res = true;
		int tradeId=0,locatId = 0,urlid = urlExcit(t.getUrl().get(0));
//				tradeId=getIdByLocatAndDate(t.getLocat().getName(),t.getStartDate(),t.getEndDate(), t.getName()),
		if(urlid==-1)
			tradeId=tradeExcit(t.getName());
		else {
			int h = getHot(t.getName());
			if(h<t.getHot())
				saveHot(t.getName(),h);
		}
		if(tradeId==-1)
			locatId=locationExcit(t.getLocat().getName(),t.getLocat().getDetail());
//			urlid=urlExcit(t.getUrl().get(0));
		if(locatId==-1){
			String urlP = null;
			try {
				urlP = URLEncoder.encode(t.getLocat().getName(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();logger.error("UnsupportedEncodingException",e);
			}
			String url = "http://api.map.baidu.com/geocoder/v2/?address="+urlP+
					"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M",province = null,city = null;
			String json = WebCrawler.get(url,null,null);
			BigDecimal lng = null,lat = null;
			if((Integer) JSONPath.read(json, "$.status")==0){
				lng=(BigDecimal) JSONPath.read(json, "$.result.location.lng");
				lat=(BigDecimal) JSONPath.read(json, "$.result.location.lat");
			}else if(t.getLocat().getDetail()!=null){
				try {
					urlP = URLEncoder.encode(t.getLocat().getDetail(),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();logger.error("UnsupportedEncodingException",e);
				}
				url = "http://api.map.baidu.com/geocoder/v2/?address="+urlP+
						"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
				json = WebCrawler.get(url,null,null);
				if((Integer) JSONPath.read(json, "$.status")==0){
					lng=(BigDecimal) JSONPath.read(json, "$.result.location.lng");
					lat=(BigDecimal) JSONPath.read(json, "$.result.location.lat");
				}
			}
			url = "http://api.map.baidu.com/geocoder/v2/?location="+lat+","+lng+
					"&output=json&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M";
			json = WebCrawler.get(url,null,null);
			if((Integer) JSONPath.read(json, "$.status")==0){
				province = (String) JSONPath.read(json, "$.result.addressComponent.province");
				city = (String) JSONPath.read(json, "$.result.addressComponent.city");
				t.getLocat().setDetail((String) JSONPath.read(json, "$.result.formatted_address"));
			}
			table="location";
			lable=new String[]{"locatname","city","detail","longitude","latitude","province"};
			data=new String[]{t.getLocat().getName(),city,t.getLocat().getDetail(),lng.toString(),lat.toString(),province};
//			System.out.println(StringHelper.sqlINSERT(table,lable,data));
			String sql = StringHelper.sqlINSERT(table,lable,data);
			logger.info(sql);
			insert(sql);
			locatId=locationExcit(t.getLocat().getName(),t.getLocat().getDetail());
		}
//			id=getIdByLocatAndDate(t.getLocat().getName(),t.getStartDate(),t.getEndDate(), t.getName());
		if(tradeId==-1){
			table="tradeshow";
			lable=new String[]{"name","en_name","picture","startDate","endDate","locat","industry",
					"host","area","times","frequency","used","hot"};
			data=new String[]{t.getName(),t.getEn_name(),t.getPicture(),StringHelper.sqlDate(t.getStartDate()),
					StringHelper.sqlDate(t.getEndDate()),Integer.toString(locatId),t.getIndustry(),t.getHost(),
					Integer.toString(t.getArea()),Integer.toString(t.getTimes()),t.getFrequency(),t.getUsed(),
					Integer.toString(t.getHot())};
//			System.out.println(StringHelper.sqlINSERT(table,lable,data));
			String sql = StringHelper.sqlINSERT(table,lable,data);
			logger.info(sql);
			insert(sql);
			tradeId=tradeExcit(t.getName());
		}
		if(urlid==-1){
			table="url";
			lable=new String[]{"url","tradeid","showtype"};
			data=new String[]{t.getUrl().get(0),Integer.toString(tradeId),"0"};
			insert(StringHelper.sqlINSERT(table,lable,data));
		}
//			try {
//				DBHelper.ps=DBHelper.getConn().prepareStatement(StringHelper.sqlINSERT(table,lable));
//				DBHelper.ps.setString(1, t.getUrl().get(0));
//				DBHelper.ps.setInt(2, id);
//				DBHelper.ps.setInt(3, 1);
//				DBHelper.ps.executeUpdate();
//				DBHelper.closeConn();
//			} catch (SQLException e) {
//				DBHelper.closeConn();
//				e.printStackTrace();logger.error("Exception",e);
//			}
//				try {
//					DBHelper.ps=DBHelper.getConn().prepareStatement(StringHelper.sqlINSERT(table,lable));
//					DBHelper.ps.setString(1, t.getName());
//					DBHelper.ps.setString(2, t.getEn_name());
//					DBHelper.ps.setString(3, t.getPicture());
//					DBHelper.ps.setDate(4, new java.sql.Date(t.getStartDate().getTime()));
//					DBHelper.ps.setDate(5, new java.sql.Date(t.getEndDate().getTime()));
//					DBHelper.ps.setInt(6, id);
//					DBHelper.ps.setString(7, t.getIndustry());
//					DBHelper.ps.setString(8, t.getHost());
//					DBHelper.ps.setInt(9, t.getArea());
//					DBHelper.ps.setInt(10, t.getTimes());
//					DBHelper.ps.setString(11, t.getFrequency());
//					DBHelper.ps.setString(12, t.getCondition());
//					DBHelper.ps.setString(13, t.getUsed());
//					DBHelper.ps.executeUpdate();
//					DBHelper.closeConn();
//				} catch (SQLException e) {
//					DBHelper.closeConn();
//					e.printStackTrace();logger.error("Exception",e);
//				}
				
//			table="trade";
//			lable=new String[]{"name","en_name","picture","startDate","endDatedate","industry",
//					"host","area","times","frequency","condition","used","locatname","city","detail","url","showtype"};
//			
//			data=new String[]{t.getName(),t.getEn_name(),t.getPicture(),StringHelper.sqlDate(t.getStartDate()),
//					StringHelper.sqlDate(t.getEndDate()),t.getIndustry(),t.getHost(),
//					Integer.toString(t.getArea()),Integer.toString(t.getTimes()),t.getFrequency(),t.getCondition(),
//					t.getUsed(),t.getLocat().getName(),t.getLocat().getCity(),t.getLocat().getDetail(),t.getUrl().get(0),"1"};
			
//			try {
//				DBHelper.ps=DBHelper.getConn().prepareStatement(StringHelper.sqlINSERT(table,lable));
//				DBHelper.ps.setString(1, t.getName());
//				DBHelper.ps.setString(2, t.getEn_name());
//				DBHelper.ps.setString(3, t.getPicture());
//				DBHelper.ps.setDate(4, new java.sql.Date(t.getStartDate().getTime()));
//				DBHelper.ps.setDate(5, new java.sql.Date(t.getEndDate().getTime()));
//				DBHelper.ps.setInt(6, id);
//				DBHelper.ps.setString(7, t.getIndustry());
//				DBHelper.ps.setString(8, t.getHost());
//				DBHelper.ps.setInt(9, t.getArea());
//				DBHelper.ps.setInt(10, t.getTimes());
//				DBHelper.ps.setString(11, t.getFrequency());
//				DBHelper.ps.setString(12, t.getCondition());
//				DBHelper.ps.setString(13, t.getUsed());
//				DBHelper.ps.setString(14, t.getLocat().getName());
//				DBHelper.ps.setString(15, t.getLocat().getCity());
//				DBHelper.ps.setString(16, t.getLocat().getDetail());
//				DBHelper.ps.setString(17, t.getUrl().get(0));
//				DBHelper.ps.setInt(18, 1);
//				DBHelper.ps.executeUpdate();
//				DBHelper.closeConn();
//			} catch (SQLException e) {
//				DBHelper.closeConn();
//				e.printStackTrace();logger.error("Exception",e);
//			}

//		try {
//			System.out.println(StringHelper.sqlINSERT(table,lable,data));
//			DBHelper.getConn().createStatement().executeUpdate(StringHelper.sqlINSERT(table,lable,data));
//			DBHelper.closeConn();
//		} catch (SQLException e) {
//			DBHelper.closeConn();
//			e.printStackTrace();logger.error("Exception",e);
//		}
		
		return res;
	}

//	public int getIdByLocatAndDate(String locat, Date startDate,
//			Date endDate,String name) {
//		List<String> nameList=new ArrayList<String>(),locatList=new ArrayList<String>();
//		List<Integer> id=new ArrayList<Integer>();
//		
//		String sql="SELECT id,name,locatname FROM trade WHERE startdate BETWEEN '"+StringHelper.sqlDate(startDate)
//				+"' and '"+StringHelper.sqlDate(endDate)+"'";
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
//			e.printStackTrace();logger.error("Exception",e);
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
		String sql="SELECT locatid FROM location WHERE locatname =\""+locat.replaceAll("\"", "'")+
				"\"";
		if(detail!=null)
			sql = sql+" OR detail =\""+detail.replaceAll("\"", "'")+"\"";
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
		String sql="SELECT id FROM tradeshow WHERE name =\""+name.replaceAll("\"", "'")+"\"";
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
	
	int getHot(String name){
		int id = -1;
		String sql="SELECT hot FROM tradeshow WHERE name =\""+name.replaceAll("\"", "'")+"\"";
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
	
	void saveHot(String name,int hot){
		insert("UPDATE tradeshow SET hot="+hot+" WHERE name=\""+name.replaceAll("\"", "'")+"\"");
//		System.out.println("UPDATA tradeshow SET hot="+hot+" WHERE namne=\""+name+"\"");
		logger.info("UPDATE tradeshow SET hot="+hot+" WHERE name=\""+name+"\"");
	}
	
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
