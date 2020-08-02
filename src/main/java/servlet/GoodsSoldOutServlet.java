package servlet;

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

@WebServlet("/delGoods")
public class GoodsSoldOutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码


        String str = req.getParameter("id");

        Integer id = Integer.valueOf(str);
        System.out.println("id " + id);


        AccountService accountService = new AccountService();
        try {
            int ret = accountService.goodsSoldOut(id);
            if (ret != 0) {
                System.out.println("下架成功 " + id);
            }else {
                System.out.println("下架失败 " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /*Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        Writer writer = resp.getWriter();
        try{
            String sql = "delete from goods where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);


            int ret = ps.executeUpdate();
            if (ret == 0) {
                writer.write("<h2> 删除失败 </h2>");
            }else {
                writer.write("<h2> 删除成功 </h2>");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DBUtil.close(connection,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
}
