import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class UserInfoTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	UserInfoTableModel(List<ProcessThread> ct){
		super();
		this.addColumn("name");
		this.addColumn("loginTime");
		this.addColumn("address");
		initTable(ct);
	}
	
	void initTable(List<ProcessThread> ct){
		for (ProcessThread t : ct) {
			this.addRow(t);
		}		
	}
	
	void refresh(List<ProcessThread> ct) {
		int rowCount = this.getRowCount();
		for(int i = 0; i< rowCount; i++){
			this.removeRow(0);
		}
		initTable(ct);
	}
	
	void addRow(ProcessThread t){
		Vector<String> row = new Vector<String>();
		row.addElement(t.user.name);
		row.addElement(t.user.loginTime);
		row.addElement(t.user.address);
		this.addRow(row);
		
	}
	
	String getUserName(int selectIndex) {
		return (String)this.getValueAt(selectIndex, 0);
	}
}
