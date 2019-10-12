package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainNetUI {

	private JFrame jf_login;
	private JFrame jf_chat;
	private JTextField jta_userName;
	private JTextField jta_pwd;
	private JTextArea jta_input = new JTextArea(5,20);
	private NetClient conn;
	
	public static void main(String[] args){
		MainNetUI qu = new MainNetUI();
		qu.showLoginUI();
	}

	public void showLoginUI() {
		jf_login = new javax.swing.JFrame("Chat Client v0.1, Please login");
		java.awt.FlowLayout fl = new java.awt.FlowLayout();
		jf_login.setLayout(fl);
		jf_login.setSize(200,200);
		jta_userName = new JTextField(12);
		jta_pwd = new JTextField(12);
		JLabel la_name = new JLabel("username: ");
		JLabel la_pwd = new JLabel("password: ");
		jf_login.add(la_name);
		jf_login.add(jta_userName);
		jf_login.add(la_pwd);
		jf_login.add(jta_pwd);
		
		javax.swing.JButton bu_login = new javax.swing.JButton("Login");
		bu_login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loginAction();
			}
		});
		jf_login.add(bu_login);
		jf_login.setVisible(true);
	}
	
	private void loginAction(){
		String name = jta_userName.getText();
		String pwd = jta_pwd.getText();
		conn = new NetClient("localhost", 9091, jta_input);
		if (conn.conn2Server()){
			if(conn.loginServer(name, pwd)){
				showMainUI();
				conn.start();
				jf_login.dispose();
			}
		} else {
			JOptionPane.showMessageDialog(jf_login,  "wrong user! user1-10");
		}
	
	}
	public void showMainUI(){
		jf_chat = new javax.swing.JFrame("chat client v0.1");
		java.awt.FlowLayout fl = new java.awt.FlowLayout();
		jf_chat.setLayout(fl);
		jf_chat.setSize(200, 300);
		javax.swing.JLabel la = new JLabel("message to be sent: ");
		
		final javax.swing.JTextArea jta_output = new JTextArea(5,20);
		javax.swing.JButton bu_send = new javax.swing.JButton("send");
		bu_send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg = jta_output.getText();
				conn.sendMsg(msg);
			}
		});
		jf_chat.add(jta_input);
		jf_chat.add(la);
		jf_chat.add(jta_output);
		jf_chat.add(bu_send);
		jf_chat.setVisible(true);

		
	}
}
