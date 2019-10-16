package type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInfo implements Serializable {

	public UserInfo(int jkNum, String nickName) {
		this.jkNum = jkNum;
		this.name = nickName;
		
	}
	public UserInfo(int jkNum) {
		this.jkNum = jkNum;
	}
	public int getJkNum() {
		return jkNum;
	}
	public void setJkNum(int jkNum) {
		this.jkNum = jkNum;
	}
	
	List<TeamInfo> teams = new ArrayList<TeamInfo>();
	int jkNum;
	String name;
	String pwd;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public List<TeamInfo> getTeams() {
		return teams;
	}
	public void setTeams(List<TeamInfo> teams) {
		this.teams = teams;
	}
	public void addTeams(TeamInfo team) {
		this.getTeams().add(team);
	}
	public boolean isBuddy(UserInfo buddy) {
		for (TeamInfo team : this.getTeams()) {
			List<UserInfo> list = team.getBuddyList();
			if (list.contains(buddy)) {
				return true;
			};
		}
		return false;
	}
	
	public boolean equals(Object obj) {
		UserInfo u = (UserInfo) obj;
		return this.getJkNum() == u.getJkNum();
		
	}
}
