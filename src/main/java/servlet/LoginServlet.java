package servlet;

import entity.Account;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码

        //从请求体中拿到用户名 和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Writer writer = resp.getWriter();

        try{
            String sql = "select id,username,password from account where username = ? and password = ?";
            connection = DBUtil.getConnection(true);//获得数据库连接
            ps = connection.prepareStatement(sql);

            //给两个占位符 ？  赋值
            ps.setString(1,username);
            ps.setString(2,password);

            rs = ps.executeQuery();//查询   返回结果集


            Account user = new Account();

            if (rs.next()) {
                Integer id = rs.getInt("id");//拿到 id 列的数据
                user.setId(id);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }

            //判断是否有这个用户
            if (user.getId() == null) {
                writer.write("<h2> 没有该用户：" + username+ "</h2>");
                resp.sendRedirect("register.html");
            }else if (!password.equals(user.getPassword())) {
                writer.write("<h2> 密码错误：" + username+ "</h2>");
                resp.sendRedirect("login.html");
            }else {

                //session   保存用户信息 用来后续的功能知道是哪个用户
                HttpSession session = req.getSession();
                session.setAttribute("user",user);
                writer.write("<h2> 登陆成功：" + username+ "</h2>");
                System.out.println("登陆成功");
                resp.sendRedirect("index.html");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DBUtil.close(connection,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
