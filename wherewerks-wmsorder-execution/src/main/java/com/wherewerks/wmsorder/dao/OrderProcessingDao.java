package com.wherewerks.wmsorder.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.derby.tools.sysinfo;
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
import com.wherewerks.wmsorder.OrderConstants;
import com.wherewerks.wmsorder.OrderTableConstants;
import com.wherewerks.wmsorder.bean.OrderProcessingException;
import com.wherewerks.wmsorder.pojo.OrderAddress;
import com.wherewerks.wmsorder.pojo.OrderDetails;
import com.wherewerks.wmsorder.pojo.OrderLine;
import com.wherewerks.wmsorder.pojo.OrderNote;

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
	 *//*
	public void addOrderDetails(final OrderDetails orderDetails)
			throws OrderProcessingException, UtilityException, OrderProcessingDaoException {
		logger.debug("inside .addOrderDetails of OrderProcessingDao.. : " + orderDetails);

		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("connection object in order details: " + connection);
			UpdateableDataContext dataContext = DataContextFactory.createJdbcDataContext(connection);
			final Table table = dataContext.getTableByQualifiedLabel("ord");
			dataContext.executeUpdate(new UpdateScript() {
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(table);
					insert.value(OrderTableConstants.COLUMN_WHID, orderDetails.getWHSE_ID())
							.value(OrderTableConstants.CLOUMN_CLIENTID, orderDetails.getCLIENT_ID())
							.value(OrderTableConstants.CLOUMN_ORDNUM, orderDetails.getORDNUM()).execute();
				}
			});

		} catch (Exception e) {
			throw new OrderProcessingDaoException("Failed to execute query for inserting Order Details", e,
					e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}
	}*/

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
//			UpdateableDataContext dataContext = DataContextFactory.createJdbcDataContext(connection);
			final Table table = dataContext.getTableByQualifiedLabel("ord_line");
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
//			dataContext.executeUpdate(new UpdateScript() {
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(table);
					insert.value(OrderTableConstants.CLOUMN_CLIENTID, "NA")
							.value(OrderTableConstants.COLUMN_WHID, "NA")
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
//							.value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD1, orderLine.getUC_IDCEDIUSERFIELD1())
//							.value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD2, orderLine.getUC_IDCEDIUSERFIELD2())
//							.value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD3, orderLine.getUC_IDCEDIUSERFIELD3())
//							.value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD4, orderLine.getUC_IDCEDIUSERFIELD4())
//							.value(OrderTableConstants.CLOUMN_UC_IDCEDIUSERFIELD5, orderLine.getUC_IDCEDIUSERFIELD5())
							.value(OrderTableConstants.CLOUMN_ORDQTY, orderLine.getORDQTY())
//							.value(OrderTableConstants.CLOUMN_UC_USERDEF5, orderLine.getUC_USERDEF5())
//							.value(OrderTableConstants.CLOUMN_UC_USERDEF6, orderLine.getUC_USERDEF6())
//							.value(OrderTableConstants.CLOUMN_UC_USERDEF1, orderLine.getUC_USERDEF1())
//							.value(OrderTableConstants.CLOUMN_UC_USERDEF2, orderLine.getUC_USERDEF2())
//							.value(OrderTableConstants.CLOUMN_UC_IDCLABELINFOSUPP, orderLine.getUC_IDCLABELINFOSUPP())
//							.value(OrderTableConstants.CLOUMN_INVSTS, orderLine.getINVSTS())
							.value(OrderTableConstants.CLOUMN_ENTDTE, orderLine.getENTDTE())
							.execute();
				}
			});
			Integer totalInsertedRows = 0;

			if (insertSummary.getInsertedRows().isPresent()) {
				totalInsertedRows = (Integer) insertSummary.getInsertedRows().get();
				logger.debug("total added ord_line : " + totalInsertedRows);
//				return totalInsertedRows > 0;
			} else {
				logger.debug("nothing added to ord_line ..");
//				return false;
			}
		} catch (Exception e) {
			
			throw new OrderProcessingDaoException("Failed to execute query for inserting Order Lines", e,
					e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}

	}

	
	
	
	
	
	
	
	/**
	 * Method to log the Order Notes of a single Order
	 * 
	 * @param orderNote
	 * @param connection
	 * @return
	 * @throws UtilityException
	 * @throws OrderProcessingDaoException
	 * @throws IOException
	 * @throws SQLException
	 *//*
	public void addOrderNote(final OrderNote orderNote)
			throws UtilityException, OrderProcessingDaoException, SQLException, IOException {
		logger.debug("inside .addOrderNote() of OrderProcessingDao : " + orderNote.toString());
		Connection connection = null;
		try {
			connection = DataSourceInstance.getConnection();
			logger.debug("connection object in order details: " + connection);
			JdbcDataContext dataContext = (JdbcDataContext) DataContextFactory.createJdbcDataContext(connection);
			dataContext.setIsInTransaction(false);
//			UpdateableDataContext dataContext = DataContextFactory.createJdbcDataContext(connection);
			final Table table = dataContext.getTableByQualifiedLabel("ord_line");
			dataContext.executeUpdate(new UpdateScript() {
				public void run(UpdateCallback callback) {
					final RowInsertionBuilder insert = callback.insertInto(table);
					insert.value(OrderConstants.ORDNUM, orderNote.getORDNUM())
							.value(OrderConstants.NOTTYP, orderNote.getNOTTYP())
							.value(OrderConstants.NOTLIN, orderNote.getNOTLIN())
							.value(OrderConstants.NOTTXT, orderNote.getNOTTXT()).execute();
				}
			});

		} catch (Exception e) {
			throw new OrderProcessingDaoException("Failed to execute query for inserting Order Notes", e,
					e.getMessage(), 400);
		} finally {
			DataSourceInstance.closeConnection(connection);
		}

	}*/

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
			final Table table = dataContext.getTableByQualifiedLabel("adrmst");
			DefaultUpdateSummary insertSummary = (DefaultUpdateSummary) dataContext.executeUpdate(new UpdateScript() {
//			dataContext.executeUpdate(new UpdateScript() {
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
//				return totalInsertedRows > 0;
			} else {
				logger.debug("nothing added to adrmst ..");
//				return false;
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
