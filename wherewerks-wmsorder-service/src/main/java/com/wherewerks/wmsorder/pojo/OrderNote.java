package com.wherewerks.wmsorder.pojo;

import java.util.Date;

public class OrderNote {
	private String client_id;
	private String ordnum;
	private String nottyp;
	private String notlin;
	private String wh_id;
	private String nottxt;
	private int edtflg;
	private double u_version;
	private Date ins_dt;
	private Date last_upd_dt;
	private String ins_user_id;
	private String last_upd_user_id;
	public OrderNote(String client_id, String ordnum, String nottyp, String notlin, String wh_id, String nottxt,
			int edtflg, double u_version, Date ins_dt, Date last_upd_dt, String ins_user_id, String last_upd_user_id) {
		this.client_id = client_id;
		this.ordnum = ordnum;
		this.nottyp = nottyp;
		this.notlin = notlin;
		this.wh_id = wh_id;
		this.nottxt = nottxt;
		this.edtflg = edtflg;
		this.u_version = u_version;
		this.ins_dt = ins_dt;
		this.last_upd_dt = last_upd_dt;
		this.ins_user_id = ins_user_id;
		this.last_upd_user_id = last_upd_user_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getOrdnum() {
		return ordnum;
	}
	public void setOrdnum(String ordnum) {
		this.ordnum = ordnum;
	}
	public String getNottyp() {
		return nottyp;
	}
	public void setNottyp(String nottyp) {
		this.nottyp = nottyp;
	}
	public String getNotlin() {
		return notlin;
	}
	public void setNotlin(String notlin) {
		this.notlin = notlin;
	}
	public String getWh_id() {
		return wh_id;
	}
	public void setWh_id(String wh_id) {
		this.wh_id = wh_id;
	}
	public String getNottxt() {
		return nottxt;
	}
	public void setNottxt(String nottxt) {
		this.nottxt = nottxt;
	}
	public int getEdtflg() {
		return edtflg;
	}
	public void setEdtflg(int edtflg) {
		this.edtflg = edtflg;
	}
	public double getU_version() {
		return u_version;
	}
	public void setU_version(double u_version) {
		this.u_version = u_version;
	}
	public Date getIns_dt() {
		return ins_dt;
	}
	public void setIns_dt(Date ins_dt) {
		this.ins_dt = ins_dt;
	}
	public Date getLast_upd_dt() {
		return last_upd_dt;
	}
	public void setLast_upd_dt(Date last_upd_dt) {
		this.last_upd_dt = last_upd_dt;
	}
	public String getIns_user_id() {
		return ins_user_id;
	}
	public void setIns_user_id(String ins_user_id) {
		this.ins_user_id = ins_user_id;
	}
	public String getLast_upd_user_id() {
		return last_upd_user_id;
	}
	public void setLast_upd_user_id(String last_upd_user_id) {
		this.last_upd_user_id = last_upd_user_id;
	}
	@Override
	public String toString() {
		return "OrderNote [client_id=" + client_id + ", ordnum=" + ordnum + ", nottyp=" + nottyp + ", notlin=" + notlin
				+ ", wh_id=" + wh_id + ", nottxt=" + nottxt + ", edtflg=" + edtflg + ", u_version=" + u_version
				+ ", ins_dt=" + ins_dt + ", last_upd_dt=" + last_upd_dt + ", ins_user_id=" + ins_user_id
				+ ", last_upd_user_id=" + last_upd_user_id + "]";
	}
	
}
