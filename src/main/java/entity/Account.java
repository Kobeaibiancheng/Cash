package entity;

import lombok.Data;

@Data
public class Account {
    private Integer id;
    private String username;
    private String password;

    /*public static void main(String[] args) {
        Account account = new Account();
    }*/
}
