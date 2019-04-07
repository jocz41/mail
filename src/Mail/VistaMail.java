/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail;

import Mail.Redactar.VistaRedactar;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel que corresponde a la interfaz de la pestaña mediante la que manejaremos
 * el e-mail.
 */
public class VistaMail extends JPanel
{

    private ArrayList<JButton> botones;
    private JPanel pnlLista;
    private VistaRedactar pnlRedactar;
    private JList listaMails;

    public VistaMail()
    {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        listaMails = new JList();
        pnlLista = new JPanel();
        pnlRedactar = new VistaRedactar();

        JScrollPane scrol = new JScrollPane();
        scrol.setPreferredSize(new Dimension(300, 200));
        scrol.add(listaMails);
        pnlLista.add(scrol);
        pnlLista.setVisible(true);
        pnlRedactar.setVisible(false);

        this.add(creaBotones());
        this.add(pnlLista);
        this.add(pnlRedactar);
    }

    /**
     * Método que genera los botones que nos permitirán seleccionar una opción
     * del email.
     */
    private JPanel creaBotones()
    {
        JPanel p = new JPanel();

        p.setLayout(new GridLayout(0, 1, 5, 5));

        botones = new ArrayList();
        botones.add(new JButton("Iniciar Sesión"));
        botones.add(new JButton("Bandeja"));
        botones.add(new JButton("Redactar"));

        JButton btn = new JButton("Cerrar Sesión");
        btn.setEnabled(false);
        botones.add(btn);
        botones.add(new JButton("Salir"));

        for (JButton b : botones)
        {
            p.add(b);
        }

        return p;
    }

    // Getters
    public ArrayList<JButton> getBotones()
    {
        return botones;
    }

    public JPanel getPnlLista()
    {
        return pnlLista;
    }

    public VistaRedactar getPnlRedactar()
    {
        return pnlRedactar;
    }

    public JList getListaMails()
    {
        return listaMails;
    }

}
