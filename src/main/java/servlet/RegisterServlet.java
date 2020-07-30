package servlet;

import entity.Account;
import service.AccountService;
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

        AccountService accountService = new AccountService();

        Account accountRegister = new Account();
        accountRegister.setUsername(username);
        accountRegister.setPassword(password);

        Writer writer = resp.getWriter();
        try {
            int ret = accountService.register(accountRegister);
            if (ret != 0) {
                resp.sendRedirect("login.html");
            }else{
                System.out.println("注册失败");
                writer.write("<h2> 注册失败 </h2>");
                resp.sendRedirect("register.html");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
