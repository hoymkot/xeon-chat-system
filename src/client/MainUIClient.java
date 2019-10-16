package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import message.IMsgConstance;
import message.MsgAddFriend;
import message.MsgFindResp;
import message.MsgHead;
import type.UserInfo;

public class MainUIClient extends JFrame implements IClientMsgListener {

	private int jkNum;
	private JKUserTree userTree;
	private ClientConnection conn = ClientConnection.getIns();

	public MainUIClient(int jkNum) {
		this.jkNum = jkNum;
		userTree = new JKUserTree(jkNum);

	}

	public void showMainUI() {
		this.setTitle("Xeon: " + jkNum);
		FlowLayout fl = new FlowLayout();
		this.setLayout(fl);
		this.setSize(200, 700);
		JMenuBar mb = getMB();
		this.setJMenuBar(mb);
		this.add(userTree);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
	}

	@Override
	public void fireMsg(MsgHead m) {
		if (m.getType() == IMsgConstance.command_find_resp) {
			MsgFindResp resp = (MsgFindResp) m;
			List<UserInfo> users = resp.getUsers();
			showFindResult(users);
		} else if (m.getType() == IMsgConstance.command_chatText || m.getType() == IMsgConstance.command_chatFile
				|| m.getType() == IMsgConstance.command_addFriend_Resp || m.getType() == IMsgConstance.command_teamList
				|| m.getType() == IMsgConstance.command_offLine || m.getType() == IMsgConstance.command_onLine) {
			this.userTree.onMsgReceive(m);
		} else {
			JOptionPane.showMessageDialog(this, "what message? " + m.getType());
		}

	}

	private JMenuBar getMB() {
		JMenuBar mb = new JMenuBar();
		JMenu me_file = new JMenu("File");
		JMenuItem mi_find = new JMenuItem("Add Buddy");
		mi_find.setActionCommand("add");
		JMenuItem mi_exit = new JMenuItem("exit");
		mi_exit.setActionCommand("exit");
		me_file.add(mi_find);
		me_file.add(mi_exit);

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String com = e.getActionCommand();
				if (com.equals("exit")) {
					// TODO: go offline;
					System.exit(0);
				}
				if (com.equals("add")) {
					final JFrame jf_add = new JFrame("add buddy");
					FlowLayout fl = new FlowLayout();
					jf_add.setLayout(fl);
					jf_add.setSize(200, 160);
					final JTextField jta_id = new JTextField(12);

					JLabel la_regName = new JLabel("ID");

					jf_add.add(la_regName);
					jf_add.add(jta_id);

					JButton bu_add = new JButton("Add Buddy");
					jf_add.add(bu_add);
					jf_add.setLocationRelativeTo(null);
					jf_add.setVisible(true);
					
					final int srcId = jkNum;

					ActionListener buttonAction = new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String id = jta_id.getText().trim();
							Integer ID = null;
							try {
								ID = Integer.parseInt(id);
							} catch (NumberFormatException exp) {
								JOptionPane.showMessageDialog(null, "ID should be a number.");
								return;
							}
							String s = "fail to connect to server";
							if (false == conn.addUser(ID, srcId)) { 
								JOptionPane.showMessageDialog(jf_add, "add user failed");
							}
							// wait for callback to return the result
						}
					};

					bu_add.addActionListener(buttonAction);

				}
			}

		};

		mi_find.addActionListener(al);
		mi_exit.addActionListener(al);
		mb.add(me_file);
		return mb;
	}

	private void findAction() {
		try {
			MsgHead findMsg = new MsgHead();
			findMsg.setTotalLen(4 + 1 + 4 + 4);
			findMsg.setType(IMsgConstance.command_find);
			findMsg.setSrc(jkNum);
			findMsg.setDest(IMsgConstance.Server_JK_NUMBER);
			conn.sendMsg(findMsg);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "fail to find");
		}
	}

	private void showFindResult(final List<UserInfo> users) {
		final JDialog jda = new JDialog();
		jda.setTitle(" friend find result ");
		jda.setSize(300, 400);
		jda.setLayout(new FlowLayout());
		final JTable table = new JTable();
		UserInfoTableModel model = new UserInfoTableModel(users);
		table.setModel(model);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = table.getSelectedRow();
					if (index != -1) {
						UserInfo destU = users.get(index);
						MsgAddFriend ma = new MsgAddFriend();
						ma.setTotalLen(4 + 1 + 4 + 4 + 4);
						ma.setDest(IMsgConstance.Server_JK_NUMBER);
						ma.setSrc(jkNum);
						ma.setType(IMsgConstance.command_addFriend);
						ma.setFriendJkNum(destU.getJkNum());
						try {
							conn.sendMsg(ma);
						} catch (Exception ef) {
							ef.printStackTrace();
						}
					}
				}
			}
		});
		jda.add(table);
		jda.setVisible(true);

	}

}
