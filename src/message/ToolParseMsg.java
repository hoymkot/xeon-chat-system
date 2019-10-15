package message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import log.LogTools;
import type.TeamInfo;
import type.UserInfo;

public class ToolParseMsg {
	public static MsgHead parseMsg(byte[] data) throws Exception {
		int totalLen = data.length + 4;
		ByteArrayInputStream bins = new ByteArrayInputStream(data);
		DataInputStream dins = new DataInputStream(bins);
		byte msgType = dins.readByte();
		int dest = dins.readInt();
		int src = dins.readInt();

		MsgHead msgHead = new MsgHead();
		msgHead.setTotalLen(totalLen);
		msgHead.setType(msgType);
		msgHead.setDest(dest);
		msgHead.setSrc(src);

		if (msgType == IMsgConstance.command_login) {
			String pwd = readString(dins, 10);
			MsgLogin ml = new MsgLogin();
			copyHead(msgHead, ml);
			ml.setPwd(pwd);
			return ml;

		} else if (msgType == IMsgConstance.command_login_resp) {
			byte state = dins.readByte();
			MsgLoginResp ml = new MsgLoginResp();
			copyHead(msgHead, ml);
			ml.setState(state);
			return ml;
		}

		else if (msgType == IMsgConstance.command_reg) {
			MsgReg mr = new MsgReg();
			copyHead(msgHead, mr);
			String nickName = readString(dins, 10);
			String pwd = readString(dins, 10);
			mr.setNickName(nickName);
			mr.setPwd(pwd);
			return mr;
		} else if (msgType == IMsgConstance.command_login_resp) {
			byte state = dins.readByte();
			MsgRegResp ml = new MsgRegResp();
			copyHead(msgHead, ml);
			ml.setState(state);
			return ml;
		} else if (msgType == IMsgConstance.command_chatText) {
			int len = totalLen - 4 - 1 - 4 - 4;
			String content = readString(dins, len);
			MsgChatText ml = new MsgChatText();
			copyHead(msgHead, ml);
			ml.setMsgContent(content);
			return ml;
		} else if (msgType == IMsgConstance.command_chatFile) {
			String fileName = readString(dins, 256);
			int fileLen = totalLen - 4 - 1 - 4 - 4 - 256;
			MsgChatFile ml = new MsgChatFile();
			copyHead(msgHead, ml);
			ml.setFileName(fileName);
			byte[] fileData = new byte[fileLen];
			dins.readFully(fileData);
			ml.setFileData(fileData);
			return ml;
		} else if (msgType == IMsgConstance.command_find_resp) {
			MsgFindResp mf = new MsgFindResp();
			copyHead(msgHead, mf);
			int userCount = dins.readInt();
			for (int i = 0; i < userCount; i++) {
				String nickName = readString(dins, 10);
				int jkNum = dins.readInt();
				UserInfo user = new UserInfo(jkNum, nickName);
				mf.addUserInfo(user);
			}
		} else if (msgType == IMsgConstance.command_teamList) {

			MsgTeamList mbl = new MsgTeamList();
			copyHead(msgHead, mbl);
			int listCount = dins.readInt();
			List<TeamInfo> teamLists = new ArrayList<TeamInfo>();
			while (listCount > 0) {
				listCount--;
				String teamName = readString(dins, 10);
				TeamInfo team = new TeamInfo(teamName);
				byte JKCount = dins.readByte();
				while (JKCount > 0) {
					JKCount--;
					// the following order matters
					int jkNum = dins.readInt();
					String buddyName = readString(dins, 10);
					UserInfo ui = new UserInfo(jkNum, buddyName);
					team.addBuddy(ui);
				}
				teamLists.add(team);
			}
			mbl.setTeamLists(teamLists);

			return mbl;
		} else if (msgType == IMsgConstance.command_addFriend) {
			MsgAddFriend mf = new MsgAddFriend();
			copyHead(msgHead, mf);
			int jkNum = dins.readInt();
			mf.setFriendJkNum(jkNum);
			return mf;

		} else if (msgType == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mfr = new MsgAddFriendResp();
			copyHead(msgHead, mfr);
			int jkNum = dins.readInt();
			String nkName = readString(dins, 10);
			mfr.setFriendJkNum(jkNum);
			mfr.setFriendNickName(nkName);
			return mfr;
		}else if (msgType == IMsgConstance.command_offLine || msgType == IMsgConstance.command_onLine || msgType == IMsgConstance.command_find) {
			return msgHead;
			
		} else {
			String logMsg = "unknow message type, unable to unpack: " + msgType ;
			LogTools.ERROR(ToolParseMsg.class, logMsg);
		}
		return null;

	}

	private static String readString(DataInputStream dins, int len) throws Exception {
		byte[] data = new byte[len];
		dins.readFully(data);
		return new String(data);
	}

	private static void copyHead(MsgHead head, MsgHead dest) {
		dest.setTotalLen(head.getTotalLen());
		dest.setType(head.getType());
		dest.setDest(head.getDest());
		dest.setSrc(head.getSrc());
	}
}
