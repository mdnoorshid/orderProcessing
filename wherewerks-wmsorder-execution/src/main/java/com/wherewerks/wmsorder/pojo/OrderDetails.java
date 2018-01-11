package com.wherewerks.wmsorder.pojo;

import java.util.Date;

public class OrderDetails {
	
	private String clientId;
	private String ordnum;
	private String wh_id;
	private String btcust;
	private String stcust;
	private String rtcust;
	private String brcust;
	private String bt_adr_id;
	private String st_adr_id;
	private String rt_adr_id;
	private String br_adr_id;
	private String ordtyp;
	private String entdte;
	private Date adddte;
	private String cpotyp;
	private String cponum;
	private Date cpodte;
	private String srv_type;
	private String paytrm;
	private int carflg;
	private String shipby;
	private int rrlflg;
	private int wave_flg;
	private int requir_tms_flg;
	private int rmanum;
	private int super_ord_flg;
	private String super_ordnum;
	private String ord_frtrte;
	private int bco_flg;
	private int sig_req_flg;
	private int bill_freight_flg;
	private int customs_clr_flg;
	private int cod_ind_type;
	private String cod_payment_type;
	private String payment_type;
	private String sfcust;
	private String sf_adr_id;
	private String csr_nam;
	private String csr_phnnum;
	private String csr_email_adr;
	private String bus_grp;
	private String host_appt_num;
	private String dlv_contact;
	private String ord_dir;
	private float frt_allow;
	private String ord_spl_cod;
	private int template_flg;
	private String template_ref;
	private String crncy_code;
	private String bto_seqnum;
	private String slot;
	private Date moddte;
	private String mod_usr_id;
	private String exp_adr_id;
	private String exp_cust;
	private String imp_adr_id;
	private String imp_cust;
	private int rtetransflg;
	private String relpartiescod;
	private String contract_nam;
	private String duty_payment_type;
	private String duty_payment_acct;
	private String ret_adr_id;
	private String ret_cust;
	private String deptno;
	private String dest_num;
	private int rush_flg;
	private String cstms_dtycust;
	private String excise_dtycust;
	private String cstms_ord_stat;
	private String cstms_stat_notes;
	private String cstms_ordtyp;
	private String ordcolcstms_addl_info;
	public OrderDetails(String clientId, String ordnum, String wh_id, String btcust, String stcust, String rtcust,
			String brcust, String bt_adr_id, String st_adr_id, String rt_adr_id, String br_adr_id, String ordtyp,
			String entdte, Date adddte, String cpotyp, String cponum, Date cpodte, String srv_type, String paytrm,
			int carflg, String shipby, int rrlflg, int wave_flg, int requir_tms_flg, int rmanum, int super_ord_flg,
			String super_ordnum, String ord_frtrte, int bco_flg, int sig_req_flg, int bill_freight_flg,
			int customs_clr_flg, int cod_ind_type, String cod_payment_type, String payment_type, String sfcust,
			String sf_adr_id, String csr_nam, String csr_phnnum, String csr_email_adr, String bus_grp,
			String host_appt_num, String dlv_contact, String ord_dir, float frt_allow, String ord_spl_cod,
			int template_flg, String template_ref, String crncy_code, String bto_seqnum, String slot, Date moddte,
			String mod_usr_id, String exp_adr_id, String exp_cust, String imp_adr_id, String imp_cust, int rtetransflg,
			String relpartiescod, String contract_nam, String duty_payment_type, String duty_payment_acct,
			String ret_adr_id, String ret_cust, String deptno, String dest_num, int rush_flg, String cstms_dtycust,
			String excise_dtycust, String cstms_ord_stat, String cstms_stat_notes, String cstms_ordtyp,
			String ordcolcstms_addl_info) {
		super();
		this.clientId = clientId;
		this.ordnum = ordnum;
		this.wh_id = wh_id;
		this.btcust = btcust;
		this.stcust = stcust;
		this.rtcust = rtcust;
		this.brcust = brcust;
		this.bt_adr_id = bt_adr_id;
		this.st_adr_id = st_adr_id;
		this.rt_adr_id = rt_adr_id;
		this.br_adr_id = br_adr_id;
		this.ordtyp = ordtyp;
		this.entdte = entdte;
		this.adddte = adddte;
		this.cpotyp = cpotyp;
		this.cponum = cponum;
		this.cpodte = cpodte;
		this.srv_type = srv_type;
		this.paytrm = paytrm;
		this.carflg = carflg;
		this.shipby = shipby;
		this.rrlflg = rrlflg;
		this.wave_flg = wave_flg;
		this.requir_tms_flg = requir_tms_flg;
		this.rmanum = rmanum;
		this.super_ord_flg = super_ord_flg;
		this.super_ordnum = super_ordnum;
		this.ord_frtrte = ord_frtrte;
		this.bco_flg = bco_flg;
		this.sig_req_flg = sig_req_flg;
		this.bill_freight_flg = bill_freight_flg;
		this.customs_clr_flg = customs_clr_flg;
		this.cod_ind_type = cod_ind_type;
		this.cod_payment_type = cod_payment_type;
		this.payment_type = payment_type;
		this.sfcust = sfcust;
		this.sf_adr_id = sf_adr_id;
		this.csr_nam = csr_nam;
		this.csr_phnnum = csr_phnnum;
		this.csr_email_adr = csr_email_adr;
		this.bus_grp = bus_grp;
		this.host_appt_num = host_appt_num;
		this.dlv_contact = dlv_contact;
		this.ord_dir = ord_dir;
		this.frt_allow = frt_allow;
		this.ord_spl_cod = ord_spl_cod;
		this.template_flg = template_flg;
		this.template_ref = template_ref;
		this.crncy_code = crncy_code;
		this.bto_seqnum = bto_seqnum;
		this.slot = slot;
		this.moddte = moddte;
		this.mod_usr_id = mod_usr_id;
		this.exp_adr_id = exp_adr_id;
		this.exp_cust = exp_cust;
		this.imp_adr_id = imp_adr_id;
		this.imp_cust = imp_cust;
		this.rtetransflg = rtetransflg;
		this.relpartiescod = relpartiescod;
		this.contract_nam = contract_nam;
		this.duty_payment_type = duty_payment_type;
		this.duty_payment_acct = duty_payment_acct;
		this.ret_adr_id = ret_adr_id;
		this.ret_cust = ret_cust;
		this.deptno = deptno;
		this.dest_num = dest_num;
		this.rush_flg = rush_flg;
		this.cstms_dtycust = cstms_dtycust;
		this.excise_dtycust = excise_dtycust;
		this.cstms_ord_stat = cstms_ord_stat;
		this.cstms_stat_notes = cstms_stat_notes;
		this.cstms_ordtyp = cstms_ordtyp;
		this.ordcolcstms_addl_info = ordcolcstms_addl_info;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getOrdnum() {
		return ordnum;
	}
	public void setOrdnum(String ordnum) {
		this.ordnum = ordnum;
	}
	public String getWh_id() {
		return wh_id;
	}
	public void setWh_id(String wh_id) {
		this.wh_id = wh_id;
	}
	public String getBtcust() {
		return btcust;
	}
	public void setBtcust(String btcust) {
		this.btcust = btcust;
	}
	public String getStcust() {
		return stcust;
	}
	public void setStcust(String stcust) {
		this.stcust = stcust;
	}
	public String getRtcust() {
		return rtcust;
	}
	public void setRtcust(String rtcust) {
		this.rtcust = rtcust;
	}
	public String getBrcust() {
		return brcust;
	}
	public void setBrcust(String brcust) {
		this.brcust = brcust;
	}
	public String getBt_adr_id() {
		return bt_adr_id;
	}
	public void setBt_adr_id(String bt_adr_id) {
		this.bt_adr_id = bt_adr_id;
	}
	public String getSt_adr_id() {
		return st_adr_id;
	}
	public void setSt_adr_id(String st_adr_id) {
		this.st_adr_id = st_adr_id;
	}
	public String getRt_adr_id() {
		return rt_adr_id;
	}
	public void setRt_adr_id(String rt_adr_id) {
		this.rt_adr_id = rt_adr_id;
	}
	public String getBr_adr_id() {
		return br_adr_id;
	}
	public void setBr_adr_id(String br_adr_id) {
		this.br_adr_id = br_adr_id;
	}
	public String getOrdtyp() {
		return ordtyp;
	}
	public void setOrdtyp(String ordtyp) {
		this.ordtyp = ordtyp;
	}
	public String getEntdte() {
		return entdte;
	}
	public void setEntdte(String entdte) {
		this.entdte = entdte;
	}
	public Date getAdddte() {
		return adddte;
	}
	public void setAdddte(Date adddte) {
		this.adddte = adddte;
	}
	public String getCpotyp() {
		return cpotyp;
	}
	public void setCpotyp(String cpotyp) {
		this.cpotyp = cpotyp;
	}
	public String getCponum() {
		return cponum;
	}
	public void setCponum(String cponum) {
		this.cponum = cponum;
	}
	public Date getCpodte() {
		return cpodte;
	}
	public void setCpodte(Date cpodte) {
		this.cpodte = cpodte;
	}
	public String getSrv_type() {
		return srv_type;
	}
	public void setSrv_type(String srv_type) {
		this.srv_type = srv_type;
	}
	public String getPaytrm() {
		return paytrm;
	}
	public void setPaytrm(String paytrm) {
		this.paytrm = paytrm;
	}
	public int getCarflg() {
		return carflg;
	}
	public void setCarflg(int carflg) {
		this.carflg = carflg;
	}
	public String getShipby() {
		return shipby;
	}
	public void setShipby(String shipby) {
		this.shipby = shipby;
	}
	public int getRrlflg() {
		return rrlflg;
	}
	public void setRrlflg(int rrlflg) {
		this.rrlflg = rrlflg;
	}
	public int getWave_flg() {
		return wave_flg;
	}
	public void setWave_flg(int wave_flg) {
		this.wave_flg = wave_flg;
	}
	public int getRequir_tms_flg() {
		return requir_tms_flg;
	}
	public void setRequir_tms_flg(int requir_tms_flg) {
		this.requir_tms_flg = requir_tms_flg;
	}
	public int getRmanum() {
		return rmanum;
	}
	public void setRmanum(int rmanum) {
		this.rmanum = rmanum;
	}
	public int getSuper_ord_flg() {
		return super_ord_flg;
	}
	public void setSuper_ord_flg(int super_ord_flg) {
		this.super_ord_flg = super_ord_flg;
	}
	public String getSuper_ordnum() {
		return super_ordnum;
	}
	public void setSuper_ordnum(String super_ordnum) {
		this.super_ordnum = super_ordnum;
	}
	public String getOrd_frtrte() {
		return ord_frtrte;
	}
	public void setOrd_frtrte(String ord_frtrte) {
		this.ord_frtrte = ord_frtrte;
	}
	public int getBco_flg() {
		return bco_flg;
	}
	public void setBco_flg(int bco_flg) {
		this.bco_flg = bco_flg;
	}
	public int getSig_req_flg() {
		return sig_req_flg;
	}
	public void setSig_req_flg(int sig_req_flg) {
		this.sig_req_flg = sig_req_flg;
	}
	public int getBill_freight_flg() {
		return bill_freight_flg;
	}
	public void setBill_freight_flg(int bill_freight_flg) {
		this.bill_freight_flg = bill_freight_flg;
	}
	public int getCustoms_clr_flg() {
		return customs_clr_flg;
	}
	public void setCustoms_clr_flg(int customs_clr_flg) {
		this.customs_clr_flg = customs_clr_flg;
	}
	public int getCod_ind_type() {
		return cod_ind_type;
	}
	public void setCod_ind_type(int cod_ind_type) {
		this.cod_ind_type = cod_ind_type;
	}
	public String getCod_payment_type() {
		return cod_payment_type;
	}
	public void setCod_payment_type(String cod_payment_type) {
		this.cod_payment_type = cod_payment_type;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getSfcust() {
		return sfcust;
	}
	public void setSfcust(String sfcust) {
		this.sfcust = sfcust;
	}
	public String getSf_adr_id() {
		return sf_adr_id;
	}
	public void setSf_adr_id(String sf_adr_id) {
		this.sf_adr_id = sf_adr_id;
	}
	public String getCsr_nam() {
		return csr_nam;
	}
	public void setCsr_nam(String csr_nam) {
		this.csr_nam = csr_nam;
	}
	public String getCsr_phnnum() {
		return csr_phnnum;
	}
	public void setCsr_phnnum(String csr_phnnum) {
		this.csr_phnnum = csr_phnnum;
	}
	public String getCsr_email_adr() {
		return csr_email_adr;
	}
	public void setCsr_email_adr(String csr_email_adr) {
		this.csr_email_adr = csr_email_adr;
	}
	public String getBus_grp() {
		return bus_grp;
	}
	public void setBus_grp(String bus_grp) {
		this.bus_grp = bus_grp;
	}
	public String getHost_appt_num() {
		return host_appt_num;
	}
	public void setHost_appt_num(String host_appt_num) {
		this.host_appt_num = host_appt_num;
	}
	public String getDlv_contact() {
		return dlv_contact;
	}
	public void setDlv_contact(String dlv_contact) {
		this.dlv_contact = dlv_contact;
	}
	public String getOrd_dir() {
		return ord_dir;
	}
	public void setOrd_dir(String ord_dir) {
		this.ord_dir = ord_dir;
	}
	public float getFrt_allow() {
		return frt_allow;
	}
	public void setFrt_allow(float frt_allow) {
		this.frt_allow = frt_allow;
	}
	public String getOrd_spl_cod() {
		return ord_spl_cod;
	}
	public void setOrd_spl_cod(String ord_spl_cod) {
		this.ord_spl_cod = ord_spl_cod;
	}
	public int getTemplate_flg() {
		return template_flg;
	}
	public void setTemplate_flg(int template_flg) {
		this.template_flg = template_flg;
	}
	public String getTemplate_ref() {
		return template_ref;
	}
	public void setTemplate_ref(String template_ref) {
		this.template_ref = template_ref;
	}
	public String getCrncy_code() {
		return crncy_code;
	}
	public void setCrncy_code(String crncy_code) {
		this.crncy_code = crncy_code;
	}
	public String getBto_seqnum() {
		return bto_seqnum;
	}
	public void setBto_seqnum(String bto_seqnum) {
		this.bto_seqnum = bto_seqnum;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public Date getModdte() {
		return moddte;
	}
	public void setModdte(Date moddte) {
		this.moddte = moddte;
	}
	public String getMod_usr_id() {
		return mod_usr_id;
	}
	public void setMod_usr_id(String mod_usr_id) {
		this.mod_usr_id = mod_usr_id;
	}
	public String getExp_adr_id() {
		return exp_adr_id;
	}
	public void setExp_adr_id(String exp_adr_id) {
		this.exp_adr_id = exp_adr_id;
	}
	public String getExp_cust() {
		return exp_cust;
	}
	public void setExp_cust(String exp_cust) {
		this.exp_cust = exp_cust;
	}
	public String getImp_adr_id() {
		return imp_adr_id;
	}
	public void setImp_adr_id(String imp_adr_id) {
		this.imp_adr_id = imp_adr_id;
	}
	public String getImp_cust() {
		return imp_cust;
	}
	public void setImp_cust(String imp_cust) {
		this.imp_cust = imp_cust;
	}
	public int getRtetransflg() {
		return rtetransflg;
	}
	public void setRtetransflg(int rtetransflg) {
		this.rtetransflg = rtetransflg;
	}
	public String getRelpartiescod() {
		return relpartiescod;
	}
	public void setRelpartiescod(String relpartiescod) {
		this.relpartiescod = relpartiescod;
	}
	public String getContract_nam() {
		return contract_nam;
	}
	public void setContract_nam(String contract_nam) {
		this.contract_nam = contract_nam;
	}
	public String getDuty_payment_type() {
		return duty_payment_type;
	}
	public void setDuty_payment_type(String duty_payment_type) {
		this.duty_payment_type = duty_payment_type;
	}
	public String getDuty_payment_acct() {
		return duty_payment_acct;
	}
	public void setDuty_payment_acct(String duty_payment_acct) {
		this.duty_payment_acct = duty_payment_acct;
	}
	public String getRet_adr_id() {
		return ret_adr_id;
	}
	public void setRet_adr_id(String ret_adr_id) {
		this.ret_adr_id = ret_adr_id;
	}
	public String getRet_cust() {
		return ret_cust;
	}
	public void setRet_cust(String ret_cust) {
		this.ret_cust = ret_cust;
	}
	public String getDeptno() {
		return deptno;
	}
	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}
	public String getDest_num() {
		return dest_num;
	}
	public void setDest_num(String dest_num) {
		this.dest_num = dest_num;
	}
	public int getRush_flg() {
		return rush_flg;
	}
	public void setRush_flg(int rush_flg) {
		this.rush_flg = rush_flg;
	}
	public String getCstms_dtycust() {
		return cstms_dtycust;
	}
	public void setCstms_dtycust(String cstms_dtycust) {
		this.cstms_dtycust = cstms_dtycust;
	}
	public String getExcise_dtycust() {
		return excise_dtycust;
	}
	public void setExcise_dtycust(String excise_dtycust) {
		this.excise_dtycust = excise_dtycust;
	}
	public String getCstms_ord_stat() {
		return cstms_ord_stat;
	}
	public void setCstms_ord_stat(String cstms_ord_stat) {
		this.cstms_ord_stat = cstms_ord_stat;
	}
	public String getCstms_stat_notes() {
		return cstms_stat_notes;
	}
	public void setCstms_stat_notes(String cstms_stat_notes) {
		this.cstms_stat_notes = cstms_stat_notes;
	}
	public String getCstms_ordtyp() {
		return cstms_ordtyp;
	}
	public void setCstms_ordtyp(String cstms_ordtyp) {
		this.cstms_ordtyp = cstms_ordtyp;
	}
	public String getOrdcolcstms_addl_info() {
		return ordcolcstms_addl_info;
	}
	public void setOrdcolcstms_addl_info(String ordcolcstms_addl_info) {
		this.ordcolcstms_addl_info = ordcolcstms_addl_info;
	}
	@Override
	public String toString() {
		return "OrderDetails [clientId=" + clientId + ", ordnum=" + ordnum + ", wh_id=" + wh_id + ", btcust=" + btcust
				+ ", stcust=" + stcust + ", rtcust=" + rtcust + ", brcust=" + brcust + ", bt_adr_id=" + bt_adr_id
				+ ", st_adr_id=" + st_adr_id + ", rt_adr_id=" + rt_adr_id + ", br_adr_id=" + br_adr_id + ", ordtyp="
				+ ordtyp + ", entdte=" + entdte + ", adddte=" + adddte + ", cpotyp=" + cpotyp + ", cponum=" + cponum
				+ ", cpodte=" + cpodte + ", srv_type=" + srv_type + ", paytrm=" + paytrm + ", carflg=" + carflg
				+ ", shipby=" + shipby + ", rrlflg=" + rrlflg + ", wave_flg=" + wave_flg + ", requir_tms_flg="
				+ requir_tms_flg + ", rmanum=" + rmanum + ", super_ord_flg=" + super_ord_flg + ", super_ordnum="
				+ super_ordnum + ", ord_frtrte=" + ord_frtrte + ", bco_flg=" + bco_flg + ", sig_req_flg=" + sig_req_flg
				+ ", bill_freight_flg=" + bill_freight_flg + ", customs_clr_flg=" + customs_clr_flg + ", cod_ind_type="
				+ cod_ind_type + ", cod_payment_type=" + cod_payment_type + ", payment_type=" + payment_type
				+ ", sfcust=" + sfcust + ", sf_adr_id=" + sf_adr_id + ", csr_nam=" + csr_nam + ", csr_phnnum="
				+ csr_phnnum + ", csr_email_adr=" + csr_email_adr + ", bus_grp=" + bus_grp + ", host_appt_num="
				+ host_appt_num + ", dlv_contact=" + dlv_contact + ", ord_dir=" + ord_dir + ", frt_allow=" + frt_allow
				+ ", ord_spl_cod=" + ord_spl_cod + ", template_flg=" + template_flg + ", template_ref=" + template_ref
				+ ", crncy_code=" + crncy_code + ", bto_seqnum=" + bto_seqnum + ", slot=" + slot + ", moddte=" + moddte
				+ ", mod_usr_id=" + mod_usr_id + ", exp_adr_id=" + exp_adr_id + ", exp_cust=" + exp_cust
				+ ", imp_adr_id=" + imp_adr_id + ", imp_cust=" + imp_cust + ", rtetransflg=" + rtetransflg
				+ ", relpartiescod=" + relpartiescod + ", contract_nam=" + contract_nam + ", duty_payment_type="
				+ duty_payment_type + ", duty_payment_acct=" + duty_payment_acct + ", ret_adr_id=" + ret_adr_id
				+ ", ret_cust=" + ret_cust + ", deptno=" + deptno + ", dest_num=" + dest_num + ", rush_flg=" + rush_flg
				+ ", cstms_dtycust=" + cstms_dtycust + ", excise_dtycust=" + excise_dtycust + ", cstms_ord_stat="
				+ cstms_ord_stat + ", cstms_stat_notes=" + cstms_stat_notes + ", cstms_ordtyp=" + cstms_ordtyp
				+ ", ordcolcstms_addl_info=" + ordcolcstms_addl_info + "]";
	}

}
