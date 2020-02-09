/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collectionex;
/**
 *
 * @author IES TRASSIERRA
 */
public class Contact implements Comparable{
    private String name = "";
    private String surname = "";
    private String address = "";
    private String city = "";
    private String phone = "";

    public Contact(String name, String surname, String address, String city, String phone) {
        this.name=name;
        this.surname=surname;
        this.address=address;
        this.city=city;
        this.phone=phone;
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" + "name=" + name + ", surname=" + surname + ", address=" + address + ", city=" + city + ", phone=" + phone + '}';
    }

    @Override
    public int compareTo(Object o) {

            Contact s=(Contact) o;

            if (this.name.compareTo(s.getSurname())==0){
                if(this.name.compareTo(s.getName())==0){
                    if(this.name==s.getName()) return 0;
                    else if(this.name==s.getName()) return -1;
                    return 1;
                   }
                return this.getName().compareToIgnoreCase(s.getName());
              }
            return this.getName().compareToIgnoreCase(s.getName());
    }

    
}
