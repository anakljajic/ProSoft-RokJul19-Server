/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Gazdinstvo;
import domain.Izvestaj;
import domain.Poljoprivrednik;
import domain.Stado;
import domain.Zivotinja;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import storage.Storage;

/**
 *
 * @author student1
 */
public class Controller {

    private static Controller instance;
    private final Storage storage;
    private List<Izvestaj> dopunjeniIzvestaji;

    private Controller() {
        storage = new Storage();
        dopunjeniIzvestaji = new ArrayList<>();

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

    public List<Izvestaj> getAllIzvestaj() throws Exception {
        return storage.getAllIzvestaj();

    }

    public BigDecimal vratiUkupnoSubvencije(String gazdinstvo) throws Exception {
        BigDecimal ukSubv = new BigDecimal(0);
        List<Izvestaj> izvestaji = getAllIzvestaj();
        for (Izvestaj izvestaj : izvestaji) {
            if (izvestaj.getGazdinstvo().equalsIgnoreCase(gazdinstvo)) {
                ukSubv = ukSubv.add(izvestaj.getUkupnoSubvencija());
            }
        }
        return ukSubv;
    }

    public Long vratiUkupnoGrla(String gazdinstvo) throws Exception {
        Long brojGrla = 0L;
        List<Izvestaj> izvestaji = getAllIzvestaj();
        for (Izvestaj izvestaj : izvestaji) {
            if (izvestaj.getGazdinstvo().equalsIgnoreCase(gazdinstvo)) {
                brojGrla = brojGrla + izvestaj.getBrojGrla();
            }
        }
        return brojGrla;
    }

    public int daLiPostojiUNovojListi(String gazdinstvo) {
        for (Izvestaj izvestaj : dopunjeniIzvestaji) {
            if (izvestaj.getGazdinstvo().equalsIgnoreCase(gazdinstvo)) {
                return 1; //postoji gazdinstvo
            }
        }
        return -1;
    }

    public List<Izvestaj> vratiDopunjeneIzvestaje() throws Exception {
        List<Izvestaj> izvestaji = getAllIzvestaj();
        for (Izvestaj izvestaj : izvestaji) {
            if (daLiPostojiUNovojListi(izvestaj.getGazdinstvo()) == -1) {
                izvestaj.setBrojGrla(vratiUkupnoGrla(izvestaj.getGazdinstvo()));
                izvestaj.setUkupnoSubvencija(vratiUkupnoSubvencije(izvestaj.getGazdinstvo()));
                izvestaj.setStado(vratiSvaStada(izvestaj.getGazdinstvo()));
                dopunjeniIzvestaji.add(izvestaj);
            }
        }
        return dopunjeniIzvestaji;
    }

    public String vratiSvaStada(String gazdinstvo) throws Exception {
        String stada = new String();
        List<Izvestaj> izvestaji = getAllIzvestaj();
        for (Izvestaj izvestaj : izvestaji) {
            if (izvestaj.getGazdinstvo().equalsIgnoreCase(gazdinstvo)) {
                stada = stada + " ," + izvestaj.getStado();
            }
        }
        return stada;
    }

    public int vratiBrojStada(String gazdinstvo) throws Exception {
        int brojac = 0;
        List<Izvestaj> izvestaji = getAllIzvestaj();
        for (Izvestaj izvestaj : izvestaji) {
            if (izvestaj.getGazdinstvo().equalsIgnoreCase(gazdinstvo)) {
                brojac++;
            }
        }
        return brojac;
    }

    public List<Izvestaj> getAllIzvestajiSaFilterom(String naziv) throws Exception {
        return storage.getAllIzvestajSaFilterom(naziv);
    }

}
