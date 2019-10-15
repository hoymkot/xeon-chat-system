import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

import log.LogTools;
import message.IMsgConstance;
import message.MsgHead;
import message.MsgLogin;
import message.MsgLoginResp;
import message.MsgReg;
import message.MsgRegResp;
import message.MsgTeamList;
import message.ToolParseMsg;
import message.ToolsCreateMsg;
import type.TeamInfo;
import type.UserDao;
import type.UserInfo;

public class ServerThread extends Thread {

	public ServerThread(Socket client) {
		this.client = client;
	}

	Socket client;
	DataOutputStream dous;
	DataInputStream dins;
	UserInfo ownerUser;
	private boolean loginOk;

	public boolean readFirstMsg() {
		try {
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			MsgHead msg = receiveData();
			if (msg.getType() == IMsgConstance.command_login) {
				MsgLogin ml = (MsgLogin) msg; 
				return checkLogin(ml);
			} else if (msg.getType() == IMsgConstance.command_reg) {
				MsgReg mr = (MsgReg) msg;
				int jkNum = UserDao.regUser(mr.getPwd(), mr.getNickName());
				MsgRegResp mrs = new MsgRegResp(); 
				mrs.setTotalLen(4+1+4+4+1);
				mrs.setType(IMsgConstance.command_reg_resp);
				mrs.setDest(jkNum);
				mrs.setSrc(IMsgConstance.Server_JK_NUMBER);
				mrs.setState((byte)0);
				this.sendMsg2Me(mrs);
			}
			
		} catch (Exception e) {
			LogTools.ERROR(this.getClass(), "read first msg failure: " + e);
		}
		return false;
	}

	private boolean checkLogin(MsgLogin msg) {
		ownerUser = UserDao.checkLogin(msg.getSrc(), msg.getPwd());
		if (ownerUser != null) {
			MsgLoginResp mlr = new MsgLoginResp();
			mlr.setTotalLen(4 + 1 + 4 + 4 + 1);
			mlr.setType(IMsgConstance.command_login_resp);
			mlr.setSrc(IMsgConstance.Server_JK_NUMBER);
			mlr.setDest(ownerUser.getJkNum());
			mlr.setState((byte) 0);
			this.sendMsg2Me(mlr);
			MsgTeamList mt = new MsgTeamList();
			int len = 4 + 1 + 4 + 4 + 4;
			List<TeamInfo> teams = ownerUser.getTeams();
			for (TeamInfo t : teams) {
				int uCount = t.getBuddyList().size();
				len += 10 + 1;
				len += uCount * (10 + 4);
			}
			mt.setTotalLen(len);
			mt.setDest(ownerUser.getJkNum());
			mt.setSrc(IMsgConstance.Server_JK_NUMBER);
			mt.setType(IMsgConstance.command_teamList);
			mt.setTeamLists(ownerUser.getTeams());
			this.sendMsg2Me(mt);
			return true;
		}
		this.disConn();
		return false;
	}


	public void run() {
		try {
			loginOk = readFirstMsg();
			if (loginOk) {
				ChatTools.addClient(this.ownerUser, this);

			}
			while (loginOk) {
				MsgHead msg = this.receiveData();
				ChatTools.sendMsg2One(this.ownerUser, msg);
			}
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "Server Message Read Error: " + ef);
		}
		ChatTools.removeClient(this.ownerUser);
	}

	private MsgHead receiveData() throws Exception {
		int len = dins.readInt();
		LogTools.INFO(this.getClass(), "Server read message length: " + len);
		byte[] data = new byte[len-4];
		dins.readFully(data);
		MsgHead msg = ToolParseMsg.parseMsg(data);
		LogTools.INFO (this.getClass(), " server read message " + msg);
		return msg;
	}
	

	public boolean sendMsg2Me(MsgHead msg) {
		try {
			byte[] data = ToolsCreateMsg.packMsg(msg);
			this.dous.write(data);
			this.dous.flush();
			LogTools.INFO(this.getClass(), "server message sent " + msg);
			return true;
			
		} catch (Exception ef) {
			LogTools.ERROR(this.getClass(), "Server message sent failure " + msg);
		}
		return false;
	}
	
	public void disConn() {
		try{
			loginOk = false;
			client.close();
		} catch (Exception e){
		}
		
	}


	public UserInfo getOwnerUser() {
		return this.ownerUser;
	}

}
