package client;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import type.UserInfo;

public class UserInfoTableModel implements TableModel{

	
	private List<UserInfo> userList;
	
	UserInfoTableModel(List<UserInfo> userList) {
		this.userList = userList;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "ID";
		} else if (columnIndex == 1) {
			return "Name";
		} else {
			return "Fault";
		}
		
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		UserInfo user = userList.get(rowIndex);
		if (columnIndex == 0) {
			return "" + user.getJkNum();
		} else if (columnIndex == 1) {
			return user.getName();
		} else {
			return "Fault";
		}
		
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	
	
}
