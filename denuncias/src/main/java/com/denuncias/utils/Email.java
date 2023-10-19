package com.denuncias.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import com.github.hugoperlin.results.Resultado;

public class Email {

    private final static String USERNAME = Env.get("EMAIL_USERNAME");
    private final static String PASSWORD = Env.get("EMAIL_PASSWORD");

    public static Resultado send(String para, String nome, String senha) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(para));
            message.setSubject("Senha do App Denúncias");

            String corpoEmail = "Olá " + nome + ",<br/><br/>" +
                    "Esta é a sua senha para acessar o App: <b>" + senha + "</b><br/><br/>" +
                    "Fique seguro, att. Denúncias.";

            message.setContent(corpoEmail, "text/html");

            Transport.send(message);

            return Resultado.sucesso("Email enviado com sucesso.", null);
        } catch (MessagingException e) {
            return Resultado.erro(e.getMessage());
        }
    }
}
