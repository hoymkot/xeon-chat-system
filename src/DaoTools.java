import java.util.HashMap;
import java.util.Map;

import type.UserInfo;

public class DaoTools {

	public static boolean checkLogin(UserInfo user){
		if(userDB.containsKey(user.getName())){
			return true;
		}
		System.out.println("Authentication Failed: " + user.getName());
		return false;
	}
	
	private static Map<String, UserInfo> userDB = new HashMap<String, UserInfo>();
	
	
	static {
		for(int i = 0; i< 10; i++ ){
			UserInfo user = new UserInfo();
			user.setName("user" + i);
			user.setPwd("pwd"+i);

			userDB.put(user.getName(), user);
		}
	}
}
