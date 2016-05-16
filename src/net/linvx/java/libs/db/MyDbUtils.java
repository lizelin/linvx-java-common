package net.linvx.java.libs.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import net.linvx.java.libs.enhance.MyReflectCache;
import net.linvx.java.libs.utils.MyStringUtils;

public abstract class MyDbUtils {
	public static <T> T getOne(Connection conn, String sql) throws Exception {
		return MyDbUtils.getOne(conn, sql, null);
	}

	/**
	 * 获取sql执行结果的第一条记录的第一个值
	 * 
	 * @param conn
	 * @param sql
	 * @param vs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getOne(Connection conn, String sql, Object[] vs) throws Exception {
		PreparedStatement pst = null;
		ResultSet rs = null;
		T ret = null;
		try {
			pst = conn.prepareStatement(sql);
			MyDbUtils.setSqlParams(pst, vs);
			rs = pst.executeQuery();
			if (rs.next()) {
				ret = (T) rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			MyDbUtils.closeResultSet(rs);
			MyDbUtils.closePreparedStatement(pst);
		}
		return ret;
	}

	/**
	 * 生成BO
	 * 
	 * @param db
	 * @param table
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static String genBO(Connection db, String table, String packageName, String className) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbmethod = new StringBuffer();
		String fieldFormat = "\tprivate %s %s;\n";
		String nmethodFormat = "\tpublic %s get%s() {\n\t\treturn %s;\n\t}\n\tpublic %s set%s(%s p) {\n\t\tthis.%s = p;\n\t\treturn this;\n\t}\n";
		String bmethodFormat = "\tpublic %s is%s() {\n\t\treturn %s;\n\t}\n\tpublic %s set%s(%s p) {\n\t\tthis.%s = p;\n\t\treturn this;\n\t}\n";
		sb.append("package " + packageName + ";\n");
		sb.append("\n");
		sb.append("public class " + className + " extends net.linvx.java.libs.enhance.BaseBean {\n");
		String sql = "select * from " + table + " where 1=0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = db.prepareStatement(sql);
			rs = pstmt.executeQuery();
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String colName = rsmd.getColumnName(i + 1);
				// String colType = rsmd.getColumnTypeName(i+1);
				String colClass = rsmd.getColumnClassName(i + 1);
				sb.append(String.format(fieldFormat, colClass, colName));
				if (colClass.equals("java.lang.Boolean") || colClass.equals("boolean"))
					sbmethod.append(String.format(bmethodFormat, colClass, MyStringUtils.upperFirstLetter(colName),
							colName, className, MyStringUtils.upperFirstLetter(colName), colClass, colName));
				else
					sbmethod.append(String.format(nmethodFormat, colClass, MyStringUtils.upperFirstLetter(colName),
							colName, className, MyStringUtils.upperFirstLetter(colName), colClass, colName));

				// sb.append(" private "+ colClass +" "+colName+";\n");
				// sbmetod.append(" public "+colClass+
				// "get"+MyStringUtils.upperFirstLetter(colName) + "() {\n")
				// .append(" return " + colName + ";");
			}
			sb.append("\n");
			sb.append(sbmethod.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MyDbUtils.closeResultSet(rs);
			MyDbUtils.closePreparedStatement(pstmt);
		}

		sb.append("}\n");

		return sb.toString();
	}

	/**
	 * 执行update或者insert
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 * @return 更新的记录数，-1代表失败
	 */
	public static int update(Connection conn, String sql) throws Exception {
		return MyDbUtils.update(conn, sql, null);
	}

	/**
	 * 执行update或者insert
	 * 
	 * @param conn
	 * @param sql
	 * @param vs
	 *            参数数组
	 * @throws Exception
	 * @return 更新的记录数，-1代表失败
	 */
	public static int update(Connection conn, String sql, Object[] vs) throws Exception {
		PreparedStatement pst = null;
		int ret = -1;
		try {
			pst = conn.prepareStatement(sql);
			MyDbUtils.setSqlParams(pst, vs);
			ret = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			MyDbUtils.closePreparedStatement(pst);
		}
		return ret;
	}

	/**
	 * 执行insert语句,返回主键
	 * 
	 * @param conn
	 * @param sql
	 * @throws Exception
	 * @return 插入的主键，-1代表未插入成功，或者主键不是int
	 */
	public static int insertReturnKey(Connection conn, String sql) throws Exception {
		return insertReturnKey(conn, sql, null);
	}

	/**
	 * 执行insert语句,返回主键
	 * 
	 * @param conn
	 * @param sql
	 * @param vs
	 *            参数数组
	 * @throws Exception
	 * @return 插入的主键，-1代表未插入成功，或者主键不是int
	 */
	public static int insertReturnKey(Connection conn, String sql, Object[] vs) throws Exception {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int ret = -1;
		try {
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			MyDbUtils.setSqlParams(pst, vs);
			int r = pst.executeUpdate();
			if (r > 0) {
				rs = pst.getGeneratedKeys();
				if (rs != null && rs.next())
					ret = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			MyDbUtils.closeResultSet(rs);
			MyDbUtils.closePreparedStatement(pst);
		}
		return ret;
	}

	/**
	 * 关闭数据库
	 * 
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		if (conn == null)
			return;
		try {
			if (!conn.isClosed()) {
				if (!conn.getAutoCommit())
					conn.rollback();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}

	/**
	 * 关闭PreparedStatement
	 * 
	 * @param pstmt
	 */
	public static void closePreparedStatement(PreparedStatement pstmt) {
		if (pstmt == null)
			return;
		try {
			if (!pstmt.isClosed())
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pstmt = null;
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = null;
	}

	/**
	 * 关闭CallableStatement
	 * 
	 * @param cs
	 */
	public static void closeCallableStatement(CallableStatement cs) {
		if (cs == null)
			return;
		try {
			if (!cs.isClosed())
				cs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cs = null;
	}

	/**
	 * 获取行数据
	 * 
	 * @param sql
	 * @param conn
	 * @param params
	 * @param clazz
	 * @return
	 * @throws java.sql.SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws java.lang.reflect.InvocationTargetException
	 */
	public static <T> T getRow(java.sql.Connection conn, String sql, Object[] params, Class<T> clazz)
			throws java.sql.SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			java.lang.reflect.InvocationTargetException {
		java.sql.PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		java.sql.ResultSetMetaData rsmd = null;
		T resultObject = null;
		try {
			ps = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				int index = 1;
				for (Object obj : params) {
					ps.setObject(index++, obj);
				}
			}

			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int colNums = rsmd.getColumnCount();

			if (rs.next()) {
				resultObject = clazz.newInstance();
				for (int i = 0; i < colNums; i++) {
					String colName = rsmd.getColumnName(i + 1);
					Object colValue = rs.getObject(colName);
					java.lang.reflect.Field field;
					try {
						field = MyReflectCache.getInstance().getReflectField(clazz, colName);
						if (field == null) {
							field = clazz.getDeclaredField(colName);
						}
						field.set(resultObject, colValue);
					} catch (Exception e) {

					}

				}
			}
		} finally {
			MyDbUtils.closeResultSet(rs);
			MyDbUtils.closePreparedStatement(ps);
		}

		return resultObject;
	}

	/**
	 * 获取所有行数据
	 * 
	 * @param sql
	 * @param conn
	 * @param params
	 * @param clazz
	 * @return
	 * @throws java.sql.SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws java.lang.reflect.InvocationTargetException
	 */
	public static <T> List<T> getAll(java.sql.Connection conn, String sql, Object[] params, Class<T> clazz)
			throws java.sql.SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			java.lang.reflect.InvocationTargetException {
		java.sql.PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		java.sql.ResultSetMetaData rsmd = null;
		T resultObject = null;
		List<T> resultObjects = new ArrayList<T>();
		try {
			ps = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				int index = 1;
				for (Object obj : params) {
					ps.setObject(index++, obj);
				}
			}

			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int colNums = rsmd.getColumnCount();
			Map<String, java.lang.reflect.Field> fs = new HashMap<String, java.lang.reflect.Field>();
			for (int k = 0; k < colNums; k++) {
				try {
					java.lang.reflect.Field field = MyReflectCache.getInstance().getReflectField(clazz,
							rsmd.getColumnName(k + 1));
					if (field == null)
						field = clazz.getDeclaredField(rsmd.getColumnName(k + 1));
					fs.put(rsmd.getColumnName(k + 1), field);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			while (rs.next()) {
				resultObject = clazz.newInstance();
				for (int i = 0; i < colNums; i++) {
					String colName = rsmd.getColumnName(i + 1);
					Object colValue = rs.getObject(colName);
					java.lang.reflect.Field field;
					try {
						field = fs.get(colName);
						field.setAccessible(true);
						field.set(resultObject, colValue);
					} catch (Exception e) {

					}
				}
				resultObjects.add(resultObject);
			}
		} finally {
			MyDbUtils.closeResultSet(rs);
			MyDbUtils.closePreparedStatement(ps);
		}

		return resultObjects;
	}

	/**
	 * 设置参数：注意，未完全覆盖所有数据库类型
	 * 
	 * @param pst
	 * @param vs
	 * @throws Exception
	 */
	private static void setSqlParams(PreparedStatement pst, Object[] vs) throws Exception {
		if (vs == null)
			return;
		for (int i = 0; i < vs.length; i++) {
			pst.setObject(i + 1, vs[i]);
		}
	}

	public static void main(String[] args) {

//		class TestClass {
//			public long numCount;
//			public String vc2Count;
//		}
//		List<TestClass> c = null;
		Connection db = null;
		try {
			MySqlDbConn con = new MySqlDbConn();
			con.setMysqlDs("").setMysqlUrl("jdbc:mysql://127.0.0.1:3306/wx?generateSimpleParameterMetadata=true")
					.setMysqlPassword("Mynormal12#").setMysqlUser("wxuser");
			db = con.getMysqlConnection();
			String a = MyDbUtils.genBO(db, "wx_received_msg", "net.linvx.java.wx.bo", "BoReceivedMsg");
			System.out.println(a);
			// c = MyDbUtils.getAll(db,
			// "select count(*) as numCount, Cast(count(*) as char) as vc2Count
			// from wx_official_account_info where numAccountId=?",
			// new Object[] { new Integer(1) }, TestClass.class);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyDbUtils.closeConn(db);
		}
		// System.out.println(c.get(0).numCount + " " + c.get(0).vc2Count);
	}

}
