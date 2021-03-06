package servlet;

import entity.Goods;
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


@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码

        //从请求体中拿到用户名 和密码
        String name = req.getParameter("name");
        String stock = req.getParameter("stock");
        String introduce = req.getParameter("introduce");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");

        double doublePrice = Double.valueOf(price);//将字符串转为小数
        int realPrice = new Double(100*doublePrice).intValue();//转为整数


        String discount = req.getParameter("discount");

        Goods addGoods = new Goods();
        addGoods.setName(name);
        addGoods.setStock(Integer.valueOf(stock));
        addGoods.setUnit(unit);
        addGoods.setPrice(realPrice);
        addGoods.setDiscount(Integer.valueOf(discount));
        addGoods.setIntroduce(introduce);
        //后端打印添加商品信息
        System.out.println(addGoods);

        Writer writer = resp.getWriter();
        AccountService accountService = new AccountService();
        try {
            int ret = accountService.goodsPutAway(addGoods);
            if (ret != 0) {
                writer.write("<h2> 商品上架成功 </h2>");
                resp.sendRedirect("index.html");
            }else {
                writer.write("<h2> 商品上架失败 </h2>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Writer writer = resp.getWriter();
        try{
            String sql = "insert into goods(name,introduce,stock,unit,price,discount) values (?,?,?,?,?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            //给 ? 赋值
            ps.setString(1,name);
            ps.setString(2,introduce);
            ps.setInt(3,Integer.valueOf(stock));
            ps.setString(4,unit);
            ps.setInt(5,realPrice);
            ps.setInt(6,Integer.valueOf(discount));

            int ret = ps.executeUpdate();
            if (ret == 0) {
                writer.write("<h2> 商品上架失败 </h2>");
            }else {
                writer.write("<h2> 商品上架成功 </h2>");
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
        }*/
    }
}
