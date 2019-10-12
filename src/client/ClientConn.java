package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class ClientConn extends Thread {
	private JTextArea jta_receive;
	private JComboBox jcb_users;
	private OutputStream ous;
	private InputStream ins;
	private boolean connOK = false;

	public void setDisplayable(JTextArea jta_receive, JComboBox jcb_users) {
		this.jta_receive = jta_receive;
		this.jcb_users = jcb_users;
	}

	public boolean conn2Server(String serverIp, int port) {
		try {
			Socket sc = new Socket(serverIp, port);
			ins = sc.getInputStream();
			ous = sc.getOutputStream();
			return connOK = true;
		} catch (Exception ef) {
			return connOK = false;
		}
	}

	public boolean login(String name, String pwd) {
		try {
			String login = "<msg><type>login</type><name>" + name + "</name>" + pwd + "</pwd></msg>";
			ous.write(login.getBytes());
			String resXML = readString();
			String state = getXMLValue("state", resXML);
			return state.equals("0");
		} catch (Exception ef) {
			return false;
		}
	}

	public boolean reg(String name, String pwd) {
		try {
			String login = "<msg><type>reg</type><name>" + name + "</name>" + pwd + "</pwd></msg>";
			ous.write(login.getBytes());
			String resXML = readString();
			String state = getXMLValue("state", resXML);
			return state.equals("0");
		} catch (Exception ef) {
			return false;
		}
	}

	public void sendTextChat(String sender, String receiver, String msg) {
		try {
			String textChatXml = "<msg><type>chat</type>" + "<sender>" + sender + "</sender>" + "<receiver>" + receiver
					+ "</receiver>" + "<content>" + msg + "</content></msg>";
			ous.write(textChatXml.getBytes());
		} catch (Exception ef) {
		}
	}

	public void run() {
		try {
			while (connOK) {
				String xmlMsg = readString();
				String type = getXMLValue("type", xmlMsg);
				if (type.equals("chat")) {
					String sender = getXMLValue("sender", xmlMsg);
					String content = getXMLValue("content", xmlMsg);
					jta_receive.append(sender + " say: " + content + "\r\n");

				}
				if (type.equals("buddyList")) {
					String users = getXMLValue("users", xmlMsg);
					StringTokenizer stk = new StringTokenizer(users, ",");
					while (stk.hasMoreTokens()) {
						String uName = stk.nextToken();
						jcb_users.addItem(uName + " ");
					}
				}
				
				if (type.equals("onLine")) {
					String uName = getXMLValue("user", xmlMsg);
					jta_receive.append(uName + " go online ");
					jcb_users.addItem(uName);

				}
				if (type.equals("offLine")) {
					String uName = getXMLValue("user", xmlMsg);
					jta_receive.append(uName + " go offline ");
					int count = jcb_users.getItemCount();
					for (int i = 0 ; i< count; i++) {
						String it = (String) jcb_users.getItemAt(i); 
						it = it.trim();
						if (it.equals(uName)) {
							jcb_users.removeItemAt(i);
						}
					}
				}
				

			}
		} catch (Exception ef) {
			connOK = false;
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

}
