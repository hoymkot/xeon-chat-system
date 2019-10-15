import java.net.ServerSocket;
import java.net.Socket;

import log.LogTools;

public class MainServer extends Thread {

	private int port;
	
	public MainServer(int port) {
		this.port = port;
	}
	
	public void run() {
		setupServer();
	}
	
	public void setupServer() {
		try {
			ServerSocket sc = new ServerSocket(this.port) ;
			LogTools.INFO(this.getClass(), "Server started at port: " + port);
			while (true) {
				Socket client = sc.accept();
				String cadd = client.getRemoteSocketAddress().toString();
				LogTools.INFO(this.getClass(),  "connecting " + cadd);
				ServerThread ct = new ServerThread(client);
				ct.start();
				
			}
			
		} catch (Exception e) {
			LogTools.ERROR(this.getClass(), "Server Startup Failure " + e);
		}
		
	}
	public static void main(String[] args) {
		MainServer cs = new MainServer(9090) ;
		cs.start();

	}

}
