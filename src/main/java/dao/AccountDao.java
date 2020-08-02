package dao;

import entity.Account;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    /**
     * 登陆
     * @param loginAccount
     * @return
     */
    public Account login(Account loginAccount) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Account account = new Account();
        try{
            String sql = "select id,username,password from account where username = ? and password = ?";
            connection = DBUtil.getConnection(true);//获得数据库连接
            ps = connection.prepareStatement(sql);

            String username = loginAccount.getUsername();
            String password = loginAccount.getPassword();

            //给两个占位符 ？  赋值
            ps.setString(1,username);
            ps.setString(2,password);

            rs = ps.executeQuery();


            if (rs.next()) {
                Integer id = rs.getInt("id");//拿到 id 列的数据
                account.setId(id);
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                return account;
            }else {
                return null;
            }
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }

    /**
     * 注册
     * @param registerAccount
     * @return
     */
    public int register(Account registerAccount) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        String username = registerAccount.getUsername();
        String password = registerAccount.getPassword();

        try {
            String sql = "select * from account where username = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();
            if (!rs.next()) {
                sql = "insert into account(username,password) values(?,?)";
                connection = DBUtil.getConnection(true);
                ps = connection.prepareStatement(sql);


                ps.setString(1, username);//下标从1开始  对第一个 ? 赋值
                ps.setString(2, password);//             对第二个 ? 赋值
                int ret = ps.executeUpdate();
                return ret;
            }else {
                return 0;
            }
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }

    /**
     * 浏览商品
     * @return
     * @throws SQLException
     */
    public List<Goods> goodsBrowse() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Goods> goodsList = new ArrayList<>();
        try{
            String sql = "select * from goods";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                Goods goods = new Goods();
                goods.setId(rs.getInt("id"));
                goods.setName(rs.getString("name"));
                goods.setIntroduce(rs.getString("introduce"));
                goods.setStock(rs.getInt("stock"));
                goods.setUnit(rs.getString("unit"));
                goods.setPrice(rs.getInt("price"));
                goods.setDiscount(rs.getInt("discount"));
                goodsList.add(goods);
            }
            //System.out.println(goodsList);
            return goodsList;
        }finally {
            DBUtil.close(connection,ps,rs);
        }

    }


    /**
     * 添加商品
     * @param addGoods
     * @return
     */
    public int goodsPutAway(Goods addGoods) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = "insert into goods(name,introduce,stock,unit,price,discount) values (?,?,?,?,?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);


            String name = addGoods.getName();
            String introduce = addGoods.getIntroduce();
            Integer stock = addGoods.getStock();
            String unit = addGoods.getUnit();
            Integer realPrice = addGoods.getPriceInt();
            Integer discount = addGoods.getDiscount();


            //给 ? 赋值
            ps.setString(1,name);
            ps.setString(2,introduce);
            ps.setInt(3,stock);
            ps.setString(4,unit);
            ps.setInt(5,realPrice);
            ps.setInt(6,discount);

            int ret = ps.executeUpdate();
            return ret;
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }


    /**
     * 下架商品
     * @param id
     * @return
     * @throws SQLException
     */
    public int goodsSoldOut(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            String sql = "delete from goods where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            int ret = ps.executeUpdate();
            return ret;
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }

    /**
     * 更新商品信息
     * @param updateGoods
     * @return
     * @throws SQLException
     */
    public int goodsUpdate(Goods updateGoods) throws SQLException {

        try{
            Goods goods = getGoods(updateGoods.getId());
            System.out.println(updateGoods.getId());
            System.out.println(goods);
            if (goods != null) {

                goods.setName(updateGoods.getName());
                goods.setIntroduce(updateGoods.getIntroduce());
                goods.setStock(updateGoods.getStock());
                goods.setUnit(updateGoods.getUnit());
                goods.setPrice(updateGoods.getPriceInt());
                goods.setDiscount(updateGoods.getDiscount());
                boolean flag = modify(goods);
                if (flag) {
                    return 1;
                }else {
                    return 0;
                }
            }else {
                System.out.println("没有该商品");
                return 0;
            }
        }finally {

        }
    }

    /**
     * 更新商品sql代码
     * @param goods
     * @return
     * @throws SQLException
     */
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
            if (ret != 0) {
                return true;
            }else {
                return false;
            }
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }

    /**
     * 查询是否有这个商品
     * @param id
     * @return
     * @throws SQLException
     */
    public Goods getGoods(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Goods goods = null;
        try {
            System.out.println(id);
            String sql = "select * from goods where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

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
            return goods;
        } finally {
            DBUtil.close(connection,ps,rs);
        }
    }


    /**
     * 购买商品
     * @param buyGoodsList
     * @return
     * @throws SQLException
     */
    public int buyGoods(List<Goods> buyGoodsList) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            for (Goods goods : buyGoodsList) {
                boolean isUpdate = updateAfterPay(goods,goods.getBuyGoodsNum());
                if (isUpdate) {
                    System.out.println("更新库存成功");
                }else {
                    System.out.println("更新库存失败");
                    return 0;
                }
            }
            return 1;
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }


    /**
     * 更新库存
     * @param goods
     * @param buyGoodsNum
     * @return
     * @throws SQLException
     */
    public boolean updateAfterPay(Goods goods,Integer buyGoodsNum) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            String sql = "update goods set stock = ? where id = ?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setInt(1, goods.getStock() - buyGoodsNum);
            ps.setInt(2, goods.getId());

            if (ps.executeUpdate() != 0) {
                return true;
            }else {
                return false;
            }
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }




    public boolean commitOder(Order order) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String insertOrderSql = "insert into `order` (id, account_id,create_time, finish_time,actual_amount,total_money,order_status,account_name) " +
                    "values (?,?,?,?,?,?,?,?)";
            connection = DBUtil.getConnection(false);
            ps = connection.prepareStatement(insertOrderSql);

            /*ps.setString(1,order.getId());
            ps.setInt(2,order.getAccount_id());
            ps.setInt(3,order.getActual_amountInt());
            ps.setInt(4,order.getTotal_moneyInt());
            ps.setInt(5,order.getOrder_status().getFlag());
            ps.setString(6,order.getAccount_name());*/
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
                //回滚
                // connection.rollback();
            }
        }finally {
            DBUtil.close(connection,ps,rs);
        }
        return true;
    }
    public static void main(String[] args) throws SQLException {
        AccountDao accountDao = new AccountDao();
        Account account = new Account();
        account.setUsername("wyx");
        account.setPassword("1");
        System.out.println(accountDao.login(account));
        /*Account account = new Account();
        account.setUsername("kobe");
        account.setPassword("123");
        System.out.println(register(account));
        System.out.println(goodsBrowse());*/
        /*Goods goods = new Goods();
        goods.setId(14);
        goods.setName("kd13");
        goods.setIntroduce("全掌zoom气垫,前掌双层zoom");
        goods.setStock(100);
        goods.setUnit("双");
        goods.setPrice(1399);
        goods.setDiscount(99);*/
        //System.out.println(goodsPutAway(goods));
        //System.out.println(goodsSoldOut(13));
        //System.out.println(goodsUpdate(goods));
    }
}
