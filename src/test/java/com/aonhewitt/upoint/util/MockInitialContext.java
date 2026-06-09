package com.aonhewitt.upoint.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;


public class MockInitialContext {
	

	public void $init() {
	}

	
	public Object lookup(String name) {
		PreparedStatement stm = new PreparedStatement() {

			@Override
			public ResultSet executeQuery(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return getResultSet();
			}

			@Override
			public int executeUpdate(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void close() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getMaxFieldSize() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setMaxFieldSize(int max) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getMaxRows() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setMaxRows(int max) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setEscapeProcessing(boolean enable) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getQueryTimeout() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setQueryTimeout(int seconds) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void cancel() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public SQLWarning getWarnings() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void clearWarnings() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCursorName(String name) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean execute(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public ResultSet getResultSet() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getUpdateCount() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean getMoreResults() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setFetchDirection(int direction) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getFetchDirection() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setFetchSize(int rows) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int getFetchSize() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getResultSetConcurrency() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getResultSetType() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void addBatch(String sql) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearBatch() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public int[] executeBatch() throws SQLException {
				// TODO Auto-generated method stub
				int[] result = null;
				return result;
			}

			@Override
			public Connection getConnection() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean getMoreResults(int current) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ResultSet getGeneratedKeys() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int executeUpdate(String sql, String[] columnNames) throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean execute(String sql, int[] columnIndexes) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean execute(String sql, String[] columnNames) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getResultSetHoldability() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public boolean isClosed() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setPoolable(boolean poolable) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isPoolable() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void closeOnCompletion() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isCloseOnCompletion() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public <T> T unwrap(Class<T> iface) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ResultSet executeQuery() throws SQLException {
				ResultSet rs = getResultSet();
				return rs;
						 
			}

			@Override
			public int executeUpdate() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setNull(int parameterIndex, int sqlType) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBoolean(int parameterIndex, boolean x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setByte(int parameterIndex, byte x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setShort(int parameterIndex, short x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setInt(int parameterIndex, int x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setLong(int parameterIndex, long x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setFloat(int parameterIndex, float x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDouble(int parameterIndex, double x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setString(int parameterIndex, String x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBytes(int parameterIndex, byte[] x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDate(int parameterIndex, Date x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTime(int parameterIndex, Time x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearParameters() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setObject(int parameterIndex, Object x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean execute() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void addBatch() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setRef(int parameterIndex, Ref x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBlob(int parameterIndex, Blob x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setClob(int parameterIndex, Clob x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setArray(int parameterIndex, Array x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public ResultSetMetaData getMetaData() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setURL(int parameterIndex, URL x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public ParameterMetaData getParameterMetaData() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setRowId(int parameterIndex, RowId x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNString(int parameterIndex, String value) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNClob(int parameterIndex, NClob value) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
					throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setClob(int parameterIndex, Reader reader) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNClob(int parameterIndex, Reader reader) throws SQLException {
				// TODO Auto-generated method stub

			}

		};

		Connection conn = new Connection() {

			@Override
			public <T> T unwrap(Class<T> iface) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTransactionIsolation(int level) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setSchema(String schema) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public Savepoint setSavepoint(String name) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Savepoint setSavepoint() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setReadOnly(boolean readOnly) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setHoldability(int holdability) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setClientInfo(String name, String value) throws SQLClientInfoException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setClientInfo(Properties properties) throws SQLClientInfoException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCatalog(String catalog) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAutoCommit(boolean autoCommit) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void rollback(Savepoint savepoint) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void rollback() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void releaseSavepoint(Savepoint savepoint) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
					int resultSetHoldability) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PreparedStatement prepareStatement(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return stm;
			}

			@Override
			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
					int resultSetHoldability) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CallableStatement prepareCall(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String nativeSQL(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isValid(int timeout) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReadOnly() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isClosed() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public SQLWarning getWarnings() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, Class<?>> getTypeMap() throws SQLException {
				// TODO Auto-generated method stub
				return Collections.emptyMap();
			}

			@Override
			public int getTransactionIsolation() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getSchema() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getNetworkTimeout() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public DatabaseMetaData getMetaData() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getHoldability() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getClientInfo(String name) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Properties getClientInfo() throws SQLException {
				// TODO Auto-generated method stub
				Properties result = null;
				return result;
			}

			@Override
			public String getCatalog() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean getAutoCommit() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Statement createStatement() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SQLXML createSQLXML() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public NClob createNClob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Clob createClob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Blob createBlob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void commit() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void close() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearWarnings() throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void abort(Executor executor) throws SQLException {
				// TODO Auto-generated method stub

			}
		};
		DataSource ds = new DataSource() {

			@Override
			public <T> T unwrap(Class<T> iface) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setLoginTimeout(int seconds) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setLogWriter(PrintWriter out) throws SQLException {
				// TODO Auto-generated method stub

			}
			@Override
			public Logger getParentLogger() throws SQLFeatureNotSupportedException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getLoginTimeout() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public PrintWriter getLogWriter() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Connection getConnection(String username, String password) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Connection getConnection() throws SQLException {
				// TODO Auto-generated method stub
				return  conn;
			}
		};
		




		return ds;
	}
}


	
