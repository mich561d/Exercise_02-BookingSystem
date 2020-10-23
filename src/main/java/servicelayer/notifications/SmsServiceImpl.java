package servicelayer.notifications;

import dto.SmsMessage;

public class SmsServiceImpl implements SmsService {

    @Override
    public boolean sendSms(SmsMessage message) {
        System.out.println("Sending sms");
        System.out.println("Recipient:" + message.getRecipient());
        System.out.println(message.getMessage());
        return true;
    }
}
