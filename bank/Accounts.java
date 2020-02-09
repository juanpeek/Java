/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.util.Objects;

/**
 *
 * @author juanpeek
 */
public abstract class Accounts {
    protected double balance;
    protected double annualint;
    private String accountnumber;

    //Constructor
    public Accounts(double balance, double annualint, String accountnumber) {
        setBalance(balance);
        setAnnualint(annualint);
        this.accountnumber = accountnumber;
    }

    // Getters and Setters

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(balance>0) this.balance = balance;
        else balance = 0;
    }

    public double getAnnualint() {
        return annualint;
    }

    public void setAnnualint(double annualint) {
        if(annualint>0) this.annualint=annualint;
        else annualint = 0;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    //To String
    @Override
    public String toString(){
        return "Accounts{" + "balance=" + balance + ", accountnumber=" + accountnumber +
                "type: "+getClass().getSimpleName()+
                + '}';
    }

    // Equals

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Accounts other = (Accounts) obj;
        if (!Objects.equals(this.accountnumber, other.accountnumber)) {
            return false;
        }
        return true;
    }
    

    //Deposit
    public void deposit(double money) {
        balance += money;
    }
    
    //GetMoney
    public boolean getMoney(double money){
        if(balance>=money){
        this.balance -= money;
        return true;
        }
        return false;
    }
    
    //Check nº of accounts
    public int checkAccounts(Client c){
        return 2;
    }
    
    //Su funcion se define en cada clase
    public abstract double calculateInterest();
}
