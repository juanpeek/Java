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
public class SavingAccount extends Accounts{

    private double minimumbalance;
    
    public SavingAccount(double balance, double annualint, String accountnumber,double minimum) {
        super(balance, annualint, accountnumber);
        minimumbalance = minimum;
    }

    @Override
    public double calculateInterest() {
        double interestGiven;
        if(balance > minimumbalance){
            interestGiven=balance*(getAnnualint()*2);
        }else{
            interestGiven=balance*(getAnnualint()/2);
        }
        balance+=interestGiven;
        return balance;
    }

    @Override
    public String toString() { //Usamos el string de super para aprovecharlo
        return super.toString()+ " minimumbalance=" + minimumbalance;
    }
    
    
}
