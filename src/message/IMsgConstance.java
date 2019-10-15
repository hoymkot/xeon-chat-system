package message;

public interface IMsgConstance {
	String serverIP="localhost";
	
	int serverPort = 9090;
	int Server_JK_NUMBER=10000;
	
	byte command_reg=0x01;
	byte command_reg_resp = 0x12;
	byte command_login=0x02;
	byte command_login_resp= 0x22;
	byte command_teamList = 0x03;
	byte command_onLine = 0x04;
	byte command_offLine = 0x05;
	byte command_chatText = 0x06;
	byte command_chatFile=0x07;
	byte command_find=0x08;
	byte command_find_resp=0x18;
	byte command_addFriend = 0x09;
	byte command_addFriend_Resp=0x19;
	
	
}
