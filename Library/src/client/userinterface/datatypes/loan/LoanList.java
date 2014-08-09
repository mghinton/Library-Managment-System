package client.userinterface.datatypes.loan;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;
import client.control.data.entity.resource.Loan;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list for storing loans
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class LoanList extends ListPanel {
	
	private ArrayList<Object> loans = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public LoanList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return loans;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		loans = (ArrayList<Object>) items;
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return loans.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				Loan loan = (Loan)loans.get(row);
				LoanViewer viewer = new LoanViewer(loan);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "ID", "Check Out Date", "Check In Date", "Due Date", "Fine Amount"};

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (loans == null)
				return 0;
			return loans.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (loans == null)
				return null;
			if (rowIndex < 0 || rowIndex >= loans.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			Loan loan = (Loan) loans.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return loan.getID();
			case 1:
				if (loan.getCheckOutDate() == null)
					return "";
				return Utilities.formatDate(loan.getCheckOutDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 2:
				if (loan.getCheckInDate() == null)
					return "";
				return Utilities.formatDate(loan.getCheckInDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 3:
				if (loan.getDueDate() == null)
					return "";
				return Utilities.formatDate(loan.getDueDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 4:
				return new DecimalFormat("0.00").format(loan.getFineAmount());
			}
			return loan;
		}

	}

}
