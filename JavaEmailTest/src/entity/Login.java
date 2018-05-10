package entity;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class Login implements HttpSessionBindingListener,Serializable{
	//在线人数
	private static int loginCount=0;
	
	
	private int id;
	private String username;
	private String password;
	private String state;
	private String usertype;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public Login(int id, String username, String password, String state, String usertype) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.state = state;
		this.usertype = usertype;
	}
	public Login() {
		super();
	}
	
	//绑定登录实体类监听事件
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		System.out.println("绑定了");
		event.getSession().getServletContext().setAttribute("loginCount", ++loginCount);
	}
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		event.getSession().getServletContext().setAttribute("loginCount", --loginCount);
	}
	
}
