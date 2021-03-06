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
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.attunedlabs.config.util.DataSourceInstance;
import com.attunedlabs.eventframework.abstractbean.AbstractMetaModelBean;
import com.wherewerks.retry.dao.FailedMessageRetryDao;
import com.wherewerks.util.Utility;
import com.wherewerks.util.UtilityException;
import com.wherewerks.wmsorder.OrderConstants;
import com.wherewerks.wmsorder.dao.OrderProcessingDao;
import com.wherewerks.wmsorder.dao.OrderProcessingDaoException;
import com.wherewerks.wmsorder.pojo.OrderAddress;
import com.wherewerks.wmsorder.pojo.OrderDetails;
import com.wherewerks.wmsorder.pojo.OrderLine;
import com.wherewerks.wmsorder.pojo.OrderNote;
import static com.wherewerks.wmsorder.OrderConstants.*;

public class OrderProcessingBean extends AbstractMetaModelBean {

	Logger logger = LoggerFactory.getLogger(OrderProcessingBean.class);
	OrderProcessingDao orderProcessDao = new OrderProcessingDao();
	FailedMessageRetryDao failedMessageDao = new FailedMessageRetryDao();
	Utility utilityBean = new Utility();

	/**
	 * @Noorshid Method to process the Order Details from a single Order
	 * 
	 * @param exchange
	 * @throws OrderProcessingException
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws Exception
	 */
	public void addOrderDetails(Exchange exchange)
			throws OrderProcessingException, UtilityException, OrderProcessingDaoException, SQLException {
		logger.debug("inside addOrderDetails method......");
		String bodyInRequestStr = exchange.getIn().getBody(String.class);
		logger.debug("bodyInRequestStr:: " + bodyInRequestStr);
		OrderProcessingDao orderProcessingDao = new OrderProcessingDao();
		OrderDetails orderDetails = new OrderDetails();
		JSONObject jsonObjectBody = null;

			try {
				jsonObjectBody = XML.toJSONObject(bodyInRequestStr);
			logger.debug("jsonObjectBody::: " + jsonObjectBody);
			String clientId = jsonObjectBody.getJSONObject(EVENT_PARAM).getString(CLIENT_ID);
			String ordnum = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(ORDNUM);
			String btcust = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(BT_CUSTOMER);
			String stcust = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(ST_CUSTOMER);
			String rtcust = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(RT_CUSTOMER);
			String ordtyp = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(ORDER_TYPE);

			long dateinLong = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getLong(ENTDTE);

			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String entdte = df.format(new Date(dateinLong));
			String cpotyp = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(CPO_TYPE);
			String cponum = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(CPO_NUMBER);
			int wave_flg = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG).getInt(WAVE_FLAG);
			String host_appt_num = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(UC_HOSTORDER);
			String deptno = jsonObjectBody.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getString(DEPARTMENT_NUMBER);
			orderDetails.setClientId(clientId);
			orderDetails.setOrdnum(ordnum);
			orderDetails.setBtcust(btcust);
			orderDetails.setStcust(stcust);
			orderDetails.setRtcust(rtcust);
			orderDetails.setOrdtyp(ordtyp);
			orderDetails.setEntdte(entdte);
			orderDetails.setCpotyp(cpotyp);
			orderDetails.setCponum(cponum);
			orderDetails.setWave_flg(wave_flg);
			orderDetails.setHost_appt_num(host_appt_num);
			orderDetails.setDeptno(deptno);
			orderDetails.setWh_id("");
			logger.debug("OrderDetails Object created successfully:: "+orderDetails);
		
			orderProcessingDao.addOrderDetails(orderDetails);
			logger.debug("Insertion done for order detail: "+orderDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

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
		// Connection connection = utilityBean.getDBConnection(exchange);

		// String originalMessage = (String)
		// exchange.getIn().getHeader(OrderConstants.MESSAGE_BODY);
		int response = 0;
		OrderLine orderLine = new OrderLine();
		String body = exchange.getIn().getBody(String.class);
		logger.debug("body in addOrderLine : " + body);
		JSONObject jsonObjectBody = null;
		JSONObject jsonBody = null;
		JSONObject orderSeg = null;
		JSONArray orderLineSeg;
		JSONObject singleOrderLine = new JSONObject();
		try {
			jsonBody = XML.toJSONObject(body);
			logger.debug("jsonBody : " + jsonBody);
			/*jsonObjectBody = jsonBody.getJSONObject(EVENT_PARAM);
			logger.debug("jsonObjectBody : " + jsonObjectBody);*/
			//orderSeg = jsonObjectBody.getJSONObject(OrderConstants.ORDER_SEG);
					orderLine.setSEGNAM(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.SEGNAM));
					orderLine.setORDNUM(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ORDNUM));
					orderLine.setORDLIN(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ORDLIN));
					orderLine.setORDSLN(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ORDSLN));
					orderLine.setINVSTS_PRG(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.INVSTS_PRG));
					orderLine.setSALES_ORDNUM(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.SALES_ORDNUM));
					orderLine.setENTDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ENTDTE));
					orderLine.setPRTNUM(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.PRTNUM));
					orderLine.setCSTPRT(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.CSTPRT));
					orderLine.setEARLY_SHPDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.EARLY_SHPDTE));
					orderLine.setEARLY_DLVDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.EARLY_DLVDTE));
					orderLine.setLATE_SHPDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.LATE_SHPDTE));
					orderLine.setLATE_DLVDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.LATE_DLVDTE));
					orderLine.setACCNUM(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ACCNUM));
					orderLine.setPCKQTY(jsonBody.getJSONObject(ORDER_LINE_SEG).getInt(OrderConstants.PCKQTY));
					orderLine.setHOST_ORDQTY(jsonBody.getJSONObject(ORDER_LINE_SEG).getInt(OrderConstants.HOST_ORDQTY));
					orderLine.setUNT_PRICE(jsonBody.getJSONObject(ORDER_LINE_SEG).getInt(OrderConstants.UNT_PRICE));
					// orderLine.setUC_IDCEDIUSERFIELD1(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD1));
					// orderLine.setUC_IDCEDIUSERFIELD2(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD2));
					// orderLine.setUC_IDCEDIUSERFIELD3(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD3));
					// orderLine.setUC_IDCEDIUSERFIELD4(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD4));
					// orderLine.setUC_IDCEDIUSERFIELD5(singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCEDIUSERFIELD5));
					orderLine.setORDQTY(jsonBody.getJSONObject(ORDER_LINE_SEG).getInt(ORDQTY));
					logger.debug("singleOrderLine.getString(OrderConstants.ORDQTY) : "
							+ jsonBody.getJSONObject(ORDER_LINE_SEG).getInt(ORDQTY));
					// orderLine.setUC_USERDEF5(singleOrderLine.getString(OrderConstants.UC_USERDEF5));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF5)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_USERDEF5));
					// orderLine.setUC_USERDEF6(singleOrderLine.getString(OrderConstants.UC_USERDEF6));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF6)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_USERDEF6));
					// orderLine.setUC_USERDEF1(singleOrderLine.getString(OrderConstants.UC_USERDEF1));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF1)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_USERDEF1));
					// orderLine.setUC_USERDEF2(singleOrderLine.getString(OrderConstants.UC_USERDEF2));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_USERDEF2)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_USERDEF2));
					// orderLine.setUC_IDCLABELINFOSUPP(singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP));
					// logger.debug("singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP)
					// : " +
					// singleOrderLine.getString(OrderConstants.UC_IDCLABELINFOSUPP));
					// orderLine.setINVSTS(singleOrderLine.getString(OrderConstants.INVSTS));
					// logger.debug("singleOrderLine.getString(OrderConstants.INVSTS)
					// : " + singleOrderLine.getString(OrderConstants.INVSTS));
					orderLine.setENTDTE(jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ENTDTE));
					logger.debug("jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ENTDTE) : "
							+ jsonBody.getJSONObject(ORDER_LINE_SEG).getString(OrderConstants.ENTDTE));

					orderProcessDao.addOrderLine(orderLine);
		} catch (JSONException | SQLException | IOException e) {
			throw new OrderProcessingException("Unable to get the required Order Line value form the JSON Object", e);
		}
		/*
		 * if (response == 1) {
		 * exchange.getIn().setBody("Order Lines added succsesfully"); }
		 */
		exchange.getIn().setBody(jsonBody);
	}

	/**
	 * @Noorshid Method to process the Order Notes from a single Order
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
		OrderNote orderNote = new OrderNote();
		OrderProcessingDao orderProcessingDao = new OrderProcessingDao();
		try {
			JSONObject requestJsonObj =XML.toJSONObject(requestBody);
			logger.debug("requestJsonObj:: " + requestJsonObj);

			/*JSONArray Order_Note_Segment = requestJsonObj.getJSONObject(EVENT_PARAM).getJSONObject(ORDER_SEG)
					.getJSONArray(ORDER_NOTE_SEG);
			logger.debug("Order_Note_Segment:: " + Order_Note_Segment);

			String client_id = requestJsonObj.getJSONObject(EVENT_PARAM).getString(CLIENT_ID);
			logger.debug("clientId:: " + client_id);*/
			String wh_id = "NA";
			/*double u_version = requestJsonObj.getJSONObject(EVENT_PARAM).getDouble(TRNVER);
			logger.debug("u_version:: " + u_version);*/
			Date ins_dt = null;
			Date last_upd_dt = null;
			String ins_user_id = "NA";
			String last_upd_user_id = "NA";

			String notlin = null;
			String nottyp = null;
			String nottxt = null;
			String ordnum = null;
			notlin = requestJsonObj.getJSONObject(ORDER_NOTE_SEG).getString(NOTLIN);
				/*nottyp = Order_Note_Segment.getJSONObject(i).getString(NOTTYP);*/
			logger.debug("notlin:: "+notlin);
			nottyp =  requestJsonObj.getJSONObject(ORDER_NOTE_SEG).getString(NOTTYP);
				/*nottxt = Order_Note_Segment.getJSONObject(i).getString(NOTTXT);*/
			logger.debug("nottyp:: "+nottyp);
			nottxt = requestJsonObj.getJSONObject(ORDER_NOTE_SEG).getString(NOTTXT);
				/*ordnum = Order_Note_Segment.getJSONObject(i).getString(ORDNUM);*/
			logger.debug("nottyp:: "+nottyp);
			ordnum = requestJsonObj.getJSONObject(ORDER_NOTE_SEG).getString(ORDNUM);
			logger.debug("ordnum:: "+ordnum);
			orderNote.setNotlin(notlin);
			orderNote.setNottyp(nottyp);
			orderNote.setNottxt(nottxt);
			orderNote.setOrdnum(ordnum);
			orderNote.setClient_id("");
			orderNote.setWh_id("");
				orderProcessingDao.addOrderNote(orderNote);
				logger.debug("Insertion done for:: " + orderNote);

		} catch (JSONException e) {
			throw new OrderProcessingException("Unable to get the required Order Line value form the JSON Object", e);
		}
	}


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
		// Connection connection = utilityBean.getDBConnection(exchange);
		// String originalMessage = (String)
		// exchange.getIn().getHeader("messageBody");
		int response = 0;
		OrderAddress orderAdd = new OrderAddress();
		String body = exchange.getIn().getBody(String.class);
		logger.debug("Data: " + body);
		JSONObject jsonObjectBody = null;
		JSONObject jsonBody = null;
		try {
			jsonBody = XML.toJSONObject(body);
			logger.debug("jsonBody : " + jsonBody);
			jsonObjectBody = jsonBody.getJSONObject(EVENT_PARAM);
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

		orderProcessDao.addAddressMaster(orderAdd);
		} catch (JSONException | SQLException | IOException e) {
			throw new OrderProcessingException("Unable to get the required Order Address value form the JSON Object",
					e);
		}
		/*
		 * if (response == 1) {
		 * exchange.getIn().setBody("Order Master Address added succsesfully");
		 * }
		 */
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
		 * public void setConnection(Exchange exchange) throws UtilityException,
		 * FailedMessageRetryDaoException { Connection connection =
		 * utilityBean.getDBConnection(exchange);
		 * logger.debug("connection object in order details: " + connection);
		 * exchange.getIn().setHeader("connectionObj", connection); }
		 */

	/**
	 * Method to get the Connection and set it on exchange header
	 * 
	 * @param exchange
	 * @throws Exception
	 *//*
		 * @Override protected void processBean(Exchange exchange) throws
		 * Exception { CamelContext camelCtx = exchange.getContext(); DataSource
		 * datasource = (DataSource)
		 * camelCtx.getRegistry().lookupByName("dataSourceA");
		 * logger.debug("datasource object : " + datasource);
		 * setDataSource(datasource); Connection con = getConnection(datasource,
		 * exchange); logger.debug("connection object : " + con);
		 * exchange.getIn().setHeader("connectionObj", con);
		 * 
		 * }
		 */
}
