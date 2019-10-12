package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class XClientChatUI extends JFrame {
	private ClientConn conn;
	private String userName;
	private JTextArea jta_receive = new JTextArea(15, 25);
	private JComboBox jcb_users = new JComboBox();

	public XClientChatUI(ClientConn conn, String userName) {
		this.userName = userName;
		this.initUI();
		this.conn = conn;
		this.conn.setDisplayable(jta_receive, jcb_users);
		this.conn.start();
	}

	private void initUI() {
		this.setTitle("Xeon Chat Room : welcome " + this.userName);
		FlowLayout fl = new FlowLayout(0);
		this.setLayout(fl);
		this.setSize(300,500);
		JLabel la_name = new JLabel (" Message Received" );
		JLabel la_users = new JLabel (" send to " ) ;
		final JTextField  jtf_send = new JTextField(20);
		JButton bu_send = new JButton("Send");
		this.add(la_name);
		this.add(jta_receive);
		this.add(la_users);
		this.add(jtf_send);
		this.add(jcb_users);
		this.add(bu_send);
		
		ActionListener sendListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String receiver = (String) jcb_users.getSelectedItem();
				receiver = receiver.trim();
				String content = jtf_send.getText();
				conn.sendTextChat(userName, receiver, content);
				
				
			}
			
		};
		bu_send.addActionListener(sendListener);
		jtf_send.addActionListener(sendListener);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
		
	}
}
