package service;

import dao.AccountDao;
import entity.Account;
import entity.Goods;
import entity.Order;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    /**
     * 登陆
     * @param loginAccount
     * @return
     * @throws SQLException
     */
    public Account login(Account loginAccount) throws SQLException {
        AccountDao accountDao = new AccountDao();
        Account account = accountDao.login(loginAccount);
        return account;
    }

    /**
     * 注册
     * @param registerAccount
     * @return
     * @throws SQLException
     */
    public int register(Account registerAccount) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.register(registerAccount);
        return ret;
    }

    /**
     * 浏览商品
     * @return
     * @throws SQLException
     */
    public List<Goods> goodsBrowse() throws SQLException {
        AccountDao accountDao = new AccountDao();
        List<Goods> listGoods = accountDao.goodsBrowse();
        return listGoods;
    }

    /**
     * 添加商品
     * @param addGoods
     * @return
     * @throws SQLException
     */
    public int goodsPutAway(Goods addGoods) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.goodsPutAway(addGoods);
        return ret;
    }

    /**
     * 下架商品
     * @param id
     * @return
     * @throws SQLException
     */
    public int goodsSoldOut(int id) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.goodsSoldOut(id);
        return ret;
    }


    /**
     * 更新商品信息
     * @param updateGoods
     * @return
     * @throws SQLException
     */
    public int goodsUpdate(Goods updateGoods) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.goodsUpdate(updateGoods);
        return ret;
    }


    /**
     * 购买商品
     * @param buyGoodsList
     * @return
     * @throws SQLException
     */
    public int buyGoods(List<Goods> buyGoodsList) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.buyGoods(buyGoodsList);
        return ret;
    }


    /**
     *
     * @param order
     * @return
     */
    public boolean commitOder(Order order) throws SQLException {
        AccountDao accountDao = new AccountDao();
        boolean ret = accountDao.commitOder(order);
        return ret;
    }

    public boolean updateAfterPay(Goods goods,Integer buyGoodsNum) throws SQLException {
        AccountDao accountDao = new AccountDao();
        boolean ret = accountDao.updateAfterPay(goods,buyGoodsNum);
        return ret;
    }

    public Goods getGoods(Integer id) throws SQLException {
        AccountDao accountDao = new AccountDao();
        Goods goods = accountDao.getGoods(id);
        return goods;
    }
}
