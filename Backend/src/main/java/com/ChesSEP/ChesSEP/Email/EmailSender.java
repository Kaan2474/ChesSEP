package com.ChesSEP.ChesSEP.Email;

public interface EmailSender {
    void send(Long user_id, Long to, String subject, String msg);
    void sendOTP(Long user_id, String msg);
}
