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
	//�����������ݿ���صĳ�Ա����
	private static String className;
	private static String url;
	private static String root;
	private static String password;
	private static DruidDataSource dds=new DruidDataSource();
	/**
	 * ��ʼ����̬��Ա����
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
	//��ȡ���ݿ����ӣ��������ݿ����Ӷ���
	public static Connection getConnection(){
		try {
			return dds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//�ر����ݿ������ͷ���Դ
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
	 * ͨ�������sql��������ɾ�Ĳ���
	 * @param sql �����sql���
	 * @param params �����Ӧsql����� �� �Ĳ���
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
	 * ���ݴ����sql��估������ѯ���ݿ⣬�����ص������
	 * @param sql sql���
	 * @param params �����Ӧsql����� �� �Ĳ���
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
			//��ȡ�������
			rs=ps.executeQuery();
			//���������
			Object obj=null;
			while(rs.next()){
				//��ȡ��������Ķ��󴴽���ʵ��
				obj=cls.newInstance();
				//��ȡ�������е��ֶ�����
				Field[] f=cls.getDeclaredFields();
				//�����ֶ�����
				outer:for (Field field : f) {
					//�����ֶ�����Ϊ�ɼ���ȡ�� Java ���Է��ʼ�飩
					field.setAccessible(true);
					//��ȡ���������ĵ�ǰ�ֶ���
					String label=field.getName();
					//�ж� ���ֶ��Ƿ��ڲ�ѯ����д���
					ResultSetMetaData rsmd = rs.getMetaData();
					//��ȡ������
					int colunmCount = rsmd.getColumnCount();
					for(int i=1;i<=colunmCount;i++){
						String columnLabel = rsmd.getColumnLabel(i);
						if(label.equals(columnLabel)){
							//���ö����ж�Ӧ�ֶε�����  �ֶ�.set(������,����ֵ(rs�л�ȡ���Ķ�Ӧ�����ֵ))
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
	 * ���ݴ����sql��估������ѯ���ݿ⣬�����ض��������ڼ����з���
	 * @param sql sql���
	 * @param params �����Ӧsql����� �� �Ĳ���
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
			//��ȡ�������
			rs=ps.executeQuery();
			//���������
			List<Object> li=new ArrayList<>();
			Object obj=null;
			while(rs.next()){
				//��ȡ��������Ķ��󴴽���ʵ��
				obj=cls.newInstance();
				//��ȡ�������е��ֶ�����
				Field[] f=cls.getDeclaredFields();
				//�����ֶ�����
				outer:for (Field field : f) {
					//�����ֶ�����Ϊ�ɼ���ȡ�� Java ���Է��ʼ�飩
					field.setAccessible(true);
					//��ȡ���������ĵ�ǰ�ֶ���
					String label=field.getName();
					
					//�ж� ���ֶ��Ƿ��ڲ�ѯ����д���
					ResultSetMetaData rsmd = rs.getMetaData();
					//��ȡ������
					int colunmCount = rsmd.getColumnCount();
					for(int i=1;i<=colunmCount;i++){
						String columnLabel = rsmd.getColumnLabel(i);
						if(label.equals(columnLabel)){
							//��ֵ(���ö����ж�Ӧ�ֶε�����)  �ֶ�.set(������,����ֵ(rs�л�ȡ���Ķ�Ӧ�����ֵ))
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
