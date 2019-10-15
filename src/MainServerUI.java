import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import server.ChatTools;

public class MainServerUI {
	private ChatServer cserver;
	private JFrame jf;
	static JTable table_onlineUser;
	private JTextField jta_msg;
	private JTextField jta_port;
	private JButton bu_control_chat;
	static UserInfoTableModel utm;

	public static void main(String[] args) {
		MainServerUI mu = new MainServerUI();
		mu.showUI();
	}

	public void showUI() {
		jf = new javax.swing.JFrame("Xeon Chat Server Administration Program");
		jf.setSize(600, 300);
		FlowLayout fl = new FlowLayout();
		jf.setLayout(fl);

		JLabel la_port = new JLabel("Server Port: ");
		jf.add(la_port);
		jta_port = new JTextField(4);
		jf.add(jta_port);
		bu_control_chat = new JButton("Turn On Server");
		jf.add(bu_control_chat);
		bu_control_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionServer();
			}
		});
		JLabel la_msg = new JLabel("Msg to be sent:");
		jf.add(la_msg);
		jta_msg = new javax.swing.JTextField(30);
		ActionListener sendCaseMsgAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAllMsg();
			}
		};
		jta_msg.addActionListener(sendCaseMsgAction);
		JButton bu_send = new JButton("send");
		bu_send.addActionListener(sendCaseMsgAction);
		jf.add(jta_msg);
		jf.add(bu_send);

		table_onlineUser = new JTable();
		List<ProcessThread> sts = ChatTools.getAllThread();
		utm = new UserInfoTableModel(sts);
		table_onlineUser.setModel(utm);
		JScrollPane scrollPane = new JScrollPane(table_onlineUser);
		table_onlineUser.setPreferredScrollableViewportSize(new Dimension(500, 100));
		scrollPane.setAutoscrolls(true);
		jf.add(scrollPane);

		JPopupMenu pop = getTablePop();
		table_onlineUser.setComponentPopupMenu(pop);

		jf.setVisible(true);
		jf.setDefaultCloseOperation(3);

	}

	private JPopupMenu getTablePop() {
		JPopupMenu pop = new JPopupMenu();
		JMenuItem mi_send = new JMenuItem("send");
		mi_send.setActionCommand("send");
		JMenuItem mi_del = new JMenuItem("kick");
		mi_del.setActionCommand("del");
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
				popMenuAction(s);
			}
		};
		mi_send.addActionListener(al);
		mi_del.addActionListener(al);
		pop.add(mi_send);
		pop.add(mi_del);
		return pop;
	}

	protected void popMenuAction(String command) {
		final int selectIndex = table_onlineUser.getSelectedRow();
		if (selectIndex == -1) {
			JOptionPane.showMessageDialog(jf, "please select an user");
			return;
		}
		String username = (String) ((UserInfoTableModel) table_onlineUser.getModel()).getUserName(selectIndex);
		if (command.equals("del")) {
			ChatTools.removeUser(username);
		} else if (command.equals("send")) {
			final JDialog jd = new JDialog(jf, true);
			jd.setLayout(new FlowLayout());
			jd.setTitle("send msg to " + username);
			jd.setSize(300, 100);
			final JTextField jtd_m = new JTextField(20);
			JButton jb = new JButton("send");
			jd.add(jtd_m);
			jd.add(jb);
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("send a message");
					String msg = "system wispher: " + jtd_m.getText();
					ChatTools.castMsg("system wispher", msg, username);
					jtd_m.setText("");
					jd.dispose();
				}
			});
			jd.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(jf, "unknown menu: " + command);
		}
		SwingUtilities.updateComponentTreeUI(table_onlineUser);
	}

	protected void sendAllMsg() {
		String msg = jta_msg.getText();
		ChatTools.castAsSystem(msg);
		jta_msg.setText("");
	}

	protected void actionServer() {
		if (null == cserver) {
			String sPort = jta_port.getText();
			int port = Integer.parseInt(sPort);
			cserver = new ChatServer(port);
			cserver.start();
			jf.setTitle("QQ Server Administration Program: Running");
			bu_control_chat.setText("Stop!");
		} else if (cserver.isRuning()) {
			cserver.stopChatServer();
			cserver = null;
			ChatTools.removeAllClient();
			jf.setTitle("QQ Server Administration Program: Stopped");
			bu_control_chat.setText("Start!");
		}
	}
}
