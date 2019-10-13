import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread {

	private int port;
	private boolean runing = false;

	private ServerSocket sc;

	public ChatServer(int port) {
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
			while (true) {
				Socket client = s.accept(); // blocking
				System.out.println("Incoming " + client.getRemoteSocketAddress());
				processChat(client);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processChat(Socket client) {
		try {
			OutputStream out = client.getOutputStream();
			InputStream ins = client.getInputStream();
			DataInputStream dIns = new DataInputStream(ins);
			while (true) {
				int totalLen = dIns.readInt();
				System.out.println("******* enter a message with length " + totalLen);
				byte flag = dIns.readByte();
				System.out.println("message type is " + flag);
				int destNum = dIns.readInt();
				System.out.println("message receiver is " + destNum);
				if (flag == 1) {
					byte[] data = new byte[totalLen - 4 - 1 - 4];
					dIns.readFully(data);
					String msg = new String(data);
					System.out.println(" send text to " + destNum + " content is: " + msg);
				} else if (flag == 2) {
					System.out.println("send file to + " + destNum);
					byte[] data = new byte[256];
					dIns.readFully(data);
					String fileName = new String(data).trim();
					System.out.println("filename read is:  " + fileName);
					data = new byte[totalLen - 4 - 1 - 4 - 256];
					dIns.readFully(data);
					FileOutputStream fous = new FileOutputStream(fileName);
					fous.write(data);
					fous.flush();
					fous.close();
					System.out.println("file saved completed");
				} else {
					System.out.println("unknown message type: " + flag);
					client.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isRuning() {
		return this.runing;
	}

	public void stopChatServer() {
		this.runing = false;
		try {
			sc.close();
		} catch (Exception ef) {
		}
	}

}
