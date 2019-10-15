package message;

import java.util.ArrayList;
import java.util.List;

import type.TeamInfo;
import type.UserInfo;

public class MsgFindResp extends MsgHead {
	private List<UserInfo> users = new ArrayList<UserInfo>();

	public List<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}

	public void addUserInfo(UserInfo user) {
		this.users.add(user);
		
	}

	
}
