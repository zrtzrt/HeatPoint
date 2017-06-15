package entity;

import CrawlerSYS.utils.StringHelper;

public class LocationEntity {
	private int id;
	private int type;
	private String  name;
	private String  city;
	private String  province;
	private String  detail;
	private Double  longitude;
	private Double  latitude;
	
	public LocationEntity(String name){
		this.name=name;
	}
	public LocationEntity(String name,String d){
		this.name=name;
		detail=d;
	}
	
	public LocationEntity() {
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		return "LocationEntity [id=" + id + ", name=" + name + ", city=" + city
				+ ", detail=" + detail + ", longitude=" + longitude
				+ ", latitude=" + latitude + "]";
	}
	
	public boolean equals(Object obj) {   
        if (obj instanceof LocationEntity) {   
        	LocationEntity l = (LocationEntity) obj;   
            return StringHelper.similarity(this.name,l.name)>0.8;
        }   
        return super.equals(obj);
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
