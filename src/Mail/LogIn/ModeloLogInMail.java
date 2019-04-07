/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail.LogIn;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.commons.net.pop3.POP3SClient;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;

/**
 * Almacena todos los datos necesarios para las conexiones de los servicios de
 * email (SMTP Y POP3).
 */
public class ModeloLogInMail
{

    // SMTP
    // GMAIL
    public static final String SMTP_GMAIL_SERV = "smtp.gmail.com";
    public static final int SMTP_GMAIL_PORT = 587;
    // OUTLOOK
    public static final String SMTP_OUTLOOK_SERV = "smtp-mail.outlook.com";
    public static final int SMTP_OUTLOOK_PORT = 587;
    // YAHOO
    public static final String SMTP_YAHOO_SERV = "smtp.mail.yahoo.com";
    public static final int SMTP_YAHOO_PORT1 = 587;
    public static final int SMTP_YAHOO_PORT2 = 465;
    // LOCAL
    public static final String SMTP_LOCAL_SERV = "localhost";
    public static final int SMTP_LOCAL_PORT = 25;

    // POP
    public static final String POP_GMAIL_SERV = "pop.gmail.com";
    public static final int POP_GMAIL_PORT = 995;
    // OUTLOOK
    public static final String POP_OUTLOOK_SERV = "pop-mail.outlook.com";
    public static final int POP_OUTLOOK_PORT = 995;
    // YAHOO
    public static final String POP_YAHOO_SERV = "pop.mail.yahoo.com";
    public static final int POP_YAHOO_PORT = 995;
    // LOCAL
    public static final String POP_LOCAL_SERV = "localhost";
    public static final int POP_LOCAL_PORT = 110;

    private AuthenticatingSMTPClient clienteSMTP;
    private POP3SClient clientePOP;
    private String user, pass, smtpServ, popServ;
    private int smtpPort, popPort;

    public ModeloLogInMail(String u, String c)
    {
        user = u;
        pass = c;

        clienteSMTP = new AuthenticatingSMTPClient();
        clientePOP = new POP3SClient(true);
    }

    public boolean conectar()
    {
        boolean logged = false;

        definirSMTPOP();

        try
        {
            clientePOP.connect(popServ, popPort);
            System.out.println("" + clientePOP.getReplyString());

            logged = clientePOP.login(user, pass);

            if (!logged)
            {
                JOptionPane.showMessageDialog(null, "ERROR al iniciar "
                        + "sesión POP.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println();
            }
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "ERROR al conectar con el "
                    + "servidor POP: ", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("ERROR al conectar con el servidor POP: ");
            ex.printStackTrace();
        }

        return logged;
    }

    private void definirSMTPOP()
    {
        if (user.contains("@gmail"))
        {
            smtpServ = SMTP_GMAIL_SERV;
            smtpPort = SMTP_GMAIL_PORT;

            popServ = POP_GMAIL_SERV;
            popPort = POP_GMAIL_PORT;
        } else if (user.contains("@outlook") || user.contains("@hotmail"))
        {
            smtpServ = SMTP_OUTLOOK_SERV;
            smtpPort = SMTP_OUTLOOK_PORT;

            popServ = POP_OUTLOOK_SERV;
            popPort = POP_OUTLOOK_PORT;
        } else if (user.contains("@yahoo"))
        {
            smtpServ = SMTP_YAHOO_SERV;
            smtpPort = SMTP_YAHOO_PORT1;

            popServ = POP_YAHOO_SERV;
            popPort = POP_YAHOO_PORT;
        } else
        {
            smtpServ = SMTP_LOCAL_SERV;
            smtpPort = SMTP_LOCAL_PORT;

            popServ = POP_LOCAL_SERV;
            popPort = POP_LOCAL_PORT;
        }
    }

    // Getters
    public AuthenticatingSMTPClient getClienteSMTP()
    {
        return clienteSMTP;
    }

    public POP3SClient getClientePOP()
    {
        return clientePOP;
    }

    public String getUser()
    {
        return user;
    }

    public String getPass()
    {
        return pass;
    }

    public String getSmtpServ()
    {
        return smtpServ;
    }

    public String getPopServ()
    {
        return popServ;
    }

    public int getSmtpPort()
    {
        return smtpPort;
    }

    public int getPopPort()
    {
        return popPort;
    }

}
