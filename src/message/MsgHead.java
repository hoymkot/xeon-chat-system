package message;

public class MsgHead {
	private int totalLen;
	private byte type;
	private int desc;
	private int src;
	public int getTotalLen() {
		return totalLen;
	}
	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getDesc() {
		return desc;
	}
	public void setDesc(int desc) {
		this.desc = desc;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
		
}
