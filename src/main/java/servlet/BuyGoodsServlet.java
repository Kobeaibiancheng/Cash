package servlet;

import commen.OrderStatus;
import dao.AccountDao;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
import lombok.SneakyThrows;
import service.AccountService;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/buyGoodsServlet")
public class BuyGoodsServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码

        HttpSession session = req.getSession();
        Order order = (Order) session.getAttribute("order");


        List<Goods> goodsList = (List<Goods>) session.getAttribute("goodsList");
        //将订单内的数据写入数据库当中
        order.setOrder_status(OrderStatus.OK);//订单状态

        /*//订单完成时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        order.setFinish_time(LocalDateTime.now().format(formatter));*/

        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
        );
        String finishTime = format.format(date);
        order.setFinish_time(finishTime);


        System.out.println(goodsList);
        AccountService accountService = new AccountService();
        boolean flag = accountService.commitOder(order);
        //boolean flag = commitOder(order);

        if (flag) {
            //更新库存
            for ( Goods goods : goodsList) {
                boolean isUpdate = accountService.updateAfterPay(goods,goods.getBuyGoodsNum());
                if(isUpdate) {
                    System.out.println("更新库存成功！");
                }else {
                    System.out.println("更新库存失败");
                    return;
                }
            }
        }else {
            System.out.println("插入失败");
            return;
        }
        resp.sendRedirect("buyGoodsSuccess.html");
    }

    /*private boolean updateAfterPay(Goods goods, Integer buyGoodsNum) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            String sql = "update goods set stock = ? where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1,goods.getStock()-buyGoodsNum);
            ps.setInt(2,goods.getId());

            if (ps.executeUpdate() == 0) {
                return false;
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
        return true;
    }*/

    /*private boolean commitOder(Order order) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String insertOrderSql = "insert into `order` (id, account_id,create_time, finish_time,actual_amount,total_money,order_status,account_name) " +
                    "values (?,?,?,?,?,?,?,?)";
            connection = DBUtil.getConnection(false);
            ps = connection.prepareStatement(insertOrderSql);

            *//*ps.setString(1,order.getId());
            ps.setInt(2,order.getAccount_id());
            ps.setInt(3,order.getActual_amountInt());
            ps.setInt(4,order.getTotal_moneyInt());
            ps.setInt(5,order.getOrder_status().getFlag());
            ps.setString(6,order.getAccount_name());*//*
            ps.setString(1,order.getId());
            ps.setInt(2,order.getAccount_id());
            ps.setString(3,order.getCreate_time());
            ps.setString(4,order.getFinish_time());
            ps.setInt(5,order.getActual_amountInt());
            ps.setInt(6,order.getTotal_moneyInt());
            ps.setInt(7,order.getOrder_status().getFlag());
            ps.setString(8,order.getAccount_name());

            int ret = ps.executeUpdate();
            if (ret == 0) {
                throw new RuntimeException("插入订单失败！");
            }

            String insertOrderItemSql = "insert into order_item(order_id, goods_id, goods_name,goods_introduce, " +
                    "goods_num, goods_unit, goods_price, goods_discount) values (?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(insertOrderItemSql);

            for (OrderItem orderItem : order.getOrderItemList()) {
                ps.setString(1,orderItem.getOrder_id());
                ps.setInt(2,orderItem.getGoods_id());
                ps.setString(3,orderItem.getGoods_name());
                ps.setString(4,orderItem.getGoods_introduce());
                ps.setInt(5,orderItem.getGoods_num());
                ps.setString(6,orderItem.getGoods_unit());
                ps.setInt(7,orderItem.getGoods_priceInt());
                ps.setInt(8,orderItem.getGoods_discount());
                ps.addBatch();//   缓存
            }

            int[] effect = ps.executeBatch();//批量插入
            for (int i : effect) {
                if (i == 0) {
                    throw new RuntimeException("插入订单明细失败！");
                }
            }
            //批量插入没有发生任何的异常
            connection.commit();
        }catch (Exception e) {
            e.printStackTrace();
            //判断链接是否断开
            if(connection != null) {
                try {
                    //回滚
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            try {
                DBUtil.close(connection,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }*/
}
