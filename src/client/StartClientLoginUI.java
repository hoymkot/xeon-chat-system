package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class StartClientLoginUI {
	private JFrame jf_login;

	private JFormattedTextField jta_jkNum;
	private JTextField jta_pwd;

	private ClientConnection conn = ClientConnection.getIns();

	public void showLoginUI() throws Exception {
		jf_login = new JFrame(" Please Login Xeon Chat");
		FlowLayout fl = new FlowLayout();
		jf_login.setLayout(fl);
		jf_login.setSize(200, 160);

		MaskFormatter mfName = new MaskFormatter("##########");
		jta_jkNum = new JFormattedTextField();
		mfName.install(jta_jkNum);
		jta_jkNum.setColumns(10);
		jta_pwd = new JPasswordField(12);
		jta_pwd.setColumns(10);
		;
		JLabel la_name = new JLabel("user id");
		JLabel la_pwd = new JLabel("password: ");

		jf_login.add(la_name);
		jf_login.add(jta_jkNum);
		jf_login.add(la_pwd);
		jf_login.add(jta_pwd);

		JButton bu_login = new JButton("Login");
		bu_login.setActionCommand("login");
		JButton bu_reg = new JButton("Register");
		bu_login.setActionCommand("reg");

		ActionListener buttonAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if (command.equals("login")) {
					loginAction();
				}
				if (command.equals("reg")) {
					showRegForm();
				}

			}

		};

		bu_login.addActionListener(buttonAction);
		bu_reg.addActionListener(buttonAction);

		jf_login.add(bu_login);
		jf_login.add(bu_reg);
		jf_login.setLocationRelativeTo(null);
		jf_login.setVisible(true);
	}

	private void showRegForm() {
		final JFrame jf_reg = new JFrame("Please Register");
		FlowLayout fl = new FlowLayout();
		jf_reg.setLayout(fl);
		jf_reg.setSize(200, 160);
		final JTextField jta_regNickName = new JTextField(12);
		final JTextField jta_regPwd = new JTextField(12);

		JLabel la_regName = new JLabel("nickname");
		JLabel la_regPwd = new JLabel("password");

		jf_reg.add(la_regName);
		jf_reg.add(jta_regNickName);
		jf_reg.add(la_regPwd);
		jf_reg.add(jta_regPwd);

		JButton bu_reg = new JButton("Register User");
		jf_reg.add(bu_reg);
		jf_reg.setLocationRelativeTo(null);
		jf_reg.setVisible(true);

		ActionListener buttonAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String nickName = jta_regNickName.getText().trim();
				String pwd = jta_regPwd.getText().trim();
				String s = "fail to connect to server";
				if (ClientConnection.getIns().conn2Server()) {
					int jkNum = conn.regServer(nickName, pwd);
					s = " fail to register: " + jkNum;
					if (jkNum != -1) {
						s = "registration success: " + jkNum;
					}
				}
				JOptionPane.showMessageDialog(jf_reg, s);
				conn.closeMe();
				jf_reg.dispose();
			}

		};

		bu_reg.addActionListener(buttonAction);
	}
	
	private void loginAction() {
		String jkStr = jta_jkNum.getText().trim();
		int jkNum = Integer.parseInt(jkStr);
		String pwd = jta_pwd.getText();
		if (conn.conn2Server()) {
			if(conn.loginServer(jkNum, pwd)) {
				MainUIClient mainUI = new MainUIClient(jkNum);
				mainUI.showMainUI();
				conn.addMsgListener(mainUI);
				conn.start();
				jf_login.dispose();
			}else {
				conn.closeMe();
				JOptionPane.showMessageDialog(jf_login, "fail to login, please make user id and password are correct");
			}
		}
	}
		
		
	public static void main(String[] args) throws Exception{
		StartClientLoginUI qu = new StartClientLoginUI();
		qu.showLoginUI();
	}
}
