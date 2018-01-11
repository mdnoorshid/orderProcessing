package com.wherewerks.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.attunedlabs.eventframework.abstractbean.AbstractLeapCamelBean;
import com.wherewerks.wmsorder.OrderConstants;

public class Utility extends AbstractLeapCamelBean {

	static Logger logger = LoggerFactory.getLogger(Utility.class);

	private static final String DATA_SOURCE_KEY = "dataSourceA";

	static Connection con = null;
	static Connection connect = null;
	static Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(Utility.class.getClassLoader().getResourceAsStream(OrderConstants.DB_CONFIG_FILE));
		} catch (IOException e) {
			logger.error("Failed to read the properties file");
		}
	}

	/**
	 * Method to get Connection using exchange and datasource
	 * 
	 * @param exchange
	 * @return Connection object
	 * @throws UtilityException
	 */
	public Connection getDBConnection(Exchange exchange) throws UtilityException {
		DataSource dataSource = getDataSource(exchange.getContext(), DATA_SOURCE_KEY);
		logger.debug("dataSource : " + dataSource);
		try {
			con = getConnection(dataSource, exchange);
			logger.debug("connection returend : " + con);
		} catch (SQLException e) {
			throw new UtilityException("Unable to establishe a proper connection on the given dataSource", e);
		}
		return con;
	}// ..end of the util

	/**
	 * Method to get Connection from the defined properties in the property file
	 * 
	 * @return Connection Object
	 * @throws UtilityException
	 */
	public static Connection getRetryConnection() throws UtilityException {

		if (connect != null) {
			return connect;
		}
		String driver = properties.getProperty(OrderConstants.DRIVER);
		String url = properties.getProperty(OrderConstants.URL);
		String user = properties.getProperty(OrderConstants.USER);
		String password = properties.getProperty(OrderConstants.PASSWORD);
		return getConnectionForRetry(driver, url, user, password);
	}

	/**
	 * Method to get Connection for Retry
	 * 
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @return Connection Object
	 * @throws UtilityException
	 */
	private static Connection getConnectionForRetry(String driver, String url, String user, String password)
			throws UtilityException {
		try {
			Class.forName(driver);
			connect = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new UtilityException("Not a proper Driver class defined for the Connection", e);
		} catch (SQLException e) {
			throw new UtilityException("Failed to get Connection Object", e);
		}
		return connect;

	}

	/**
	 * Method to close the Connection Object
	 * 
	 * @param connection
	 * @throws UtilityException
	 */
	/*public static void dbClose(Connection connection) throws UtilityException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new UtilityException("Failed to close the conenction/Connection object is null", e);
		}
	}*/// ..end of method

	/**
	 * Method to close the COnnection & PreparedStatement together
	 * 
	 * @param connection
	 * @throws UtilityException
	 */
	/*public static void dbClose(Connection connection, PreparedStatement preparedStatement) throws UtilityException {
		try {
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			throw new UtilityException(
					"Failed to close the conenction/preparedstatement or Connection/PreparedStatement object is null", e);
		}
	}*/// ..end of method

	/**
	 * Method to get DataSource object from registry lookup
	 * 
	 * @param context
	 * @param lookupName
	 * @return datasource object
	 */
	public static DataSource getDataSource(CamelContext context, String lookupName) {
		DataSource datasource = (DataSource) context.getRegistry().lookupByName(lookupName);
		logger.debug("dataSource object by exchange lookup..: " + datasource);
		if (!(datasource == null)) {
			return datasource;
		} else {
			logger.error("Unable to lookup " + lookupName + " the dataSource from the Context");
		}
		return datasource;
	}

	/**
	 * Method to convert the body in exchange from JSON to XML
	 * 
	 * @param exchange
	 * @throws UtilityException
	 */
	public void xmlToJson(Exchange exchange) throws UtilityException {
		String exchangeBody = exchange.getIn().getBody(String.class);
		JSONObject exchangeJson = null;
		String exchangeXml = null;
		try {
				exchangeJson = new JSONObject(exchangeBody);
				exchangeXml = XML.toString(exchangeJson);
		} catch (JSONException e) {
			throw new UtilityException("Unable to transform the body to the desired format", e);
		}
		exchange.getIn().setBody(exchangeXml);
	}

	@Override
	protected void processBean(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}
