/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import domain.Izvestaj;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author anakl
 */
public class TableModelServer extends AbstractTableModel {

    private List<Izvestaj> izvestaji;
    String[] columnNames = new String[]{"Gazdinstvo", "Datum registracije", "Ukupno subvencija", "Broj grla", "Stado"};

    public TableModelServer(List<Izvestaj> izvestaji) {
        this.izvestaji = izvestaji;
    }

    @Override
    public int getRowCount() {
        return izvestaji.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Izvestaj i = izvestaji.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return i.getGazdinstvo();
            case 1:
                return i.getDatumRegistracije();
            case 2:
                return i.getUkupnoSubvencija();
            case 3:
                return i.getBrojGrla();
            case 4:
                return i.getStado();
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void azuriraj(List<Izvestaj> izvestaji) {
        this.izvestaji = izvestaji;
        fireTableDataChanged();
    }

}
