import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread{

	private int port;
	private boolean runing = false;
	
	private ServerSocket sc;
	
	public ChatServer(int port){
		this.port = port;
	}
	
	public void run() {
		this.setUpServer();
	}
	
	public void setUpServer() {
		try {

			ServerSocket s = new ServerSocket(this.port);
			runing = true;
			System.out.println("Server Created: " + port);
			while (this.runing) {
				Socket client = s.accept(); // blocking
				System.out.println("Incoming " + client.getRemoteSocketAddress());
				ProcessThread ct = new ProcessThread(client);
				ct.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isRuning(){
		return this.runing;
	}

	public void stopChatServer(){
		this.runing = false; 
		try{
			sc.close();
		} catch (Exception ef){}
	}
	

}
