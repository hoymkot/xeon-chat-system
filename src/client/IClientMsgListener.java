package client;
import message.MsgHead;

public interface IClientMsgListener {
	public void fireMsg(MsgHead msg) ;
}
