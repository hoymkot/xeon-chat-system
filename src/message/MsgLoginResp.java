package message;

public class MsgLoginResp extends MsgHead {
	byte state;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	
}
