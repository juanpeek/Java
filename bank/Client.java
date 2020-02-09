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
public class Client {
    private String name;
    private String idcard;
    //Comopsition
    private Accounts accounts[] = new Accounts[10];
    

    public Client(String name, String idcard, Accounts account) {
        this.name = name;
        this.idcard = idcard;
        this.accounts[0] = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Accounts[] getAccounts() {
        return accounts;
    }   
    
    //Overload
    public Client(String name, String idcard) {
        this.name = name;
        this.idcard = idcard;
    }
    
    public boolean addAccount(Accounts account){
        //No usar aqui el for enhanced
        for (int i = 0;i<accounts.length; i++) {
            if(accounts[i]==null){
                accounts[i]=account;
                return true;
            }
        }
        return false;
    }

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
        final Client other = (Client) obj;
        if (!Objects.equals(this.idcard, other.idcard)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Client{" + "name=" + name + ", idcard=" + idcard + '}';
    }
    
    
}
