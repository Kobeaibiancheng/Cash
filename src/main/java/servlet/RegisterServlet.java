package servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("注册");
        req.setCharacterEncoding("UTF-8");

        //响应体  响应给客户端的可以是 text  也可以是 html
        resp.setContentType("text/html; charset=utf-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "insert into account(username,password) values(?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            ps.setString(1,username);//下标从1开始  对第一个 ? 赋值
            ps.setString(2,password);//             对第二个 ? 赋值

            Writer writer = resp.getWriter();//响应体流    可以往前端页面写东西

            /**
             * 缺少查询用户名是否重复
             */

            int ret = ps.executeUpdate();//返回0表示  插入失败
            if (ret == 0) {
                System.out.println("注册失败");
                //writer.write("<h2>注册失败</h2>");
                resp.sendRedirect("register.html");//响应体流    sendRedirect可以跳转到其他页面

            }else {
                System.out.println("注册成功");
                //writer.write("<h2>注册成功</h2>");
                resp.sendRedirect("login.html");//响应体流    sendRedirect可以跳转到其他页面
            }



        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtil.close(connection,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
