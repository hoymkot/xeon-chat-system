package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import message.IMsgConstance;
import message.MsgAddFriend;
import message.MsgAddFriendResp;
import message.MsgFindResp;
import message.MsgHead;
import type.TeamInfo;
import type.UserDao;
import type.UserInfo;

/**
 * be aware of synchronization problems, may use database for process thread
 * storage. we may ignore this problem, given that we don't expect massive
 * clients. by the way, the books might talk about synchronization problem
 * later. 2017-08-29 Twitter - 1.2.5
 * 
 * @return
 */
public class ChatTools {

	static Map<UserInfo, ServerThread> stList = new HashMap<UserInfo, ServerThread>();

	private ChatTools() {

	}



	
	 public static void addClient(UserInfo user, ServerThread ct) {
		 stList.put(user, ct);
		 sendOnOffLineMsg(user,true);
	
	 }




	public static void removeClient(UserInfo user) {
		ServerThread ct = stList.remove(user);
		if (ct == null) return;
		ct.disConn();
		ct = null;
		sendOnOffLineMsg(user,false);
	}

	public static void sendOnOffLineMsg(UserInfo user, boolean online) {
		List<TeamInfo> teams = user.getTeams();
		for (TeamInfo t: teams) {
			List<UserInfo> users = t.getBuddyList();
			for (UserInfo destU : users) {
				ServerThread otherSt = stList.get(destU);
				if (null != otherSt) {
					MsgHead onLineMsg = new MsgHead();
					onLineMsg.setTotalLen(4+1+4+4);
					if (online) {
						onLineMsg.setType(IMsgConstance.command_onLine);
					} else {
						onLineMsg.setType(IMsgConstance.command_offLine);
					}
					onLineMsg.setDest(destU.getJkNum());
					onLineMsg.setSrc(user.getJkNum());
					otherSt.sendMsg2Me(onLineMsg);
				}
			}
		}
	}
	
	public synchronized static void sendMsg2One(UserInfo srcUser, MsgHead msg) {
		if (msg.getType() == IMsgConstance.command_find) {
			Set<UserInfo > users = stList.keySet();
			
			
			Iterator<UserInfo> its = users.iterator();
			MsgFindResp mfr = new MsgFindResp();
			mfr.setType(IMsgConstance.command_find_resp);
			mfr.setSrc(msg.getDest()); // desc user is the one to receive message 
			mfr.setDest(msg.getSrc()); 
			
			while (its.hasNext()) {
				UserInfo user = its.next();
				if (user.getJkNum() != msg.getSrc()) {
					mfr.addUserInfo(user);
				}
			}
			int count = mfr.getUsers().size();
			mfr.setTotalLen(4+1+4+4+4+count * (4+10));
			stList.get(srcUser).sendMsg2Me(mfr);
		}
		else if (msg.getType() == IMsgConstance.command_addFriend) {
			MsgAddFriend maf = (MsgAddFriend) msg;
			UserInfo destUser = UserDao.addFriend(msg.getSrc(), maf.getFriendJkNum());
			MsgAddFriendResp mar = new MsgAddFriendResp();
			mar.setTotalLen(4+1+4+4+4+10);
			mar.setDest(msg.getSrc());
			mar.setSrc(msg.getDest());
			mar.setType(IMsgConstance.command_addFriend_Resp);
			mar.setFriendJkNum(destUser.getJkNum());
			mar.setFriendNickName(destUser.getName());
			stList.get(srcUser).sendMsg2Me(mar);
			mar.setDest(destUser.getJkNum());
			mar.setSrc(msg.getDest());
			mar.setFriendJkNum(srcUser.getJkNum());
			mar.setFriendNickName(srcUser.getName());
			stList.get(destUser).sendMsg2Me(mar);
			return;
		}
		if (msg.getType() == IMsgConstance.command_chatText || msg.getType() == IMsgConstance.command_chatFile) {
			// check if the user is online
			Set<UserInfo> users = stList.keySet();
			Iterator<UserInfo> its = users.iterator();
			while (its.hasNext()) {
				UserInfo user  = its.next();
				if (user.getJkNum() == msg.getDest()) {
					stList.get(user).sendMsg2Me(msg);
				}
			}
		}
	}


}
