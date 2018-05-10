package entity;

import java.io.Serializable;

public class User implements Serializable{
	private String username;
	private String password;
	private int sex;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public User(String username, String password, int sex) {
		super();
		this.username = username;
		this.password = password;
		this.sex = sex;
	}
	public User() {
		super();
	}
	
	

}
