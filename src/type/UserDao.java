package type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log.LogTools;

public class UserDao {
	
	
	
	public static UserInfo checkLogin(int jkNum, String pwd) {
		UserInfo user = userDB.get(jkNum);
		if (null!= user&& user.getPwd().equals(pwd)) {
			LogTools.INFO(UserDao.class, " login success " + jkNum );
			return user;
		} else {
			LogTools.ERROR(UserDao.class, " login failure " + jkNum );
			return null;
		}
	}
	
	public static int regUser(String pwd, String nickname) {
		if (userDB.size() > 0 ) {
			maxJKNum = Collections.max(userDB.keySet());
		}
		maxJKNum++;
		
		UserInfo user = new UserInfo (maxJKNum);
		user.setPwd(pwd);
		user.setName(nickname);
		TeamInfo team = new TeamInfo(0, "My Buddies", user);
		user.addTeams(team);
		userDB.put(maxJKNum, user);
		saveDB();
		
		return maxJKNum;
		
		
	}
	
	public static List<UserInfo> findUser() {
		List<UserInfo> list = new ArrayList<UserInfo>() ;
		list.addAll(userDB.values());
		return list;
		
	}
	
	public static UserInfo addFriend(int srcJKNum, int destJKNum) {
		UserInfo user1 = userDB.get(srcJKNum);
		UserInfo user2 = userDB.get(destJKNum);
		user1.getTeams().get(0).addBuddy(user2);
		user2.getTeams().get(0).addBuddy(user1);
		saveDB();
		return user2;
	}
	
	private static void saveDB() {
		try {
			OutputStream ous = new FileOutputStream(dbFileName);
			ObjectOutputStream oos = new ObjectOutputStream(ous) ;
			oos.writeObject(userDB);
			oos.flush();
			ous.close();
			LogTools.INFO(UserDao.class, "data file refresh success ");
		} catch (Exception e) {
			LogTools.ERROR(UserDao.class, "data file refresh failure " + e);
		}
	}
	
	private static int maxJKNum = 1000;
	private static final String dbFileName = "xeon-chat.dat";
	private static Map<Integer, UserInfo> userDB = new HashMap<Integer, UserInfo>();
	
	static {
		try {
			File df = new File(dbFileName);
			if(df.exists() && !df.isDirectory()) {
				InputStream ins = new FileInputStream(dbFileName);
				ObjectInputStream ons = new ObjectInputStream(ins);
				userDB = (Map<Integer, UserInfo>)ons.readObject();
				LogTools.INFO(UserDao.class, "load data from file success ");
			} else {
				LogTools.ERROR(UserDao.class, "data file not exists, create a new data file");
			}
		} catch (Exception ef) {
			LogTools.ERROR(UserDao.class, "data file initialization error " + ef );
		}
	}
}
