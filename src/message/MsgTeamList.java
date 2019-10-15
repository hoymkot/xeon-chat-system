package message;

import java.util.ArrayList;
import java.util.List;

import type.TeamInfo;

public class MsgTeamList extends MsgHead {
	private List<TeamInfo> teamLists = new ArrayList<TeamInfo>();

	public List<TeamInfo> getTeamLists() {
		return teamLists;
	}

	public void setTeamLists(List<TeamInfo> teamLists) {
		this.teamLists = teamLists;
	}
	
}
