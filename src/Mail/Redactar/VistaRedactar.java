/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail.Redactar;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Genera el panel que hace de interfaz para el envío de un correo.
public class VistaRedactar extends JPanel
{

    private JTextField txtDest, txtAsunto;
    private JTextArea txtContenido;
    private JButton btnEnviar;

    public VistaRedactar()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        txtContenido = new JTextArea();
        JScrollPane scrol = new JScrollPane(txtContenido);
        scrol.setPreferredSize(new Dimension(300, 200));
        btnEnviar = new JButton("Enviar");

        this.add(generarLinea("Destinatario:"));
        this.add(generarLinea("Asunto:"));

        this.add(scrol);
        this.add(btnEnviar);
    }

    // Genera una línea con una etiqueta y un campo a rellenar.
    private JPanel generarLinea(String s)
    {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 0, 5, 5));

        p.add(new JLabel(s));

        if (s.equals("Destinatario:"))
        {
            txtDest = new JTextField();
            txtDest.setColumns(15);
            p.add(txtDest);
        } else
        {
            txtAsunto = new JTextField();
            txtAsunto.setColumns(15);
            p.add(txtAsunto);
        }

        return p;
    }

    // Getters
    public JTextField getTxtDest()
    {
        return txtDest;
    }

    public JTextField getTxtAsunto()
    {
        return txtAsunto;
    }

    public JTextArea getTxtContenido()
    {
        return txtContenido;
    }

    public JButton getBtnEnviar()
    {
        return btnEnviar;
    }

}
