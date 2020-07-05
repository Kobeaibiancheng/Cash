package servlet;

import commen.OrderStatus;
import entity.Account;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/pay")
public class ReadyBuyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");//设置响应体返回  剋是text/html   及编码

        String goodsIDAndNum = req.getParameter("goodsIDAndNum");
        //1-8,3-2

        //因为买的货物可以是多个   需要将货物Goods保存
        List<Goods> goodsList = new ArrayList<Goods>();


        Writer writer = resp.getWriter();

        String[] strings1 = goodsIDAndNum.split(",");
        for (String s : strings1) {
            String[] strings2 = s.split("-");
            Goods goods = getGoods(Integer.valueOf(strings2[0]));
            if (goods != null) {
                if (Integer.valueOf(strings2[1]) > goods.getStock()){
                    System.out.println("商品数量不足 " + strings2[0]);
                    writer.write("<h2> 所购商品不足 "+ strings2[1] + "</h2>" + "\n购买失败");
                    return;
                }else {
                    goods.setBuyGoodsNum(Integer.valueOf(strings2[1]));
                    goodsList.add(goods);
                }
            }else {
                writer.write("<h2>没有该商品" + strings2[0] + "</h2>");
                System.out.println("没有该商品 " + strings2[0]);
            }
        }
        System.out.println("购买商品列表");
        System.out.println(goodsList);

        //创建订单
        Order order = new Order();
        order.setId(String.valueOf(System.currentTimeMillis()));//订单id时间戳
        HttpSession session = req.getSession();
        Account account = (Account)session.getAttribute("user");
        order.setAccount_id(account.getId());
        order.setAccount_name(account.getUsername());


        //订单创建时间
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        order.setCreate_time(LocalDateTime.now().format(formatter));*/

        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
        );
        String createTime = format.format(date);
        order.setCreate_time(createTime);

        int totalMoney = 0;
        int actualMoney = 0;
        //遍历goodsList中的所够商品
        for (Goods goods : goodsList) {
            //每一个商品就是一个订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder_id(order.getId());

            orderItem.setGoods_id(goods.getId());
            orderItem.setGoods_name(goods.getName());
            orderItem.setGoods_introduce(goods.getIntroduce());
            orderItem.setGoods_num(goods.getBuyGoodsNum());
            orderItem.setGoods_unit(goods.getUnit());
            orderItem.setGoods_price(goods.getPriceInt());
            orderItem.setGoods_discount(goods.getDiscount());

            order.orderItemList.add(orderItem);

            //currentMoney  代表每种商品的钱
            int currentMoney = goods.getBuyGoodsNum() * goods.getPriceInt();
            //totalMoney   总共的钱
            totalMoney += currentMoney;
            //每种商品实际的钱
            actualMoney += currentMoney * goods.getDiscount()/100;
        }
        order.setTotal_money(totalMoney);
        order.setActual_amount(actualMoney);
        order.setOrder_status(OrderStatus.PLAYING);

        System.out.println("订单表： ");
        System.out.println(order);

        HttpSession sessionOrder = req.getSession();
        session.setAttribute("order",order);

        HttpSession sessionGoodsList = req.getSession();
        sessionGoodsList.setAttribute("goodsList",goodsList);


        //如果是跳转到另一个网页的话，对应的数据不好拿到，所以在这里直接进行打印网页
        //通过响应体对前端传入数据。
        resp.getWriter().println("<html>");
        resp.getWriter().println("<p>"+"【用户名称】:"+order.getAccount_name()+"</p>");
        resp.getWriter().println("<p>"+"【订单编号】:"+order.getId()+"</p>");
        resp.getWriter().println("<p>"+"【订单状态】:"+order.getOrder_status().getDesc()+"</p>");
        resp.getWriter().println("<p>"+"【创建时间】:"+order.getCreate_time()+"</p>");

        resp.getWriter().println("<p>"+"编号  "+"名称   "+"数量  "+"单位  "+"单价（元）   "+"</p>");
        resp.getWriter().println("<ol>");
        for (OrderItem orderItem  : order.orderItemList) {
            resp.getWriter().println("<li>" + orderItem.getGoods_name() +" " + orderItem.getGoods_num()+ " "+
                    orderItem.getGoods_unit()+" " + orderItem.getGoods_price()+"</li>");
        }
        resp.getWriter().println("</ol>");
        resp.getWriter().println("<p>"+"【总金额】:"+order.getTotal_money() +"</p>");
        resp.getWriter().println("<p>"+"【优惠金额】:"+order.getDiscount() +"</p>");
        resp.getWriter().println("<p>"+"【应支付金额】:"+order.getActual_amount() +"</p>");
        //这个标签<a href = > 只会以get方式请求，所以buyGoodsServlet的 doGet方法
        resp.getWriter().println("<a href=\"BuyGoodsServlet\">确认</a>");
        //resp.getWriter().println("<form action=\"buyGoodsServlet\" method=\"post\"><button type=\"submit\">确认</button></form>");
        resp.getWriter().println("<a href= \"index.html\">取消</a>");
        resp.getWriter().println("</html>");
    }





    public Goods getGoods(int goodsID) {
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
            try {
                DBUtil.close(connection,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return goods;
    }
}
