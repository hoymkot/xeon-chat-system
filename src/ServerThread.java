import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class ServerThread extends Thread {

//	Socket client;
//	OutputStream out;
//	UserInfo user;
//
//	public ServerThread(Socket sc) {
//		this.client = sc;
//	}
//
//	public void sendMsg2Me(String msg) {
//		try {
//			msg = "\r\n" + msg + "\r\n";
//			byte[] data = msg.getBytes();
//			out.write(data);
//			out.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void run() {
//		processSocket(this.client);
//	}
//
//	
//	private void processSocket(Socket client) {
//		try {
//			out = client.getOutputStream();
//			InputStream ins = client.getInputStream(); //blocking
//			BufferedReader brd = new BufferedReader(new InputStreamReader(ins));
//			this.sendMsg2Me("Welcome, please enter your user name: ");
//			String userName = brd.readLine();
//			this.sendMsg2Me("please enter your password: ");
//			String pwd = brd.readLine();
//			user = new UserInfo();
//			user.setName(userName);
//			user.setPwd(pwd);
//			user.setLoginTime((new Date()).toString());
//			user.setAddress(client.getInetAddress().getHostAddress());
//			boolean loginState = DaoTools.checkLogin(user);
//			if (!loginState){
//				this.sendMsg2Me("incorrect username/password please try again ");
//				this.closeMe();
//				return;
//			}
//			ChatTools.addClient(this);;
//			String input = brd.readLine();
//			while (input!= null && !"bye".equals(input)){
//				System.out.println("server accepted: " + input);
//				ChatTools.castMsg(this.user, input);
//				input = brd.readLine();
//			}
//		} catch (SocketException se){
//			System.out.println("server error: " + se.getMessage());
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//			
//		this.closeMe();
//		ChatTools.removeClient(this);
//	}
//
//	public void closeMe() {
//		try{
//			client.close();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		
//	}
//
//
//	public UserInfo getOwnerUser() {
//		return this.user;
//	}
//

	
}
