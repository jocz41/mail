/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package peval5;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// Ventana que sirve como plantilla para la entrada de datos de dos campos
public class PlantillaInput extends JFrame
{

    private JTextField txtUno, txtDos;
    private JButton btnAceptar;

    public PlantillaInput(String titulo, String campo1, String campo2)
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(0, 1, 10, 10));

        this.setTitle(titulo);

        String uno = "", dos = "";

        //Separar por conexión y LogIn
        if (titulo.equals("Conexión a FTP"))
        {
            uno = "83.34.249.5";
            dos = "";
            txtDos = new JTextField();
        } else if (titulo.equals("LogIn FTP"))
        {
            uno = "usuario1";
            dos = "pass1";
            txtDos = new JPasswordField();
        } else if (titulo.contains("Mail"))
        {
            uno = "sarapspftp@gmail.com";
            dos = "123456asdasd";
            txtDos = new JPasswordField();
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

        JPanel p = new JPanel();
        p.add(new JLabel(titulo));

        this.add(p);

        p = new JPanel();
        p.add(new JLabel(campo1));

        txtUno = new JTextField();
        txtUno.setText(uno);
        txtUno.setColumns(15);
        p.add(txtUno);

        this.add(p);

        p = new JPanel();
        p.add(new JLabel(campo2));

        txtDos.setText(dos);
        txtDos.setColumns(15);
        p.add(txtDos);

        this.add(p);

        btnAceptar = new JButton("Entrar");
        this.getRootPane().setDefaultButton(btnAceptar);
        p = new JPanel();
        p.add(btnAceptar);

        this.add(p);

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Getters
    public JTextField getTxtUno()
    {
        return txtUno;
    }

    public JTextField getTxtDos()
    {
        return txtDos;
    }

    public JButton getBtnAceptar()
    {
        return btnAceptar;
    }

}
