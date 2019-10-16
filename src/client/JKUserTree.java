package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import log.LogTools;
import message.IMsgConstance;
import message.MsgAddFriendResp;
import message.MsgChatFile;
import message.MsgChatText;
import message.MsgHead;
import message.MsgTeamList;
import type.TeamInfo;
import type.UserInfo;

public class JKUserTree extends JTree {

	private DefaultMutableTreeNode root;
	private int jkNum;
	private ClientConnection conn = ClientConnection.getIns();

	public JKUserTree(int jkNum) {
		this.jkNum = jkNum;
		root = new DefaultMutableTreeNode("my buddies");
		DefaultTreeModel tm = new DefaultTreeModel(root);
		this.setModel(tm);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (2== e.getClickCount()) {
					showSendFrame();
				}
			}
		});

	}

	public void onMsgReceive(MsgHead m) {
		if (m.getType() == IMsgConstance.command_teamList) {
			MsgTeamList ms = (MsgTeamList) m;
			List<TeamInfo> teams = ms.getTeamLists();
			for (TeamInfo t : teams) {
				List<UserInfo> users = t.getBuddyList();
				for (UserInfo u : users) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(u);
					root.add(newNode);
				}
			}
		} else if (m.getType() == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mf = (MsgAddFriendResp) m;
			if (mf.getFriendNickName().trim().equals("") ) {
				JOptionPane.showMessageDialog(null, " unable to add friend "  + mf.getFriendJkNum());
			} else {
				UserInfo buddy = new UserInfo(mf.getFriendJkNum());
				buddy.setName(mf.getFriendNickName());
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(buddy);
				root.add(node);
			}
		} else if (m.getType() == IMsgConstance.command_chatText) {
			MsgChatText mt = (MsgChatText) m;
			JOptionPane.showMessageDialog(this, mt.getSrc() + " says " + mt.getMsgContent());
		} else if (m.getType() == IMsgConstance.command_chatFile) {
			MsgChatFile mt = (MsgChatFile) m;
			JOptionPane.showMessageDialog(this, "received file, what are you going to do?");
		} else if (m.getType() == IMsgConstance.command_onLine) {
			JOptionPane.showMessageDialog(this, m.getSrc() + " go online!");
		} else if (m.getType() == IMsgConstance.command_offLine) {
			JOptionPane.showMessageDialog(this, m.getSrc() + " go offline!");
		} else {
			JOptionPane.showMessageDialog(this, " unidentified message type " + m.getType());
		}
		SwingUtilities.updateComponentTreeUI(this);
	}

	private void showSendFrame() {
		TreePath tp = this.getSelectionPath();
		if (tp == null ) return;
		Object obj = tp.getLastPathComponent();
		DefaultMutableTreeNode node =  (DefaultMutableTreeNode) obj;
		Object userO= node.getUserObject();
		if(userO instanceof UserInfo) {
			final UserInfo destUser = (UserInfo) userO; 
			final JDialog jda = new JDialog();
			String s = "to " + destUser.getName();
			jda.setTitle(s);
			jda.setSize(200,150);
			FlowLayout fl = new FlowLayout();
			jda.setLayout(fl);
			JLabel la = new JLabel("enter your message to " + destUser.getName());
			jda.add(la);
			final JTextField jta = new JTextField(15);
			jda.add(jta);
			jda.setVisible(true);
			jta.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent a) {
					String msg = jta.getText();
					MsgChatText mct = new MsgChatText();
					mct.setType(IMsgConstance.command_chatText);
					mct.setTotalLen(4+1+4+4+msg.getBytes().length);
					mct.setDest(destUser.getJkNum());
					mct.setSrc(jkNum);
					mct.setMsgContent(msg);
					try {
						conn.sendMsg(mct);
					} catch (Exception e) {
						e.printStackTrace();
						LogTools.ERROR(this.getClass(), "send error " + e);
					}
					jda.dispose();
				}
				
			});
			
		}
	}

}
