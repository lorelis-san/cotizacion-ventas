package com.lorelis.cotizacion.service.usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoVerificacion(String toEmail, String token) {
        String link = "http://localhost:8080/usuario/verificar?token=" + token;

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(toEmail);
        mensaje.setSubject("Verifica tu cuenta");
        mensaje.setText("Haz clic en este enlace para verificar tu cuenta:\n" + link);
        mensaje.setFrom("tucorreo@gmail.com");

        mailSender.send(mensaje);
    }
}
