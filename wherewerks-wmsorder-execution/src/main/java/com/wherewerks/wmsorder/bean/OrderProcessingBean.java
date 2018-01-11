package com.wherewerks.wmsorder.bean;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.metamodel.DataContextFactory;
import org.apache.metamodel.insert.InsertInto;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.attunedlabs.config.util.DataSourceInstance;
import com.attunedlabs.eventframework.abstractbean.AbstractMetaModelBean;
import com.wherewerks.util.Utility;
import com.wherewerks.util.UtilityException;
import com.wherewerks.wmsorder.OrderConstants;
import com.wherewerks.wmsorder.dao.OrderProcessingDao;
import com.wherewerks.wmsorder.dao.OrderProcessingDaoException;
import com.wherewerks.wmsorder.pojo.OrderAddress;
import com.wherewerks.wmsorder.pojo.OrderDetails;
import com.wherewerks.wmsorder.pojo.OrderLine;
import com.wherewerks.wmsorder.pojo.OrderNote;

public class OrderProcessingBean extends AbstractMetaModelBean {

	Logger logger = LoggerFactory.getLogger(OrderProcessingBean.class);
	OrderProcessingDao orderProcessDao = new OrderProcessingDao();
	Utility utilityBean = new Utility();

	
	/**
	 *  @Noorshid
	  * Method to process the Order Details from a single Order
	  * 
	  * @param exchange
	  * @throws OrderProcessingException
	  * @throws UtilityException
	  * @throws OrderProcessingDaoException
	  * @throws Exception
	  */
	 public void addOrderDetails(Exchange exchange)
	   throws OrderProcessingException, UtilityException, OrderProcessingDaoException, SQLException {
	  logger.debug("inside addOrderDetails methodl......");
	  String bodyInRequestStr = exchange.getIn().getBody(String.class);
	  logger.debug("bodyInRequestStr:: " + bodyInRequestStr);

	  OrderDetails orderDetails = null;
	  int id = 0;
	  int response = 0;
	  JSONObject jsonObjectBody = null;

	  try {
	   jsonObjectBody = new JSONObject(bodyInRequestStr);
	   logger.debug("jsonObjectBody::: " + jsonObjectBody);
	   String clientId = jsonObjectBody.getJSONObject("eventParam").getString("ClientId");
	   String ordnum = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("OrderNumber");
	   String wh_id = "NA";
	   String btcust = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("BTCustomer");
	   String stcust = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("STCustomer");
	   String rtcust = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("RTCustomer");
	   String brcust = "NA";
	   String bt_adr_id = "NA";
	   String st_adr_id = "NA";
	   String rt_adr_id = "NA";
	   String br_adr_id = "NA";
	   String ordtyp = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("OrderType");
	   
	   long dateinLong=jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getLong("EntryDate");
	   
	   DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	   String entdte = df.format(new Date(dateinLong));
	   Date adddte = null;
	   String cpotyp = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("CPOType");
	   String cponum = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("CPONumber");
	   Date cpodte = null;
	   String srv_type = "NA";
	   String paytrm = "NA";
	   int carflg =0;
	   String shipby = "NA";
	   int rrlflg =0;
	   int wave_flg = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getInt("Wave_Flag");
	   int requir_tms_flg = 0;
	   int rmanum = 0;
	   int super_ord_flg =0;
	   String super_ordnum = "NA";
	   String ord_frtrte = "NA";
	   int bco_flg = 0;
	   int sig_req_flg = 0;
	   int bill_freight_flg = 0;
	   int customs_clr_flg = 0;
	   int cod_ind_type = 0;
	   String cod_payment_type = "NA";
	   String payment_type = "NA";
	   String sfcust = "NA";
	   String sf_adr_id = "NA";
	   String csr_nam = "NA";
	   String csr_phnnum = "NA";
	   String csr_email_adr = "NA";
	   String bus_grp = "NA";
	   String host_appt_num = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("UC_HostOrder");
	   String dlv_contact = "NA";
	   String ord_dir = "NA";
	   float frt_allow = 0.0f;
	   String ord_spl_cod = "NA";
	   int template_flg = 0;
	   String template_ref = "NA";
	   String crncy_code = "NA";
	   String bto_seqnum = "NA";
	   String slot = "NA";
	   Date moddte = null;
	   String mod_usr_id = "NA";
	   String exp_adr_id = "NA";
	   String exp_cust = "NA";
	   String imp_adr_id = "NA";
	   String imp_cust = "NA";
	   int rtetransflg = 0;
	   String relpartiescod = "NA";
	   String contract_nam = "NA";
	   String duty_payment_type = "NA";
	   String duty_payment_acct = "NA";
	   String ret_adr_id = "NA";
	   String ret_cust = "NA";
	   String deptno = jsonObjectBody.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getString("DepartmentNumber");
	   ;
	   String dest_num = "NA";
	   int rush_flg = 0;
	   String cstms_dtycust = "NA";
	   String excise_dtycust = "NA";
	   String cstms_ord_stat = "NA";
	   String cstms_stat_notes = "NA";
	   String cstms_ordtyp = "NA";
	   String ordcolcstms_addl_info = "NA";
	   logger.debug("All Value set!!");
	        
	   orderDetails=new OrderDetails(clientId, ordnum, wh_id, btcust, stcust, rtcust, brcust, bt_adr_id, st_adr_id, rt_adr_id, br_adr_id, cstms_ordtyp, entdte, adddte, cpotyp, cponum, cpodte, srv_type, paytrm, carflg, shipby, rrlflg, wave_flg, requir_tms_flg, rmanum, super_ord_flg, super_ordnum, ord_frtrte, bco_flg, sig_req_flg, bill_freight_flg, customs_clr_flg, cod_ind_type, cod_payment_type, payment_type, sfcust, sf_adr_id, csr_nam, csr_phnnum, csr_email_adr, bus_grp, host_appt_num, dlv_contact, ord_dir, frt_allow, ord_spl_cod, template_flg, template_ref, crncy_code, bto_seqnum, slot, moddte, mod_usr_id, exp_adr_id, exp_cust, imp_adr_id, imp_cust, rtetransflg, relpartiescod, contract_nam, duty_payment_type, duty_payment_acct, ret_adr_id, ret_cust, deptno, dest_num, rush_flg, cstms_dtycust, excise_dtycust, cstms_ord_stat, cstms_stat_notes, cstms_ordtyp, ordcolcstms_addl_info);
	   
	   logger.debug("orderDetails Object created successfully----> "+orderDetails);
	   Connection connection = null;
	   connection = DataSourceInstance.getConnection();
//	   UpdateableDataContext dataContext = (UpdateableDataContext) getDataContext();
	   JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
	   Table table = dataContext.getTableByQualifiedLabel("ord");
	   logger.debug("table created successfully:: " + table.getName());

	   dataContext.executeUpdate(new InsertInto(table).value("client_id", orderDetails.getClientId())
	     .value("ordnum", orderDetails.getOrdnum()).value("wh_id", orderDetails.getWh_id())
	     .value("btcust", orderDetails.getBtcust()).value("stcust", orderDetails.getStcust())
	     .value("rtcust", orderDetails.getRtcust()).value("brcust", orderDetails.getBrcust())
	     .value("bt_adr_id", orderDetails.getBt_adr_id()).value("st_adr_id", orderDetails.getSt_adr_id())
	     .value("rt_adr_id", orderDetails.getRt_adr_id()).value("br_adr_id", orderDetails.getBr_adr_id())
	     .value("ordtyp", orderDetails.getOrdtyp()).value("entdte", orderDetails.getEntdte())
	     .value("adddte", orderDetails.getAdddte()).value("cpotyp", orderDetails.getCpotyp())
	     .value("cponum", orderDetails.getCponum()).value("cpodte", orderDetails.getCpodte())
	     .value("srv_type", orderDetails.getSrv_type()).value("paytrm", orderDetails.getPaytrm())
	     .value("carflg", orderDetails.getCarflg()).value("shipby", orderDetails.getShipby())
	     .value("rrlflg", orderDetails.getRrlflg()).value("wave_flg", orderDetails.getWave_flg())
	     .value("requir_tms_flg", orderDetails.getRequir_tms_flg()).value("rmanum", orderDetails.getRmanum())
	     .value("super_ord_flg", orderDetails.getSuper_ord_flg())
	     .value("super_ordnum", orderDetails.getSuper_ordnum())
	     .value("ord_frtrte", orderDetails.getOrd_frtrte()).value("bco_flg", orderDetails.getBco_flg())
	     .value("sig_req_flg", orderDetails.getSig_req_flg())
	     .value("bill_freight_flg", orderDetails.getBill_freight_flg())
	   
	     .value("customs_clr_flg", orderDetails.getCustoms_clr_flg())
	     .value("cod_ind_type", orderDetails.getCod_ind_type())
	     .value("cod_payment_type", orderDetails.getCod_payment_type())
	     .value("payment_type",
	     orderDetails.getPayment_type()).value("sfcust",
	     orderDetails.getSfcust()) .value("sf_adr_id",
	     orderDetails.getSf_adr_id()).value("csr_nam",
	     orderDetails.getCsr_nam()) .value("csr_phnnum",
	     orderDetails.getCsr_phnnum()) .value("csr_email_adr",
	     orderDetails.getCsr_email_adr()).value("bus_grp",
	     orderDetails.getBus_grp()) .value("host_appt_num",
	     orderDetails.getHost_appt_num()) .value("dlv_contact",
	     orderDetails.getDlv_contact()).value("ord_dir",
	     orderDetails.getOrd_dir()) .value("frt_allow",
	     orderDetails.getFrt_allow()).value("ord_spl_cod",
	     orderDetails.getOrd_spl_cod()) .value("template_flg",
	     orderDetails.getTemplate_flg()) .value("template_ref",
	     orderDetails.getTemplate_ref()) .value("crncy_code",
	     orderDetails.getCrncy_code()).value("bto_seqnum",
	     orderDetails.getBto_seqnum()) .value("slot",
	     orderDetails.getSlot()).value("moddte", orderDetails.getModdte())
	     .value("mod_usr_id",
	     orderDetails.getMod_usr_id()).value("exp_adr_id",
	     orderDetails.getExp_adr_id()) .value("exp_cust",
	     orderDetails.getExp_cust()).value("imp_adr_id",
	     orderDetails.getImp_adr_id()) .value("imp_cust",
	     orderDetails.getImp_cust()).value("rtetransflg",
	     orderDetails.getRtetransflg()) .value("relpartiescod",
	     orderDetails.getRelpartiescod()) .value("contract_nam",
	     orderDetails.getContract_nam()) .value("duty_payment_type",
	     orderDetails.getDuty_payment_type()) .value("duty_payment_acct",
	     orderDetails.getDuty_payment_acct()) .value("ret_adr_id",
	     orderDetails.getRet_adr_id()).value("ret_cust",
	     orderDetails.getRet_cust()) .value("deptno",
	     orderDetails.getDeptno()).value("dest_num",
	     orderDetails.getDest_num()) .value("rush_flg",
	     orderDetails.getRush_flg()) .value("cstms_dtycust",
	     orderDetails.getCstms_dtycust()) .value("excise_dtycust",
	     orderDetails.getExp_cust()) .value("cstms_ord_stat",
	     orderDetails.getCstms_ord_stat()) .value("cstms_stat_notes",
	     orderDetails.getCstms_stat_notes()) .value("cstms_ordtyp",
	     orderDetails.getCstms_ordtyp()) .value("ordcolcstms_addl_info",
	     orderDetails.getOrdcolcstms_addl_info())
	    );
	   logger.info("Order Details Inserted Successfully....");

	  } catch (Exception e) {
		  e.printStackTrace();
	   throw new OrderProcessingException("There is an exception", e);
	  }
	 }
	
	
	
	/**
	 * Method to process the Order Details from a single Order
	 * 
	 * @param exchange
	 * @throws OrderProcessingException
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws Exception
	 */
	/*public void addOrderDetails(Exchange exchange)
			throws OrderProcessingException, UtilityException, OrderProcessingDaoException {
//		Connection connection = utilityBean.getDBConnection(exchange);
		String originalMessage = (String) exchange.getIn().getHeader(OrderConstants.MESSAGE_BODY);
		int response = 0;
		OrderDetails orderDetails = new OrderDetails();
		String body = exchange.getIn().getBody(String.class);
		logger.debug("Data in addOrderDetails: " + body);
		JSONObject jsonBody = null;
		JSONObject orderInbIfd = null;
		JSONObject ctrlSeg = null;
		JSONObject orderSeg = null;
		JSONObject jsonObjectBody = null;
		try {
			jsonBody = new JSONObject(body);
			logger.debug("jsonBody : " + jsonBody);
			jsonObjectBody = jsonBody.getJSONObject("eventParam");
			logger.debug("jsonObjectBody : " + jsonObjectBody);
			orderSeg = jsonObjectBody.getJSONObject(OrderConstants.ORDER_SEG);
			logger.debug("orderSeg : " + orderSeg);
			logger.debug("ORDNUM : "+orderSeg.getString(OrderConstants.ORDNUM)+"\n"+"TRNVER : "+jsonObjectBody.getString(OrderConstants.TRNVER)+"\n"+
					"WHSE_ID : "+jsonObjectBody.getString(OrderConstants.WHSE_ID)+"\n"+"CLIENT_ID : "+jsonObjectBody.getString(OrderConstants.CLIENT_ID)
					+"\n"+"TRNNAM : "+jsonObjectBody.getString(OrderConstants.TRNNAM));
		} catch (JSONException e) {
			throw new OrderProcessingException("Unable to get the required order details from exchange body", e);
		}
		try {
				orderDetails.setTRNNAM(jsonObjectBody.getString(OrderConstants.TRNNAM));
				orderDetails.setTRNVER(jsonObjectBody.getString(OrderConstants.TRNVER));
				orderDetails.setWHSE_ID(jsonObjectBody.getString(OrderConstants.WHSE_ID));
				orderDetails.setCLIENT_ID(jsonObjectBody.getString(OrderConstants.CLIENT_ID));
				orderDetails.setORDNUM(orderSeg.getString(OrderConstants.ORDNUM));
				logger.debug("orderDetails : "+orderDetails.toString());
				response = orderProcessDao.addOrderDetails(orderDetails);
		} catch (JSONException e) {
			throw new OrderProcessingException(
					"Unable to get the required Order Details value form ORDER_SEG JSON Object", e);
		}
		exchange.getIn().setBody(originalMessage);
	}*/

	/**
	 * Method to process the Order Lines from a single Order
	 * 
	 * @param exchange
	 * @throws UtilityException
	 * @throws OrderProcessingException
	 * @throws OrderProcessingDaoException
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void addOrderLine(Exchange exchange)
			throws UtilityException, OrderProcessingException, OrderProcessingDaoException {
//		Connection connection = utilityBean.getDBConnection(exchange);
		
//		String originalMessage = (String) exchange.getIn().getHeader(OrderConstants.MESSAGE_BODY);
		int response = 0;
		OrderLine orderLine = new OrderLine();
		String body = exchange.getIn().getBody(String.class);
		logger.debug("body in addOrderLine : " + body);
		JSONObject jsonObjectBody = null;
		JSONObject jsonBody = null;
		JSONObject orderInbIfd = null;
		JSONObject ctrlSeg = null;
		JSONObject orderSeg = null;
		JSONArray orderLineSeg;
		JSONObject singleOrderLine = new JSONObject();
		try {
			jsonBody = new JSONObject(body);
			logger.debug("jsonBody : " + jsonBody);
			jsonObjectBody = jsonBody.getJSONObject("eventParam");
			logger.debug("jsonObjectBody : " + jsonObjectBody);
			orderSeg = jsonObjectBody.getJSONObject(OrderConstants.ORDER_SEG);
			logger.debug("orderSeg : " + orderSeg);
			if (orderSeg.has(OrderConstants.ORDER_LINE_SEG)) {
				orderLineSeg = orderSeg.getJSONArray(OrderConstants.ORDER_LINE_SEG);
				logger.debug("orderLineSeg : " + orderLineSeg);
				for (int i = 0; i < orderLineSeg.length(); i++) {
					singleOrderLine = orderLineSeg.getJSONObject(i);
					logger.debug("singleOrderLine : " + singleOrderLine);
					orderLine.setSEGNAM(singleOrderLine.getString(OrderConstants.SEGNAM));
					orderLine.setORDNUM(singleOrderLine.getString(OrderConstants.ORDNUM));
					orderLine.setORDLIN(singleOrderLine.getString(OrderConstants.ORDLIN));
					orderLine.setORDSLN(singleOrderLine.getString(OrderConstants.ORDSLN));
					orderLine.setINVSTS_PRG(singleOrderLine.getString(OrderConstants.INVSTS_PRG));
					orderLine.setSALES_ORDNUM(singleOrderLine.getString(OrderConstants.SALES_ORDNUM));
					orderLine.setENTDTE(singleOrderLine.getString(OrderConstants.ENTDTE));
					orderLine.setPRTNUM(singleOrderLine.getString(OrderConstants.PRTNUM));
					orderLine.setCSTPRT(singleOrderLine.getString(OrderConstants.CSTPRT));
					orderLine.setEARLY_SHPDTE(singleOrderLine.getString(OrderConstants.EARLY_SHPDTE));
					orderLine.setEARLY_DLVDTE(singleOrderLine.getString(OrderConstants.EARLY_DLVDTE));
					orderLine.setLATE_SHPDTE(singleOrderLine.getString(OrderConstants.LATE_SHPDTE));
					orderLine.setLATE_DLVDTE(singleOrderLine.getString(OrderConstants.LATE_DLVDTE));
					orderLine.setACCNUM(singleOrderLine.getString(OrderConstants.ACCNUM));
					orderLine.setPCKQTY(singleOrderLine.getInt(OrderConstants.PCKQTY));
					orderLine.setHOST_ORDQTY(singleOrderLine.getInt(OrderConstants.HOST_ORDQTY));
					orderLine.setUNT_PRICE(singleOrderLine.getInt(OrderConstants.UNT_PRICE));
//					orderLine.setUC_IDCEDIUSERFIELD1(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1) : " + singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1));
//					orderLine.setUC_IDCEDIUSERFIELD2(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2) : " + singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2));
//					orderLine.setUC_IDCEDIUSERFIELD3(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3) : " + singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3));
//					orderLine.setUC_IDCEDIUSERFIELD4(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4) : " + singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4));
//					orderLine.setUC_IDCEDIUSERFIELD5(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5) : " + singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5));
					orderLine.setORDQTY(singleOrderLine.getInt(OrderConstants.ORDQTY));
					logger.debug("singleOrderLine.getString(OrderConstants.ORDQTY) : " + singleOrderLine.getString(OrderConstants.ORDQTY));
//					orderLine.setUC_USERDEF5(singleOrderLine.getString(OrderConstants.UC_USERDEF5));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF5) : " + singleOrderLine.getString(OrderConstants.UC_USERDEF5));
//					orderLine.setUC_USERDEF6(singleOrderLine.getString(OrderConstants.UC_USERDEF6));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF6) : " + singleOrderLine.getString(OrderConstants.UC_USERDEF6));
//					orderLine.setUC_USERDEF1(singleOrderLine.getString(OrderConstants.UC_USERDEF1));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF1) : " + singleOrderLine.getString(OrderConstants.UC_USERDEF1));
//					orderLine.setUC_USERDEF2(singleOrderLine.getString(OrderConstants.UC_USERDEF2));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF2) : " + singleOrderLine.getString(OrderConstants.UC_USERDEF2));
//					orderLine.setUC_IDCLABELINFOSUPP(singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP));
//					logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP) : " + singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP));
//					orderLine.setINVSTS(singleOrderLine.getString(OrderConstants.INVSTS));
//					logger.debug("singleOrderLine.getString(OrderConstants.INVSTS) : " + singleOrderLine.getString(OrderConstants.INVSTS));
					orderLine.setENTDTE(singleOrderLine.getString(OrderConstants.ENTDTE));
					logger.debug("singleOrderLine.getString(OrderConstants.ENTDTE) : " + singleOrderLine.getString(OrderConstants.ENTDTE));
					
					orderProcessDao.addOrderLine(orderLine);
				}
			}
		} catch (JSONException | SQLException | IOException e) {
			throw new OrderProcessingException("Unable to get the required Order Line value form the JSON Object", e);
		}
		/*if (response == 1) {
			exchange.getIn().setBody("Order Lines added succsesfully");
		}*/
		exchange.getIn().setBody(jsonBody);
	}
	
	
	/**
	 * @Noorshid
	  * Method to process the Order Notes from a single Order
	  * 
	  * @param exchange
	  * @throws OrderProcessingException
	  * @throws UtilityException
	  * @throws OrderProcessingDaoException
	 * @throws IOException 
	 * @throws SQLException 
	  */
	 public void addOrderNote(Exchange exchange)
	   throws OrderProcessingException, UtilityException, OrderProcessingDaoException, SQLException, IOException {
	  logger.debug("inside last_upd_user_id method.....");
	  String requestBody = exchange.getIn().getBody(String.class);
	  logger.debug("requestBody:: " + requestBody);
	  OrderNote orderNote = null;
//	  UpdateableDataContext dataContext = (UpdateableDataContext) getDataContext();
	  Connection connection = null;
	  connection = DataSourceInstance.getConnection();
		logger.debug("connection object in order details: " + connection);
		JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
		dataContext.setIsInTransaction(false);
	  try {
	   JSONObject requestJsonObj = new JSONObject(requestBody);
	   logger.debug("requestJsonObj:: " + requestJsonObj);

	   JSONArray Order_Note_Segment = requestJsonObj.getJSONObject("eventParam").getJSONObject("OrderSegment")
	     .getJSONArray("Order_Note_Segment");
	   logger.debug("Order_Note_Segment:: " + Order_Note_Segment);

	   String client_id = requestJsonObj.getJSONObject("eventParam").getString("ClientId");
	   logger.debug("clientId:: " + client_id);
	   String wh_id = "NA";
	   int edtflg =0;
	   double u_version = requestJsonObj.getJSONObject("eventParam").getDouble("TransactionVersion");
	   logger.debug("u_version:: " + u_version);
	   Date ins_dt = null;
	   Date last_upd_dt = null;
	   String ins_user_id = "NA";
	   String last_upd_user_id = "NA";

	   String notlin = null;
	   String nottyp = null;
	   String nottxt = null;
	   String ordnum = null;

	   Table table = dataContext.getTableByQualifiedLabel("ord_note");
	   logger.debug("Table in datacontext:: " + table.getName());

	   for (int i = 0; i < Order_Note_Segment.length(); i++) {
	    notlin = Order_Note_Segment.getJSONObject(i).getString("NoteLine");
	    nottyp = Order_Note_Segment.getJSONObject(i).getString("NoteType");
	    nottxt = Order_Note_Segment.getJSONObject(i).getString("NoteText");
	    ordnum = Order_Note_Segment.getJSONObject(i).getString("OrderNumber");
	    orderNote = new OrderNote(client_id, ordnum, nottyp, notlin, wh_id, nottxt, edtflg, u_version, ins_dt,
	      last_upd_dt, ins_user_id, last_upd_user_id);
	    logger.debug("orderNote---->:: " + orderNote);
	    dataContext.executeUpdate(new InsertInto(table).value("client_id", orderNote.getClient_id())
	      .value("ordnum", orderNote.getOrdnum()).value("nottyp", orderNote.getNottyp())
	      .value("notlin", orderNote.getNotlin()).value("wh_id", orderNote.getWh_id())
	      .value("nottxt", orderNote.getNottxt()).value("edtflg", orderNote.getEdtflg())
	      .value("u_version", orderNote.getU_version()).value("ins_dt", orderNote.getIns_dt())
	      .value("last_upd_dt", orderNote.getLast_upd_dt())
	      .value("ins_user_id", orderNote.getIns_user_id())
	      .value("last_upd_user_id", orderNote.getLast_upd_user_id()));
	    logger.debug("Insertion done for:: " + orderNote);
	   }

	  } catch (JSONException e) {
	   throw new OrderProcessingException("Unable to get the required Order note value form the JSON Object", e);
	  }
	 }
	

	/**
	 * Method to process the Order Notes from a single Order
	 * 
	 * @param exchange
	 * @throws OrderProcessingException
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 *//*
	public void addOrderNote(Exchange exchange)
			throws OrderProcessingException, UtilityException, OrderProcessingDaoException {
//		Connection connection = utilityBean.getDBConnection(exchange);
		String originalMessage = (String) exchange.getIn().getHeader("messageBody");
		OrderNote orderNote = new OrderNote();
		int response = 0;
		String body = exchange.getIn().getBody(String.class);
		logger.debug("body in addOrderNote : " + body);
		JSONObject jsonObjectBody = null;
		JSONObject jsonBody = null;
		JSONObject orderInbIfd = null;
		JSONObject ctrlSeg = null;
		JSONObject orderSeg = null;
		JSONArray orderNoteSeg;
		JSONObject singleNoteSeg = new JSONObject();
		try {
//			jsonObjectBody = XML.toJSONObject(body);
			jsonBody = new JSONObject(body);
			logger.debug("jsonObjectBody : " + jsonObjectBody);
			jsonObjectBody = jsonBody.getJSONObject("eventParam");
			if (jsonObjectBody.has(OrderConstants.ORDER_INB_IFD)) {
				orderInbIfd = jsonObjectBody.getJSONObject(OrderConstants.ORDER_INB_IFD);
				logger.debug("orderInbIfd : " + orderInbIfd);
			}
			if (orderInbIfd.has(OrderConstants.CTRL_SEG)) {
				ctrlSeg = orderInbIfd.getJSONObject(OrderConstants.CTRL_SEG);
				logger.debug("ctrlSeg : " + ctrlSeg);
			}
			if (ctrlSeg.has(OrderConstants.ORDER_SEG)) {
				orderSeg = ctrlSeg.getJSONObject(OrderConstants.ORDER_SEG);
				logger.debug("orderSeg : " + orderSeg);
			}else{
				orderSeg = jsonObjectBody.getJSONObject(OrderConstants.ORDER_SEG);
				logger.debug("orderSeg : " + orderSeg);
//			}
			if (orderSeg.has(OrderConstants.ORDER_NOTE_SEG)) {
				orderNoteSeg = orderSeg.getJSONArray(OrderConstants.ORDER_NOTE_SEG);
				for (int i = 0; i < orderNoteSeg.length(); i++) {
					logger.debug("orderNoteSeg : " + orderNoteSeg);
					singleNoteSeg = orderNoteSeg.getJSONObject(i);
					orderNote.setSEGNAM(singleNoteSeg.getString(OrderConstants.SEGNAM));
					orderNote.setORDNUM(singleNoteSeg.getString(OrderConstants.ORDNUM));
					orderNote.setNOTLIN(singleNoteSeg.getString(OrderConstants.NOTLIN));
					orderNote.setNOTTYP(singleNoteSeg.getString(OrderConstants.NOTTYP));
					orderNote.setNOTTXT(singleNoteSeg.getString(OrderConstants.NOTTXT));
					response = orderProcessDao.addOrderNote(orderNote);
				}
			}
		} catch (JSONException | SQLException | IOException e) {
			throw new OrderProcessingException("Unable to get the required Order Note value form the JSON Object", e);
		}
		logger.debug("response : " + response);
		if (response == 1) {
			exchange.getIn().setBody("Order Notes added succsesfully");
		}
		exchange.getIn().setBody(originalMessage);
	}*/

	/**
	 * Method to process the Master Address Details from a single Order
	 * 
	 * @param exchange
	 * @throws UtilityException
	 * @throws OrderProcessingException
	 * @throws OrderProcessingDaoException
	 */
	public void addAddressMaster(Exchange exchange)
			throws UtilityException, OrderProcessingException, OrderProcessingDaoException {
//		Connection connection = utilityBean.getDBConnection(exchange);
//		String originalMessage = (String) exchange.getIn().getHeader("messageBody");
		int response = 0;
		OrderAddress orderAdd = new OrderAddress();
		String body = exchange.getIn().getBody(String.class);
		logger.debug("Data: " + body);
		JSONObject jsonObjectBody = null;
		JSONObject jsonBody = null;
		JSONObject orderInbIfd = null;
		JSONObject ctrlSeg;
		try {
			jsonBody = new JSONObject(body);
			logger.debug("jsonBody : "+jsonBody);
			jsonObjectBody = jsonBody.getJSONObject("eventParam");
			JSONObject orderSeg = jsonObjectBody.getJSONObject(OrderConstants.ORDER_SEG);
			logger.debug("orderSeg : " + orderSeg);
					logger.debug("orderSeg : " + orderSeg);
					JSONObject shiptoAddrSeg = jsonObjectBody.getJSONObject(OrderConstants.SHIPTO_ADDR_SEG);
					JSONObject routetoAddrSeg = jsonObjectBody.getJSONObject(OrderConstants.ROUTETO_ADDR_SEG);
					logger.debug("shiptoAddrSeg : " + shiptoAddrSeg);
					orderAdd.setORDNUM(orderSeg.getString(OrderConstants.ORDNUM));
					orderAdd.setSEGNAM(shiptoAddrSeg.getString(OrderConstants.SEGNAM));
					orderAdd.setADRNAM(shiptoAddrSeg.getString(OrderConstants.ADRNAM));
					orderAdd.setADRLN1(shiptoAddrSeg.getString(OrderConstants.ADRLN1));
					orderAdd.setADRLN2(routetoAddrSeg.getString(OrderConstants.ADRLN2));
					orderAdd.setADRCTY(shiptoAddrSeg.getString(OrderConstants.ADRCTY));
					orderAdd.setCTRY_NAME(shiptoAddrSeg.getString(OrderConstants.CTRY_NAME));
					orderAdd.setADRPSZ(shiptoAddrSeg.getString(OrderConstants.ADRPSZ));
					orderAdd.setADRSTC(shiptoAddrSeg.getString(OrderConstants.ADRSTC));
					orderAdd.setATTN_NAME(routetoAddrSeg.getString(OrderConstants.ATTN_NAME));
					orderAdd.setFAXNUM(routetoAddrSeg.getString(OrderConstants.FAXNUM));
					orderAdd.setFAXNUM(routetoAddrSeg.getString(OrderConstants.PHNNUM));
					
					/*response =*/ orderProcessDao.addAddressMaster(orderAdd);
//				}
//			}
		} catch (JSONException | SQLException | IOException e) {
			e.printStackTrace();
			throw new OrderProcessingException("Unable to get the required Order Address value form the JSON Object", e);
		}
		/*if (response == 1) {
			exchange.getIn().setBody("Order Master Address added succsesfully");
		}*/
		exchange.getIn().setBody(jsonBody);
	}

	@Override
	protected void processBean(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Method to get the Connection and set it on exchange header 
	 * 
	 * @param exchange
	 * @throws UtilityException
	 * @throws FailedMessageRetryDaoException 
	 *//*
	public void setConnection(Exchange exchange) throws UtilityException, FailedMessageRetryDaoException {
		Connection connection = utilityBean.getDBConnection(exchange);
		logger.debug("connection object in order details: " + connection);
		exchange.getIn().setHeader("connectionObj", connection);
	}*/

	/**
	 * Method to get the Connection and set it on exchange header
	 *  
	 * @param exchange
	 * @throws Exception
	 *//*
	@Override
	protected void processBean(Exchange exchange) throws Exception {
		CamelContext camelCtx = exchange.getContext();
		DataSource datasource = (DataSource) camelCtx.getRegistry().lookupByName("dataSourceA");
		logger.debug("datasource object : " + datasource);
		setDataSource(datasource);
		Connection con = getConnection(datasource, exchange);
		logger.debug("connection object : " + con);
		exchange.getIn().setHeader("connectionObj", con);
		
	}*/
}
