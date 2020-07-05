package servlet;

import entity.Goods;
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

@WebServlet("/updategoods")
public class GoodsUpdateServlet extends HttpServlet {

   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码

        String goodsIDString = req.getParameter("goodsID");
        int goodsID = Integer.valueOf(goodsIDString);

        String name = req.getParameter("name");
        String stock = req.getParameter("stock");
        String introduce = req.getParameter("introduce");
        String unit = req.getParameter("unit");
        String price = req.getParameter("price");
        String discount = req.getParameter("discount");


        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(100*doublePrice).intValue();

        Writer writer = resp.getWriter();


        //1.看是否存在   goodsID这样的商品
        // 如果有   拿到这条数据
        try {
           Goods goods = getGoods(goodsID);
           if (goods != null) {
               //要更新的商品
               goods.setName(name);
               goods.setStock(Integer.valueOf(stock));
               goods.setUnit(unit);
               goods.setIntroduce(introduce);
               goods.setPrice(realPrice);
               goods.setDiscount(Integer.valueOf(discount));

               //将新的数据写回到数据库
               boolean flag = modify(goods);


               if (flag) {
                   writer.write("<h2>商品更新成功</h2>");
                   resp.sendRedirect("goodsbrowse.html");
               }else {
                   writer.write("<h2>商品更新失败</h2>");
               }
           }else {
               writer.write("<h2>没有该商品" + goodsID + "</h2>");
               System.out.println("没有该商品");
           }
        } catch (SQLException e) {
           e.printStackTrace();
        }




    }

    //更新改进
    private boolean modify(Goods goods) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "update goods set name=?,introduce=?,stock=?,unit=?,price=?,discount=? where id=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            ps.setString(1,goods.getName());
            ps.setString(2,goods.getIntroduce());
            ps.setInt(3,goods.getStock());
            ps.setString(4,goods.getUnit());
            ps.setInt(5,goods.getPriceInt());
            ps.setInt(6,goods.getDiscount());
            ps.setInt(7,goods.getId());

            int ret = ps.executeUpdate();
            if (ret == 0) {
                return false;
            }else {
                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,rs);
        }
        return false;
    }

    //功能：找到goodsID
    public Goods getGoods(int goodsID) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Goods goods = null;
        try{
            String sql = "select * from goods where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            ps.setInt(1,goodsID);

            rs = ps.executeQuery();
            if (rs.next()) {
                goods = new Goods();
                goods.setId(rs.getInt("id"));
                goods.setName(rs.getString("name"));
                goods.setIntroduce(rs.getString("introduce"));
                goods.setStock(rs.getInt("stock"));
                goods.setUnit(rs.getString("unit"));
                goods.setPrice(rs.getInt("price"));
                goods.setDiscount(rs.getInt("discount"));
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,ps,rs);
        }
        return goods;
    }
}
