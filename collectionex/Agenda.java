/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collectionex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IES TRASSIERRA
 */
public abstract class Agenda implements Collection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Scanner sc = new Scanner(System.in);
            int option;
            Scanner read = null;
            FileWriter fw = new FileWriter("agenda.txt");
            Formatter fm = new Formatter(fw);
            String name;
            String surname;
            String address;
            String city;
            String phone;

            try {
                read = new Scanner(new File("agenda.txt"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
            }

            ArrayList<Contact> list = new ArrayList< Contact>();

            do {

                System.out.println("1. Insert new contact on the agenda");
                System.out.println("2. Search a contact in the agenda");
                System.out.println("3. Edit a contact in the agenda");
                System.out.println("4. Remove a contact from the agenda");
                System.out.println("5. Sort the agenda by different fields");
                System.out.println("6. Exit");

                System.out.println("Press a number to select an option:");
                option = sc.nextInt();

                while (read.hasNext()) {
                    name = read.next();
                    surname = read.next();
                    address = read.next();
                    city = read.next();
                    phone = read.next();
                    Contact c = new Contact(name, surname, address, city, phone);
                    list.add(c);
                }

                switch (option) {
                    case 1:
                        System.out.println("Has seleccionado la opcion 1");
                        System.out.println("Enter name:");
                        name = sc.next();
                        System.out.println("Enter surname:");
                        surname = sc.next();
                        System.out.println("Enter address:");
                        address = sc.next();
                        System.out.println("Enter city:");
                        city = sc.next();
                        System.out.println("Enter phone:");
                        phone = sc.next();
                        Contact newcontact = new Contact(name, surname, address, city, phone);
                        list.add(newcontact);
                        break;

                    case 2:
                        System.out.println("Has seleccionado la opcion 2");
                        for (Contact c : list) {
                            System.out.printf("%s %s %s %s %s\n",c.getName(),c.getSurname(),c.getAddress(),c.getCity(),c.getPhone());
                        }
                        break;

                    case 3:
                        System.out.println("Has seleccionado la opcion 3");
                        System.out.println("Seleccione un index para editar");
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(i + " " + list.get(i).getName());
                        }
                        int index = sc.nextInt();
                        System.out.println("Se va a editar el contacto " + list.get(index).getName() + " :");
                        System.out.println("Enter name:");
                        name = sc.next();
                        System.out.println("Enter surname:");
                        surname = sc.next();
                        System.out.println("Enter address:");
                        address = sc.next();
                        System.out.println("Enter city:");
                        city = sc.next();
                        System.out.println("Enter phone:");
                        phone = sc.next();
                        Contact editedcontact = new Contact(name, surname, address, city, phone);
                        list.set(index, editedcontact);
                        break;

                    case 4:
                        System.out.println("Has seleccionado la opción 4");
                        System.out.println("Selecciones un index para eliminar:");
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(i + " " + list.get(i).getName());
                        }
                        int index2 = sc.nextInt();
                        list.remove(index2);
                        System.out.println("Contacto eliminado correctamente");
                        break;

                    case 5:
                        Collections.sort(list);
                        break;

                    case 6:
                        break;

                    default:
                        break;
                }
            } while (option != 6);
            
            for(Contact x: list){
                fm.format("%s %s %s %s %s\n",x.getName(),x.getSurname(),x.getAddress(),x.getCity(),x.getPhone());
            }
            read.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

} // end CollectionTest constructor

