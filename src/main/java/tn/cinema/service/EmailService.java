package tn.cinema.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.cinema.entity.Reservation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(Reservation reservation) throws Exception {
        String to = reservation.getClient().getEmail();
        String subject = "Votre réservation CinéMax a été confirmée !";

        String htmlContent = buildEmailContent(reservation);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Générer et attacher le QR Code
        byte[] qrCodeImage = generateQRCode(reservation);
        helper.addAttachment("ticket-qr.png", new ByteArrayResource(qrCodeImage));

        mailSender.send(message);
    }

    private String buildEmailContent(Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return """
            <h2>Confirmation de réservation - CinéMax</h2>
            <p>Bonjour <strong>%s</strong>,</p>
            <p>Votre réservation a été <strong>confirmée</strong> avec succès !</p>
            
            <h3>Détails de la réservation :</h3>
            <ul>
                <li><strong>Film :</strong> %s</li>
                <li><strong>Séance :</strong> %s</li>
                <li><strong>Salle :</strong> %s</li>
                <li><strong>Sièges :</strong> %s</li>
            </ul>
            
            <p><strong>Présentez ce QR Code à l'entrée.</strong></p>
            <p>Merci et bon film ! 🍿</p>
            """.formatted(
                reservation.getClient().getNom(),
                reservation.getSeance().getFilm().getTitre(),
                reservation.getSeance().getDateHeure().format(formatter),
                reservation.getSeance().getSalle().getNom(),
                reservation.getSieges().stream()
                        .map(s -> s.getRangee() + s.getNumero())
                        .reduce((a,b) -> a + ", " + b).orElse("")
        );
    }

    private byte[] generateQRCode(Reservation reservation) throws Exception {
        String qrText = "RES-" + reservation.getId() +
                "|Film:" + reservation.getSeance().getFilm().getTitre() +
                "|Date:" + reservation.getSeance().getDateHeure();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
