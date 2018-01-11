package com.wherewerks.wmsorder.dao;

import static com.wherewerks.wmsorder.OrderTableConstants.TABLE_ORD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.metamodel.DataContextFactory;
import org.apache.metamodel.DefaultUpdateSummary;
import org.apache.metamodel.UpdateCallback;
import org.apache.metamodel.UpdateScript;
import org.apache.metamodel.UpdateableDataContext;
import org.apache.metamodel.insert.RowInsertionBuilder;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.attunedlabs.config.util.DataSourceInstance;
import com.wherewerks.util.Utility;
import com.wherewerks.util.UtilityException;
import com.wherewerks.wmsorder.OrderTableConstants;
import com.wherewerks.wmsorder.bean.OrderProcessingException;
import com.wherewerks.wmsorder.pojo.OrderAddress;
import com.wherewerks.wmsorder.pojo.OrderDetails;
import com.wherewerks.wmsorder.pojo.OrderLine;
import com.wherewerks.wmsorder.pojo.OrderNote;
import static com.wherewerks.wmsorder.OrderTableConstants.*;

public class OrderProcessingDao {

	Logger logger = LoggerFactory.getLogger(OrderProcessingDao.class);
	Utility utilityBean = new Utility();

	static final String ADD_ORDERDETAILS_QUERY = "insert into ord(id, client_id, ordnum, wh_id) values(?, ?, ?, ?)";
	static final String ADD_ORDERLINE_QUERY = "insert into ord_line(ord_id, ordlin, ordsln, invsts_prg, sales_ordnum, entdte, prtnum, cstprt, early_shpdte, "
			+ "early_dlvdte, late_shpdte, late_dlvdte, accnum, pckqty, host_ordqty, unt_price) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	static final String ADD_ORDERNOTE_QUERY = "insert into ord_note(ord_id, nottyp, notlin, nottxt) value(?,?,?,?)";
	static final String ADD_ADDRESSMASTER_QUERY = "insert into adrmst(adr_id, adrnam, adrln1, adrcty, ctry_name, adrpsz, adrstc) values(?,?,?,?,?,?,?)";

	/*
	 * public OrderProcessingDao() { connection = OrderUtil.getDBConnection(); }
	 */

	/**
	 * Method to log the Order Details of a single Order
	 * 
	 * @param orderDetails
	 * @param connection2
	 * @throws OrderProcessingException
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws SQLException
	 */
	public void addOrderDetails(final OrderDetails orderDetails) throws OrderProcessingDaoException, SQLException {
		logger.debug("inside addOrderDetails method......");
		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("Connection object in addOrderDetails::: " + connection);
			JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
			dataContext.setIsInTransaction(false);
			final Table table = dataContext.getTableByQualifiedLabel(TABLE_ORD);
			logger.debug("Table defined successfully in datacontext:: " + table.getName());
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
				@Override
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(TABLE_ORD);
					insert.value(CLOUMN_CLIENTID, orderDetails.getClientId())
							.value(CLOUMN_ORDNUM, orderDetails.getOrdnum()).value(COLUMN_WHID, orderDetails.getWh_id())
							.value(COLUMN_BTCUST, orderDetails.getBtcust())
							.value(COLUMN_STCUST, orderDetails.getStcust())
							.value(COLUMN_RTCUST, orderDetails.getRtcust())
							.value(COLUMN_BRCUST, orderDetails.getBrcust())
							.value(COLUMN_BT_ADR_ID, orderDetails.getBt_adr_id())
							.value(COLUMN_ST_ADR_ID, orderDetails.getSt_adr_id())
							.value(CLOUMN_RT_ADR_ID, orderDetails.getRt_adr_id())
							.value(COLUMN_BR_ADR_ID, orderDetails.getBr_adr_id())
							.value(COLUMN_ORDTYP, orderDetails.getOrdtyp())
							.value(CLOUMN_ENTDTE, orderDetails.getEntdte())
							.value(COLUMN_ADDTE, orderDetails.getAdddte())
							.value(COLUMN_CPOTYP, orderDetails.getCpotyp())
							.value(COLUMN_CPONUM, orderDetails.getCponum())
							.value(COLUMN_CPODTE, orderDetails.getCpodte())
							.value(COLUMN_SRVTYPE, orderDetails.getSrv_type())
							.value(CLOUMN_PAYTRM, orderDetails.getPaytrm())
							.value(COLUMN_CARFLG, orderDetails.getCarflg())
							.value(COLUMN_SHIPBY, orderDetails.getShipby())
							.value(COLUMN_RRLFLG, orderDetails.getRrlflg())
							.value(COLUMN_WAVEFLG, orderDetails.getWave_flg())
							.value(COLUMN_REQUIR_TMS_FLG, orderDetails.getRequir_tms_flg())
							.value(COLUMN_RMANUM, orderDetails.getRmanum())
							.value(COLUMN_SUPER_ORD_FLG, orderDetails.getSuper_ord_flg())
							.value(COLUMN_SUPER_ORDNUM, orderDetails.getSuper_ordnum())
							.value(COLUMN_ORD_FRTRETE, orderDetails.getOrd_frtrte())
							.value(COLUMN_BCOFLG, orderDetails.getBco_flg())
							.value(COLUMN_SIG_REQ_FLG, orderDetails.getSig_req_flg())
							.value(COLUMN_BILL_FREIFGHT_FLG, orderDetails.getBill_freight_flg())
							.value(COLUMN_CUSTOMS_CLR_FLG, orderDetails.getCustoms_clr_flg())
							.value(COLUMN_COD_IBD_TYPE, orderDetails.getCod_ind_type())
							.value(COLUMN_COD_PAYMENT_TYPE, orderDetails.getCod_payment_type())
							.value(COLUMN_PAYMENT_TYPE, orderDetails.getPayment_type())
							.value(COLUMN_SFCUST, orderDetails.getSfcust())
							.value(CLOUMN_SF_ADR_ID, orderDetails.getSf_adr_id())
							.value(COLUMN_CSRNAM, orderDetails.getCsr_nam())
							.value(COLUMN_CSR_PHNUM, orderDetails.getCsr_phnnum())
							.value(COLUMN_CSR_EMAIL_ADR, orderDetails.getCsr_email_adr())
							.value(COLUMN_BUS_GRP, orderDetails.getBus_grp())
							.value(COLUMN_HOST_APPT_NUM, orderDetails.getHost_appt_num())
							.value(COLUMN_DLV_CONTACT, orderDetails.getDlv_contact())
							.value(COLUMN_ORD_DIR, orderDetails.getOrd_dir())
							.value(COLUMN_FRT_ALLOW, orderDetails.getFrt_allow())
							.value(COLUMN_ORD_SPL_COD, orderDetails.getOrd_spl_cod())
							.value(COLUMN_TEMPLATE_FLG, orderDetails.getTemplate_flg())
							.value(COLUMN_TEMPLATE_REF, orderDetails.getTemplate_ref())
							.value(CLOUMN_CRNCY_CODE, orderDetails.getCrncy_code())
							.value(COLUMN_BTO_SQENUM, orderDetails.getBto_seqnum())
							.value(COLUMN_SLOT, orderDetails.getSlot()).value(CLOUMN_MODDTE, orderDetails.getModdte())
							.value(CLOUMN_MOD_USR_ID, orderDetails.getMod_usr_id())
							.value(COLUMN_EXP_ADR_ID, orderDetails.getExp_adr_id())
							.value(COLUMN_EXP_CUST, orderDetails.getExp_cust())
							.value(COLUMN_IMP_ADR_ID, orderDetails.getImp_adr_id())
							.value(COLUMN_IMP_CUST, orderDetails.getImp_cust())
							.value(COLUMN_RETTRANSFLG, orderDetails.getRtetransflg())
							.value(COLUMN_REPARTIESCOD, orderDetails.getRelpartiescod())
							.value(COLUMN_CONTRACT_NUM, orderDetails.getContract_nam())
							.value(COLUMN_DUTY_PAYMENT_TYP, orderDetails.getDuty_payment_type())
							.value(COLUMN_DUTY_PAYMENT_ACCT, orderDetails.getDuty_payment_acct())
							.value(COLUMN_RET_ADR_ID, orderDetails.getRet_adr_id())
							.value(COLUMN_RET_CUST, orderDetails.getRet_cust())
							.value(CLOUMN_DEPTNO, orderDetails.getDeptno())
							.value(COLUMN_DEST_NUM, orderDetails.getDest_num())
							.value(COLUMN_RUSH_FLG, orderDetails.getRush_flg())
							.value(COLUMN_CSTMS_DTYCUST, orderDetails.getCstms_dtycust())
							.value(COLUMN_EXCISE_DTYCUST, orderDetails.getExp_cust())
							.value(COLUMN_CSTMS_ORD_STAT, orderDetails.getCstms_ord_stat())
							.value(COLUMN_CSTMS_STAT_NOTES, orderDetails.getCstms_stat_notes())
							.value(COLUMN_CSTMS_ORDTYP, orderDetails.getCstms_ordtyp())
							.value(COLUMN_ORDOCOLCSTMS_ADDL_INFO, orderDetails.getOrdcolcstms_addl_info()).execute();
				}
			});
			Integer totalInsertedRows = 0;

			if (insertSummary.getInsertedRows().isPresent()) {
				totalInsertedRows = (Integer) insertSummary.getInsertedRows().get();
				logger.debug("total added ord_line : " + totalInsertedRows);
			} else {
				logger.debug("nothing added to ord_line ..");
			}
		} catch (SQLException e) {
			throw new OrderProcessingDaoException("Failed to run the insert  query", e, e.getMessage(), 400);
		} catch (IOException e) {
			throw new OrderProcessingDaoException("Failed to run the insert  query", e, e.getMessage(), 400);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception :: "+e.getMessage());
		}
		
		finally {
			DataSourceInstance.closeConnection(connection);
		}

	}

	public void addOrderNote(final OrderNote orderNote) throws OrderProcessingDaoException, SQLException {
		logger.debug("inside addOrderNote method......");
		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("Connection object in addOrderDetails::: " + connection);
			JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
			dataContext.setIsInTransaction(false);
			final Table table = dataContext.getTableByQualifiedLabel(TABLE_ORD_NOTE);
			logger.debug("Table defined successfully in datacontext:: " + table.getName());
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
				@Override
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(TABLE_ORD_NOTE);
					insert.value(CLOUMN_CLIENTID, orderNote.getClient_id()).value(CLOUMN_ORDNUM, orderNote.getOrdnum())
							.value(COLUMN_NOTTYP, orderNote.getNottyp()).value(COLUMN_NOTLIN, orderNote.getNotlin())
							.value(COLUMN_WHID, orderNote.getWh_id()).value(COLUMN_NOTTXT, orderNote.getNottxt())
							.value(CLOUMN_EDTFLG, orderNote.getEdtflg())
							.value(COLUMN_U_VERSION, orderNote.getU_version())
							.value(COLUMN_INS_DT, orderNote.getIns_dt())
							.value(COULUMN_LAST_UP_DT, orderNote.getLast_upd_dt())
							.value(COLUMN_INS_USER_ID, orderNote.getIns_user_id())
							.value(COLUMN_LAST_UPD_USER_ID, orderNote.getLast_upd_user_id()).execute();
				}
			});
			Integer totalInsertedRows = 0;

			if (insertSummary.getInsertedRows().isPresent()) {
				totalInsertedRows = (Integer) insertSummary.getInsertedRows().get();
				logger.debug("total added ord_line : " + totalInsertedRows);
			} else {
				logger.debug("nothing added to ord_line ..");
			}
		} catch (SQLException e) {
			throw new OrderProcessingDaoException("Failed to run the insert  query", e, e.getMessage(), 400);
		} catch (IOException e) {
			throw new OrderProcessingDaoException("Failed to run the insert  query", e, e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}
	}

	/**
	 * Method to log the Order Lines of a single Order
	 * 
	 * @param orderLine
	 * @param connection2
	 * @return
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void addOrderLine(final OrderLine orderLine)
			throws UtilityException, OrderProcessingDaoException, SQLException, IOException {
		logger.debug("inside .addOrderLine() of OrderProcessingDao : " + orderLine.toString());
		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("connection object in order details: " + connection);
			JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
			dataContext.setIsInTransaction(false);
			// UpdateableDataContext dataContext =
			// DataContextFactory.createJdbcDataContext(connection);
			final Table table = dataContext.getTableByQualifiedLabel(TABLE_ORD_LINE);
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
				// dataContext.executeUpdate(new UpdateScript() {
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(table);
					insert.value(OrderTableConstants.CLOUMN_CLIENTID, "NA").value(OrderTableConstants.COLUMN_WHID, "NA")
							.value(OrderTableConstants.CLOUMN_ORDNUM, orderLine.getORDNUM())
							.value(OrderTableConstants.CLOUMN_ORDLIN, orderLine.getORDLIN())
							.value(OrderTableConstants.CLOUMN_ORDSLN, orderLine.getORDSLN())
							.value(OrderTableConstants.CLOUMN_INVSTS_PRG, orderLine.getINVSTS_PRG())
							.value(OrderTableConstants.CLOUMN_ORDNUM, orderLine.getSALES_ORDNUM())
							.value(OrderTableConstants.CLOUMN_ENTDTE, orderLine.getENTDTE())
							.value(OrderTableConstants.CLOUMN_PRTNUM, orderLine.getPRTNUM())
							.value(OrderTableConstants.CLOUMN_CSTPRT, orderLine.getCSTPRT())
							.value(OrderTableConstants.CLOUMN_EARLY_SHPDTE, orderLine.getEARLY_SHPDTE())
							.value(OrderTableConstants.CLOUMN_EARLY_DLVDTE, orderLine.getEARLY_DLVDTE())
							.value(OrderTableConstants.CLOUMN_LATE_SHPDTE, orderLine.getLATE_SHPDTE())
							.value(OrderTableConstants.CLOUMN_LATE_DLVDTE, orderLine.getLATE_DLVDTE())
							.value(OrderTableConstants.CLOUMN_ACCNUM, orderLine.getACCNUM())
							.value(OrderTableConstants.CLOUMN_PCKQTY, orderLine.getPCKQTY())
							.value(OrderTableConstants.CLOUMN_HOST_ORDQTY, orderLine.getHOST_ORDQTY())
							.value(OrderTableConstants.CLOUMN_UNT_PRICE, orderLine.getUNT_PRICE())
							// .value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD1,
							// orderLine.getUC_IDCEDIUSERFIELD1())
							// .value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD2,
							// orderLine.getUC_IDCEDIUSERFIELD2())
							// .value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD3,
							// orderLine.getUC_IDCEDIUSERFIELD3())
							// .value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD4,
							// orderLine.getUC_IDCEDIUSERFIELD4())
							// .value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD5,
							// orderLine.getUC_IDCEDIUSERFIELD5())
							.value(OrderTableConstants.CLOUMN_ORDQTY, orderLine.getORDQTY())
							// .value(OrderTableConstants.CLOUMN_UC_USERDEF5,
							// orderLine.getUC_USERDEF5())
							// .value(OrderTableConstants.CLOUMN_UC_USERDEF6,
							// orderLine.getUC_USERDEF6())
							// .value(OrderTableConstants.CLOUMN_UC_USERDEF1,
							// orderLine.getUC_USERDEF1())
							// .value(OrderTableConstants.CLOUMN_UC_USERDEF2,
							// orderLine.getUC_USERDEF2())
							// .value(OrderTableConstants.CLOUMN_UC_IDCLABELINFOSUPP,
							// orderLine.getUC_IDCLABELINFOSUPP())
							// .value(OrderTableConstants.CLOUMN_INVSTS,
							// orderLine.getINVSTS())
							.value(OrderTableConstants.CLOUMN_ENTDTE, orderLine.getENTDTE()).execute();
				}
			});
			Integer totalInsertedRows = 0;

			if (insertSummary.getInsertedRows().isPresent()) {
				totalInsertedRows = (Integer) insertSummary.getInsertedRows().get();
				logger.debug("total added ord_line : " + totalInsertedRows);
				// return totalInsertedRows > 0;
			} else {
				logger.debug("nothing added to ord_line ..");
				// return false;
			}
		} catch (Exception e) {

			throw new OrderProcessingDaoException("Failed to execute query for inserting Order Lines", e,
					e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}

	}

	/**
	 * Method to log the Master Address of a single Order
	 * 
	 * @param orderAdd
	 * @param connection
	 * @return
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void addAddressMaster(final OrderAddress orderAdd)
			throws UtilityException, OrderProcessingDaoException, SQLException, IOException {
		logger.debug("inside .addAddressMaster() of OrderProcessingDao : " + orderAdd.toString());
		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("connection object in order details: " + connection);
			UpdateableDataContext dataContext = DataContextFactory.createJdbcDataContext(connection);
			final Table table = dataContext.getTableByQualifiedLabel(TABLE_ADRMST);
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
				// dataContext.executeUpdate(new UpdateScript() {
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(table);
					insert.value(OrderTableConstants.COLUMN_ARD_ID, "NA")
							.value(OrderTableConstants.CLOUMN_ADRNAM, orderAdd.getADRNAM())
							.value(OrderTableConstants.CLOUMN_ADRLN1, orderAdd.getADRLN1())
							.value(OrderTableConstants.CLOUMN_ADRLN2, orderAdd.getADRLN2())
							.value(OrderTableConstants.CLOUMN_ADRCTY, orderAdd.getADRCTY())
							.value(OrderTableConstants.CLOUMN_CTRY_NAME, orderAdd.getCTRY_NAME())
							.value(OrderTableConstants.CLOUMN_ADRPSZ, orderAdd.getADRPSZ())
							.value(OrderTableConstants.CLOUMN_ADRSTC, orderAdd.getADRSTC())
							.value(OrderTableConstants.CLOUMN_ATTN_NAME, orderAdd.getATTN_NAME())
							.value(OrderTableConstants.CLOUMN_FAXNUM, orderAdd.getFAXNUM())
							.value(OrderTableConstants.CLOUMN_PHNNUM, orderAdd.getPHNNUM()).execute();
				}
			});
			Integer totalInsertedRows = 0;

			if (insertSummary.getInsertedRows().isPresent()) {
				totalInsertedRows = (Integer) insertSummary.getInsertedRows().get();
				logger.debug("total added adrmst : " + totalInsertedRows);
				// return totalInsertedRows > 0;
			} else {
				logger.debug("nothing added to adrmst ..");
				// return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderProcessingDaoException("Failed to execute query for inserting Master Address", e,
					e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}
	}
}
