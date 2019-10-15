package message;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

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
	public int getDest() {
		return desc;
	}
	public void setDest(int desc) {
		this.desc = desc;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
		
}
