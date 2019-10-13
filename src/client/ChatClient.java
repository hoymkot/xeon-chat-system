package client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private DataOutputStream dous;

	private void writeString(DataOutputStream out, String str, int len) {
		try {
			byte[] data = str.getBytes();
			out.write(data);
			while (len > data.length) {
				out.writeByte('\0');
				len--;
			}

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		;
	}

	private void sendTextMsg(String msg, int destNum) {
		try {
			byte[] strb = msg.getBytes();
			int totalLen = 4 + 1 + 4 + strb.length;
			System.out.println("total message length: " + totalLen);
			dous.writeInt(totalLen);
			dous.writeByte(1);
			dous.writeInt(destNum);
			dous.write(strb);
			dous.flush();
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}

	private void sendFileMsg(String fileName, int destNum) {
		try {
			File file = new File(fileName);
			InputStream ins = new FileInputStream(file);
			int fileDataLen = ins.available();
			int totalLen = 4 + 1 + 4 + 256 + fileDataLen;
			dous.writeInt(totalLen);
			dous.writeByte(2);
			dous.writeInt(destNum);
			String shortFileName = file.getName();
			writeString(dous, shortFileName,256);
			byte[] fileData=new byte[fileDataLen];
			ins.read(fileData);
			dous.write(fileData);
			dous.flush();
			
		} catch (Exception ef) {
			ef.printStackTrace();
		}

	}

	public void conn2Server(String ip, int port) {
		try {
			Socket client = new Socket(ip, port);
			InputStream ins = client.getInputStream();
			OutputStream ous = client.getOutputStream();
			dous = new DataOutputStream(ous);

			int testCount = 0;
			while (true) {
				System.out.println("Login success please choose your message type");
				Scanner sc = new Scanner(System.in);
				int type = sc.nextInt();
				if (type == 1) {
					sendTextMsg("abc chat content" + testCount, 8888);
				}
				if (type == 2) {
					sendFileMsg("e:\\netjavalogo.gif", 8888);
				}
				testCount++;
				

			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ChatClient qqc = new ChatClient();
		qqc.conn2Server("localhost", 9090);
	}
}
