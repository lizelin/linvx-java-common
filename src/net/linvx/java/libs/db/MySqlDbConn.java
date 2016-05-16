package net.linvx.java.libs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.linvx.java.libs.utils.MyStringUtils;

/**
 * mysql数据源（数据库连接），如果有ds，则从ds取，否则，直接建立简单连接。本class基本用不到，可以是用MyDbHelper即可。MyDbHelper从配置文件建立连接池
 * @author lizelin
 *
 */
public class MySqlDbConn {
	private String mysql_ds = "";
	private String mysql_url = "";
	private String mysql_user = "";
	private String mysql_password = "";

	private DataSource dataSource = null;
	
	public MySqlDbConn() {

	}

	public String getMysqlDs() {
		return mysql_ds;
	}

	public MySqlDbConn setMysqlDs(String mysql_ds) {
		this.mysql_ds = mysql_ds;
		return this;
	}

	public String getMysqlUrl() {
		return mysql_url;
	}

	public MySqlDbConn setMysqlUrl(String mysql_url) {
		this.mysql_url = mysql_url;
		return this;
	}

	public String getMysqlUser() {
		return mysql_user;
	}

	public MySqlDbConn setMysqlUser(String mysql_user) {
		this.mysql_user = mysql_user;
		return this;
	}

	public String getMysqlPassword() {
		return mysql_password;
	}

	public MySqlDbConn setMysqlPassword(String mysql_password) {
		this.mysql_password = mysql_password;
		return this;
	}

	public java.sql.Connection getMysqlConnection() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, NamingException {
		String classloader = Thread.currentThread().getContextClassLoader().getClass().getName();
		if (MyStringUtils.isEmpty(classloader) || classloader.equalsIgnoreCase("sun.misc.Launcher$AppClassLoader")
				|| MyStringUtils.isEmpty(this.getMysqlDs()))
			return getMysqlConnectionDirect();
		else
			return getMysqlConnectionByContext();
	}

	private java.sql.Connection getMysqlConnectionByContext() throws NamingException, SQLException {
		
		if (dataSource == null) {
			// 初始化查找命名空间
			Context ctx = new InitialContext();
			// 参数java:/comp/env为固定路径
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			// 参数jdbc/mysqlds为数据源和JNDI绑定的名字
			dataSource = (DataSource) envContext.lookup(this.getMysqlDs());
		}
		return dataSource.getConnection();
	}

	private java.sql.Connection getMysqlConnectionDirect()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); // MYSQL驱动
		Connection conn = DriverManager.getConnection(this.getMysqlUrl(), this.getMysqlUser(), this.getMysqlPassword()); // 链接本地MYSQL
		return conn;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
