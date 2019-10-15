package type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamInfo implements Serializable {
	private int id;
	private String name;
	private UserInfo ownerUser;
	private List<UserInfo> buddyList = new ArrayList<UserInfo>();

	public TeamInfo(int id, String name, UserInfo ownerUser) {
		this.id = id;
		this.name = name;
		this.ownerUser = ownerUser;
	}

	public TeamInfo(String teamName) {
		this.name = teamName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserInfo getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(UserInfo ownerUser) {
		this.ownerUser = ownerUser;
	}

	public List<UserInfo> getBuddyList() {
		return buddyList;
	}

	public void setBuddyList(List<UserInfo> buddyList) {
		this.buddyList = buddyList;
	}

	public void addBuddy(UserInfo buddy) {
		this.buddyList.add(buddy);
	}
}
