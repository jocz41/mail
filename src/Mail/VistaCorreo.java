/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// Representación gráfica de un e-mail.
public class VistaCorreo extends JFrame
{

    public VistaCorreo(String rem, String dest, String asunto, String contenido)
    {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("De: " + rem));
        this.add(p);

        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Para: " + dest));
        this.add(p);

        p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Asunto: " + asunto));
        this.add(p);

        JTextArea mensaje = new JTextArea(contenido);
        mensaje.setEditable(false);
        JScrollPane barra = new JScrollPane(mensaje);

        this.add(barra);

        this.pack();
        this.setSize(new Dimension(600, 500));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
