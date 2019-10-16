package message;


public class MsgAddFriendResp extends MsgHead {
	private int friendJkNum;

	private String friendNickName;
	private byte state ;
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public int getFriendJkNum() {
		return friendJkNum;
	}

	public void setFriendJkNum(int friendJkNum) {
		this.friendJkNum = friendJkNum;
	}

	public String getFriendNickName() {
		return friendNickName;
	}

	public void setFriendNickName(String friendNickName) {
		this.friendNickName = friendNickName;
	}

}
