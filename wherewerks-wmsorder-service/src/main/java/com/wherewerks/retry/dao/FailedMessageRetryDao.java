package com.wherewerks.retry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wherewerks.retry.pojo.FailedRetryLogs;
import com.wherewerks.util.Utility;
import com.wherewerks.util.UtilityException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class FailedMessageRetryDao {

	static Logger logger = LoggerFactory.getLogger(FailedMessageRetryDao.class);
	Utility utilityBean = new Utility();

	static final String GET_POLICY_DETAILS = "Select * from roi.policy where retrypolicyId = ?";
	static final String GET_FAILEDMESSAGE_DETAILS = "Select * from roi.failedretrylogs where uniqueId  = ?";
	static final String REGISTER_FAILED_MESSAGES = "Insert into roi.failedretrylogs(uniqueId, originalmsg, processstage, retrypolicyID, retryindex, lasterrmsg, manualretryflag, retrystatus, recordTime) values(?,?,?,?,?,?,?,?,?)";
	static final String UPDATE_RETRY_INDEX = "Update roi.failedretrylogs set retryindex = ?, lasterrmsg = ?, retrystatus = ?, updatedTime = ? where uniqueId = ?";
	static final String UPDATE_RETRY_STATUS = "Update roi.failedretrylogs set processstage = ?, retrystatus = ?, retrypolicyID = ? where uniqueId = ?";
//	static final String UPDATE_RETRY_STATUS_PROCESS_POLICYID = "SET foreign_key_checks = 0; Update roi.failedretrylogs set processstage = ?, retrystatus = ?, retrypolicyID = ? where uniqueId = ?";
	static final String UPDATE_RETRY_STATUS_PROCESS_POLICYID = "update roi.failedretrylogs set processstage = ?, retrystatus = ?, retrypolicyID = ? where uniqueId = ?";
	static final String GET_FAILEDMESSAGE = "Select * from roi.failedretrylogs where retrypolicyID  = ?";
	static final String DELETE_FAILED_MESSAGE = "Delete from roi.failedretrylogs where uniqueId  = ?";

	/**
	 * Method to fetch the Policy Details of a given policyId
	 * 
	 * @param orderNumber
	 * @return
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public ResultSet getPolicyDetails(String orderNumber) throws FailedMessageRetryDaoException, UtilityException {
		logger.debug("inside .getPolicyDetails of FailedMessageRetryDao");
		ResultSet resultset = null;
		PreparedStatement preparedStatement = null;
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		try {
			preparedStatement = connection.prepareStatement(GET_POLICY_DETAILS);
			preparedStatement.setString(1, orderNumber);
			logger.debug("Prepared Statement : " + preparedStatement);
			resultset = preparedStatement.executeQuery();
			logger.debug("resultset : " + resultset.toString());

		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to get Policy Details for the given policyId", e);
		}
		return resultset;
	}

	/**
	 * Method to log the failed Order Details
	 * 
	 * @param failedRetryLogs
	 * @return
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public String registerFailedMessage(FailedRetryLogs failedRetryLogs)
			throws FailedMessageRetryDaoException, UtilityException {
		logger.debug("inside .registerFailedMessage of FailedMessageRetryBean" + failedRetryLogs.getRetryPolicyId());
		int response = 0;
		PreparedStatement preparedStatement = null;
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		try {
			preparedStatement = connection.prepareStatement(REGISTER_FAILED_MESSAGES);
			preparedStatement.setString(1, failedRetryLogs.getUniqueId());
			preparedStatement.setString(2, failedRetryLogs.getOriginalMsg());
			preparedStatement.setString(3, failedRetryLogs.getProcessstage());
			preparedStatement.setString(4, failedRetryLogs.getRetryPolicyId());
			preparedStatement.setInt(5, failedRetryLogs.getRetryIndex());
			preparedStatement.setString(6, failedRetryLogs.getLasterrMsg());
			preparedStatement.setString(7, failedRetryLogs.isManualRetryFlag());
			preparedStatement.setString(8, failedRetryLogs.getRetyrStatus());
			preparedStatement.setString(9, failedRetryLogs.getRecordTime().toString());
			logger.debug("uniqueId : " + failedRetryLogs.getUniqueId() + " " + "original message : "
					+ failedRetryLogs.getOriginalMsg() + " " + "retry index : " + failedRetryLogs.getRetryIndex() + " "
					+ "retry status : " + failedRetryLogs.getRetyrStatus() + " " + "recordTime : "
					+ failedRetryLogs.getRecordTime());
			logger.debug("Prepared Statement: " + preparedStatement);
			response = preparedStatement.executeUpdate();
			if (response == 0) {
				logger.debug("failed to insert failed message details");
			} else {
				logger.debug("done inserting failed message details");
			}
		} catch (MySQLIntegrityConstraintViolationException mysql) {
			logger.error(
					"MySQL Integrity Constraint Violation Exception, data to be inserted already exists in the database");
		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to insert the failed Order Details", e);
		}
		return failedRetryLogs.getRetyrStatus();
	}

	/**
	 * Method to fetch the Order Details of the failed Orders registered
	 * 
	 * @param orderNumber
	 * @return
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public ResultSet getFailedMessageDetails(String orderNumber)
			throws FailedMessageRetryDaoException, UtilityException {
		ResultSet resultset = null;
		PreparedStatement preparedStatement = null;
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		try {
			preparedStatement = connection.prepareStatement(GET_FAILEDMESSAGE_DETAILS);
			preparedStatement.setString(1, orderNumber);
			logger.debug("Prepared Statement: " + preparedStatement);
			resultset = preparedStatement.executeQuery();
			logger.debug("resultset : " + resultset);

		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to get details of failed Orders", e);
		}
		return resultset;
	}

	/**
	 * Method to Update the retryIndex of a given failed Order
	 * 
	 * @param retryInterval
	 * @param retryIndex
	 * @param failureLogs
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public void updateRetryIndex(final int retryIndex, final ResultSet failureLogs)
			throws FailedMessageRetryDaoException, UtilityException {
		// ExecutorService executor = Executors.newSingleThreadExecutor();
		// Future<Object> future = executor.submit(new Callable<Object>() {
		// public Object call() throws Exception {
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		logger.debug("inside call for updateRetryIndex : " + retryIndex);
		int response = 0;
		PreparedStatement preparedStatement = null;
		Timestamp recordTime = new Timestamp(System.currentTimeMillis());
		try {
			preparedStatement = connection.prepareStatement(UPDATE_RETRY_INDEX);
			preparedStatement.setInt(1, retryIndex);
			preparedStatement.setString(2, failureLogs.getString("lasterrmsg"));
			preparedStatement.setString(3, failureLogs.getString("retrystatus"));
			preparedStatement.setString(4, recordTime.toString());
			preparedStatement.setString(5, failureLogs.getString("uniqueId"));
			response = preparedStatement.executeUpdate();
			logger.debug("response : " + response);
		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to Update retryIndex for the failed Order", e);
		}

		// try {
		// Thread.sleep(retryInterval);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// return response;
		// }
		//
		// });
		//
		// try {
		// logger.debug("futrue value : " + future.get());
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }
		// executor.shutdown();
	}

	/**
	 * Method to Update the retryStatus of a Failed Order
	 * 
	 * @param retryCount
	 * @param retryIndex
	 * 
	 * @param orderNumber
	 * @throws UtilityException
	 */
	public void updateRetryStatus(int retryIndex, int retryCount, String orderNumber) throws UtilityException {
		// ExecutorService executor = Executors.newSingleThreadExecutor();
		// Future<Object> future = executor.submit(new Callable<Object>() {
		// public Object call() throws Exception {
		Connection connection = Utility.getRetryConnection();
		logger.debug("inside call for updateRetryStatus : ");
		int response = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(UPDATE_RETRY_STATUS);
			preparedStatement.setString(1, "ignored");
			preparedStatement.setString(2, "Failed");
			preparedStatement.setString(3, "");
			response = preparedStatement.executeUpdate("SET foreign_key_checks = 0");
			preparedStatement.setString(4, orderNumber);
			response = preparedStatement.executeUpdate("SET foreign_key_checks = 1");
			response = preparedStatement.executeUpdate();
			logger.debug("response : " + response);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// try {
		// Thread.sleep(retryInterval);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// return response;
		// }
		//
		// });

		// try {
		// logger.debug("futrue value : " + future.get());
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }
		// executor.shutdown();
	}
	
	public void updateRetryStatusProcessPolicyId(String orderNumber) throws UtilityException {

		Connection connection = Utility.getRetryConnection();
		logger.debug("inside call for updateRetryStatusProcessPolicyId : ");
		int response = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(UPDATE_RETRY_STATUS_PROCESS_POLICYID);
			preparedStatement.setString(1, "success");
			preparedStatement.setString(2, "Successful");
			preparedStatement.setString(3, "");
			preparedStatement.setString(4, orderNumber);
			response = preparedStatement.executeUpdate("SET foreign_key_checks = 0");
			response = preparedStatement.executeUpdate();
			response = preparedStatement.executeUpdate("SET foreign_key_checks = 1");
			logger.debug("response : " + response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the failed Orders based on the policyId
	 * 
	 * @param policyId
	 * @return
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public ResultSet getFailedMessage(String policyId) throws FailedMessageRetryDaoException, UtilityException {
		ResultSet resultset = null;

		PreparedStatement preparedStatement = null;
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		try {
			preparedStatement = connection.prepareStatement(GET_FAILEDMESSAGE);
			preparedStatement.setString(1, policyId);
			logger.debug("Prepared Statement: " + preparedStatement);
			resultset = preparedStatement.executeQuery();
			logger.debug("resultset : " + resultset);
		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to get details of failed Orders", e);
		}
		return resultset;
	}

	/**
	 * Method to delete the Orders based on the Order Number
	 * 
	 * @param orderNumber
	 * @throws FailedMessageRetryDaoException
	 * @throws UtilityException
	 */
	public void deleteFailedMessage(String orderNumber) throws FailedMessageRetryDaoException, UtilityException {
		PreparedStatement preparedStatement = null;
		Connection connection = Utility.getRetryConnection();
		logger.debug("connection : " + connection);
		try {
			preparedStatement = connection.prepareStatement(DELETE_FAILED_MESSAGE);
			preparedStatement.setString(1, orderNumber);
			logger.debug("Prepared Statement: " + preparedStatement);
			preparedStatement.execute();
		} catch (SQLException e) {
			throw new FailedMessageRetryDaoException("Failed to get details of failed Orders", e);
		}
	}

//	public static void main(String[] args) throws UtilityException {
//		Connection connection = Utility.getRetryConnection();
//		logger.debug("inside call for updateRetryStatusProcessPolicyId : ");
//		int response = 0;
//		PreparedStatement preparedStatement = null;
//		try {
//			preparedStatement = connection.prepareStatement(UPDATE_RETRY_STATUS_PROCESS_POLICYID);
//			preparedStatement.setString(1, "success");
//			preparedStatement.setString(2, "Successful");
//			preparedStatement.setString(3, "");
//			preparedStatement.setString(4, "22778210-02363150");
//			response = preparedStatement.executeUpdate("SET foreign_key_checks = 0");
//			response = preparedStatement.executeUpdate();
//			logger.debug("response : " + response);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
