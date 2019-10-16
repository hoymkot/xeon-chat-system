package message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import log.LogTools;
import type.TeamInfo;
import type.UserInfo;

public class ToolsCreateMsg {
	public static byte[] packMsg(MsgHead msg) throws IOException {
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		DataOutputStream dous = new DataOutputStream(bous);
		writeHead(dous, msg);
		int msgType = msg.getType();
		if (msgType == IMsgConstance.command_login) {
			MsgLogin ml = (MsgLogin) msg;
			writeString(dous, ml.getPwd(), 10);
		} else if (msgType == IMsgConstance.command_login_resp) {
			MsgLoginResp mlr = (MsgLoginResp) msg;
			dous.writeByte(mlr.getState());
		} else if (msgType == IMsgConstance.command_reg) {
			MsgReg ml = (MsgReg) msg;
			writeString(dous, ml.getNickName(), 10);
			writeString(dous, ml.getPwd(), 10);
		} else if (msgType == IMsgConstance.command_reg_resp) {
			MsgRegResp mlr = (MsgRegResp) msg;
			dous.writeByte(mlr.getState());
		} else if (msgType == IMsgConstance.command_chatText) {
			MsgChatText mlr = (MsgChatText) msg;
			dous.write(mlr.getMsgContent().getBytes(), 0, mlr.getMsgContent().length());
		} else if (msgType == IMsgConstance.command_chatFile) {
			MsgChatFile mt = (MsgChatFile) msg;
			writeString(dous, mt.getFileName(), 256);
			dous.write(mt.getFileData());
		} else if (msgType == IMsgConstance.command_teamList) {
			MsgTeamList mdb = (MsgTeamList) msg;
			List<TeamInfo> teams = mdb.getTeamLists();
			dous.writeInt(teams.size());

			for (TeamInfo team : teams) {
				writeString(dous, team.getName(), 10);
				List<UserInfo> users = team.getBuddyList();
				dous.writeByte(users.size());
				for (UserInfo user : users) {
					dous.writeInt(user.getJkNum());
					writeString(dous, user.getName(), 10);
				}
			}
		} else if (msgType == IMsgConstance.command_addFriend) {
			MsgAddFriend mt = (MsgAddFriend) msg;
			dous.writeInt(mt.getFriendJkNum());
		} else if (msgType == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mt = (MsgAddFriendResp) msg;
			dous.writeByte(mt.getState());
			writeString(dous, mt.getFriendNickName(), 10);
			dous.writeInt(mt.getFriendJkNum());
		} else if (msgType == IMsgConstance.command_offLine 
				|| msgType == IMsgConstance.command_onLine) {
		} else {
			String logMsg = "unknown message type, unable to pack " + msgType;
			LogTools.ERROR(ToolsCreateMsg.class, logMsg);
		}

		dous.flush();
		byte[] data = bous.toByteArray();
		return data;

	}

	public static void writeHead(DataOutputStream dous, MsgHead msg) throws IOException {
		dous.writeInt(msg.getTotalLen());
		dous.writeByte(msg.getType());
		dous.writeInt(msg.getDest());
		dous.writeInt(msg.getSrc());
	}

	private static void writeString(DataOutputStream out, String str, int len) throws IOException {

		byte[] data = str.getBytes();
		if (data.length > len) {
			throw new IOException(" string is too long. string length: " + len);
		}
		out.write(data);
		while (len > data.length) {
			out.writeByte('\0');
			len--;
		}

	}
}