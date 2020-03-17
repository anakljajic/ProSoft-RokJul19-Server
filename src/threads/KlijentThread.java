/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import domain.Izvestaj;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import view.TableModelServer;

/**
 *
 * @author anakl
 */
public class KlijentThread extends Thread {
    
    private boolean signal = true;
    private TableModelServer tms;
    private JCheckBox check;
    
    public KlijentThread(TableModelServer tms, JCheckBox check) {
        this.tms = tms;
        this.check = check;
    }
    
    @Override
    public void run() {
        while (signal) {
            try {
                sleep(10000);
                System.out.println("Okinula nit.................");
                List<Izvestaj> izvestaji = controller.Controller.getInstance().vratiDopunjeneIzvestaje();
                tms.azuriraj(izvestaji);
                check.setSelected(false);
            } catch (InterruptedException ex) {
                signal = false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
