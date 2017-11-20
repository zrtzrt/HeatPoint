package out;

import CrawlerSYS.utils.DBHelper;
import CrawlerSYS.utils.StringHelper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


public class Output {
	private Logger logger = Logger.getLogger(this.getClass());  
//	private final String[] title = {"基本属性","主办方级别","主办方类型","主要影响年龄阶段"};
	private final String[] title2 = {"事件名称","结束日期","举办城市","是否是国际性组织","是否是国家政府",
			"是否是省政府","是否是地方级政府","是否是国内民间协会","是否是国际民间协会","是否是国内行业协会",
			"是否是国际行业协会","主要影响年龄层为儿童","主要影响年龄层为青年","主要影响年龄层为成年","主要影响年龄层为老年",
			"是否有固定的参与人群","是否影响商务人群","是否影响社会大众","最大影响全球","最大影响洲际",
			"最大影响全国","最大影响全省","最大影响全市","是否是展会","是否是演唱会",
			"是否是体育赛事","是否是会议","是否是地方性节假日","事件热度","事件历史悠久程度",
			"事件一年内频率","开始日期"};
	
	public Output(String type,String path){
		long startTime ,endTime;
		System.out.println("========开始生成输出文件=========");
		logger.info("========开始生成输出文件=========");
		startTime = new Date().getTime();
		
		if("xls".equalsIgnoreCase(type))
			createXLSFile(path);
		else if("xlsx".equalsIgnoreCase(type))
			createXLSXFile(path);
		
		endTime = new Date().getTime();
		System.out.println("========输出文件生成完成=========");
		logger.info("========输出文件生成完成=========");
		System.out.println("用时为："+(endTime-startTime)+"ms");
		logger.info("用时为："+(endTime-startTime)+"ms");
	}
	
	private boolean createXLSXFile(String path) {
		// TODO Auto-generated method stub
		
		//创建Excel工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建一个工作表sheet
		Sheet sheet = workbook.createSheet("事件表");
		//创建第一行
		Row row = sheet.createRow(0);
		Cell cell = null;
		for (int i = 0; i < title2.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title2[i]);
		}
		//追加数据
		String sql="SELECT distinct name,endDate,city,host,hot,times,frequency,startDate FROM trade";
		int t=0;
		try {
			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
			DBHelper.rs=DBHelper.ps.executeQuery();
			for (int i = 1; DBHelper.rs.next(); i++) {
				Row nextrow = sheet.createRow(i);
				Cell cell2;
				String name = DBHelper.rs.getString(1);
				String host = DBHelper.rs.getString(4);
				int hot = DBHelper.rs.getInt(5);
				int times = DBHelper.rs.getInt(6);
				String city = DBHelper.rs.getString(3);
				if(city==null||DBHelper.rs.getString(8)==null){
					i--;
					continue;
				}
				if(city.contains("市"))
					city=city.substring(0, city.length()-1);
				else if(city.contains("区"))
					city=city.substring(0, 2);
				String fq = DBHelper.rs.getString(7);
				for (int j = 0; j < title2.length; j++) {
					cell2 = nextrow.createCell(j);
					if(j==0)
						cell2.setCellValue(name);
					else if(j==1){
						if(DBHelper.rs.getString(2)!=null)
							cell2.setCellValue(DBHelper.rs.getString(2));
						else cell2.setCellValue(DBHelper.rs.getString(8));
					}
					else if(j==2)
						cell2.setCellValue(city);
					else if(j==16||j==23||j>27&&j<31)
						cell2.setCellValue(1);
					else if(j==title2.length-1)
						cell2.setCellValue(DBHelper.rs.getString(8));
					else cell2.setCellValue(0);
				}
				
				if(host!=null){
//					if(host.contains("国际")){
//						nextrow.getCell(3).setCellValue(1);
//					}
					if(host.contains("中华人民共和国")||host.contains("国家")){
						nextrow.getCell(4).setCellValue(1);
					}
					else if((host.contains("合作")&&(host.contains("联盟")||host.contains("组织")))||host.contains("世界")||host.contains("全球")){
						nextrow.getCell(3).setCellValue(1);
					}
					else if(host.contains("政府")||host.contains("院")||host.contains("部")||host.contains("委")
							||host.contains("行")||host.contains("署")||host.contains("委员会")||host.contains("办公室")){
						if(host.contains("省"))
							nextrow.getCell(5).setCellValue(1);
//						if(host.contains("市"))
						else
							nextrow.getCell(6).setCellValue(1);
					}
					if(host.contains("会")){
//						if(host.contains("民间")){
//							if(host.contains("国际"))
//								nextrow.getCell(8).setCellValue(1);
//							else
//								nextrow.getCell(7).setCellValue(1);
//						}
//						else
						if(host.contains("国际"))
							nextrow.getCell(10).setCellValue(1);
						else
							nextrow.getCell(9).setCellValue(1);
						
					}
				}
				
				if(name.contains("民间")){
					if(name.contains("国际"))
						nextrow.getCell(8).setCellValue(1);
					else
						nextrow.getCell(7).setCellValue(1);
				}
				
				if(name.contains("老")){
					nextrow.getCell(14).setCellValue(1);
				}else if(name.contains("动漫")||name.contains("游戏")){
					nextrow.getCell(12).setCellValue(1);
					nextrow.getCell(16).setCellValue(0);
					nextrow.getCell(17).setCellValue(1);
				}else nextrow.getCell(13).setCellValue(1);
				
//				nextrow.getCell(16).setCellValue(1);
				if(name.contains("国际")){
					if(name.contains("洲")||name.contains("亚太")||name.contains("日韩")){
						nextrow.getCell(19).setCellValue(1);
					}else{
						nextrow.getCell(18).setCellValue(1);
					}
				}else{
					if(name.contains("国")){
						nextrow.getCell(20).setCellValue(1);
					}else if(name.contains("省")){
						nextrow.getCell(21).setCellValue(1);
					}else{
						nextrow.getCell(22).setCellValue(1);
					}
				}
				
//				nextrow.getCell(23).setCellValue(1);
				if(name.contains("节")&&!(name.contains("节能")||name.contains("节水"))){
					nextrow.getCell(23).setCellValue(0);
					nextrow.getCell(27).setCellValue(1);
				}
				
				if(hot>3000)
					nextrow.getCell(28).setCellValue(3);
				else if(hot>1000)
					nextrow.getCell(28).setCellValue(2);
//				else nextrow.getCell(28).setCellValue(1);
				if(times>20)
					nextrow.getCell(29).setCellValue(3);
				else if(times>5)
					nextrow.getCell(29).setCellValue(2);
//				else nextrow.getCell(29).setCellValue(1);
				
				if("一年三届".equals(fq))
					nextrow.getCell(30).setCellValue(3);
				else if("一年两届".equals(fq))
					nextrow.getCell(30).setCellValue(2);
//				else nextrow.getCell(30).setCellValue(1);
				
				if((i+1)%500==0)
					System.out.println("已完成："+(i+1));
				
				if(DBHelper.rs.isLast())
					t=i;
			}
			DBHelper.closeConn();
		} catch (SQLException e) {
			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
		}
		
		sql="SELECT distinct name,endDate,city,minprice,showtype,startDate FROM oshow";
		try {
			DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
			DBHelper.rs=DBHelper.ps.executeQuery();
			for (int i = t+1; DBHelper.rs.next(); i++) {
				Row nextrow = sheet.createRow(i);
				Cell cell2;
				String name = DBHelper.rs.getString(1);
				int hot = DBHelper.rs.getInt(4);
				int type = DBHelper.rs.getInt(5);
				String city = DBHelper.rs.getString(3);
				if(city==null||DBHelper.rs.getString(6)==null){
					i--;
					continue;
				}
				if(city.contains("市"))
					city=city.substring(0, city.length()-1);
				else if(city.contains("区"))
					city=city.substring(0, 2);
				for (int j = 0; j < title2.length; j++) {
					cell2 = nextrow.createCell(j);
					if(j==0)
						cell2.setCellValue(name);
					else if(j==1){
						if(DBHelper.rs.getString(2)!=null)
							cell2.setCellValue(DBHelper.rs.getString(2));
						else cell2.setCellValue(DBHelper.rs.getString(6));
					}
					else if(j==2)
						cell2.setCellValue(city);
					else if((j==12&&type==1)||(j==13&&type!=1&&type!=5)||(j==14&&type==5)||(j==15&&type==6)
							||(j==16&&type==7)||(j==17&&type!=7)||(j==20&&type==7)||(j==21&&type==1)
							||(j==22&&type!=1&&type!=7)||(j==24&&type==1)||(j==25&&type==6)||(j==26&&type==7)||j>27&&j<31)
						cell2.setCellValue(1);
					else if(j==title2.length-1)
						cell2.setCellValue(DBHelper.rs.getString(6));
					else cell2.setCellValue(0);
				}
				
				if(name.contains("游戏")){
					nextrow.getCell(11).setCellValue(0);
					nextrow.getCell(12).setCellValue(1);
					nextrow.getCell(13).setCellValue(0);
					nextrow.getCell(14).setCellValue(0);
				}
				if(name.contains("幽默")||name.contains("儿童")||name.contains("马戏")||name.contains("动漫")||
						name.contains("小丑")||name.contains("亲子")||name.contains("家庭")){
					nextrow.getCell(11).setCellValue(1);
					nextrow.getCell(12).setCellValue(0);
					nextrow.getCell(13).setCellValue(0);
					nextrow.getCell(14).setCellValue(0);
				}
				
				if(hot>300)
					nextrow.getCell(28).setCellValue(3);
				else if(hot>50)
					nextrow.getCell(28).setCellValue(3);
					
				if(type==7){
					if(name.contains("世界")||name.contains("全球")||((name.contains("国际")||name.contains("亚太"))
							&&(name.contains("峰会")||name.contains("高峰论坛"))))
						nextrow.getCell(3).setCellValue(1);
					else if(name.contains("中国")&&(name.contains("峰会")||name.contains("高峰论坛")))
						nextrow.getCell(4).setCellValue(1);
					if(name.contains("国际")){
						nextrow.getCell(10).setCellValue(1);
						if(name.contains("洲")||name.contains("亚太")||name.contains("日韩")){
							nextrow.getCell(19).setCellValue(1);
						}else{
							nextrow.getCell(18).setCellValue(1);
						}
					}else{
						nextrow.getCell(9).setCellValue(1);
						nextrow.getCell(20).setCellValue(1);
					}
					if(hot>3000)
						nextrow.getCell(28).setCellValue(3);
					else if(hot>1000)
						nextrow.getCell(28).setCellValue(2);
					
					if(name.contains("第")&&name.contains("届")){
						String times = name.split("第",2)[1].split("届", 2)[0];
						if(StringHelper.toInt(times)>10||times.length()>1)
							nextrow.getCell(29).setCellValue(3);
						else nextrow.getCell(29).setCellValue(2);
					}
					if(name.contains("班")&&!(name.contains("峰会")))
						nextrow.getCell(21).setCellValue(2);
					else
					if(name.contains("课")&&!(name.contains("峰会")))
						nextrow.getCell(22).setCellValue(2);
				}else
					if(type==6){
						if(name.contains("世界")||name.contains("全球")||name.contains("国际"))
							nextrow.getCell(3).setCellValue(1);
						else if(name.contains("中国"))
							nextrow.getCell(7).setCellValue(1);
					}else
				if(name.contains("节")&&!(name.contains("母亲节")||name.contains("儿童节")||name.contains("春节")||
						name.contains("节日")||name.contains("节目")||name.contains("节奏"))){
					nextrow.getCell(24).setCellValue(0);
					nextrow.getCell(25).setCellValue(0);
					nextrow.getCell(26).setCellValue(0);
					nextrow.getCell(27).setCellValue(1);
				}
				
				if((i+1)%500==0)
					System.out.println("已完成："+(i+1));
				
				if(DBHelper.rs.isLast()){
					CellRangeAddress c = new CellRangeAddress(0,i,0,title2.length-1);
					sheet.setAutoFilter(c);
					sheet.setColumnWidth(0, 10000);
					for (int j = 1; j < title2.length; j++) {
						sheet.autoSizeColumn(j);
					}
				}
			}
			DBHelper.closeConn();
		} catch (SQLException e) {
			DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
		}
		//创建一个文件
		//创建一个文件
		String filename = new Date().toString().split(":", 2)[0];
		filename = filename.substring(0, filename.length()-3);
		File file = new File(path+"\\"+filename+".xlsx");
		try {
			file.createNewFile();
			//将Excel内容存盘
			FileOutputStream stream = FileUtils.openOutputStream(file);
			workbook.write(stream);
			stream.close();
		} catch (IOException e) {
e.printStackTrace();logger.error("Exception",e);
			return false;
		}
		return true;
		
	
	}

	public boolean createXLSFile(String path) {
			//创建Excel工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			//创建一个工作表sheet
			HSSFSheet sheet = workbook.createSheet("事件表");
			//创建第一行
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = null;
			//插入第一行数据 id,name,sex
			for (int i = 0; i < title2.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(title2[i]);
			}
			//追加数据
			String sql="SELECT distinct name,endDate,city,host,hot,times,frequency,startDate FROM trade";
			int t=0;
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				for (int i = 1; DBHelper.rs.next(); i++) {
					HSSFRow nextrow = sheet.createRow(i);
					HSSFCell cell2;
					String name = DBHelper.rs.getString(1);
					String host = DBHelper.rs.getString(4);
					int hot = DBHelper.rs.getInt(5);
					int times = DBHelper.rs.getInt(6);
					String city = DBHelper.rs.getString(3);
					if(city==null||DBHelper.rs.getString(8)==null){
						i--;
						continue;
					}
					if(city.contains("市"))
						city=city.substring(0, city.length()-1);
					else if(city.contains("区"))
						city=city.substring(0, 2);
					String fq = DBHelper.rs.getString(7);
					for (int j = 0; j < title2.length; j++) {
						cell2 = nextrow.createCell(j);
						if(j==0)
							cell2.setCellValue(name);
						else if(j==1){
							if(DBHelper.rs.getString(2)!=null)
								cell2.setCellValue(DBHelper.rs.getString(2));
							else cell2.setCellValue(DBHelper.rs.getString(8));
						}
						else if(j==2)
							cell2.setCellValue(city);
						else if(j==16||j==23||j>27&&j<31)
							cell2.setCellValue(1);
						else if(j==title2.length-1)
							cell2.setCellValue(DBHelper.rs.getString(8));
						else cell2.setCellValue(0);
					}
					
					if(host!=null){
//						if(host.contains("国际")){
//							nextrow.getCell(3).setCellValue(1);
//						}
						if(host.contains("中华人民共和国")||host.contains("国家")){
							nextrow.getCell(4).setCellValue(1);
						}
						else if((host.contains("合作")&&(host.contains("联盟")||host.contains("组织")))||host.contains("世界")||host.contains("全球")){
							nextrow.getCell(3).setCellValue(1);
						}
						else if(host.contains("政府")||host.contains("院")||host.contains("部")||host.contains("委")
								||host.contains("行")||host.contains("署")||host.contains("委员会")||host.contains("办公室")){
							if(host.contains("省"))
								nextrow.getCell(5).setCellValue(1);
//							if(host.contains("市"))
							else
								nextrow.getCell(6).setCellValue(1);
						}
						if(host.contains("会")){
//							if(host.contains("民间")){
//								if(host.contains("国际"))
//									nextrow.getCell(8).setCellValue(1);
//								else
//									nextrow.getCell(7).setCellValue(1);
//							}
//							else
							if(host.contains("国际"))
								nextrow.getCell(10).setCellValue(1);
							else
								nextrow.getCell(9).setCellValue(1);
							
						}
					}
					
					if(name.contains("民间")){
						if(name.contains("国际"))
							nextrow.getCell(8).setCellValue(1);
						else
							nextrow.getCell(7).setCellValue(1);
					}
					
					if(name.contains("老")){
						nextrow.getCell(14).setCellValue(1);
					}else if(name.contains("动漫")||name.contains("游戏")){
						nextrow.getCell(12).setCellValue(1);
						nextrow.getCell(16).setCellValue(0);
						nextrow.getCell(17).setCellValue(1);
					}else nextrow.getCell(13).setCellValue(1);
					
//					nextrow.getCell(16).setCellValue(1);
					if(name.contains("国际")){
						if(name.contains("洲")||name.contains("亚太")||name.contains("日韩")){
							nextrow.getCell(19).setCellValue(1);
						}else{
							nextrow.getCell(18).setCellValue(1);
						}
					}else{
						if(name.contains("国")){
							nextrow.getCell(20).setCellValue(1);
						}else if(name.contains("省")){
							nextrow.getCell(21).setCellValue(1);
						}else{
							nextrow.getCell(22).setCellValue(1);
						}
					}
					
//					nextrow.getCell(23).setCellValue(1);
					if(name.contains("节")&&!(name.contains("节能")||name.contains("节水"))){
						nextrow.getCell(23).setCellValue(0);
						nextrow.getCell(27).setCellValue(1);
					}
					
					if(hot>3000)
						nextrow.getCell(28).setCellValue(3);
					else if(hot>1000)
						nextrow.getCell(28).setCellValue(2);
//					else nextrow.getCell(28).setCellValue(1);
					if(times>20)
						nextrow.getCell(29).setCellValue(3);
					else if(times>5)
						nextrow.getCell(29).setCellValue(2);
//					else nextrow.getCell(29).setCellValue(1);
					
					if("一年三届".equals(fq))
						nextrow.getCell(30).setCellValue(3);
					else if("一年两届".equals(fq))
						nextrow.getCell(30).setCellValue(2);
//					else nextrow.getCell(30).setCellValue(1);
					
					if((i+1)%500==0)
						System.out.println("已完成："+(i+1));
					
					if(DBHelper.rs.isLast())
						t=i;
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
			
			sql="SELECT distinct name,endDate,city,minprice,showtype,startDate FROM oshow";
			try {
				DBHelper.ps=DBHelper.getConn().prepareStatement(sql);
				DBHelper.rs=DBHelper.ps.executeQuery();
				for (int i = t+1; DBHelper.rs.next(); i++) {
					HSSFRow nextrow = sheet.createRow(i);
					HSSFCell cell2;
					String name = DBHelper.rs.getString(1);
					int hot = DBHelper.rs.getInt(4);
					int type = DBHelper.rs.getInt(5);
					String city = DBHelper.rs.getString(3);
					if(city==null||DBHelper.rs.getString(6)==null){
						i--;
						continue;
					}
					if(city.contains("市"))
						city=city.substring(0, city.length()-1);
					else if(city.contains("区"))
						city=city.substring(0, 2);
					for (int j = 0; j < title2.length; j++) {
						cell2 = nextrow.createCell(j);
						if(j==0)
							cell2.setCellValue(name);
						else if(j==1){
							if(DBHelper.rs.getString(2)!=null)
								cell2.setCellValue(DBHelper.rs.getString(2));
							else cell2.setCellValue(DBHelper.rs.getString(6));
						}
						else if(j==2)
							cell2.setCellValue(city);
						else if((j==12&&type==1)||(j==13&&type!=1&&type!=5)||(j==14&&type==5)||(j==15&&type==6)
								||(j==16&&type==7)||(j==17&&type!=7)||(j==20&&type==7)||(j==21&&type==1)
								||(j==22&&type!=1&&type!=7)||(j==24&&type==1)||(j==25&&type==6)||(j==26&&type==7)||j>27&&j<31)
							cell2.setCellValue(1);
						else if(j==title2.length-1)
							cell2.setCellValue(DBHelper.rs.getString(6));
						else cell2.setCellValue(0);
					}
					
					if(name.contains("游戏")){
						nextrow.getCell(11).setCellValue(0);
						nextrow.getCell(12).setCellValue(1);
						nextrow.getCell(13).setCellValue(0);
						nextrow.getCell(14).setCellValue(0);
					}
					if(name.contains("幽默")||name.contains("儿童")||name.contains("马戏")||name.contains("动漫")||
							name.contains("小丑")||name.contains("亲子")||name.contains("家庭")){
						nextrow.getCell(11).setCellValue(1);
						nextrow.getCell(12).setCellValue(0);
						nextrow.getCell(13).setCellValue(0);
						nextrow.getCell(14).setCellValue(0);
					}
					
					if(hot>300)
						nextrow.getCell(28).setCellValue(3);
					else if(hot>50)
						nextrow.getCell(28).setCellValue(3);
						
					if(type==7){
						if(name.contains("世界")||name.contains("全球")||((name.contains("国际")||name.contains("亚太"))
								&&(name.contains("峰会")||name.contains("高峰论坛"))))
							nextrow.getCell(3).setCellValue(1);
						else if(name.contains("中国")&&(name.contains("峰会")||name.contains("高峰论坛")))
							nextrow.getCell(4).setCellValue(1);
						if(name.contains("国际")){
							nextrow.getCell(10).setCellValue(1);
							if(name.contains("洲")||name.contains("亚太")||name.contains("日韩")){
								nextrow.getCell(19).setCellValue(1);
							}else{
								nextrow.getCell(18).setCellValue(1);
							}
						}else{
							nextrow.getCell(9).setCellValue(1);
							nextrow.getCell(20).setCellValue(1);
						}
						if(hot>3000)
							nextrow.getCell(28).setCellValue(3);
						else if(hot>1000)
							nextrow.getCell(28).setCellValue(2);
						
						if(name.contains("第")&&name.contains("届")){
							String times = name.split("第",2)[1].split("届", 2)[0];
							if(StringHelper.toInt(times)>10||times.length()>1)
								nextrow.getCell(29).setCellValue(3);
							else nextrow.getCell(29).setCellValue(2);
						}
						if(name.contains("班")&&!(name.contains("峰会")))
							nextrow.getCell(21).setCellValue(2);
						else
						if(name.contains("课")&&!(name.contains("峰会")))
							nextrow.getCell(22).setCellValue(2);
					}else
						if(type==6){
							if(name.contains("世界")||name.contains("全球")||name.contains("国际"))
								nextrow.getCell(3).setCellValue(1);
							else if(name.contains("中国"))
								nextrow.getCell(7).setCellValue(1);
						}else
					if(name.contains("节")&&!(name.contains("母亲节")||name.contains("儿童节")||name.contains("春节")||
							name.contains("节日")||name.contains("节目")||name.contains("节奏"))){
						nextrow.getCell(24).setCellValue(0);
						nextrow.getCell(25).setCellValue(0);
						nextrow.getCell(26).setCellValue(0);
						nextrow.getCell(27).setCellValue(1);
					}
					
					if((i+1)%500==0)
						System.out.println("已完成："+(i+1));
					
					if(DBHelper.rs.isLast()){
						CellRangeAddress c = new CellRangeAddress(0,i,0,title2.length-1);
						sheet.setAutoFilter(c);
						sheet.setColumnWidth(0, 10000);
						for (int j = 1; j < title2.length; j++) {
							sheet.autoSizeColumn(j);
						}
					}
				}
				DBHelper.closeConn();
			} catch (SQLException e) {
				DBHelper.closeConn();
e.printStackTrace();logger.error("Exception",e);
			}
			
			//创建一个文件
			String filename = new Date().toString().split(":", 2)[0];
			filename = filename.substring(0, filename.length()-3);
			File file = new File(path+"\\"+filename+".xls");
			try {
				file.createNewFile();
				//将Excel内容存盘
				FileOutputStream stream = FileUtils.openOutputStream(file);
				workbook.write(stream);
				stream.close();
			} catch (IOException e) {
e.printStackTrace();logger.error("Exception",e);
				return false;
			}
			return true;
			
		}
	
	
}
