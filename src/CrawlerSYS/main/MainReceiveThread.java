package CrawlerSYS.main;

import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;

public class MainReceiveThread  extends Thread{
	private MainNodeControler nc;
	private int index;
	private Logger logger = Logger.getLogger(MainReceiveThread.class);  
	
	public MainReceiveThread(MainNodeControler nc,int index) {
		super();
		this.nc = nc;
		this.index = index;
	}

	public void run() {
		while(nc.getsList()[index]!=null&&!nc.getsList()[index].isClosed()&&!nc.isOver()){
			try {
				JSONObject res = (JSONObject)nc.getInList()[index].readObject();
				if(res!=null){
					MainAfterReceive mar = new MainAfterReceive(nc,res);
					mar.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//e.printStackTrace();logger.error("Exception",e);
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
e.printStackTrace();logger.error("Exception",e);
			}
		}
	}
}
