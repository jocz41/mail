/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Principal;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import Mail.VistaMail;
import Nube.VistaNube;

/**
 * Vista de gestión del servidor donde se almacenan los ficheros en ftp, junto
 * con la de gestión de emails.
 */
public class VistaNubEmail extends JFrame
{

    private VistaMail pnlMail;
    private VistaNube pnlNube;

    public VistaNubEmail()
    {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setTitle("NubE-mail");

        JTabbedPane p = new JTabbedPane();
        pnlMail = new VistaMail();
        pnlNube = new VistaNube();

        p.addTab("Nube", null, pnlNube, null);
        p.addTab("E-mail", null, pnlMail, null);
        this.add(p);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Getters
    public VistaMail getPnlMail()
    {
        return pnlMail;
    }

    public VistaNube getPnlNube()
    {
        return pnlNube;
    }

}
