/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Nube;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

// Genera el panel que representa la pestaña para gestionar el email.
public class VistaNube extends JPanel
{

    private JList listaArchivos;
    private ArrayList<JButton> botones;

    public VistaNube()
    {
        this.setLayout(new FlowLayout());

        listaArchivos = new JList();
        listaArchivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane barra = new JScrollPane(listaArchivos);
        barra.setPreferredSize(new Dimension(300, 200));

        this.add(barra);
        this.add(opciones());
    }

    private JPanel opciones()
    {
        JPanel p = new JPanel();

        p.setLayout(new GridLayout(0, 1, 5, 5));

        botones = new ArrayList();
        botones.add(new JButton("Subir"));
        botones.add(new JButton("Descargar"));
        botones.add(new JButton("Eliminar"));
        botones.add(new JButton("Crear carpeta"));
        botones.add(new JButton("Eliminar carpeta"));
        botones.add(new JButton("Salir"));

        for (JButton b : botones)
        {
            p.add(b);
        }

        return p;
    }

    // Getters
    public JList getListaArchivos()
    {
        return listaArchivos;
    }

    public ArrayList<JButton> getBotones()
    {
        return botones;
    }
}
