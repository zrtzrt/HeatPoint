package entity;

import CrawlerSYS.utils.StringHelper;

import java.util.Date;
import java.util.List;

public class TradeEntity {
	private int id;
	private String  name;
	private String  en_name;
	private String  picture;
	private Date  startDate;
	private Date  endDate;
	private LocationEntity locat;
	private String  industry;
	private String  host;
	private int  area;
	private int  times;
	private String  frequency;
	private String  used;
	private List<String> url;
	private int  hot;
	
	public TradeEntity(int id, String name, String en_name, String picture,
			Date startDate, Date endDate, LocationEntity locat,
			String industry, String host, int area, int times,
			String frequency, String used, List<String> url,int  hot) {
		super();
		this.id = id;
		this.name = name;
		this.en_name = en_name;
		this.picture = picture;
		this.startDate = startDate;
		this.endDate = endDate;
		this.locat = locat;
		this.industry = industry;
		this.host = host;
		this.area = area;
		this.times = times;
		this.frequency = frequency;
		this.used = used;
		this.url = url;
		this.hot = hot;
	}
	public TradeEntity() {
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
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String en_name) {
		this.en_name = en_name;
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public List<String> getUrl() {
		return url;
	}
	public void setUrl(List<String> url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "TradeEntity [id=" + id + ", name=" + name + ", en_name="
				+ en_name + ", picture=" + picture + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", locat=" + locat + ", industry="
				+ industry + ", host=" + host + ", area=" + area + ", times="
				+ times + ", frequency=" + frequency + ", used=" + used
				+ ", url=" + url + "]";
	}
	
	public String toJson() {
		return "{\"name\":\"" + name + "\", \"en_name\":\""
				+ en_name + "\", \"picture\":\"" + picture + "\", \"startDate\":\"" + StringHelper.sqlDate(startDate)
				+ "\", \"endDate\":\"" + StringHelper.sqlDate(endDate) + "\", \"locat\":\"" + locat.getName() + "\", \"detail\":\""
				+ locat.getDetail() + "\", \"industry\":\""+ industry + "\", \"hot\":\"" + hot
				+ "\", \"host\":\"" + host + "\", \"area\":\"" + area + "\", \"times\":\"" + times + "\", \"frequency\":\""
				+ frequency + "\", \"used\":\"" + used + "\", \"urls\":" + toUrl() + ",\"showtype\":0}";
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
	public int getHot() {
		return hot;
	}
	public void setHot(int hot) {
		this.hot = hot;
	}
}
