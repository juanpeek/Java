/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoppingcart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author IES TRASSIERRA
 */
public class ShoppingCart1 {

    // JDBC driver name and database URL                              
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/addbooks?serverTimezone=UTC";

    //Changing user and password 1 time will be enough as I have used this method always for creating the connection
    public static Connection getConnection() throws SQLException {
        String user = "root";
        String password = "";
        return DriverManager.getConnection(DATABASE_URL, user, password);
    }

    public static void main(String[] args) {
        HashMap<String, Integer> cart = new HashMap();
        Scanner sc = new Scanner(System.in);
        int opt = 0;
        boolean exit = false, valid;
        do {
            System.out.printf("******** SHOPPING CART ********\n"
                    + "* 1. List of books                    *\n"
                    + "* 2. Add to Cart                      *\n"
                    + "* 3. Display Cart                     *\n"
                    + "* 4. Buy now                          *\n"
                    + "* 5. Exit                             *\n"
                    + "* 6. List of orders and total sales   *\n"
                    + "* 7. Search an order by number        *\n"
                    + "* 8. Delete an order                  *\n"
                    + "***************************************\n");

            do {
                System.out.print("Select an option ? ");
                valid = true;
                try {
                    opt = Integer.parseInt(sc.nextLine());
                    if (opt < 1 || opt > 8) {
                        throw new NumberFormatException("The option must be one of the shown above");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                    valid = false;
                }
            } while (!valid);
            switch (opt) {
                case 1: {
                    /*
                    All the books store in Title table will be displayed. The program will only show the ISBN,
                    title and quantity columns. The quantity column represents the number of copies for each
                    book in the store (stock).
                     */
                    try (Connection connection = getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT isbn, title, quantity FROM titles");) {
                        System.out.printf("%-12s\t"
                                + "%-40s\t"
                                + "%s\n", "ISBN", "Title", "Quantity");
                        while (resultSet.next()) {
                            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                                System.out.printf(i == 1 ? "%-12s\t" : i == 2 ? "%-40s\t" : "%s\n", resultSet.getObject(i));
                            }
                        }
                        System.out.println("Press enter to continue");
                        sc.nextLine();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } //End case 1

                case 2: {
                    /*
                    Users can add to cart a book to be bought later. The program will ask users for the ISBN
                    book and the number of copies for that book. Afterwards, this information will be added
                    to a Map<String,Integer> cart. “Cart” is the name of the map object.
                    Manage the following prompts appropriately:
                        • System.out.println("The quantity of copies must be greater than 0");
                        • System.out.println("There aren't so many copies in stock");
                        • System.out.println("The cart has been updated");
                     */
                    System.out.printf("ISBN of the book: ");
                    String isbn = sc.nextLine();
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement("SELECT quantity FROM titles WHERE isbn = ?");) {
                        preparedStat.setString(1, isbn);
                        //First of all check if there is a book with the given ISBN
                        try (ResultSet resultSet = preparedStat.executeQuery()) {

                            boolean found = resultSet.next(); //If there is a book with the given ISBN found will be true

                            if (!found) {
                                System.out.printf("Book not found\n");
                                try {
                                    Thread.sleep(1250);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                            }

                            if (resultSet.getInt("quantity") == 0) {
                                System.out.println("Out of Stock\n");
                                try {
                                    Thread.sleep(1250);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                            }

                            if (cart.containsKey(isbn)) {
                                boolean stock = resultSet.getInt("quantity") > cart.get(isbn);
                                //Check option
                                do {
                                    System.out.printf("You already have ordered %d copies of this book (%s)\n"
                                            + "%s"
                                            + "Make a new order for this book\n"
                                            + "Select an option: ", cart.get(isbn), isbn, stock ? "1.- Add more copies of this book to the cart\n2.- " : "There aren't more copies in stock\n1.- ");
                                    valid = true;
                                    try {
                                        opt = Integer.parseInt(sc.nextLine());
                                        if ((stock ? opt != 1 && opt != 2 : opt != 1)) { // If there is more stock then it could be option 1 or 2
                                            throw new NumberFormatException("The option must be one of the shown above");
                                        }
                                    } catch (NumberFormatException ex) {
                                        System.out.println(ex.getMessage());
                                        valid = false;
                                    }
                                } while (!valid);
                                //End check
                                if (stock && opt == 1) {
                                    System.out.printf("You already have ordered %d copies\n", cart.get(isbn));
                                    int copies = 0;

                                    //Check if the number of copies is valid
                                    do {
                                        System.out.print("Type the number of copies you want to add: ");
                                        valid = true;
                                        try {
                                            copies = Integer.parseInt(sc.nextLine());
                                            if (copies < 0) {
                                                throw new NumberFormatException("The quantity of copies must be greater than 0");
                                            } else if ((copies += cart.get(isbn)) > resultSet.getInt("quantity")) {
                                                throw new NumberFormatException("There aren't so many copies in stock");
                                            }
                                        } catch (NumberFormatException ex) {
                                            valid = false;
                                            System.out.println(ex.getMessage());
                                        }
                                    } while (!valid);
                                    //End check of int copies

                                    cart.put(isbn, copies);
                                    System.out.println("The cart has been updated");
                                    break;
                                }
                            }

                            int copies = 0;

                            //Check if the number of copies is valid
                            do {
                                System.out.printf("Number of copies: ");
                                valid = true;
                                try {
                                    copies = Integer.parseInt(sc.nextLine());
                                    if (copies > resultSet.getInt("quantity")) {
                                        throw new NumberFormatException("There aren't so many copies in stock");
                                    } else if (copies < 0) {
                                        throw new NumberFormatException("The quantity of copies must be greater than 0");
                                    }
                                } catch (NumberFormatException ex) {
                                    valid = false;
                                    System.out.println(ex.getMessage());
                                }
                            } while (!valid);
                            //End check of int copies

                            cart.put(isbn, copies); //Cart updated

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } //End case 2

                case 3: {
                    /*
                    List of books in the cart (ISBN and quantity pair).
                     */
                    if (cart.isEmpty()) {
                        System.out.println("The cart is empty\n");
                        break;
                    }
                    String query = "SELECT isbn, title FROM titles WHERE isbn = ?";
                    for (int i = 1; i < cart.size(); i++) {
                        query += " OR isbn = ?";
                    }
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement(query)) {

                        Set<String> keys = cart.keySet();
                        Iterator<String> iterator = keys.iterator();
                        for (int i = 1; iterator.hasNext(); i++) {
                            preparedStat.setString(i, iterator.next());
                        }
                        try (ResultSet resultSet = preparedStat.executeQuery();) {

                            System.out.printf("%-12s\t"
                                    + "%-40s\t"
                                    + "%s\n", "ISBN", "Title", "Quantity");
                            while (resultSet.next()) {
                                System.out.printf("%-12s\t"
                                        + "%-40s\t"
                                        + "%s\n", resultSet.getString("isbn"), resultSet.getString("title"), cart.get(resultSet.getString("isbn")));
                            }
                            System.out.println("\nPress enter to continue");
                            sc.nextLine();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } //End case 3
                break;

                case 4: {
                    /*
                    Modify appropriately the database for updating the stock (number of copies of a book)
                    according to the shopping cart content. The program will have to subtract the quantity in
                    the map from the quantity column.
                    Remove the cart as the process finishes.
                    Code this option only using the resultSet object. Don’t use execute methods.
                    Manage the following prompts appropriately:
                        • System.out.println("The cart is empty");
                        • System.out.println("The purchase has been made correctly");
                     */
                    if (cart.isEmpty()) {
                        System.out.println("The cart is empty\n");
                        try {
                        Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    }
                    System.out.print("Introduce your user: ");
                    String user = sc.nextLine();
                    String query = "SELECT isbn, quantity FROM titles WHERE isbn = ?";
                    for (int i = 1; i < cart.size(); i++) {
                        query += " OR isbn = ?";
                    }
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
                        int id;
                        connection.setAutoCommit(false);
                        try (ResultSet rs = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM orders")) {
                            rs.moveToInsertRow();
                            rs.updateString("user", user);
                            rs.updateDate("date", new java.sql.Date(new GregorianCalendar().getTimeInMillis()));
                            rs.insertRow();
                            rs.last();
                            id = rs.getInt("id");
                        }

                        Set<String> keys = cart.keySet();
                        Iterator<String> iterator = keys.iterator();
                        for (int i = 1; iterator.hasNext(); i++) {
                            preparedStat.setString(i, iterator.next());
                        }

                        try (ResultSet rs = preparedStat.executeQuery();
                                ResultSet rsDetails = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM order_details");) {
                            rsDetails.moveToInsertRow();
                            for (int i = 1; rs.next(); i++) {  //Update quantity from titles and insert a row in order_details for each book
                                rs.updateInt("quantity", (rs.getInt("quantity") - cart.get(rs.getString("isbn"))));
                                rs.updateRow();
                                rsDetails.updateInt("id_order", id);
                                rsDetails.updateInt("order_line", i);
                                rsDetails.updateString("isbn", rs.getString("isbn"));
                                rsDetails.updateInt("quantity", cart.get(rs.getString("isbn")));
                                rsDetails.insertRow();
                            }
                        }
                        connection.setAutoCommit(true);
                        cart.clear();
                        System.out.println("\nYour order has been placed");
                        Thread.sleep(1500);
                    } catch (SQLException | InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } //End case 4

                case 5: {
                    /*
                    Before exiting, in case that the cart was not empty, the application must request for
                    saving the cart before losing it. As option 4, all the necessary changes will be made if the
                    answer is affirmative. For this option the programmer can’t use the resultSet object. Use
                    prepared statements to update the database.
                    Manage the following prompts appropriately:
                        • System.out.println("The shopping cart is not empty.");
                        • System.out.println("Do you want to buy the books before exiting Y/N?");
                        • System.out.println("The purchase has been made correctly");
                        • System.out.println("You have lost all the books in the shopping cart");
                     */
                    exit = true;
                    if (cart.isEmpty()) {
                        break;
                    }
                    System.out.println("The cart isn't empty, do you want to place an order ? (y/N)");
                    String sure = sc.nextLine();
                    if (sure.isEmpty() || sure.toLowerCase().charAt(0) == 'n') {

                        System.out.println("You have lost all the books in the shopping cart");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    }
                    System.out.print("Introduce your user: ");
                    String user = sc.nextLine();
                    String query = "INSERT INTO orders (user, date) VALUES (?, ?)";
                    try (Connection connection = getConnection()) {
                        connection.setAutoCommit(false);
                        int id;
                        try (PreparedStatement preparedStat = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                            preparedStat.setString(1, user);
                            preparedStat.setDate(2, new java.sql.Date(new GregorianCalendar().getTimeInMillis()));
                            preparedStat.executeUpdate();
                            try (ResultSet rs = preparedStat.getGeneratedKeys()) {
                                rs.next();
                                id = rs.getInt(1);
                            }
                        }
                        String queryTitles = "UPDATE titles SET quantity=quantity-? WHERE isbn=?";
                        String queryDetails = "INSERT INTO order_details (id_order, order_line, isbn, quantity) VALUES (" + id + ", ?, ?, ?)";
                        try (PreparedStatement prepStatTit = connection.prepareStatement(queryTitles);
                                PreparedStatement prepStatDet = connection.prepareStatement(queryDetails)) {
                            Iterator<Map.Entry<String, Integer>> iterator = cart.entrySet().iterator();
                            for (int i = 1; iterator.hasNext(); i++) {
                                Map.Entry<String, Integer> entry = iterator.next(); //Map entry
                                //Set title table update parameters
                                prepStatTit.setInt(1, entry.getValue());
                                prepStatTit.setString(2, entry.getKey());
                                //Set order_details table insert parameters
                                prepStatDet.setInt(1, i);
                                prepStatDet.setString(2, entry.getKey());
                                prepStatDet.setInt(3, entry.getValue());
                                //Execute update and insert
                                prepStatTit.executeUpdate();
                                prepStatDet.executeUpdate();
                            }
                        }
                        connection.setAutoCommit(true);
                        System.out.println("\nYour order has been placed");
                        Thread.sleep(1500);
                    } catch (SQLException | InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } //End case 5

                case 6: {
                    /*
                    List of orders for a specific book (isbn). Total sales.
                     */
                    System.out.printf("ISBN of the book: ");
                    String isbn = sc.nextLine();
                    String query = "SELECT o.id, o.user, o.date, odet.order_line, odet.quantity, t.title"
                            + " FROM orders o JOIN order_details odet ON odet.id_order=o.id JOIN titles t ON t.isbn=odet.isbn "
                            + " WHERE odet.isbn = ?";
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement(query);) {
                        preparedStat.setString(1, isbn);
                        //Check if there is any order for that book
                        try (ResultSet resultSet = preparedStat.executeQuery()) {

                            boolean found = resultSet.next(); //If there is an order of the book the boolean found will be true

                            if (!found) {
                                System.out.println("There isn't orders for that book\n");
                                try {
                                    Thread.sleep(1250);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                            }

                            System.out.printf("Orders for book %s: \n", resultSet.getString("title"));
                            int total_sales = 0;
                            do {
                                total_sales += resultSet.getInt("quantity");
                                LocalDate date = resultSet.getDate("date").toLocalDate();
                                System.out.printf("\tOrder id %d by user %s on %s %d of %d, order line %d, %d %s ordered\n",
                                        resultSet.getInt("id"), resultSet.getString("user"),
                                        date.getMonth().toString().toLowerCase(), date.getDayOfMonth(), date.getYear(),
                                        resultSet.getInt("order_line"), resultSet.getInt("quantity"),
                                        resultSet.getInt("quantity") == 1 ? "copy" : "copies");
                            } while (resultSet.next());
                            System.out.printf("\n\tTotal sales: %d\n", total_sales);

                            System.out.println("\nPress enter to continue");
                            sc.nextLine();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                } //End case 6

                case 7: {
                    /*
                    Search an order through its number.
                    Display the details in case of that order exists
                    and "Order doesn't exist" otherwise.
                     */

                    //Check if the id is valid
                    int id = 0;
                    do {
                        System.out.print("Order id: ");
                        valid = true;
                        try {
                            id = Integer.parseInt(sc.nextLine());
                            if (id < 0) {
                                throw new NumberFormatException("The id must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println(ex.getMessage());
                            valid = false;
                        }
                    } while (!valid);

                    String query = "SELECT o.user, o.date, odet.order_line, odet.isbn, odet.quantity, t.title"
                            + " FROM orders o JOIN order_details odet ON odet.id_order=o.id JOIN titles t ON t.isbn=odet.isbn"
                            + " WHERE id = ?";
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement(query);) {
                        preparedStat.setInt(1, id);
                        //Check if the order exists
                        try (ResultSet resultSet = preparedStat.executeQuery()) {

                            boolean found = resultSet.next(); //If the order exists then found will be true otherwise it will be false

                            if (!found) {
                                System.out.print("Order doesn't exist\n");
                                try {
                                    Thread.sleep(1250);
                                } catch (InterruptedException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                break;
                            }
                            LocalDate date = resultSet.getDate("date").toLocalDate();

                            System.out.printf("The order was placed by the user %s on %s %d of %d\n",
                                    resultSet.getString("user"),
                                    date.getMonth().toString().toLowerCase(), date.getDayOfMonth(), date.getYear());
                            System.out.printf("%-4s\t"
                                    + "%-12s\t"
                                    + "%-40s\t"
                                    + "%s\n",
                                    "Line", "ISBN", "Title", "Quantity");

                            do {
                                System.out.printf("%-4s\t"
                                        + "%-12s\t"
                                        + "%-40s\t"
                                        + "%s\n",
                                        resultSet.getInt("order_line"), resultSet.getString("isbn"), resultSet.getString("title"), resultSet.getString("Quantity"));

                            } while (resultSet.next());
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.out.println("\nPress enter to continue");
                    sc.nextLine();
                    break;
                } //End case 7

                case 8: {
                    /*
                    Delete an order asking for its number.
                     */

                    //Check if the id is valid
                    int id = 0;
                    do {
                        System.out.print("Order id: ");
                        valid = true;
                        try {
                            id = Integer.parseInt(sc.nextLine());
                            if (id < 0) {
                                throw new NumberFormatException("The id must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println(ex.getMessage());
                            valid = false;
                        }
                    } while (!valid);

                    String query = "DELETE FROM orders WHERE id = ?";
                    try (Connection connection = getConnection();
                            PreparedStatement preparedStat = connection.prepareStatement(query)) {
                        preparedStat.setInt(1, id);
                        boolean deleted = preparedStat.executeUpdate() != 0;
                        if (deleted) {
                            System.out.printf("Sucessfully deleted the order with id %d\n\n",id);
                        }
                        else {
                            System.out.printf("Couldn't find an order with id %d\n\n", id);
                        }
                        Thread.sleep(1250);
                    } catch (SQLException|InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    } //End try connection
                    break;
                } //End case 8
            } //End switch

        } while (!exit);
    }
}
