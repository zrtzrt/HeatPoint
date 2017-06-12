package entity;

import java.util.Date;
import java.util.List;

import CrawlerSYS.utils.StringHelper;

public class ShowEntity {
	private int id;
	private String  name;
	private String  picture;
	private Date  startDate;
	private Date  endDate;
	private LocationEntity locat;
	private int  MINprice;
	private int  MAXprice;
	private int  showtype;
	private String  info;
	private List<String> url;
	
	public ShowEntity(int id, String name, String picture, Date startDate,
			Date endDate, LocationEntity locat, int mINprice, int mAXprice,
			int showtype, String info, List<String> url) {
		super();
		this.id = id;
		this.name = name;
		this.picture = picture;
		this.startDate = startDate;
		this.endDate = endDate;
		this.locat = locat;
		MINprice = mINprice;
		MAXprice = mAXprice;
		this.showtype = showtype;
		this.info = info;
		this.url = url;
	}
	public ShowEntity() {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public LocationEntity getLocat() {
		return locat;
	}
	public void setLocat(LocationEntity locat) {
		this.locat = locat;
	}
	public int getMINprice() {
		return MINprice;
	}
	public void setMINprice(int mINprice) {
		MINprice = mINprice;
	}
	public int getMAXprice() {
		return MAXprice;
	}
	public void setMAXprice(int mAXprice) {
		MAXprice = mAXprice;
	}
	public int getShowtype() {
		return showtype;
	}
	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<String> getUrl() {
		return url;
	}
	public void setUrl(List<String> url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ShowEntity [id=" + id + ", name=" + name + ", picture="
				+ picture + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", locat=" + locat + ", MINprice=" + MINprice + ", MAXprice="
				+ MAXprice + ", showtype=" + showtype + ", info=" + info
				+ ", url=" + url + "]";
	}
	
	public String toJson() {
		return "{ \"name\":\"" + name + "\", \"picture\":\""
				+ picture + "\", \"startDate\":\"" + StringHelper.sqlDate(startDate) + "\", \"endDate\":\""
				+ StringHelper.sqlDate(endDate)+ "\", \"locat\":\"" + locat.getName() + "\", \"MINprice\":\""
				+ MINprice + "\", \"MAXprice\":\"" + MAXprice + "\", \"showtype\":\"" + showtype
				+ "\", \"info\":\"" + info + "\", \"urls\":" + toUrl() + "}";
	}
	String toUrl(){
		String ul="[";
		for (int i = 0; i < url.size(); i++) {
			ul+="{\"url\":\""+url.get(i)+"\"}";
			if(i!=url.size()-1)
				ul+=",";
		}
		return ul+="]";
	}
}
