/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Gazdinstvo;
import domain.Poljoprivrednik;
import domain.Stado;
import domain.Zivotinja;
import java.util.List;
import storage.Storage;

/**
 *
 * @author student1
 */
public class Controller {
    
    private static Controller instance;
    private final Storage storage;
    
    private Controller() {
        storage = new Storage();
    }
    
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }
    
    public Poljoprivrednik logIn(String username, String password) throws Exception {
        List<Poljoprivrednik> poljoprivrednici = storage.getAllPoljoprivrednici();
        for (Poljoprivrednik p : poljoprivrednici) {
            if (p.getKorisnickoIme().equalsIgnoreCase(username)) {
                if (p.getLozinka().equals(password)) {
                    return p;
                } else {
                    throw new Exception("Lozinka nije odgovarajuća!");
                }
            }
        }
        throw new Exception("Korisnik nije registrovan!");
    }
//    
//    public List<Manufacturer> getAllManufacturers() throws Exception{
//        //return storageManufacturer.getAll();
//        return serviceManufacturer.getAll();
//    }
//    
//    public void saveProduct(Product product) throws Exception{
//        //storageProduct.insert(product);
//        SystemOperation so=new SOInsertProduct(product);
//        so.execute();
//    }
//    
//    public List<Product> getAllProducts() throws Exception{
//        return storageProduct.getAll();
//    }
//    
//    public Invoice saveInvoice(Invoice invoice) throws Exception{
//        //return serviceInvoice.save(invoice);
//        SystemOperation so=new SOInsertInvoice(invoice);
//        so.execute();
//        return (Invoice)so.getDomainObject();
//    }

    public List<Poljoprivrednik> getAllPoljoprivrednici() throws Exception {
        return storage.getAllPoljoprivrednici();
    }
    
    public List<Zivotinja> getAllZivotinje() throws Exception {
        return storage.getAllZivotinje();
    }
    
    public List<Gazdinstvo> getAllGazdinstva() throws Exception {
        return storage.getAllGazdinstva();
    }
    
    public void sacuvajGazdinstvo(Gazdinstvo g) throws Exception {
        storage.sacuvajGazdinstvo(g);
    }
    
    public void sacuvajStada(List<Stado> s) throws Exception {
        storage.sacuvajStada(s);
    }
    
}
