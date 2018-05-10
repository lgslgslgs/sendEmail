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
	//发送验证码
	protected void sendCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		 // 收件人电子邮箱
	      String to = req.getParameter("email");
	      resp.setCharacterEncoding("utf-8");
	      if(to ==null || to.equals("")) {
	    	  resp.getWriter().write("请输入邮箱!");
	      }else {
				//消息头内容
			      //String title=req.getParameter("title");
			      //消息体内容
			      //String container=req.getParameter("container");
			      String container = "";  
			      //已当前时间毫秒值生成一个伪随机数
			        Random random = new Random(System.currentTimeMillis());
			        //循环生成验证码
			        for(int i = 0; i < 6; i++) {  
			              
			            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
			            //输出字母还是数字  
			            if( "char".equalsIgnoreCase(charOrNum) ) {  
			                //输出是大写字母还是小写字母  
			                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
			                container += (char)(random.nextInt(26) + temp);  
			            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
			            	container += String.valueOf(random.nextInt(10));  
			            }  
			        }  
			      // 发件人电子邮箱
			      String from = "961156701@qq.com";
			 
			      // 指定发送邮件的主机为 smtp.qq.com
			      String host = "smtp.qq.com";  //QQ 邮件服务器
			 
			      // 获取系统属性
			      Properties properties = System.getProperties();
			 
			      // 设置邮件服务器
			      properties.setProperty("mail.smtp.host", host);
			 
			      properties.put("mail.smtp.auth", "true");
			      
			      try{
			    	  //设置SSL加密
			    	  MailSSLSocketFactory sf = new MailSSLSocketFactory();
				      sf.setTrustAllHosts(true);
				      properties.put("mail.smtp.ssl.enable", "true");
				      properties.put("mail.smtp.ssl.socketFactory", sf);
				      
				      // 获取默认session对象
				      Session session = Session.getDefaultInstance(properties,new Authenticator(){
				        public PasswordAuthentication getPasswordAuthentication()
				        {
				         return new PasswordAuthentication("961156701@qq.com", "fxjzerflehqfbcci"); //发件人邮件用户名、密码
				        }
				       });
			         // 创建默认的 MimeMessage 对象
			         MimeMessage message = new MimeMessage(session);
			 
			         // Set From: 头部头字段
			         message.setFrom(new InternetAddress(from));
			 
			         // Set To: 头部头字段
			         message.addRecipient(Message.RecipientType.TO,
			                                  new InternetAddress(to));
			 
			         // Set Subject: 头部头字段
			         message.setSubject("验证码");
			 
			         // 设置消息体
			         message.setText(container.toString());
			 
			         // 发送消息
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
		//判断输入的信息是否正确
		if(!password.equals(re_password)) {
			req.setAttribute("error","两次密码不一致!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(send_email ==null || send_email.equals("")) {
			req.setAttribute("error","请先获取验证码!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(!send_email.equals(email)) {
			req.setAttribute("error","请不要更改验证的邮箱!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else if(!code.equalsIgnoreCase(req.getSession().getAttribute("code").toString())){
			req.setAttribute("error","验证码有误!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}else {
			//查看数据库中是否存在该用户
			String query_sql="select * from tb_login where username=?";
			String update_sql="insert into tb_login values(default,?,?,0,0)";
			Object user=null;
			user=JDBCUtil.query(Login.class, query_sql, username);
			System.out.println(user);
			System.out.println(((Login)user));
//			if(user!=null) {
//				req.setAttribute("error","该用户已存在!");
//				req.getRequestDispatcher("/login.jsp").forward(req, resp);
//			}
//				//注册用户,保存数据到数据库
//				user=JDBCUtil.update(update_sql,username,password);
//				if((int)user==0) {
//					req.setAttribute("error","注册失败!");
//				}else{
//					req.setAttribute("error","注册成功!");
//				}
//				req.getRequestDispatcher("/login.jsp").forward(req, resp);
//			
		}
	}
	
	//登录
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
			req.setAttribute("error", "登录失败!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}
	
	//查询请求中相应id的学生信息
	protected void queryStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid=req.getParameter("cid");
		List<Student> list=JDBCUtil.querys(Student.class, "select ts.*,tc.cname from test_student ts join test_class tc on ts.s_cid=tc.cid where s_cid=?", cid);
		JSONArray json=JSONArray.fromObject(list);
		resp.setCharacterEncoding("utf-8");
		resp.getWriter().write(json.toString());
	}
}
