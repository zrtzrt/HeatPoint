package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import CrawlerSYS.utils.DBHelper;
import CrawlerSYS.utils.StringHelper;

import dao.LocationDao;
import dao.impl.LocationDaoImpl;
import entity.LocationEntity;
import entity.ShowEntity;
import entity.TradeEntity;


public class LocationServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(this.getClass());  
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int actionSign=Integer.parseInt(request.getParameter("action"));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");//小写的mm表示的是分钟
		Date startDate = null,endDate = null;
		response.setContentType("application/xml;utf-8");
		response.setCharacterEncoding("utf-8");
		try {
			String t = request.getParameter("start");
			if(t!=null)
				startDate = sdf.parse(t);
			else startDate = new Date();
			t=request.getParameter("end");
			if(t!=null)
				endDate=sdf.parse(t);
			else endDate=startDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
		}
		LocationDao dao=new LocationDaoImpl();
		if(actionSign==0){//actionSign=0表示获取地址
			int showtype=Integer.parseInt(request.getParameter("type"));
			String p = request.getParameter("p");
			if(p!=null){
				p = new String(p.getBytes("iso8859-1"),"UTF-8");
//				System.out.println(p);
			}
			PrintWriter out = response.getWriter();
			if(showtype<8){
				List<LocationEntity> ll = dao.getlocation(showtype,startDate,endDate,p);
//			System.out.println(ll);
				out.write("{\"location\":[");
				for (int i = 0; i < ll.size(); i++) {
					out.write("{\"id\":\""+ll.get(i).getId()+"\"");
					out.write(",\"name\":\""+ll.get(i).getName()+"\"");
					if(ll.get(i).getLatitude()!=null&&ll.get(i).getLatitude()!=0){
						out.write(",\"x\":\""+ll.get(i).getLongitude()+"\"");
						out.write(",\"y\":\""+ll.get(i).getLatitude()+"\"");
					}
					if(i==ll.size()-1)
						out.write("}");
					else out.write("},");
				}
				out.write("],\"size\":"+ll.size()+"}");
			}else{
				String[] lable=new String[]{"locat","file","type","color"};
				String sql=StringHelper.sqlSELECT("weatheralarm", lable, null);
				out.write("{\"weatheralarm\":[");
				try {
					DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
					DBHelper.rs=DBHelper.ps.executeQuery();
					while(DBHelper.rs.next()){
						out.write("{\"locat\":\""+DBHelper.rs.getString(1)+"\"");
						out.write(",\"file\":\""+DBHelper.rs.getString(2)+"\"");
						out.write(",\"type\":\""+DBHelper.rs.getString(3)+"\"");
						out.write(",\"color\":\""+DBHelper.rs.getString(4)+"\"");
						if(DBHelper.rs.isLast())
							out.write("}");
						else out.write("},");
					}
					out.write("]}");
					DBHelper.closeConn();
				} catch (SQLException e) {
					DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
				}
			}
			out.flush();
			out.close();
		}else
		if(actionSign==1){//actionSign=1表示set地址
			Double lng = Double.parseDouble(request.getParameter("lng"));
			Double lat = Double.parseDouble(request.getParameter("lat"));
			int id = Integer.parseInt(request.getParameter("id"));
			if(dao.saveL(id, lng, lat))
				System.out.println("save id, lng, lat"+id+lng+ lat);
		}else
		if(actionSign==2){//actionSign=2表示获取info
			int id=Integer.parseInt(request.getParameter("id"));
			int showtype=Integer.parseInt(request.getParameter("type"));
			PrintWriter out = response.getWriter();  
			out.write("{\"show\":[");
			if(showtype == 0){
				List<TradeEntity> ol =dao.getInfo0(id,startDate,endDate);
				for (int i = 0; i < ol.size(); i++) {
					out.write(ol.get(i).toJson());
					if(i!=ol.size()-1)
						out.write(",");
				}
				out.write("],\"size\":"+ol.size()+"}");
				out.flush();
				out.close();
			}else if(showtype>0&&showtype<8){
				List<ShowEntity> ol =dao.getInfo1_6(id,showtype, startDate,endDate);
				for (int i = 0; i < ol.size(); i++) {
					out.write(ol.get(i).toJson());
					if(i!=ol.size()-1)
						out.write(",");
				}
				out.write("],\"size\":"+ol.size()+"}");
				out.flush();
				out.close();
			}
		}else
		if(actionSign==3){
			int id=Integer.parseInt(request.getParameter("id"));
			PrintWriter out = response.getWriter();  
			String d = dao.getDetail(id);
			if(d!=null)
				out.write("{\"det\":\""+d+"\"}");
			else {
//				out.write(LocatFromDaMai.getLocation(id));
				out.write("{\"det\":\"null\"}");
			}
			out.flush();
			out.close();
		}else
		if(actionSign==4){
//			String p = request.getParameter("p");
			PrintWriter out = response.getWriter();  
			String d = dao.getCountP(startDate, endDate);
			if(d!=null)
				out.write(d);
			out.flush();
			out.close();
		}else
		if(actionSign==5){
			String k = request.getParameter("key");
			k = new String(k.getBytes("iso8859-1"),"UTF-8");
//			System.out.println(k);
			List<LocationEntity> ll = dao.search(k,startDate,endDate);
//			System.out.println(ll);
			PrintWriter out = response.getWriter();  
				out.write("{\"location\":[");
				for (int i = 0; i < ll.size(); i++) {
//					out.write("{\"id\":\""+ll.get(i).getId()+"\"");
					out.write("{\"name\":\""+ll.get(i).getName()+"\"");
					out.write(",\"type\":\""+ll.get(i).getId()+"\"");
					if(ll.get(i).getLatitude()!=null&&ll.get(i).getLatitude()!=0){
						out.write(",\"x\":\""+ll.get(i).getLongitude()+"\"");
						out.write(",\"y\":\""+ll.get(i).getLatitude()+"\"");
					}
					if(i==ll.size()-1)
						out.write("}");
					else out.write("},");
				}
				out.write("],\"size\":"+ll.size()+"}");
				out.flush();
				out.close();
		}else
			if(actionSign==6){
				PrintWriter out = response.getWriter();  
				String d = dao.getCount(new Date().getYear()+1900);
				if(d!=null)
					out.write(d);
				out.flush();
				out.close();
			}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
//		new CrawlerServer(6543).start();
	}

}
