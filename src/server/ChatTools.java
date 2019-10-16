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

public class ChatTools {

	static Map<UserInfo, ServerThread> stList = new HashMap<UserInfo, ServerThread>();

	private ChatTools() {
	}

	public static void addClient(UserInfo user, ServerThread ct) {
		stList.put(user, ct);
		sendOnOffLineMsg(user, true);
	}

	public static void removeClient(UserInfo user) {
		ServerThread ct = stList.remove(user);
		if (ct == null)
			return;
		ct.disConn();
		ct = null;
		sendOnOffLineMsg(user, false);
	}

	public static void sendOnOffLineMsg(UserInfo user, boolean online) {
		List<TeamInfo> teams = user.getTeams();
		for (TeamInfo t : teams) {
			List<UserInfo> users = t.getBuddyList();
			for (UserInfo destU : users) {
				ServerThread otherSt = stList.get(destU);
				if (null != otherSt) {
					MsgHead onLineMsg = new MsgHead();
					onLineMsg.setTotalLen(4 + 1 + 4 + 4);
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
		if (msg.getType() == IMsgConstance.command_addFriend) {
			MsgAddFriend maf = (MsgAddFriend) msg;
			UserInfo destUser = UserDao.addFriend(msg.getSrc(), maf.getFriendJkNum());
			MsgAddFriendResp mar = new MsgAddFriendResp();
			mar.setTotalLen(4 + 1 + 4 + 4 + +1 + 4 + 10);
			mar.setDest(msg.getSrc());
			mar.setSrc(msg.getDest());
			mar.setType(IMsgConstance.command_addFriend_Resp);
			mar.setState((byte) 0);
			if (destUser != null) {
				mar.setFriendJkNum(destUser.getJkNum());
				mar.setFriendNickName(destUser.getName());
				stList.get(srcUser).sendMsg2Me(mar);
				if (stList.get(destUser) != null) {
					mar.setDest(destUser.getJkNum());
					mar.setSrc(msg.getDest());
					mar.setFriendJkNum(srcUser.getJkNum());
					mar.setFriendNickName(srcUser.getName());
					stList.get(destUser).sendMsg2Me(mar);
				}
			} else {

				// buddy to be added not found
				mar.setFriendJkNum(maf.getFriendJkNum());
				mar.setFriendNickName("");
				mar.setState((byte) 1);
				stList.get(srcUser).sendMsg2Me(mar);
			}
			return;
		}
		if (msg.getType() == IMsgConstance.command_chatText || msg.getType() == IMsgConstance.command_chatFile) {
			// check if the user is online
			Set<UserInfo> users = stList.keySet();
			Iterator<UserInfo> its = users.iterator();
			while (its.hasNext()) {
				UserInfo user = its.next();
				if (user.getJkNum() == msg.getDest()) {
					stList.get(user).sendMsg2Me(msg);
				}
			}
		}
	}

}
