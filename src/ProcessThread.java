import java.net.Socket;

public class ProcessThread extends Thread {

	private java.net.Socket client;
	private java.io.OutputStream ous;
	private java.io.InputStream ins;
	private String myUserName;
	UserInfo user;
	private boolean connOK = false;

	public ProcessThread(Socket client) throws Exception {
		this.client = client;
		ous = client.getOutputStream();
		ins = client.getInputStream();
		connOK = true;
	}

	public void run() {
		try {
			processClient(this.client);

		} catch (Exception ef) {
			ef.printStackTrace();
		}
		ChatTools.removeUser(this.myUserName);

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

	public void sendMsg2Me(String msg) {
		try {
			ous.write(msg.getBytes());
		} catch (Exception ef) {
			connOK = false;
		}
	}

	private void processClient(Socket client2) throws Exception {
		if (connOK == false)
			return;
		this.sendMsg2Me("connection established with server");
		if (readFirstMsg()) {
			ChatTools.addPT(this.myUserName, this);
			while (connOK) {
				String msg = readString();
				String type = getXMLValue("type", msg);
				if (type.equals("chat")) {
					String destUserName = getXMLValue("receiver", msg);
					ChatTools.castMsg(this.myUserName, msg, destUserName);
				} else {
					System.out.println("unknown Msg type: " + type);
				}
			}
		}
	}

	private String readString() throws Exception {
		String msg = "";
		int i = ins.read();
		StringBuffer stb = new StringBuffer();
		boolean end = false;
		while (!end) {
			char c = (char) i;
			stb.append(c);
			msg = stb.toString().trim();
			if (msg.endsWith("</msg>"))
				break;
			i = ins.read();
		}
		msg = new String(msg.getBytes("ISO-8859-1"), "GBK").trim();
		return msg;

	}

	private String getXMLValue(String flagName, String xmlMsg) throws Exception {
		try {
			int start = xmlMsg.indexOf("<" + flagName + ">");
			start += flagName.length() + 2;
			int end = xmlMsg.indexOf("</" + flagName + ">");
			String value = xmlMsg.substring(start, end).trim();
			return value;
		} catch (Exception ef) {
			throw new Exception("Analyze " + flagName + " failure: " + xmlMsg);
		}
	}

	private boolean readFirstMsg() throws Exception {
		String msg = readString();
		String type = getXMLValue("type", msg);
		if (type.equals("reg")) {
			myUserName = this.getXMLValue("name", msg);
			String pwd = this.getXMLValue("pwd", msg);
			int state = -1;
			if (ServerDao.saveUser(this.myUserName, pwd)) {
				state = 0;
			}
			String resp = "<msg><type>regResp</type><state>" + state + "</state></msg>";
			sendMsg2Me(resp);
			this.client.close();
		}
		if (type.equals("login")) {
			this.myUserName = this.getXMLValue("login", msg);
			String pwd = this.getXMLValue("pwd", msg);
			int state = -1;
			if (ServerDao.hasUser(this.myUserName, pwd)) {
				state = 0;
			}
			String resp = "<msg><type>loginResp</type><state>" + state + "</state></msg>";
			sendMsg2Me(resp);
			if (state == -1) {
				this.client.close();
			} else {
				return true;
			}
		}
		return false;
	}

	public Object getMyUserName() {
		return this.myUserName;
	}

}
