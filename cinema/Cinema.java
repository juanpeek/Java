/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author IES TRASSIERRA
 */
public class Cinema {

    /**
     * @param args the command line arguments
     */
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/cine";

    public static void main(String[] args) {
        // TODO code application logic here
        try {

            Connection connection = DriverManager.getConnection(DATABASE_URL, "root", ""); //DriverManager.getConnection(DATABASE_URL, "root","")
            Statement statement = null;
            ResultSet resultSet = null;
            Scanner sc = new Scanner(System.in);
            Map<Integer, Screening> screenmap = new HashMap<>();
            int opt = 0;

            do {

                System.out.println("****** TRASSIERRA CINEMA ******\n"
                        + "* 1. List of rooms            *\n"
                        + "* 2. Buy tickets              *\n"
                        + "* 3. List of tickets          *\n"
                        + "* 4. Exit                     *\n"
                        + "*******************************");
                System.out.println("Select an option?");
                opt = sc.nextInt();
                sc.nextLine();
                switch (opt) {
                    case 1:
                        PreparedStatement p1 = connection.prepareStatement("select number,capacity from rooms");
                        resultSet = p1.executeQuery();

                        ResultSetMetaData metadata = resultSet.getMetaData();
                        int numberColumns = metadata.getColumnCount();

                        for (int i = 1; i <= numberColumns; i++) {
                            System.out.printf("%-15s\t", metadata.getColumnName(i));
                        }
                        System.out.println("");

                        while (resultSet.next()) {
                            for (int i = 1; i <= numberColumns; i++) {
                                System.out.printf("%-15s\t", resultSet.getObject(i));
                            }
                            System.out.println("");
                        }
                        break;

                    case 2:
                        PreparedStatement p2 = connection.prepareStatement("select a.id_screening,id_room,date,time,film,ticketssold from tickets as a,screenings as b where a.id_screening=b.id_screening ");
                        resultSet = p2.executeQuery();

                        java.util.Date d = new java.util.Date();
                        java.sql.Date dsql = new java.sql.Date(d.getTime());

                        System.out.println("The following list shows the screenings files");
                        ResultSetMetaData metadata2 = resultSet.getMetaData();
                        int numberColumns2 = metadata2.getColumnCount();

                        int idavailable = 0;
                        int idroom = 0;
                        int ticketssold = 0;
                        
                        for (int i = 1; i <= numberColumns2; i++) {
                            System.out.printf("%-20s\t", metadata2.getColumnName(i));
                        }
                        System.out.println("");


                        while (resultSet.next()) {
                            int date = resultSet.getDate(3).compareTo(dsql);
                            if (date >= 0) {
                                for (int i = 1; i <= numberColumns2; i++) {
                                    System.out.printf("%-20s\t", resultSet.getObject(i));
                                    if(i==1){
                                        idavailable = resultSet.getInt(i);
                                    }
                                    if(i==2){
                                        idroom = resultSet.getInt(i);
                                    }
                                    if(i==6){
                                        screenmap.put(idavailable, new Screening(idroom,resultSet.getInt(i)));
                                    }
                                }
                                System.out.println("");
                            }
                        }
                        for (Map.Entry<Integer, Screening> entry : screenmap.entrySet()) {
                            System.out.println(entry);
                        }
                        String user = "";
                        int quantity = 0;

                        System.out.println("Select the screening id:");
                        int idscreening = sc.nextInt();
                        sc.nextLine();
                        if(screenmap.containsKey(idscreening)){
                            PreparedStatement p3 = connection.prepareStatement("Update screenings set ticketssold=? where id_screening=?",ResultSet.CONCUR_UPDATABLE);
                            System.out.println("Type a quantity of seats that you want to buy:");
                            quantity = sc.nextInt();
                            sc.nextLine();
                            p3.setInt(1, quantity);
                            p3.setInt(2, idscreening);
                            p3.executeUpdate();
                        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        resultSet = statement.executeQuery("select * from tickets");
                        
                        System.out.println("Enter your username:");
                        user = sc.nextLine();
                        
                        resultSet.moveToInsertRow();
                        resultSet.updateInt("id_screening", idscreening);
                        resultSet.updateString("user", user);
                        resultSet.updateInt("seats", quantity);

                        }else{
                            System.out.println("The screening id doesn't exist");
                        }
                        break;

                    case 3:
                        PreparedStatement p3 = connection.prepareStatement("select user,date,time,film,seats from tickets as a,screenings as b where a.id_screening=b.id_screening  ");
                        resultSet = p3.executeQuery();

                        ResultSetMetaData metadata3 = resultSet.getMetaData();
                        int numberColumns3 = metadata3.getColumnCount();

                        for (int i = 1; i <= numberColumns3; i++) {
                            System.out.printf("%-20s\t", metadata3.getColumnName(i));
                        }
                        System.out.println("");

                        while (resultSet.next()) {
                            for (int i = 1; i <= numberColumns3; i++) {
                                System.out.printf("%-20s\t", resultSet.getObject(i));
                                if (i == 1) {

                                }
                            }
                            System.out.println("");

                        }

                        break;
                    case 4:
                        break;
                }
            } while (opt != 4);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

}
