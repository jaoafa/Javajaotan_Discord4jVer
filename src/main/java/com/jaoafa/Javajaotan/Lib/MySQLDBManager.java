package com.jaoafa.Javajaotan.Lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDBManager {
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	Connection conn = null;
	public MySQLDBManager(String hostname, String port, String database,
			String username, String password) throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
	}
	public Connection getConnection() throws SQLException{
		if(conn != null && !conn.isClosed()){
			return conn;
		}
		String jdbcUrl = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
		conn = DriverManager.getConnection(jdbcUrl, this.user, this.password);
		return conn;
	}
}
