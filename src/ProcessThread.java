import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import message.MsgHead;
import message.ToolsCreateMsg;
import type.UserInfo;

public class ProcessThread extends Thread {

	private java.net.Socket client;
	private java.io.OutputStream ous;
	private java.io.InputStream ins;
	private DataInputStream dins;
	private DataOutputStream dous;
	private String myUserName;
	UserInfo user;
	private boolean connOK = false;

	public ProcessThread(Socket client) throws Exception {
		this.client = client;
		ous = client.getOutputStream();
		ins = client.getInputStream();
		dous = new DataOutputStream(ous);
		dins = new DataInputStream(ins);
		connOK = true;
	}

	public void run() {
		try {
			processClient();

		} catch (Exception ef) {
			ef.printStackTrace();
		}
//		ChatTools.removeUser(this.myUserName);

	}

	private MsgHead receiveData() throws Exception {
		int len = dins.readInt();
		byte[] data = new byte[len - 4];
		dins.readFully(data);
		MsgHead msg = MsgHead.parseMsg(data);
		return msg;
	}

	public void closeMe() {
		try {
			ins.close();
			ous.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean sendMsg2Me(MsgHead msg) throws IOException {
		byte[] data = ToolsCreateMsg.packMsg(msg);
		dous.write(data);
		dous.flush();
		return true;
	}

	private void processClient() throws Exception {

//		ChatTools.addPT(this.myUserName, this);
		while (connOK) {
			MsgHead msg = this.receiveData();
		}
	}



	

	public Object getMyUserName() {
		return this.myUserName;
	}

}
