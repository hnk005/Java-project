package com.example.quanlykho.util;

import com.example.quanlykho.model.account.Account;

public class SessionManager {
    private static SessionManager instance;
    private Account currentAccount;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    public void logout() {
        this.currentAccount = null;
    }
}

