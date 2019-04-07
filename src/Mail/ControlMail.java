/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail;

import Mail.LogIn.ModeloLogInMail;
import Mail.Redactar.VistaRedactar;
import Principal.VistaNubEmail;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

/**
 * Esta clase se encarga de controlar todos los eventos relacionados con los
 * elementos del panel de gestión de emails.
 */
public class ControlMail implements ActionListener, ListSelectionListener
{

    private VistaNubEmail ventana;
    private VistaMail pnlMail;
    private VistaRedactar pnlRedactar;
    private ModeloLogInMail datos;

    public ControlMail(VistaNubEmail vent, ModeloLogInMail m)
    {
        ventana = vent;
        datos = m;
        pnlMail = vent.getPnlMail();
        pnlRedactar = pnlMail.getPnlRedactar();
        
        listaMensajes();
        for (int i = pnlMail.getListaMails().getListSelectionListeners().length; i > 0; i--)
        {
            pnlMail.getListaMails().removeListSelectionListener(this);
        }
        pnlMail.getListaMails().addListSelectionListener(this);
        ventana.pack();

        if (pnlRedactar.getBtnEnviar().getActionListeners().length == 0)
        {
            pnlRedactar.getBtnEnviar().addActionListener(this);
        }

        for (JButton b : ventana.getPnlMail().getBotones())
        {
            if (!b.getText().equals("Salir"))
            {
                if (b.getActionListeners().length == 0)
                {
                    b.addActionListener(this);
                }
            }
        }

        buscaBoton(pnlMail.getBotones(), "Cerrar Sesión").setEnabled(true);
        buscaBoton(pnlMail.getBotones(), "Iniciar Sesión").setEnabled(false);

    }

    //Eventos
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton btn = (JButton) e.getSource();

        switch (btn.getText())
        {

            case "Redactar":
            {
                pnlMail.getPnlRedactar().setVisible(true);
                pnlMail.getPnlLista().setVisible(false);
                ventana.pack();
            }
            break;

            case "Bandeja":
            {
                listaMensajes();
                pnlMail.getPnlRedactar().setVisible(false);
                pnlMail.getPnlLista().setVisible(true);
                ventana.pack();
            }
            break;

            case "Enviar":
            {
                enviar();

            }
            break;

            case "Cerrar Sesión":
            {
                cerrarSesion();
                pnlMail.getListaMails().removeListSelectionListener(this);
            }
            break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getSource() == pnlMail.getListaMails() && e.getValueIsAdjusting())
        {
            JList lista = pnlMail.getListaMails();
            int i = lista.getSelectedIndex();
            String rem, asunto, cont;
            rem = asunto = cont = "";

            if (i >= 0)
            {
                try
                {
                    //Sacar mensaje y mostrarlo
                    BufferedReader br;
                    String linea, comienzoGmail, stop;
                    boolean comCont = false, esGmail = false;

                    comienzoGmail = "text/plain";
                    stop = "text/html";

                    synchronized (datos.getClientePOP())
                    {
                        br = (BufferedReader) datos.getClientePOP().retrieveMessage(i + 1);

                        while ((linea = br.readLine()) != null)
                        {
                            if (comCont)
                            {
                                cont += "\n" + linea;
                            }

                            if (linea.contains("From"))
                            {
                                rem = linea.substring(linea.indexOf(" ") + 1);
                            } else if (linea.contains("Subject"))
                            {
                                asunto = linea.substring(linea.indexOf(" ") + 1);

                                if (!esGmail)
                                {
                                    comCont = true;
                                }
                            } else if (linea.contains(comienzoGmail))
                            {
                                comCont = true;
                                esGmail = true;
                            } else if (linea.contains(stop))
                            {
                                comCont = false;
                            }
                        }
                        br.close();
                    }

                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Error al cambiar al "
                            + "directorio padre.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException enp)
                {
                    //System.out.println("Hola peto");
                }

                new VistaCorreo(rem, datos.getUser(), asunto, cont);
            }
        }
    }

    // Métodos asociados a los eventos
    private void enviar()
    {
        String rem, dest, asunto, contenido;

        rem = datos.getUser();
        dest = pnlRedactar.getTxtDest().getText();
        asunto = pnlRedactar.getTxtAsunto().getText();
        contenido = pnlRedactar.getTxtContenido().getText();

        if (dest.isEmpty() || asunto.isEmpty() || contenido.isEmpty())
        {
            JOptionPane.showMessageDialog(ventana, "No debe haber ningún campo "
                    + "vacío.", "Error", JOptionPane.ERROR_MESSAGE);
        } else
        {

            try
            {
                // SMTP Key Authentication
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(null, null);
                KeyManager km = kmf.getKeyManagers()[0];

                datos.getClienteSMTP().connect(datos.getSmtpServ(), datos.getSmtpPort());
                System.out.println("1 - " + datos.getClienteSMTP().getReplyString());

                datos.getClienteSMTP().setKeyManager(km);

                int resp = datos.getClienteSMTP().getReplyCode();

                if (SMTPReply.isPositiveCompletion(resp))
                {
                    datos.getClienteSMTP().ehlo(datos.getSmtpServ());
                    System.out.println("2 - " + datos.getClienteSMTP().getReplyString());

                    if (datos.getClienteSMTP().execTLS())
                    {
                        System.out.println("3 - " + datos.getClienteSMTP().getReplyString());

                        if (datos.getClienteSMTP().auth(AuthenticatingSMTPClient.AUTH_METHOD.PLAIN,
                                datos.getUser(), datos.getPass()))
                        {
                            System.out.println("4 - " + datos.getClienteSMTP().getReplyString());

                            // Envío de cabecera
                            SimpleSMTPHeader cabecera = new SimpleSMTPHeader(rem, dest, asunto);

                            datos.getClienteSMTP().setSender(rem);
                            datos.getClienteSMTP().addRecipient(dest);

                            Writer w = datos.getClienteSMTP().sendMessageData();

                            if (w != null)
                            {
                                w.write(cabecera.toString());
                                System.out.println("5 - " + datos.getClienteSMTP().getReplyString());
                                w.write(contenido);
                                System.out.println("6 - " + datos.getClienteSMTP().getReplyString());
                                w.close();
                            }

                            if (!datos.getClienteSMTP().completePendingCommand())
                            {
                                JOptionPane.showMessageDialog(ventana, "Error al finalizar "
                                        + "envío de mail.", "Error", JOptionPane.INFORMATION_MESSAGE);
                            } else
                            {
                                JOptionPane.showMessageDialog(ventana, "Mail enviado con éxito. ",
                                        "Información", JOptionPane.INFORMATION_MESSAGE);
                            }

                        }
                    }
                }

                vaciarCampos();

            } catch (NoSuchAlgorithmException ex)
            {
                System.out.println("Error con el algorirmo de la key del SMTP.");
            } catch (KeyStoreException | UnrecoverableKeyException ex)
            {
                System.out.println("Error al almacenar o recuperar la key del SMTP.");
            } catch (InvalidKeyException | InvalidKeySpecException ex)
            {
                System.out.println("Error, key del SMTP no válida.");
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(ventana, "Error al enviar mail.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cerrarSesion()
    {
        try
        {
            // Desconexión de POP3
            synchronized (datos.getClientePOP())
            {
                if (datos.getClientePOP().isConnected())
                {
                    datos.getClientePOP().logout();
                    datos.getClientePOP().disconnect();
                }
            }

            buscaBoton(pnlMail.getBotones(), "Cerrar Sesión").setEnabled(false);
            buscaBoton(pnlMail.getBotones(), "Iniciar Sesión").setEnabled(true);

            for (JButton b : pnlMail.getBotones())
            {
                if (!b.getText().equals("Iniciar Sesión") || !b.getText().equals("Salir"))
                {
                    b.removeActionListener(this);
                }
            }

            DefaultListModel modelo = (DefaultListModel) pnlMail.getListaMails().getModel();
            modelo.removeAllElements();
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error al "
                    + "desconectar.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void listaMensajes()
    {
        try
        {
            POP3MessageInfo[] mensajes = datos.getClientePOP().listMessages();

            if (mensajes == null)
            {
                throw new IOException();
            } else
            {
                llenarLista(mensajes);
                ventana.pack();
            }

        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(ventana, "Error al listar mails.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarLista(POP3MessageInfo[] lista)
    {
        pnlMail.getListaMails().removeAll();

        DefaultListModel modeloLista = new DefaultListModel();
        BufferedReader br;
        String linea, lineaLista;

        //modeloLista.addElement("Remitente ----------------------------- Asunto");

        for (POP3MessageInfo m : lista)
        {
            try
            {
                br = (BufferedReader) datos.getClientePOP().retrieveMessageTop(m.number, 0);
                lineaLista = "";

                while ((linea = br.readLine()) != null)
                {
                    if (linea.contains("From"))
                    {
                        lineaLista += linea.substring(linea.indexOf(" "));
                    } else if (linea.contains("Subject"))
                    {
                        lineaLista += " - \t" + linea.substring(linea.indexOf(" "));
                    }
                }

                modeloLista.addElement(lineaLista);
                pnlMail.getPnlLista().removeAll();
                JScrollPane p = new JScrollPane(pnlMail.getListaMails());
                p.setPreferredSize(new Dimension(300, 200));
                pnlMail.getPnlLista().add(p);

                br.close();

            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(ventana, "Error al recuperar "
                        + "cabeceras de mails.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        pnlMail.getListaMails().setModel(modeloLista);
    }

    // Métodos auxiliares
    private JButton buscaBoton(ArrayList<JButton> b, String s)
    {
        int c = 0;

        while (c < b.size() && !b.get(c).getText().equals(s))
        {
            c++;
        }

        return b.get(c);
    }

    private void vaciarCampos()
    {
        VistaRedactar p = pnlMail.getPnlRedactar();

        p.getTxtDest().setText("");
        p.getTxtAsunto().setText("");
        p.getTxtContenido().setText("");
    }

    // Getters
    public ModeloLogInMail getDatos()
    {
        return datos;
    }

}
