package net.linvx.java.libs.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import net.linvx.java.libs.conf.MyConfigReader;
import net.linvx.java.libs.tools.MyLog;

/**
 * 约定：配置文件在WEB-INF/conf/dbconfig.xml或者和src同一目录级别的conf/dbconfig.xml
 * 
 * @author lizelin
 *
 */
public class MyDbHelper {
	private static MyDbHelper instance = null;
	private static Logger log = MyLog.getLogger(MyDbHelper.class);

	public static MyDbHelper getInstance() {
		if (instance == null) {
			synchronized (MyDbHelper.class) {
				if (instance == null) {
					instance = new MyDbHelper();
				}
			}
		}
		return instance;
	}

	private MyDbHelper() {
		XMLConfiguration config = MyConfigReader.getConfig("dbconfig");
		List<HierarchicalConfiguration> dbs = config.configurationsAt("db");
		for (HierarchicalConfiguration sub : dbs) {
			initDataSource(sub);
		}
	}

	private void initDataSource(HierarchicalConfiguration c) {
		/*
		 * <db> <name>wx</name>
		 * <driverClassName>com.mysql.jdbc.Driver</driverClassName>
		 * <initialSize>5</initialSize> <maxActive>10</maxActive>
		 * <maxIdle>5</maxIdle> <minIdle>5</minIdle> <maxWait>10000</maxWait>
		 * <logAbandoned>true</logAbandoned>
		 * <removeAbandoned>true</removeAbandoned>
		 * <removeAbandonedTimeout>60</removeAbandonedTimeout>
		 * <validationQuery>select now()</validationQuery>
		 * <autoReconnect>true</autoReconnect> <user>wxuser</user>
		 * <password><![CDATA[Mynormal12#]]></password>
		 * <url><![CDATA[jdbc:mysql://127.0.0.1:3306/wx?
		 * generateSimpleParameterMetadata=true&useUnicode=true&
		 * characterEncoding=utf8]]></url> </db>
		 * 
		 */

		BasicDataSource dbcpDataSource = new BasicDataSource();
		String name = c.getString("name");
		log.info("init data source : " + name);
		String driverClassName = c.getString("driverClassName");
		// try {
		// Class.forName(driverClassName);
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		int initialSize = Integer.valueOf(c.getString("initialSize"));
		int maxActive = Integer.valueOf(c.getString("maxActive"));
		int maxIdle = Integer.valueOf(c.getString("maxIdle"));
		int minIdle = Integer.valueOf(c.getString("minIdle"));
		int maxWait = Integer.valueOf(c.getString("maxWait"));
		Boolean logAbandoned = Boolean.valueOf(c.getString("logAbandoned"));
		Boolean removeAbandoned = Boolean.valueOf(c.getString("removeAbandoned"));
		int removeAbandonedTimeout = Integer.valueOf(c.getString("removeAbandonedTimeout"));
		String validationQuery = c.getString("validationQuery");

		String url = c.getString("url");
		String userName = c.getString("user");
		String password = c.getString("password");

		dbcpDataSource.setUrl(url);
		dbcpDataSource.setDriverClassName(driverClassName);
		dbcpDataSource.setUsername(userName);
		dbcpDataSource.setPassword(password);
		dbcpDataSource.setDefaultAutoCommit(true);
		dbcpDataSource.setInitialSize(initialSize);
		dbcpDataSource.setMaxActive(maxActive);
		dbcpDataSource.setMinIdle(minIdle);
		dbcpDataSource.setMaxIdle(maxIdle);
		dbcpDataSource.setMaxWait(maxWait);
		dbcpDataSource.setLogAbandoned(logAbandoned);
		dbcpDataSource.setRemoveAbandoned(removeAbandoned);
		dbcpDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		dbcpDataSource.setValidationQuery(validationQuery);
		DataSource dataSource = (DataSource) dbcpDataSource;

		conns.put(name, dataSource);

	}

	private Map<String, DataSource> conns = new Hashtable<String, DataSource>();

	public Connection getConnection(String dbname) throws SQLException {
		DataSource ds = conns.get(dbname);
		Connection conn = null;
		if (ds != null) {
			conn = ds.getConnection();
		}
		return conn;
	}

	public void reload() {
		closeDatasource();
		instance = null;
	}

	private void closeDatasource() {
		if (conns == null || conns.size() == 0)
			return;
		for (Entry<String, DataSource> entry : conns.entrySet()) {
			DataSource ds = entry.getValue();
			try {
				ds.getClass().getMethod("close").invoke(ds);
			} catch (NoSuchMethodException e) {
				log.error("", e);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("", e);
			}
		}
		conns.clear();
	}
}
