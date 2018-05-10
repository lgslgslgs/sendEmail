package entity;

import java.io.Serializable;

public class Cls implements Serializable{
	private int cid;
	private String cname;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Cls(int cid, String cname) {
		super();
		this.cid = cid;
		this.cname = cname;
	}
	public Cls() {
		super();
	}
	
}
