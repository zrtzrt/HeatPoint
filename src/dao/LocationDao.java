package dao;

import entity.LocationEntity;
import entity.ShowEntity;
import entity.TradeEntity;

import java.util.Date;
import java.util.List;

public interface LocationDao {
	
	public boolean saveL(int name,Double lo,Double la);
	public boolean saveD(int id, String d);
	public String getName(int id);
	public String getDetail(int id);
	public List<LocationEntity> getlocation(int type, Date startDate, Date endDate, String p);
	public List<TradeEntity> getInfo0(int id, Date startDate, Date endDate);
	public List<ShowEntity> getInfo1_6(int id, int showtype, Date startDate, Date endDate);
	public List<LocationEntity> search( String k,Date startDate, Date endDate);
	public String getCountP(Date startDate, Date endDate);
	public String getCount(int year);
	public void saveBrowser(String ip,String user);
}
