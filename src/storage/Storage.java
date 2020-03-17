/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import database.connection.ConnectionFactory;
import domain.Gazdinstvo;
import domain.Izvestaj;
import domain.Poljoprivrednik;
import domain.Stado;
import domain.Zivotinja;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anakl
 */
public class Storage {

    List<Poljoprivrednik> poljoprivrednici;
    List<Zivotinja> zivotinje;
    List<Gazdinstvo> gazdinstva;
    List<Izvestaj> izvestaji;

    public Storage() {
        poljoprivrednici = new ArrayList<>();
        zivotinje = new ArrayList<>();
        gazdinstva = new ArrayList<>();
        izvestaji = new ArrayList<>();
    }

    public List<Poljoprivrednik> getAllPoljoprivrednici() throws Exception {
        poljoprivrednici.clear();
        try {
            String upit = "SELECT * FROM poljoprivrednik";

            Statement statement = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Poljoprivrednik p = new Poljoprivrednik();
                p.setPoljoprivrednikID(rs.getLong("PoljoprivrednikID"));
                p.setIme(rs.getString("Ime"));
                p.setPrezime(rs.getString("Prezime"));
                p.setKorisnickoIme(rs.getString("KorisnickoIme"));
                p.setLozinka(rs.getString("Lozinka"));
                poljoprivrednici.add(p);

            }
            rs.close();
            statement.close();
            return poljoprivrednici;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public List<Zivotinja> getAllZivotinje() throws Exception {
        zivotinje.clear();
        try {
            String upit = "SELECT * FROM zivotinja";

            Statement statement = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Zivotinja z = new Zivotinja();
                z.setZivotinjaID(rs.getLong("ZivotinjaID"));
                z.setNaziv(rs.getString("Naziv"));
                z.setAutohtonaVrsta(rs.getInt("AutohtonaVrsta"));
                z.setSubvencijaPoGrlu(rs.getBigDecimal("SubvencijaPoGrlu"));
                zivotinje.add(z);
            }
            rs.close();
            statement.close();
            return zivotinje;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public List<Gazdinstvo> getAllGazdinstva() throws Exception {
        gazdinstva.clear();
        try {
            String upit = "SELECT * FROM gazdinstvo";

            Statement statement = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(upit);

            while (rs.next()) {
                Gazdinstvo g = new Gazdinstvo();
                g.setGazdinstvoID(rs.getLong("GazdinstvoID"));
                g.setNaziv(rs.getString("Naziv"));
                g.setDatumRegistracije(new Date(rs.getDate("DatumRegistracije").getTime()));
                g.setUkupnoSubvencija(rs.getBigDecimal("UkupnoSubvencija"));
                Poljoprivrednik p = new Poljoprivrednik();
                p = vratiPoljoprivrednika(rs.getLong("PoljoprivrednikID"));
                g.setPoljoprivrednik(p);
                gazdinstva.add(g);
            }
            rs.close();
            statement.close();
            return gazdinstva;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public Poljoprivrednik vratiPoljoprivrednika(Long ID) {
        for (Poljoprivrednik poljoprivrednik : poljoprivrednici) {
            if (poljoprivrednik.getPoljoprivrednikID().equals(ID)) {
                return poljoprivrednik;
            }
        }
        return null;
    }

    public Long vratiIDGazdinstva() {
        Long id = 0L;
        for (Gazdinstvo gazdinstvo : gazdinstva) {
            id = gazdinstvo.getGazdinstvoID();
        }
        return id + 1L;
    }

    public void sacuvajGazdinstvo(Gazdinstvo g) throws Exception, SQLException, Exception {
        try {
            String upit = "INSERT INTO gazdinstvo(GazdinstvoID, Naziv, DatumRegistracije, UkupnoSubvencija, PoljoprivrednikID) VALUES (?,?,?,?,?)";
            PreparedStatement statement = ConnectionFactory.getInstance().getConnection().prepareStatement(upit);
            statement.setLong(1, g.getGazdinstvoID());
            statement.setString(2, g.getNaziv());
            statement.setDate(3, new java.sql.Date(g.getDatumRegistracije().getTime()));
            statement.setBigDecimal(4, g.getUkupnoSubvencija());
            statement.setLong(5, g.getPoljoprivrednik().getPoljoprivrednikID());
            statement.executeUpdate();
            ConnectionFactory.getInstance().getConnection().commit();
            statement.close();
        } catch (Exception ex) {
            ConnectionFactory.getInstance().getConnection().rollback();
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public void sacuvajStada(List<Stado> stada) throws Exception {
        try {
            String upit = "INSERT INTO stado (GazdinstvoID, StadoID, BrojGrla, IznosSubvencije, ZivotinjaID) VALUES (?,?,?,?,?)";
            PreparedStatement statement = ConnectionFactory.getInstance().getConnection().prepareStatement(upit);
            for (Stado s : stada) {
                statement.setLong(1, s.getGazdinstvo().getGazdinstvoID());
                statement.setLong(2, s.getStadoID());
                statement.setLong(3, s.getBrojGrla());
                statement.setBigDecimal(4, s.getIznosSubvencije());
                statement.setLong(5, s.getZivotinja().getZivotinjaID());
                statement.executeUpdate();
            }
            ConnectionFactory.getInstance().getConnection().commit();
            statement.close();
        } catch (Exception ex) {
            ConnectionFactory.getInstance().getConnection().rollback();
            ex.printStackTrace();
            throw new Exception();
        }

    }

    //SELECT CONCAT(p.Ime,' ', p.Prezime) AS 'Gazdinstvo', g.DatumRegistracije AS 'Datum registracije', s.IznosSubvencije  AS 'UkupnoSubvencije', s.BrojGrla AS 'Broj grla', z.Naziv AS 'Stado' FROM poljoprivrednik p JOIN gazdinstvo g ON (p.PoljoprivrednikID=g.PoljoprivrednikID) JOIN stado s ON (g.GazdinstvoID=s.GazdinstvoID) JOIN zivotinja z ON (s.ZivotinjaID=z.ZivotinjaID)
    public List<Izvestaj> getAllIzvestaj() throws Exception {
        izvestaji.clear();
        try {
            String upit = "SELECT CONCAT(p.Ime,' ', p.Prezime) AS 'Gazdinstvo', g.DatumRegistracije AS 'DatumRegistracije', s.IznosSubvencije  AS 'UkupnoSubvencije', s.BrojGrla AS 'BrojGrla', z.Naziv AS 'Stado' FROM poljoprivrednik p JOIN gazdinstvo g ON (p.PoljoprivrednikID=g.PoljoprivrednikID) JOIN stado s ON (g.GazdinstvoID=s.GazdinstvoID) JOIN zivotinja z ON (s.ZivotinjaID=z.ZivotinjaID)";
            System.out.println(upit);

            Statement statement = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(upit);

            while (rs.next()) {
                Izvestaj i = new Izvestaj();
                i.setGazdinstvo(rs.getString("Gazdinstvo"));
                i.setDatumRegistracije(new Date(rs.getDate("DatumRegistracije").getTime()));
                i.setUkupnoSubvencija(rs.getBigDecimal("UkupnoSubvencije"));
                i.setBrojGrla(rs.getLong("BrojGrla"));
                i.setStado(rs.getString("Stado"));
                izvestaji.add(i);
            }
            rs.close();
            statement.close();
            return izvestaji;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

}
