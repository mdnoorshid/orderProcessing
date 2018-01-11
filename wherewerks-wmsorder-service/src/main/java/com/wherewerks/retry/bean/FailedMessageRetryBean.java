package com.wherewerks.retry.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.restlet.RestletConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.restlet.Response;
import org.restlet.data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.wherewerks.retry.RetryConstants;
import com.wherewerks.retry.dao.FailedMessageRetryDao;
import com.wherewerks.retry.dao.FailedMessageRetryDaoException;
import com.wherewerks.retry.pojo.FailedRetryLogs;
import com.wherewerks.util.Utility;
import com.wherewerks.util.UtilityException;

public class FailedMessageRetryBean {

	static Logger logger = LoggerFactory.getLogger(FailedMessageRetryBean.class);
	private static Properties propsStaticConfig = new Properties();

	FailedMessageRetryDao retryDao = new FailedMessageRetryDao();
	Utility utilityBean = new Utility();
	Connection connection;
	static String serviceUrl;

	static {
		InputStream serviceProperties = FailedMessageRetryBean.class.getClassLoader()
				.getResourceAsStream(RetryConstants.SERVICE_PROP_FILE);
		try {
			propsStaticConfig.load(serviceProperties);
			serviceUrl = propsStaticConfig.getProperty(RetryConstants.SERVICE_URL);

		} catch (IOException e) {
			logger.error("Unable to load the file properties..", e);
		}
	}

	/**
	 * Method to process the Failed Orders and log it into database for future
	 * retry
	 * 
	 * @param exchange
	 * @throws UtilityException
	 * @throws FailedMessageRetryBeanException
	 * @throws FailedMessageRetryDaoException
	 */
	public void processFailedMessage(Exchange exchange)
			throws UtilityException, FailedMessageRetryBeanException, FailedMessageRetryDaoException {
		logger.debug("inside .processFailedMessage of FailedMessageRetryBean : " + exchange.getIn().getBody(String.class));
		
		deleteIfOrderExists(exchange);
		setFailedMessageDetailsWithRetryDetails(exchange);
		
	}

	private void deleteIfOrderExists(Exchange exchange) throws FailedMessageRetryBeanException, FailedMessageRetryDaoException, UtilityException {
		String policyId = (String) exchange.getIn().getHeader(RetryConstants.POLICY_ID);
		logger.debug("policyId : " + policyId);
		ResultSet failedMessages = retryDao.getFailedMessage(policyId);
		String uniqueId = null;
		try {
			while(failedMessages.next()){
				uniqueId = failedMessages.getString(RetryConstants.UNIQUEID);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String originalMsg = exchange.getIn().getBody(String.class);
		logger.debug("original message inside exchange : " + originalMsg);
		InputSource source = new InputSource(new StringReader(originalMsg));
		XPath xpath = XPathFactory.newInstance().newXPath();
		Object orders;
		String orderNumber = null;
		try {
			orders = xpath.evaluate("/ORDER_INB_IFD/CTRL_SEG/ORDER_SEG", source, XPathConstants.NODE);
			orderNumber = (String) xpath.evaluate("ORDNUM", orders);
			logger.debug("orderNumber : " + orderNumber);
		} catch (XPathExpressionException e) {
			throw new FailedMessageRetryBeanException("Unable to get Order Number from the defined XPath", e);
		}
		
		if(uniqueId.equals(orderNumber)){
			retryDao.deleteFailedMessage(orderNumber);
		}
	}

	/**
	 * Method to set the failed Order details
	 * 
	 * @param exchange
	 * @return
	 * @throws FailedMessageRetryBeanException
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	private String setFailedMessageDetailsWithRetryDetails(Exchange exchange)
			throws FailedMessageRetryBeanException, FailedMessageRetryDaoException, UtilityException {
		logger.debug("inside .setFailesMessageDetailsWithRetryDetails of FailedMessageRetryBean");
		FailedRetryLogs failedRetryLogs = new FailedRetryLogs();
		int count = 0;
		String originalMsg = exchange.getIn().getBody(String.class);
		logger.debug("original message inside exchange : " + originalMsg);
		InputSource source = new InputSource(new StringReader(originalMsg));
		XPath xpath = XPathFactory.newInstance().newXPath();
		Object orders;
		String orderNumber = null;
		try {
			orders = xpath.evaluate("/ORDER_INB_IFD/CTRL_SEG/ORDER_SEG", source, XPathConstants.NODE);
			orderNumber = (String) xpath.evaluate("ORDNUM", orders);
			logger.debug("orderNumber : " + orderNumber);
		} catch (XPathExpressionException e) {
			throw new FailedMessageRetryBeanException("Unable to get Order Number from the defined XPath", e);
		}

		failedRetryLogs.setUniqueId(orderNumber);
		String originalMessage = (String) exchange.getIn().getHeader(RetryConstants.MESSAGE_BODY);
		logger.debug("original request message  : " + originalMsg);
		failedRetryLogs.setOriginalMsg(originalMessage);

		Response response = exchange.getIn().getHeader(RestletConstants.RESTLET_RESPONSE, Response.class);
		response.setStatus(Status.SERVER_ERROR_INTERNAL);
		exchange.getIn().setHeader(RestletConstants.RESTLET_RESPONSE, response);

		Message message = exchange.getIn();
		Object statusCode = exchange.getIn().getHeader("CamelRestletResponse");
		logger.debug("statusCode : " + statusCode.toString());

		if (statusCode.toString().contains("200")) {
			failedRetryLogs.setProcessstage(RetryConstants.PROCESSED);
			logger.debug("processStage : " + failedRetryLogs.getProcessstage());
		} else {
			failedRetryLogs.setProcessstage(RetryConstants.RETRY);
			logger.debug("processStage : " + failedRetryLogs.getProcessstage());
		}

		String retryPolicyId = (String) message.getHeader(RetryConstants.POLICY_ID);
		logger.debug("retryPolicyId : " + retryPolicyId);
		failedRetryLogs.setRetryPolicyId(retryPolicyId);

		int retryPolicyIndex = count;
		logger.debug("retryPolicyIndex : " + retryPolicyIndex);
		failedRetryLogs.setRetryIndex(retryPolicyIndex);

		Exception exceptionMsg = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		logger.debug("exception message inside exchange : " + exceptionMsg);
		failedRetryLogs.setLasterrMsg(exceptionMsg.toString());

		String manualRetryFlag = "true";
		failedRetryLogs.setManualRetryFlag(manualRetryFlag);

		if (statusCode.toString().contains("200")) {
			failedRetryLogs.setRetyrStatus(RetryConstants.SUCCESS);
		} else {
			failedRetryLogs.setRetyrStatus(RetryConstants.FAILED);
		}

		Timestamp recordTime = new Timestamp(System.currentTimeMillis());
		failedRetryLogs.setRecordTime(recordTime);

		String retryStatus;
		String uniqueId = null;
		int retryIndex = 0;
		ResultSet failedMessage = retryDao.getFailedMessageDetails(orderNumber);
		try {
			if (failedMessage.next()) {
				uniqueId = failedMessage.getString(RetryConstants.UNIQUEID);
				retryIndex = failedMessage.getInt(RetryConstants.RETRY_INDEX);
			}
		} catch (SQLException e) {
			throw new FailedMessageRetryBeanException("Failed to get the UniqueId & retryIndex for the failed Order, may be null", e);
		}

		if (uniqueId != null && uniqueId.equals(orderNumber)) {
			logger.debug("inside if to update the retryIndex");
			retryIndex = retryIndex + 1;
			retryDao.updateRetryIndex(retryIndex, failedMessage);
		} else {
			logger.debug("inside else to register the failed message");
			retryStatus = retryDao.registerFailedMessage(failedRetryLogs);
			logger.debug("retryStatus : " + retryStatus);
		}

		return originalMsg;
	}

	/**
	 * Method to get the failed Orders and process
	 * 
	 * @param exchange
	 * @throws FailedMessageRetryBeanException
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public void getFailedMessage(Exchange exchange)
			throws FailedMessageRetryBeanException, FailedMessageRetryDaoException, UtilityException {
		logger.debug("inside .getFailedMessage of FailedMessageRetryBean");
		int retryInterval = 0;
		int retryCount = 0;
		int retryIndex = 0;
		String processStage;
		String orderNumber;
		String originalMessage = null;
		HttpResponse response;

		String policyId = (String) exchange.getIn().getHeader(RetryConstants.POLICY_ID);
		logger.debug("policyId : " + policyId);
		ResultSet policyDetails = retryDao.getPolicyDetails(policyId);
		if (policyDetails == null) {
			logger.error("Failed to get policy details for the PolicyId");
		}

		try {
			if (policyDetails.next()) {
				retryInterval = policyDetails.getInt(RetryConstants.RETRY_INTERVAL);
				retryCount = policyDetails.getInt(RetryConstants.RETRY_COUNT);
			}
		} catch (SQLException e) {
			throw new FailedMessageRetryBeanException("Unable to get the required values from the policy resultset", e);
		}

		
		ResultSet failedMessages = retryDao.getFailedMessage(policyId);

		ResultSetMetaData rsmd;
		try {
			rsmd = failedMessages.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			if (failedMessages.wasNull()) {
				logger.debug("No failed Orders to process");
				exchange.getOut().setHeader("dataExists", "NO");
			}

			while (failedMessages.next()) {
				logger.debug("inside while to get resultset values");
				originalMessage = failedMessages.getString(RetryConstants.ORIGINAL_MESSAGE);
				orderNumber = failedMessages.getString(RetryConstants.UNIQUEID);
				retryIndex = failedMessages.getInt(RetryConstants.RETRY_INDEX);
				processStage = failedMessages.getString(RetryConstants.PROCESS_STAGE);
				logger.debug("orderNumber : " + orderNumber + " " + "retryindex : " + retryIndex + " "
						+ "originalMessage : " + originalMessage);
				if (retryIndex == (retryCount - 1) && orderNumber.equals("22778210-02363150")) {
					logger.debug("just here to get the correct data and ckeck if the logic is working");
					String data = "<ORDER_INB_IFD><CTRL_SEG><TRNNAM>ORDER_TRAN</TRNNAM><TRNVER>2012.1</TRNVER><WHSE_ID>ID</WHSE_ID><CLIENT_ID>Key2Act</CLIENT_ID><ORDER_SEG><SEGNAM>ORDER</SEGNAM><TRNTYP>A</TRNTYP><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>1</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>#ofpallets___________________________</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>2</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>Envelopeenclosed____________________</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>3</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>(packinglistmanifest)</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>4</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>Datepickedup:_______________________</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>5</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>Timein:_______________________________</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>6</NOTLIN><NOTTYP>BOL</NOTTYP><NOTTXT>Timeout:______________________________</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>7</NOTLIN><NOTTYP>PICK</NOTTYP><NOTTXT>Webbasedrtg-mustrequestrtg72hrsb/4reqshipdate.</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>8</NOTLIN><NOTTYP>PICK</NOTTYP><NOTTXT>Auth#mustbeinCIDfieldonBOL.Multiplep.o.onpallet</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>9</NOTLIN><NOTTYP>PICK</NOTTYP><NOTTXT>mustbeslipsheeted.Palletlabelreqw/dcaddress,p.o.#'</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>10</NOTLIN><NOTTYP>PICK</NOTTYP><NOTTXT>s,and#ofcartonsperp.o.BOLmustbesignedbyIDI.</NOTTXT></ORDER_NOTE_SEG><ORDER_NOTE_SEG><SEGNAM>ORDER_NOTE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><NOTLIN>11</NOTLIN><NOTTYP>PICK</NOTTYP><NOTTXT>RtgQuestions262-703-2000</NOTTXT></ORDER_NOTE_SEG><STCUST>15341</STCUST><RTCUST>15920</RTCUST><BTCUST>3297</BTCUST><CPONUM>10477177</CPONUM><CPODAT>20160111000000</CPODAT><CPOTYP>RL</CPOTYP><WAVE_FLG>0</WAVE_FLG><ORDNUM>22778210-02363150</ORDNUM><ENTDTE>20160206062956</ENTDTE><LATE_SHPDTE>20160206062956</LATE_SHPDTE><ORDTYP>EDI</ORDTYP><UC_HSTORD>22778210</UC_HSTORD><DEPTNO>00147</DEPTNO><UC_IDCDEPTDESC>115</UC_IDCDEPTDESC><UC_STOREID>00147</UC_STOREID><UC_EDIUSERFIELD1>SDQ</UC_EDIUSERFIELD1><UC_DEPTNO>115</UC_DEPTNO><UC_RTCODE>00875</UC_RTCODE><UC_STCODE>00147</UC_STCODE><UC_USERDEF2>115</UC_USERDEF2><UC_USERDEF3>15920</UC_USERDEF3><UC_SMLFRTTRM>COL</UC_SMLFRTTRM><UC_LTLFRTTRM>COL</UC_LTLFRTTRM><UC_CONSOLIDATE>1</UC_CONSOLIDATE><ORDER_LINE_SEG><SEGNAM>ORDER_LINE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><ORDLIN>0003</ORDLIN><ORDSLN>0000</ORDSLN><INVSTS_PRG>A</INVSTS_PRG><INVSTS>A</INVSTS><SALES_ORDNUM>10477177</SALES_ORDNUM><ENTDTE>20160206063155</ENTDTE><PRTNUM>52497KH</PRTNUM><CSTPRT>1151016</CSTPRT><EARLY_SHPDTE>20160215000000</EARLY_SHPDTE><EARLY_DLVDTE>20160206000000</EARLY_DLVDTE><LATE_SHPDTE>20160220000000</LATE_SHPDTE><LATE_DLVDTE>19000101000000</LATE_DLVDTE><ACCNUM>15341</ACCNUM><PCKQTY>4</PCKQTY><ORDQTY>4</ORDQTY><HOST_ORDQTY>4</HOST_ORDQTY><UNT_PRICE>5.00000</UNT_PRICE><UC_USERDEF1></UC_USERDEF1><UC_USERDEF2></UC_USERDEF2><UC_USERDEF5>00147</UC_USERDEF5><UC_USERDEF6>00875</UC_USERDEF6><UC_IDCEDIUSERFIELD1></UC_IDCEDIUSERFIELD1><UC_IDCEDIUSERFIELD2></UC_IDCEDIUSERFIELD2><UC_IDCEDIUSERFIELD3></UC_IDCEDIUSERFIELD3><UC_IDCEDIUSERFIELD4></UC_IDCEDIUSERFIELD4><UC_IDCEDIUSERFIELD5></UC_IDCEDIUSERFIELD5><UC_IDCLABELINFOSUPP></UC_IDCLABELINFOSUPP></ORDER_LINE_SEG><ORDER_LINE_SEG><SEGNAM>ORDER_LINE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><ORDLIN>0001</ORDLIN><ORDSLN>0000</ORDSLN><INVSTS_PRG>A</INVSTS_PRG><INVSTS>A</INVSTS><SALES_ORDNUM>10477177</SALES_ORDNUM><ENTDTE>20160206063155</ENTDTE><PRTNUM>68790KH</PRTNUM><CSTPRT>1151016</CSTPRT><EARLY_SHPDTE>20160215000000</EARLY_SHPDTE><EARLY_DLVDTE>20160206000000</EARLY_DLVDTE><LATE_SHPDTE>20160220000000</LATE_SHPDTE><LATE_DLVDTE>19000101000000</LATE_DLVDTE><ACCNUM>15341</ACCNUM><PCKQTY>4</PCKQTY><ORDQTY>4</ORDQTY><HOST_ORDQTY>4</HOST_ORDQTY><UNT_PRICE>11.00000</UNT_PRICE><UC_USERDEF1></UC_USERDEF1><UC_USERDEF2></UC_USERDEF2><UC_USERDEF5>00147</UC_USERDEF5><UC_USERDEF6>00875</UC_USERDEF6><UC_IDCEDIUSERFIELD1></UC_IDCEDIUSERFIELD1><UC_IDCEDIUSERFIELD2></UC_IDCEDIUSERFIELD2><UC_IDCEDIUSERFIELD3></UC_IDCEDIUSERFIELD3><UC_IDCEDIUSERFIELD4></UC_IDCEDIUSERFIELD4><UC_IDCEDIUSERFIELD5></UC_IDCEDIUSERFIELD5><UC_IDCLABELINFOSUPP></UC_IDCLABELINFOSUPP></ORDER_LINE_SEG><ORDER_LINE_SEG><SEGNAM>ORDER_LINE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><ORDLIN>0002</ORDLIN><ORDSLN>0000</ORDSLN><INVSTS_PRG>A</INVSTS_PRG><INVSTS>A</INVSTS><SALES_ORDNUM>10477177</SALES_ORDNUM><ENTDTE>20160206063155</ENTDTE><PRTNUM>68948KH</PRTNUM><CSTPRT>1151016</CSTPRT><EARLY_SHPDTE>20160215000000</EARLY_SHPDTE><EARLY_DLVDTE>20160206000000</EARLY_DLVDTE><LATE_SHPDTE>20160220000000</LATE_SHPDTE><LATE_DLVDTE>19000101000000</LATE_DLVDTE><ACCNUM>15341</ACCNUM><PCKQTY>4</PCKQTY><ORDQTY>4</ORDQTY><HOST_ORDQTY>4</HOST_ORDQTY><UNT_PRICE>9.80000</UNT_PRICE><UC_USERDEF1></UC_USERDEF1><UC_USERDEF2></UC_USERDEF2><UC_USERDEF5>00147</UC_USERDEF5><UC_USERDEF6>00875</UC_USERDEF6><UC_IDCEDIUSERFIELD1></UC_IDCEDIUSERFIELD1><UC_IDCEDIUSERFIELD2></UC_IDCEDIUSERFIELD2><UC_IDCEDIUSERFIELD3></UC_IDCEDIUSERFIELD3><UC_IDCEDIUSERFIELD4></UC_IDCEDIUSERFIELD4><UC_IDCEDIUSERFIELD5></UC_IDCEDIUSERFIELD5><UC_IDCLABELINFOSUPP></UC_IDCLABELINFOSUPP></ORDER_LINE_SEG><ORDER_LINE_SEG><SEGNAM>ORDER_LINE</SEGNAM><ORDNUM>22778210-02363150</ORDNUM><ORDLIN>0004</ORDLIN><ORDSLN>0000</ORDSLN><INVSTS_PRG>A</INVSTS_PRG><INVSTS>A</INVSTS><SALES_ORDNUM>10477177</SALES_ORDNUM><ENTDTE>20160206063155</ENTDTE><PRTNUM>69398KH</PRTNUM><CSTPRT>1151016</CSTPRT><EARLY_SHPDTE>20160215000000</EARLY_SHPDTE><EARLY_DLVDTE>20160206000000</EARLY_DLVDTE><LATE_SHPDTE>20160220000000</LATE_SHPDTE><LATE_DLVDTE>19000101000000</LATE_DLVDTE><ACCNUM>15341</ACCNUM><PCKQTY>4</PCKQTY><ORDQTY>4</ORDQTY><HOST_ORDQTY>4</HOST_ORDQTY><UNT_PRICE>6.50000</UNT_PRICE><UC_USERDEF1></UC_USERDEF1><UC_USERDEF2></UC_USERDEF2><UC_USERDEF5>00147</UC_USERDEF5><UC_USERDEF6>00875</UC_USERDEF6><UC_IDCEDIUSERFIELD1></UC_IDCEDIUSERFIELD1><UC_IDCEDIUSERFIELD2></UC_IDCEDIUSERFIELD2><UC_IDCEDIUSERFIELD3></UC_IDCEDIUSERFIELD3><UC_IDCEDIUSERFIELD4></UC_IDCEDIUSERFIELD4><UC_IDCEDIUSERFIELD5></UC_IDCEDIUSERFIELD5><UC_IDCLABELINFOSUPP></UC_IDCLABELINFOSUPP></ORDER_LINE_SEG></ORDER_SEG><ROUTETO_ADDR_SEG><SEGNAM>ROUTETO_ADDR_SEG</SEGNAM><ADRNAM>Kohl'SDistributionCenter#875</ADRNAM><ADRLN1>MACONDC#00875</ADRLN1><ADRLN2>3030AIRPORTROADEAST</ADRLN2><ATTN_NAME>Kohl'SDistributionCenter#875</ATTN_NAME><ADRCTY>MACON</ADRCTY><CTRY_NAME>US</CTRY_NAME><FAXNUM>9999999999</FAXNUM><PHNNUM>9999999999</PHNNUM><ADRPSZ>31216</ADRPSZ><ADRSTC>GA</ADRSTC></ROUTETO_ADDR_SEG><SHIPTO_ADDR_SEG><SEGNAM>SHIPTO_ADDR_SEG</SEGNAM><ADRNAM>Kohl'S#147</ADRNAM><ADRLN1>3030AirportRoadEast</ADRLN1><ADRCTY>Macon</ADRCTY><CTRY_NAME>US</CTRY_NAME><ADRPSZ>31216</ADRPSZ><ADRSTC>GA</ADRSTC></SHIPTO_ADDR_SEG></CTRL_SEG></ORDER_INB_IFD>";
					response = makeServiceCall(data, orderNumber, retryIndex, retryCount);
				} else if (retryIndex == retryCount) {
					retryDao.updateRetryStatus(retryIndex, retryCount, orderNumber);
//					retryDao.deleteFailedMessage(orderNumber);
					break;
				} else if (processStage.equals("success")) {
					break;
				} else {
					response = makeServiceCall(originalMessage, orderNumber, retryIndex, retryCount);
				}
//				exchange.getOut().setHeader("orderNumber", orderNumber);s
//				exchange.getOut().setHeader("StatusLine", response.getStatusLine());
			}
		} catch (SQLException e) {
			throw new FailedMessageRetryBeanException("Failed to process the resultSet for the required data", e);
		}
	}

	/**
	 * Method to request the service upon retry
	 * 
	 * @param originalMessage
	 * @param orderNumber 
	 * @param retryCount 
	 * @param retryIndex 
	 * @param exchange 
	 * @return HttpResponse Object
	 * @throws FailedMessageRetryBeanException 
	 * @throws UtilityException 
	 * @throws FailedMessageRetryDaoException 
	 */
	private HttpResponse makeServiceCall(String originalMessage, String orderNumber, int retryIndex, int retryCount) throws FailedMessageRetryBeanException, FailedMessageRetryDaoException, UtilityException {
		logger.debug("inside .makeServiceCall of FailedMessageRetryBean : " + serviceUrl);
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = null;
		URI obj = null;
		try {
			obj = new URI(serviceUrl.trim());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		postRequest = new HttpPost(obj);
		postRequest.setEntity(new StringEntity(originalMessage.toString(), "UTF8"));
		postRequest.setHeader("Content-Type", "application/xml");

		HttpResponse response = null;
		BufferedReader reader;
		StringBuffer result = new StringBuffer();
		try {
			response = client.execute(postRequest);
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			logger.debug("result : " + result);
		} catch (ClientProtocolException e) {
			throw new FailedMessageRetryBeanException(e.getMessage());
		} catch (IOException e) {
			throw new FailedMessageRetryBeanException(e.getMessage());
		}
		logger.debug("StatusLine : "+response.getStatusLine());
		if(orderNumber != null && response.getStatusLine().toString().contains("200")) {
			logger.debug("inside else if to delete the messgae if retry is succesfull");
			retryDao.updateRetryStatusProcessPolicyId(orderNumber);
//			retryDao.deleteFailedMessage(orderNumber);
		}
		
		return response;
	}
}
