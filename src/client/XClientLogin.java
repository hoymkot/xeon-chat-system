package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class XClientLogin {
	private JFrame jf_login;
	private JTextField jta_userName = new JTextField(10);
	private JTextField jta_pwd = new JTextField(10);
	private JTextField jta_serverIp = new JTextField("localhost");
	private JTextField jta_serverPort = new JTextField("9090");
	
	public void showLoginUI(){
		jf_login = new JFrame("netjava-xmpp client");
		FlowLayout fl = new FlowLayout();
		jf_login.setLayout(fl);
		jf_login.setSize(300, 320);
		JLabel la_ip = new JLabel("Service IP: ");
		JLabel la_port = new JLabel("port:");
		jf_login.add(la_ip);
		jf_login.add(jta_serverIp);
		jf_login.add(la_port);
		jf_login.add(jta_serverPort);
		JLabel la_name= new JLabel("username: ");
		JLabel la_pwd = new JLabel("password: "); 
		jf_login.add(la_name);
		jf_login.add(jta_userName);
		jf_login.add(la_pwd);
		jf_login.add(jta_pwd);
		JButton bu_login = new JButton("Login");
		bu_login.setActionCommand("login");
		JButton bu_register = new JButton("Register");
		bu_register.setActionCommand("reg");
		ActionListener buttonAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if(command.equals("login")){
					loginAction();
				} 
				if(command.equals("reg")){
					regAction();
				}
			}
		};

		bu_login.addActionListener(buttonAction);
		bu_register.addActionListener(buttonAction);
		jf_login.add(bu_login);
		jf_login.add(bu_register);
		jf_login.setLocationRelativeTo(null);
		jf_login.setVisible(true);
		
	}
	
	private void regAction(){
		String name = this.jta_userName.getText().trim();
		String pwd = this.jta_pwd.getText().trim();
		String serverIp = this.jta_serverIp.getText().trim();
		String sPort = this.jta_serverPort.getText().trim();
		int port = Integer.parseInt(sPort);
		ClientConn conn = new ClientConn();
		if(conn.conn2Server(serverIp, port)){
			if (conn.reg(name,pwd)){
				JOptionPane.showMessageDialog(jf_login, "registration succeeded, please login now");
				return;
			}
		}
		JOptionPane.showMessageDialog(jf_login, "registration failure.");
	}
		
	private void loginAction(){
		String name = this.jta_userName.getText().trim();
		String pwd = this.jta_pwd.getText().trim();
		String serverIp = this.jta_serverIp.getText().trim();
		String sPort = this.jta_serverPort.getText().trim();
		int port = Integer.parseInt(sPort);
		ClientConn conn = new ClientConn();
		if(conn.conn2Server(serverIp, port)){
			if (conn.login(name,pwd)){
				JOptionPane.showMessageDialog(jf_login, "login succeeded");
				jf_login.dispose();
				XClientChatUI chat = new XClientChatUI(conn, name);
				return;
			}
		}
		JOptionPane.showMessageDialog(jf_login, "login failure.");
		
	}
	
	public static void main(String[] args) throws Exception{
		XClientLogin qu = new XClientLogin();
		qu.showLoginUI();
		
	}
}
