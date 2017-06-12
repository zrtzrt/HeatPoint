package dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;

import CrawlerSYS.utils.DBHelper;
import CrawlerSYS.utils.StringHelper;


import dao.LocationDao;
import entity.LocationEntity;
import entity.ShowEntity;
import entity.TradeEntity;

public class LocationDaoImpl implements LocationDao{
	private Logger logger = Logger.getLogger(this.getClass());  
	
	public List<LocationEntity> search(String key, Date startDate, Date endDate){
		List<LocationEntity> ll =  new ArrayList<LocationEntity>();

		String[] type=new String[]{"trade","oshow"};
		for (int i = 0; i < type.length; i++) {
			String sql="SELECT distinct locatid,locatName,longitude,latitude,showtype FROM "+type[i]+" WHERE name like \"%"+key+
					"%\" and startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				while(DBHelper.rs.next()){
					LocationEntity l = new LocationEntity();
//					l.setId(DBHelper.rs.getInt(1));
					l.setName(DBHelper.rs.getString(2));
					l.setLongitude(DBHelper.rs.getDouble(3));
					l.setLatitude(DBHelper.rs.getDouble(4));
					l.setId(DBHelper.rs.getInt(5));
					ll.add(l);
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
		}
		return ll;
	}

	@Override
	public List<LocationEntity> getlocation(int type, Date startDate, Date endDate, String p) {
		List<Integer> id = new ArrayList<Integer>();
		List<String> name = new ArrayList<String>();
		List<Double> longitude = new ArrayList<Double>();
		List<Double> latitude = new ArrayList<Double>();
		List<LocationEntity> ll =  new ArrayList<LocationEntity>();
		String sql = null;
		if(type==0)
			sql="SELECT distinct locatid,locatName,longitude,latitude FROM trade WHERE";
		else if(type>0&&type<8)
			sql="SELECT distinct locatid,locatName,longitude,latitude FROM oshow WHERE showtype = "
					+type+" AND";
		if(p!=null)
			sql+=" province like \""+p+"%\" AND";
		sql+=" startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
		try {
			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
			DBHelper.rs=DBHelper.ps.executeQuery();
			while(DBHelper.rs.next()){
				id.add(DBHelper.rs.getInt(1));
				name.add(DBHelper.rs.getString(2));
				longitude.add(DBHelper.rs.getDouble(3));
				latitude.add(DBHelper.rs.getDouble(4));
			}
			DBHelper.closeConn();
		} catch (SQLException e) {
			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
		}
		for (int i = 0; i < id.size(); i++) {
			LocationEntity l = new LocationEntity(name.get(i));
//			int index = ll.indexOf(l);
//			if(index==-1){
//				List<Integer> sid = new ArrayList<Integer>();
//				sid.add(id.get(i));
				l.setId(id.get(i));
				l.setLongitude(longitude.get(i));
				l.setLatitude(latitude.get(i));
				ll.add(l);
//			}else
//				ll.get(index).getId().add(id.get(i));
		}
		return ll;
	}
	public String getCountP(Date startDate, Date endDate) {
//		if(p!=null){
			String sql="SELECT count(*),province FROM trade where startdate BETWEEN '"+StringHelper.sqlDate(startDate)
				+"' and '"+StringHelper.sqlDate(endDate)+"' group by province",res="{\"series\":[{\"name\":\"展会\",\"data\":[";
			int[] max = new int[34];
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				int i=0;
				while(DBHelper.rs.next()){
					res+="{\"name\":\""+DBHelper.rs.getString(2).substring(0, 2)+"\",\"value\":"+DBHelper.rs.getInt(1)+"}";
//					max[0] = Math.max(max[0], DBHelper.rs.getInt(1));
					max[i++] = DBHelper.rs.getInt(1);
					if(!DBHelper.rs.isLast())
						res+=",";
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
			res+="]},";
			sql="SELECT count(*),province,showtype FROM oshow where startdate BETWEEN '"+StringHelper.sqlDate(startDate)
					+"' and '"+StringHelper.sqlDate(endDate)+"' group by province,showtype";
			String[][][] data=new String[7][34][2];
			int[] i = new int[7];
				try {
					DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
					DBHelper.rs=DBHelper.ps.executeQuery();
					while(DBHelper.rs.next()){
						int t=DBHelper.rs.getInt(3)-1;
						data[t][i[t]][0]=DBHelper.rs.getString(2);
						if(data[t][i[t]][0]!=null)
							data[t][i[t]][0]=data[t][i[t]][0].substring(0, 2);
						data[t][i[t]++][1]=DBHelper.rs.getString(1);
//						max[t+1] = Math.max(max[t+1], DBHelper.rs.getInt(1));
					}
					DBHelper.closeConn();
				} catch (SQLException e) {
					DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
				}
				int[] sumMax= new int[34];
				for (int j2 = 0; j2 < max.length; j2++) {
					sumMax[j2]+=max[j2];
				}
				for (int j = 0; j < data.length; j++) {
					for (int j2 = 0; j2 < data[j].length; j2++) {
						if(data[j][j2][1]!=null)
							sumMax[j2]+=Integer.parseInt(data[j][j2][1]);
					}
				}
				int fmax=0;
				for (int j = 0; j < sumMax.length; j++) {
					fmax = Math.max(fmax, sumMax[j]);
				}
			String[] type=new String[]{"演唱会","音乐会","话剧歌剧","舞蹈芭蕾","曲苑杂坛","体育赛事","会议"};
			for (int j = 0; j < data.length; j++) {
				res+="{\"name\":\""+type[j]+"\",\"data\":[";
				for (int j2 = 0; j2 < i[j]; j2++) {
					res+="{\"name\":\""+data[j][j2][0]+"\",\"value\":"+data[j][j2][1]+"}";
					if(j2!=i[j]-1)
						res+=",";
				}
				res+="]}";
				if(j!=data.length-1)
					res+=",";
				else res+="],\"max\":"+fmax+"}";
			}
			
			return res;
		}
	
	public String getCount(int year) {
//		if(p!=null){
			String sql="SELECT count(*),startdate FROM trade where startdate BETWEEN '"+year+"-1-1' and '"+year
				+"-12-31' group by startdate",res="{\"series\":[{\"name\":\"展会\",\"data\":[";
			int[] max = new int[365];
//			for (int i = 0; i < max.length; i++) {
//				max[i]=0;
//			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				int i=0;
				while(DBHelper.rs.next()){
					Date d = null;
					try {
						d = sdf.parse(DBHelper.rs.getString(2));
					} catch (ParseException e) {
e.printStackTrace();logger.error("Exception",e);
					}
					res+="["+(d.getDate()-1)+","+d.getMonth()+","+DBHelper.rs.getInt(1)+"]";
//					max[0] = Math.max(max[0], DBHelper.rs.getInt(1));
					max[i++] = DBHelper.rs.getInt(1);
					if(!DBHelper.rs.isLast())
						res+=",";
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
			res+="]},";
			sql="SELECT count(*),startdate,showtype FROM oshow where startdate BETWEEN '"+year
					+"-1-1' and '"+year+"-12-31' group by startdate,showtype";
			String[][][] data=new String[7][365][2];
			int[] i = new int[7];
				try {
					DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
					DBHelper.rs=DBHelper.ps.executeQuery();
					while(DBHelper.rs.next()){
						int t=DBHelper.rs.getInt(3)-1;
						data[t][i[t]][0]=DBHelper.rs.getString(2);
						data[t][i[t]++][1]=DBHelper.rs.getString(1);
//						max[t+1] = Math.max(max[t+1], DBHelper.rs.getInt(1));
//						max[t][i[t]-1] = DBHelper.rs.getInt(1);
					}
					DBHelper.closeConn();
				} catch (SQLException e) {
					DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
				}
//				int[] sumMax= new int[365];
				int fmax=0;
				for (int j2 = 0; j2 < max.length; j2++) {
					fmax = Math.max(fmax, max[j2]);
//					sumMax[j2]+=max[j2];
				}
				for (int j = 0; j < data.length; j++) {
					for (int j2 = 0; j2 < data[j].length; j2++) {
						if(data[j][j2][1]!=null)
							fmax = Math.max(fmax, Integer.parseInt(data[j][j2][1]));
//							sumMax[j2]+=Integer.parseInt(data[j][j2][1]);
					}
				}
//				for (int j = 0; j < sumMax.length; j++) {
//					fmax = Math.max(fmax, sumMax[j]);
//				}
			String[] type=new String[]{"演唱会","音乐会","话剧歌剧","舞蹈芭蕾","曲苑杂坛","体育赛事","会议"};
			for (int j = 0; j < data.length; j++) {
				res+="{\"name\":\""+type[j]+"\",\"data\":[";
				for (int j2 = 0; j2 < i[j]; j2++) {
					Date d = null;
					try {
						d = sdf.parse(data[j][j2][0]);
					} catch (ParseException e) {
e.printStackTrace();logger.error("Exception",e);
					}
					res+="["+(d.getDate()-1)+","+d.getMonth()+","+data[j][j2][1]+"]";
					if(j2!=i[j]-1)
						res+=",";
				}
				res+="]}";
				if(j!=data.length-1)
					res+=",";
				else res+="],\"max\":"+fmax+"}";
			}
			
			return res;
		}
//		String sql="SELECT count(*),startdate,showtype FROM trade where startdate BETWEEN '"+StringHelper.sqlDate(startDate)
//				+"' and '"+StringHelper.sqlDate(endDate)+"' group by startdate";
//		try {
//			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
//			DBHelper.rs=DBHelper.ps.executeQuery();
//			while(DBHelper.rs.next()){
//				sql=DBHelper.rs.getString(1);
//			}
//			DBHelper.closeConn();
//		} catch (SQLException e) {
//			DBHelper.closeConn();
//			e.printStackTrace();logger.error("Exception",e);
//		}
//		return sql;
//		return p;
//	}
	public String getDetail(int id) {
		String sql="SELECT detail FROM location WHERE locatid ="+id;
		try {
			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
			DBHelper.rs=DBHelper.ps.executeQuery();
			while(DBHelper.rs.next()){
				sql=DBHelper.rs.getString(1);
			}
			DBHelper.closeConn();
		} catch (SQLException e) {
			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
		}
		return sql;
		
	}
	@Override
	public boolean saveL(int id, Double lng, Double lat) {
		String sql = "UPDATE location SET longitude='"+lng+"',latitude='"+lat+"' WHERE locatid="+id;
		boolean res = false;
		try {
			res=DBHelper.getConn().prepareStatement(sql).executeUpdate()==1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
		}
		return res;
	}
	
	public boolean saveD(int id, String d) {
		String sql = "UPDATE location SET detail='"+d+"' WHERE locatid="+id;
		boolean res = false;
		try {
			res=DBHelper.getConn().prepareStatement(sql).executeUpdate()==1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
		}
		return res;
	}
	
	public String getName(int id) {
		String sql = "SELECT locatname From location WHERE locatid="+id;
		String res = null;
		try {
			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
			DBHelper.rs=DBHelper.ps.executeQuery();
			while(DBHelper.rs.next()){
				res=DBHelper.rs.getString(1);
			}
			DBHelper.closeConn();
		} catch (SQLException e) {
			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
		}
		return res;
	}

//	public <T> List<T> getInfo(int id, int type, Date startDate, Date endDate) {
//		List<T> ol = new ArrayList<Object>();
//		String sql = null;
//		if(type==0){
//			sql="SELECT name,en_name,picture,startDate,endDate,locat,industry,host,area,times,frequency,used,hot FROM trade WHERE locatid = "
//					+id+" AND  startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
//			try {
//				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
//				DBHelper.rs=DBHelper.ps.executeQuery();
//				while(DBHelper.rs.next()){
//					TradeEntity t = new TradeEntity(0,DBHelper.rs.getString(1),
//							DBHelper.rs.getString(2),DBHelper.rs.getString(3),
//							new java.util.Date (DBHelper.rs.getDate(4).getTime()),
//							new java.util.Date (DBHelper.rs.getDate(5).getTime()),
//							new LocationEntity(DBHelper.rs.getString(6)),
//							DBHelper.rs.getString(7),DBHelper.rs.getString(8),
//							DBHelper.rs.getInt(9),DBHelper.rs.getInt(10),
//							DBHelper.rs.getString(11),DBHelper.rs.getString(12),
//							null,DBHelper.rs.getInt(13)
//							);
//					ol.add(t);
//				}
//				DBHelper.closeConn();
//				return ol;
//			} catch (SQLException e) {
//				DBHelper.closeConn();
//				e.printStackTrace();logger.error("Exception",e);
//			}
//		}
//		if(type>0&&type<7){
//			sql="SELECT name,picture,startdate,enddate,locat,minprice,maxprice,info FROM show WHERE locatid = "
//					+id+" AND  startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
//			try {
//				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
//				DBHelper.rs=DBHelper.ps.executeQuery();
//				while(DBHelper.rs.next()){
//					ShowEntity s = new ShowEntity(0,DBHelper.rs.getString(1),DBHelper.rs.getString(3),
//							new java.util.Date (DBHelper.rs.getDate(4).getTime()),
//							new java.util.Date (DBHelper.rs.getDate(5).getTime()),
//							new LocationEntity(DBHelper.rs.getString(6)),
//							DBHelper.rs.getInt(9),DBHelper.rs.getInt(10),
//							type,DBHelper.rs.getString(1),null
//							);
//					ol.add(s);
//				}
//				DBHelper.closeConn();
//			} catch (SQLException e) {
//				DBHelper.closeConn();
//				e.printStackTrace();logger.error("Exception",e);
//			}
//		
//		}
//		return ol;
//		
//	}
	
	public List<TradeEntity> getInfo0(int id, Date startDate, Date endDate) {
		List<TradeEntity> ol = new ArrayList<TradeEntity>();
		LinkedHashSet<String> nl = new LinkedHashSet<String>();
		String sql = null;
			sql="SELECT name,en_name,picture,startDate,endDate,locatname,industry,host,area,times,frequency,used,url,detail FROM trade WHERE locatid = "
					+id+" AND  startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				while(DBHelper.rs.next()){
					if(nl.add(DBHelper.rs.getString(1))){
						List<String> ul = new ArrayList<String>();
						ul.add(DBHelper.rs.getString(13));
						TradeEntity t = new TradeEntity(0,DBHelper.rs.getString(1),
							DBHelper.rs.getString(2),DBHelper.rs.getString(3),
							new java.util.Date (DBHelper.rs.getDate(4).getTime()),
							new java.util.Date (DBHelper.rs.getDate(5).getTime()),
							new LocationEntity(DBHelper.rs.getString(6),DBHelper.rs.getString(14)),
							DBHelper.rs.getString(7),DBHelper.rs.getString(8),
							DBHelper.rs.getInt(9),DBHelper.rs.getInt(10),
							DBHelper.rs.getString(11),DBHelper.rs.getString(12),
							ul);
						ol.add(t);
					}else{
						for (int i = 0; i < ol.size(); i++) {
							if(ol.get(i).getName().equals(DBHelper.rs.getString(1)))
								ol.get(i).getUrl().add(DBHelper.rs.getString(13));
						}
					}
				}
				DBHelper.closeConn();
				return ol;
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
		return ol;
	}
	
	public List<ShowEntity> getInfo1_6(int id, int type, Date startDate, Date endDate) {
		List<ShowEntity> ol = new ArrayList<ShowEntity>();
		LinkedHashSet<String> nl = new LinkedHashSet<String>();
		String sql = null;
			sql="SELECT name,picture,startdate,enddate,locatname,minprice,maxprice,info,url,detail FROM oshow WHERE locatid = "
					+id+" AND showtype = "+type+" AND startdate BETWEEN '"+StringHelper.sqlDate(startDate)+"' and '"+StringHelper.sqlDate(endDate)+"'";
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				while(DBHelper.rs.next()){
					if(nl.add(DBHelper.rs.getString(1))){
						List<String> ul = new ArrayList<String>();
						ul.add(DBHelper.rs.getString(9));
						ShowEntity s = new ShowEntity(0,DBHelper.rs.getString(1),DBHelper.rs.getString(2),
							DBHelper.rs.getDate(3)!=null?new java.util.Date (DBHelper.rs.getDate(3).getTime()):null,
							DBHelper.rs.getDate(4)!=null?new java.util.Date (DBHelper.rs.getDate(4).getTime()):null,
							new LocationEntity(DBHelper.rs.getString(5),DBHelper.rs.getString(10)),
							DBHelper.rs.getInt(6),DBHelper.rs.getInt(7),
							type,DBHelper.rs.getString(8),ul
							);
						ol.add(s);
					}else{
						for (int i = 0; i < ol.size(); i++) {
							if(ol.get(i).getName().equals(DBHelper.rs.getString(1)))
								ol.get(i).getUrl().add(DBHelper.rs.getString(9));
						}
					}
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
		return ol;
		
	}
}
