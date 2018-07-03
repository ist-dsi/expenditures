package pt.ist.expenditureTrackingSystem.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.BarcodeQRCode;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class QRCodeGenerator {

    public static byte[] generate(String identifier, int width, int height) throws NullPointerException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            final BarcodeQRCode qrcode = new BarcodeQRCode(identifier, width, height, null);
            final Image awtImage = qrcode.createAwtImage(Color.BLACK, Color.WHITE);
            final BufferedImage buffer =
                    new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
            buffer.getGraphics().drawImage(awtImage, 0, 0, null);
            ImageIO.write(buffer, "png", bytes);
            return bytes.toByteArray();
        } catch (final IOException e) {
            throw new NullPointerException("Error while generating qr code for identifier " + identifier);
        }
    }
}