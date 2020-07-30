package servlet;

import entity.Account;
import service.AccountService;
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

        AccountService accountService = new AccountService();
        Account accountLogin = new Account();

        accountLogin.setUsername(username);
        accountLogin.setPassword(password);

        System.out.println(accountLogin);

        try {
            Account account = accountService.login(accountLogin);
            System.out.println(account);
            if (account != null) {
                //session   保存用户信息 用来后续的功能知道是哪个用户
                HttpSession session = req.getSession();
                session.setAttribute("account",account);
                resp.sendRedirect("index.html");
                System.out.println("登陆成功: " + account);
            }else {
                resp.sendRedirect("login.html");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
