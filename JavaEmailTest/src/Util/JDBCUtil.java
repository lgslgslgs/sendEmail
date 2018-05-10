package Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;



public class JDBCUtil {
	//定义连接数据库相关的成员变量
	private static String className;
	private static String url;
	private static String root;
	private static String password;
	private static DruidDataSource dds=new DruidDataSource();
	/**
	 * 初始化静态成员变量
	 */
	static{
		Properties pt=new Properties();
		InputStream in=null;
		try {
			in=JDBCUtil.class.getResourceAsStream("jdbc.properties");
			pt.load(in);
			className=pt.getProperty("jdbc.className");
			url=pt.getProperty("jdbc.url");
			root=pt.getProperty("jdbc.root");
			password=pt.getProperty("jdbc.password");
			
			
			dds.setDriverClassName(className);
			dds.setUrl(url);
			dds.setUsername(root);
			dds.setPassword(password);
			dds.setMinIdle(10);
			dds.setMaxActive(50);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取数据库连接，返回数据库连接对象
	public static Connection getConnection(){
		try {
			return dds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//关闭数据库连接释放资源
	public static void closeConnection(Connection con,Statement st,ResultSet rs) {
		try {
			if(rs!=null)
				rs.close();
			if(st!=null)
				st.close();
			if(con!=null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过传入的sql语句进行增删改操作
	 * @param sql 传入的sql语句
	 * @param params 传入对应sql语句中 ？ 的参数
	 */
	public static int update(String sql,Object...params){
		Connection con=null;
		PreparedStatement ps=null;
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1,params[i]);
				}
			}
			ps.executeUpdate();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			closeConnection(con,ps,null);
		}
	}
	/**
	 * 根据传入的sql语句及参数查询数据库，并返回单个结果
	 * @param sql sql语句
	 * @param params 传入对应sql语句中 ？ 的参数
	 */
	public static Object query(Class cls,String sql,Object...params){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1,params[i]);
				}
			}
			//获取到结果集
			rs=ps.executeQuery();
			//遍历结果集
			Object obj=null;
			while(rs.next()){
				//获取到反射出的对象创建的实例
				obj=cls.newInstance();
				//获取到对象中的字段数组
				Field[] f=cls.getDeclaredFields();
				//遍历字段数组
				outer:for (Field field : f) {
					//设置字段数组为可见（取消 Java 语言访问检查）
					field.setAccessible(true);
					//获取到遍历出的当前字段名
					String label=field.getName();
					//判断 该字段是否在查询结果中存在
					ResultSetMetaData rsmd = rs.getMetaData();
					//获取总列数
					int colunmCount = rsmd.getColumnCount();
					for(int i=1;i<=colunmCount;i++){
						String columnLabel = rsmd.getColumnLabel(i);
						if(label.equals(columnLabel)){
							//设置对象中对应字段的属性  字段.set(对象名,设置值(rs中获取到的对应标题的值))
							field.set(obj, rs.getObject(label));
							continue outer;
						}
					}
					
				}
			}
			return obj;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally {
			closeConnection(con,ps,rs);
		}
		return null;
	}
	/**
	 * 根据传入的sql语句及参数查询数据库，并返回多个结果放在集合中返回
	 * @param sql sql语句
	 * @param params 传入对应sql语句中 ？ 的参数
	 */
	public static List querys(Class cls,String sql,Object...params){
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			con=getConnection();
			ps=con.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1,params[i]);
				}
			}
			//获取到结果集
			rs=ps.executeQuery();
			//遍历结果集
			List<Object> li=new ArrayList<>();
			Object obj=null;
			while(rs.next()){
				//获取到反射出的对象创建的实例
				obj=cls.newInstance();
				//获取到对象中的字段数组
				Field[] f=cls.getDeclaredFields();
				//遍历字段数组
				outer:for (Field field : f) {
					//设置字段数组为可见（取消 Java 语言访问检查）
					field.setAccessible(true);
					//获取到遍历出的当前字段名
					String label=field.getName();
					
					//判断 该字段是否在查询结果中存在
					ResultSetMetaData rsmd = rs.getMetaData();
					//获取总列数
					int colunmCount = rsmd.getColumnCount();
					for(int i=1;i<=colunmCount;i++){
						String columnLabel = rsmd.getColumnLabel(i);
						if(label.equals(columnLabel)){
							//赋值(设置对象中对应字段的属性)  字段.set(对象名,设置值(rs中获取到的对应标题的值))
							field.set(obj, rs.getObject(label));
							continue outer;
						}
					}
					
				}
				li.add(obj);
			}
			return li;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally {
			closeConnection(con,ps,rs);
		}
		return null;
	}
}
