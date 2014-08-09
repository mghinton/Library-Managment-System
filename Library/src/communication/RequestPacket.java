package communication;
/**
 * RequestPacket application - file RequestPacket.java 
 * 
 * This object carries message between server and client
 * 
 * @author Sardor Isakov
 * @version 3.0
 */

import java.io.Serializable;

import com.sun.rowset.CachedRowSetImpl;

/** Representation for RequestPacket object
 */
public class RequestPacket implements Serializable {
	
	private static final long serialVersionUID = 9649112634549L;

	public static final int REQUEST_SQL = 1, REQUEST_LOGIN = 2, REQUEST_LOGOUT = 3,REQUEST_CHANGEPASSWORD = 4, REQUEST_DISCONNECT = 99;
	public static final int SELECT = 1, UPDATE = 2, DELETE = 3, INSERT = 4;
	
	
	private int sqlType;
	private int packetType;
	private String sqlStatment;
	private String sqlCheck[];	//the checks that will be performed before performing the sqlStatement
	private CachedRowSetImpl rowSet;
	private int insertID;
	private boolean success;
	private String errorMsg = null;
	
	private int userID;
	private String userPassword;
	private String newPassword;
	
	public RequestPacket(){
	}
	
	
	public int getSQLType() {
		return sqlType;
	}
	
	public void setSQLType(int sqlType) {
		this.sqlType = sqlType;
	}
	
	public String getSQLStatment() {
		return sqlStatment;
	}
	
	public void setSQLStatment(String sqlStatment) {
		this.sqlStatment = sqlStatment;
	}
	
	public CachedRowSetImpl getRowSet() {
		return rowSet;
	}
	
	public void setRowSet(CachedRowSetImpl rowSet) {
		this.rowSet = rowSet;
	}

	public int getPacketType() {
		return packetType;
	}

	public void setPacketType(int packetType) {
		this.packetType = packetType;
	}

	public int getInsertResult() {
		return insertID;
	}

	public void setInsertResult(int insertID) {
		this.insertID = insertID;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String[] getSQLCheck(){
		return sqlCheck;
	}
	
	public void setSQLCheck(String sqlCheck[]){
		this.sqlCheck = sqlCheck;
	}
	
	public String getErrorMsg(){
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public void setPassword(String userPassword){
		this.userPassword = userPassword;
	}
	
	public String getPassword(){
		return userPassword;
	}
	
	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}
	
	public String getNewPassword(){
		return this.newPassword;
	}

}
