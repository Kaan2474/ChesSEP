package com.ChesSEP.ChesSEP.Email;

public interface EmailSender {
    void send(Long user_id, Long to, String subject, String msg);
}
