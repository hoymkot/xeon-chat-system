import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * be aware of synchronization problems, may use database for process thread
 * storage. we may ignore this problem, given that we don't expect massive
 * clients. by the way, the books might talk about synchronization problem
 * later. 2017-08-29 Twitter - 1.2.5
 * 
 * @return
 */
public class ChatTools {

	static Map<String, ProcessThread> pts = new HashMap<String, ProcessThread>();

	private ChatTools() {

	}

	/**
	 * be aware of synchronization problems
	 * 
	 * @return
	 */
	public static List<ProcessThread> getAllThread() {
		List<ProcessThread> list = new LinkedList<ProcessThread>();
		// not thread safe ?
		list.addAll(pts.values());
		return list;
	}

	//
	// public static void addClient(ProcessThread ct) {
	// castMsg(ct.getOwnerUser(), " I am online! current number " +
	// stList.size());
	// stList.add(ct);
	// ChatTools.castAsSystem("the number of clients connected is " + (new
	// Integer(ChatTools.stList.size()).toString()));
	// MainServerUI.utm.refresh(stList);
	//
	//
	// }
	//
	// public static void sendMsg2One(int index, String msg) {
	// stList.get(index).sendMsg2Me(msg);
	// }
	//
	// public static UserInfo getUser(String username) {
	// return ((ProcessThread)pts.get(username)).getUser;
	// }
	//
	// /**
	// * right after closing the client socket, the corresponding server thread
	// kill itself automatically and refresh the user info table as well.
	// * @param index
	// */
	// public static void removeClient(int index) {
	//
	// ProcessThread st = stList.remove(index);
	// st.closeMe();
	// }
	//
	// /**
	// * Remove all clients of a particular user.
	// *
	// * @param user
	// */
	// public static void removeAllClient(UserInfo user) {
	// for (ProcessThread ct : stList)
	// if (ct.getOwnerUser().getName().equals(user.getName())) {
	// ct.closeMe();
	// stList.remove(ct);
	// ct = null;
	// castMsg(user, "I am down");
	// }
	// }
	//
	//
	// public static void removeClient(ProcessThread serverThread) {
	// UserInfo user = serverThread.getOwnerUser();
	// serverThread.closeMe();
	// stList.remove(serverThread);
	// castMsg(user, "I am down");
	// MainServerUI.utm.refresh(stList);
	// }
	//
	//
	
	/**
	 * not thread safe
	 */
	public static void removeAllClient() {
		try {
			for (ProcessThread pt : pts.values()){
				pt.sendMsg2Me("System Notification: Server Shutting Down");
				pt.closeMe();
				pts.remove(pt.getMyUserName());
				System.out.println(pt.getMyUserName() + " disconnected");
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
	}
	
	//
	//
	// public static void castAsSystem(String msg){
	// UserInfo user = new UserInfo();
	// user.setName("system");
	// user.setPwd("pwd");
	// ChatTools.castMsg(user, msg);
	// }
	//
	// public static void castMsg(UserInfo sender, String msg) {
	// msg = sender.getName() + " says: " + msg;
	// for (int i = 0; i < stList.size(); i++) {
	// ProcessThread st = stList.get(i);
	// try {
	// st.sendMsg2Me(msg);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

	public static void removeUser(String userName) {
		ProcessThread pt = pts.remove(userName);
		Set<String> set = pts.keySet();
		Iterator<String> it = set.iterator();
		String names = "";
		String onlineMsg = "<msg><type>offLine</type>";
		String onlineContent = onlineMsg + "<user>" + userName + "</user></msg>";
		while (it.hasNext()) {
			String nextName = it.next();
			if (!nextName.equals(userName)) {
				pts.get(nextName).sendMsg2Me(onlineContent);
				names += "," + nextName;
			}
		}

	}

	public static void castMsg(String userName, String msg, String destUserName) {
		for (int i = 0; i < pts.size(); i++) {
			ProcessThread pt = pts.get(destUserName);
			try {
				if (null != pt & pt.getMyUserName().equals(destUserName)) {
					pt.sendMsg2Me(msg);
					break;
				}
			} catch (Exception ef) {
				ef.printStackTrace();
			}
		}

	}

	public static void castAsSystem(String msg) {
		for (ProcessThread pt : pts.values()) {
			pt.sendMsg2Me(msg);
		}
	}

	public static void addPT(String userName, ProcessThread pt) {
		pts.put(userName, pt);
		Set<String> set = pts.keySet();
		Iterator<String> it = set.iterator();
		String names = "";
		String onlineMsg = "<msg><type>onLine</type>";
		String onlineContent = onlineMsg + "<user>" + userName + "</user></msg>";
		while (it.hasNext()) {
			String nextName = it.next();
			pts.get(nextName).sendMsg2Me(onlineContent);
			names += "," + nextName;
		}
		String head = "<msg><type>buddyList</type>";
		String content = "<users>" + names + "</users>";
		String buddyListMsg = head + content + "</msg>";
		pt.sendMsg2Me(buddyListMsg);
	}

}
