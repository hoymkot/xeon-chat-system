import java.util.HashMap;
import java.util.Map;

public class ServerDao {

	private static Map<String, String> userMap = new HashMap<String, String>();
	
	public static boolean saveUser(String name, String pwd){
		if(userMap.containsKey(name)){
			return false;
		}
		userMap.put(name,  pwd);
		return true;
	}
	
	public static boolean hasUser(String name, String pwd){
		String s = userMap.get(name);
		if(s!= null) {
			// &&s.equals(pwd)// 
			return true;
		}
		return false;
	}
	
}
