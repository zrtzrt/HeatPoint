package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import dataSource.AllSiteCrawler;
import dataSource.MeetingFromHDJ;
import dataSource.ShowFrom228;
import dataSource.ShowFromDaMai;
import dataSource.TradeFromCnena;
import dataSource.TradeFromEShow;
import dataSource.WeatherAlarm;

import CrawlerSYS.crawler.DefaultConfig;
import CrawlerSYS.node.CrawlerServer;
import CrawlerSYS.utils.WebCrawler;

import out.Output;


public class CrawlerServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(this.getClass());  
	/**
	 * Constructor of the object.
	 */
	public CrawlerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int actionSign=Integer.parseInt(request.getParameter("action"));
		if(actionSign==0){//actionSign=0表示下载文件
			String fileType = request.getParameter("file");
			String filename = new Date().toString().split(":", 2)[0];
			filename = filename.substring(0, filename.length()-3);
			String path=this.getServletContext().getRealPath("/WEB-INF/outputFile");
			System.out.println(path);
			File file = new File(path+"\\"+filename+"."+fileType);
			if(!file.exists())
				new Output(fileType,path);
			//设置响应头，控制浏览器下载该文件
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
			//读取要下载的文件，保存到文件输入流
			FileInputStream in = new FileInputStream(file.getPath());
			//创建输出流
			OutputStream out = response.getOutputStream();
			//创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			//循环将输入流中的内容读取到缓冲区当中
			while((len=in.read(buffer))>0){
			//输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len);
			}
			out.flush();
			//关闭文件输入流
			in.close();
			//关闭输出流
			out.close();
		}else if(actionSign==1){
			int showtype=Integer.parseInt(request.getParameter("type"));
			String pw = request.getParameter("pw");
			PrintWriter out = response.getWriter();  
			if(pw.equals("crawlerAdmin")){
				out.write("true");
				out.flush();
				out.close();
				if(showtype==0){
//				Spider.create(new tradeFromEShow()).addUrl("http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx?1=1&tag=0&starttime="
//						+StringHelper.getStringTime("yyyy/MM/dd")+"&page=1").thread(3).run();
//				Spider.create(new tradeFromHZH()).addUrl("http://www.haozhanhui.com/zhanlanjihua/").thread(2).run();
//			    Spider.create(new tradeFromCnena()).addUrl("http://www.cnena.com/showroom/list-htm-fid-1.html").thread(2).run();
					new TradeFromEShow().init();
					new TradeFromCnena().init();
				}
				else if(showtype>0&&showtype<7){
					new ShowFromDaMai().init(showtype);
					new ShowFrom228().init(showtype);
				}
				else if(showtype==7)
					new MeetingFromHDJ().init();
				else if(showtype==8)
					new WeatherAlarm().init();
				else
					new AllSiteCrawler().run();
			}else{
				out.write("false");
				out.flush();
				out.close();
			}
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
	
//	public void init(ServletConfig config) throws ServletException {  
//        System.out.println("Log4JInitServlet 正在初始化 log4j日志设置信息");  
//        String log4jLocation = config.getInitParameter("log4j-properties-location");  
//
//        ServletContext sc = config.getServletContext();  
//
//        if (log4jLocation == null) {  
//            System.err.println("*** 没有 log4j-properties-location 初始化的文件, 所以使用 BasicConfigurator初始化");  
//            BasicConfigurator.configure();  
//        } else {  
//            String log4jProp = sc.getRealPath(log4jLocation);
//            File yoMamaYesThisSaysYoMama = new File(log4jProp);  
//            if (yoMamaYesThisSaysYoMama.exists()) {
//                System.out.println("使用: " + log4jProp+"初始化日志设置信息");  
//                PropertyConfigurator.configure(log4jProp);  
//            } else {
//                System.err.println("*** " + log4jProp + " 文件没有找到， 所以使用 BasicConfigurator初始化");  
//                BasicConfigurator.configure();  
//            }
//        }  
//        super.init(config);  
//    }  

	
}
