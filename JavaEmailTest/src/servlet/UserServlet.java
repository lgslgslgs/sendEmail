package servlet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.sun.mail.util.MailSSLSocketFactory;

import Util.JDBCUtil;
import entity.Cls;
import entity.Login;
import entity.Student;
import entity.User;
import net.sf.json.JSONArray;

public class UserServlet extends BaseServlet{
	//������֤��
	protected void sendCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		 // �ռ��˵�������
	      String to = req.getParameter("email");
	      resp.setCharacterEncoding("utf-8");
	      if(to ==null || to.equals("")) {
	    	  resp.getWriter().write("����������!");
	      }else {
				//��Ϣͷ����
			      //String title=req.getParameter("title");
			      //��Ϣ������
			      //String container=req.getParameter("container");
			      String container = "";  
			      //�ѵ�ǰʱ�����ֵ����һ��α�����
			        Random random = new Random(System.currentTimeMillis());
			        //ѭ��������֤��
			        for(int i = 0; i < 6; i++) {  
			              
			            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
			            //�����ĸ��������  
			            if( "char".equalsIgnoreCase(charOrNum) ) {  
			                //����Ǵ�д��ĸ����Сд��ĸ  
			                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
			                container += (char)(random.nextInt(26) + temp);  
			            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
			            	container += String.valueOf(random.nextInt(10));  
			            }  
			        }  
			      // �����˵�������
			      String from = "961156701@qq.com";
			 
			      // ָ�������ʼ�������Ϊ smtp.qq.com
			      String host = "smtp.qq.com";  //QQ �ʼ�������
			 
			      // ��ȡϵͳ����
			      Properties properties = System.getProperties();
			 
			      // �����ʼ�������
			      properties.setProperty("mail.smtp.host", host);
			 
			      properties.put("mail.smtp.auth", "true");
			      
			      try{
			    	  //����SSL����
			    	  MailSSLSocketFactory sf = new MailSSLSocketFactory();
				      sf.setTrustAllHosts(true);
				      properties.put("mail.smtp.ssl.enable", "true");
				      properties.put("mail.smtp.ssl.socketFactory", sf);
				      
				      // ��ȡĬ��session����
				      Session session = Session.getDefaultInstance(properties,new Authenticator(){
				        public PasswordAuthentication getPasswordAuthentication()
				        {
				         return new PasswordAuthentication("961156701@qq.com", "fxjzerflehqfbcci"); //�������ʼ��û���������
				        }
				       });
			         // ����Ĭ�ϵ� MimeMessage ����
			         MimeMessage message = new MimeMessage(session);
			 
			         // Set From: ͷ��ͷ�ֶ�
			         message.setFrom(new InternetAddress(from));
			 
			         // Set To: ͷ��ͷ�ֶ�
			         message.addRecipient(Message.RecipientType.TO,
			                                  new InternetAddress(to));
			 
			         // Set Subject: ͷ��ͷ�ֶ�
			         message.setSubject("��֤��");
			 
			         // ������Ϣ��
			         message.setText(container.toString());
			 
			         // ������Ϣ
			         Transport.send(message);
			         System.out.println(container);
			         req.getSession().setAttribute("code", container);
			         req.getSession().setAttribute("email", to);
			      }catch (MessagingException mex) {
			         mex.printStackTrace();
			      } catch (GeneralSecurityException e) {
					e.printStackTrace();
			      }
			}
	}
	
	protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String re_password = req.getParameter("re_password");
		String email = req.getParameter("email");
		String code = req.getParameter("code");
		String send_email=(String) req.getSession().getAttribute("email");
		//�ж��������Ϣ�Ƿ���ȷ
		if(!password.equals(re_password)) {
			req.setAttribute("error","�������벻һ��!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(send_email ==null || send_email.equals("")) {
			req.setAttribute("error","���Ȼ�ȡ��֤��!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(!send_email.equals(email)) {
			req.setAttribute("error","�벻Ҫ������֤������!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(!code.equalsIgnoreCase(req.getSession().getAttribute("code").toString())){
			req.setAttribute("error","��֤������!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else {
			//�鿴���ݿ����Ƿ���ڸ��û�
			String query_sql="select * from tb_login where username=?";
			String update_sql="insert into tb_login values(default,?,?,0,0)";
			Object user=null;
			user=JDBCUtil.query(Login.class, query_sql, username);
			System.out.println(user);
			System.out.println(((Login)user));
//			if(user!=null) {
//				req.setAttribute("error","���û��Ѵ���!");
//				req.getRequestDispatcher("/login.jsp").forward(req, resp);
//			}
//				//ע���û�,�������ݵ����ݿ�
//				user=JDBCUtil.update(update_sql,username,password);
//				if((int)user==0) {
//					req.setAttribute("error","ע��ʧ��!");
//				}else{
//					req.setAttribute("error","ע��ɹ�!");
//				}
//				req.getRequestDispatcher("/login.jsp").forward(req, resp);
//			
		}
	}
	
	//��¼
	protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		Object user=JDBCUtil.query(User.class, "select * from test_user where username=? and password=?",username,password);
		if(user!=null) {
			req.setCharacterEncoding("utf-8");
			System.out.println("user");
			req.setAttribute("user", (User)user);
			List<Cls> cls=JDBCUtil.querys(Cls.class, "select * from test_class");
			req.setAttribute("cls",cls);
			req.getRequestDispatcher("/main.jsp").forward(req, resp);
		}else {
			req.setAttribute("error", "��¼ʧ��!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}
	
	//��ѯ��������Ӧid��ѧ����Ϣ
	protected void queryStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid=req.getParameter("cid");
		List<Student> list=JDBCUtil.querys(Student.class, "select ts.*,tc.cname from test_student ts join test_class tc on ts.s_cid=tc.cid where s_cid=?", cid);
		JSONArray json=JSONArray.fromObject(list);
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(json.toString());
	}
}
