/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

/**
 *
 * @author juanpeek
 */
public class CurrentAccount extends Accounts{

    public CurrentAccount(double balance, double annualint, String accountnumber) {
        super(balance, annualint, accountnumber);
    }

    @Override
    public double calculateInterest() {
        balance += (balance * getAnnualint());
        return balance;
    }
   
}
