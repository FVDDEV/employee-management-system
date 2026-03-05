package com.company.ems.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface EmailService {

    public void sendEmail(
            String to,
            String subject,
            String body);
}
