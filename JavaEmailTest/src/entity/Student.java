package entity;

import java.io.Serializable;

public class Student implements Serializable{
	private int sid;
	private String sname;
	private int s_cid;
	private String cname;
	
	public Student(int sid, String sname, int s_cid, String cname) {
		super();
		this.sid = sid;
		this.sname = sname;
		this.s_cid = s_cid;
		this.cname = cname;
	}
	public Student() {
		super();
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public int getS_cid() {
		return s_cid;
	}
	public void setS_cid(int s_cid) {
		this.s_cid = s_cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
}
