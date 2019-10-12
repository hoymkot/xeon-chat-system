package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

public class NetClient extends Thread {

	private String serverIp;
	private int port;
	private OutputStream ous;
	private BufferedReader brd;
	private JTextArea jta_recvie;

	public NetClient(String serverIp, int port, JTextArea jta_recive) {
		this.serverIp = serverIp;
		this.port = port;
		this.jta_recvie = jta_recive;
	}

	public boolean conn2Server() {
		try {
			Socket client = new Socket(this.serverIp, this.port);
			InputStream ins = client.getInputStream();
			brd = new BufferedReader(new InputStreamReader(ins));
			ous = client.getOutputStream();
			return true;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

//	public boolean loginServer(String name, String pwd) {
//		try {
//			String input = brd.readLine();
//			System.out.println("Server say: " + input);
//			name += "\r\n";
//			ous.write(name.getBytes());
//			ous.flush();
//			System.out.println("client sent user name, waiting for server to response");
//			input = brd.readLine();
//			System.out.println("server says: " + input);
//			pwd += "\r\n";
//			ous.write(pwd.getBytes());
//			ous.flush();
//			return true;
//
//		} catch (Exception ef) {
//			ef.printStackTrace();
//		}
//		return false;
//	}

	public void run() {
		while (true) {
			readFromServer();
		}
	}

	public void readFromServer() {
		try {
			String input = brd.readLine();
			System.out.println("server say: " + input);
			this.jta_recvie.append(input+"\r\n");
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}

	public void sendMsg(String msg){
		try{
			msg += "\r\n";
			this.ous.write(msg.getBytes());
			this.ous.flush();
		} catch (Exception ef ) {
			ef.printStackTrace();
		}
	}

}
