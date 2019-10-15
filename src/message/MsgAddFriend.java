package message;

import java.util.ArrayList;
import java.util.List;

import type.TeamInfo;

public class MsgAddFriend extends MsgHead {
	private int friendJkNum;

	public int getFriendJkNum() {
		return friendJkNum;
	}

	public void setFriendJkNum(int friendJkNum) {
		this.friendJkNum = friendJkNum;
	}

}
