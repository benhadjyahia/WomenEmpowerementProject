package tn.esprit.spring.serviceInterface;

import tn.esprit.spring.entities.SmsRequest;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);
}
