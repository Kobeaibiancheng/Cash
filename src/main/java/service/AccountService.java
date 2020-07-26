package service;

import dao.AccountDao;
import entity.Account;

import java.sql.SQLException;

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

    public int register(Account registerAccount) throws SQLException {
        AccountDao accountDao = new AccountDao();
        int ret = accountDao.register(registerAccount);
        return ret;
    }
}
