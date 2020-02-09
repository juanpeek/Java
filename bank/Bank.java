/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

/**
 *
 * @author IES TRASSIERRA
 */
public class Bank { //Hacer ejercicios sobre polimorfismo, interfaces, herencia y InstanceOf Goo:Polimorfism Java Exercises
    
    //interfaces: no tiene atributos y los metodos solo se definen, no se implementa. Ver Comparable
    //abstract: tiene atributos, metodos abstractos(solo se instancian) y metodos normales
    public static void main(String[] args) {
        Client c1 = new Client("Juan","12345");
        Client c2 = new Client("Rafa","41231");
        Client c3 = new Client("Carlos","76546");
        Client c4 = new Client("Julian","63435");
        
        //Polimorfismo
        Accounts ca1 = new CurrentAccount(500,0.2,"5555-111");
        Accounts sa1 = new SavingAccount(500,0.4,"7777-111",100);
        
        c1.addAccount(ca1);
        c1.addAccount(sa1);
        
        for(Accounts ac:c1.getAccounts()){
            if(ac!=null)System.out.println(ac);
        }
        
        CurrentAccount ca2 = new CurrentAccount(500,0.3,"6666-111");
        CurrentAccount ca3 = new CurrentAccount(500,0.5,"7777-111");
        SavingAccount sa2 = new SavingAccount(500,0.3,"7777-111",0);
        SavingAccount sa3 = new SavingAccount(500,0.2,"7777-111",0);
        
        
        System.out.println(c1);
        
        
    }
}
