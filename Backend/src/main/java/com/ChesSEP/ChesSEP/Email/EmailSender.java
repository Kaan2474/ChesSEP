package com.ChesSEP.ChesSEP.Email;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void send(Long user_id, Long to, String subject, String msg);
}
