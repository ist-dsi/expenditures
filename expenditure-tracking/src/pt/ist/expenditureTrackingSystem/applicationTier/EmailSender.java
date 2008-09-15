package pt.ist.expenditureTrackingSystem.applicationTier;

import sendmail.ArrayOfString;
import net.sourceforge.fenixedu.webServices.generatedStubs.SendMailClient;

public class EmailSender {

    public static void sendEmail(String istUsername, String topic, String message) {
	SendMailClient client = new SendMailClient();
	client.getSendMailHttpPort().sendMail("Central de Compras", "no-reply@compras.ist.utl.pt", istUsername, new ArrayOfString(), new ArrayOfString(), topic, message);
    }
}
