package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import log.LogTools;
import message.IMsgConstance;
import message.MsgAddFriend;
import message.MsgAddFriendResp;
import message.MsgHead;
import message.MsgLogin;
import message.MsgLoginResp;
import message.MsgReg;
import message.MsgRegResp;
import message.ToolParseMsg;
import message.ToolsCreateMsg;

public class ClientConnection extends Thread {
	private static ClientConnection ins;
	private Socket client;
	private DataOutputStream dous;
	private DataInputStream dins;

	private List<IClientMsgListener> listeners = new ArrayList<IClientMsgListener>();

	private ClientConnection() {
	};

	public static ClientConnection getIns() {
		if (null == ins) {
			ins = new ClientConnection();
		}
		return ins;
	}
	
	public boolean conn2Server() {
		try {
			client = new Socket ( IMsgConstance.serverIP, IMsgConstance.serverPort);
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
	}
	
	public boolean addUser(int id, int srcId) {
		try {
			MsgAddFriend maf = new MsgAddFriend () ;
			maf.setTotalLen(4+1+4+4+4);
			maf.setType(IMsgConstance.command_addFriend);
			maf.setDest(IMsgConstance.Server_JK_NUMBER);
			maf.setSrc(srcId);
			maf.setFriendJkNum(id);
			this.sendMsg(maf);
			return true;
			
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}
	
	public int regServer(String nickName, String pwd) {
		try {
			MsgReg mrg = new MsgReg() ;
			mrg.setTotalLen(4+1+4+4+10+10);
			mrg.setType(IMsgConstance.command_reg);
			mrg.setDest(IMsgConstance.Server_JK_NUMBER);
			mrg.setSrc(0);
			mrg.setNickName(nickName);
			mrg.setPwd(pwd);
			this.sendMsg(mrg);
			MsgHead loginResp = readFromServer() ;
			MsgRegResp mr = (MsgRegResp) loginResp;
			if (mr.getState() == 0 ) {
				return mr.getDest();
			}
			
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return -1 ;
	}
	
	public boolean loginServer (int jkNum, String pwd) {
		try {
			MsgLogin ml = new MsgLogin();
			ml.setTotalLen(4+1+4+4+10);
			ml.setType(IMsgConstance.command_login);
			ml.setDest(IMsgConstance.Server_JK_NUMBER);
			ml.setSrc(jkNum);
			ml.setPwd(pwd);
			this.sendMsg(ml);
			MsgHead loginResp = readFromServer();
			MsgLoginResp mlr = (MsgLoginResp) loginResp ;
			return mlr.getState() == 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void run() {
		while(true) {
			try {
				MsgHead m = readFromServer();
			for (IClientMsgListener lis : listeners) {
				lis.fireMsg(m);
			}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		LogTools.INFO(this.getClass(), "client thread exit!");
	}
	
	
	public MsgHead readFromServer() throws Exception {
		int totalLen = dins.readInt();
		LogTools.INFO(this.getClass(), "the length of message length is " + totalLen);
		byte[] data = new byte[totalLen-4];
		dins.readFully(data);
		MsgHead msg = ToolParseMsg.parseMsg(data);
		LogTools.INFO(this.getClass(), "message received " + msg);
		return msg;
		
	}
	public void sendMsg(MsgHead msg) throws Exception {
		LogTools.INFO( this.getClass(), "message sent");
		byte[] data = ToolsCreateMsg.packMsg(msg);
		this.dous.write(data);
		this.dous.flush();
		
	}
	
	public void addMsgListener(IClientMsgListener l) {
		this.listeners.add(l);
	}
	
	public void closeMe() {
		try {
			this.client.close();
		} catch (Exception e) {}
		
	}
}
