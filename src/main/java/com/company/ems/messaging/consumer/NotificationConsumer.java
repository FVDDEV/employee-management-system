package com.company.ems.messaging.consumer;

import com.company.ems.dto.event.NotificationEvent;
import com.company.ems.service.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "notification.queue")
    public void consume(NotificationEvent event) {

        log.info("Notification received: {}", event);

        try {
            switch (event.getEventType()) {

                case "EMPLOYEE_CREATED" ->
                        sendEmployeeWelcomeMail(event);

                case "LEAVE_APPLIED" ->
                        sendLeaveAppliedMail(event);

                case "LEAVE_STATUS_CHANGED" ->
                        sendLeaveStatusMail(event);

                default ->
                        throw new IllegalArgumentException(
                                "Unknown event type");
            }

        } catch (Exception ex) {
            log.error("Notification failed → DLQ", ex);
            throw ex;
        }
    }

    private void sendEmployeeWelcomeMail(NotificationEvent e) {

        String subject = "Welcome Notification";

        String body =
                "Dear " + e.getEmployeeName() + ",\n\n"
                        + "Welcome to the company!\n\n"
                        + "Employee ID : " + e.getEmployeeId() + "\n"
                        + "Employee name  : " + e.getEmployeeName() + "\n"
                        + "Email address  : " + e.getEmail() + "\n"
                        + "Department  : " + e.getDepartmentName() + "\n\n"
                        + "Best Regards,\nHR Team";

        emailService.sendEmail(e.getEmail(), subject, body);
    }

    private void sendLeaveAppliedMail(NotificationEvent e) {

        String subject = "Leave Request Submitted";

        String body =
                "Dear " + e.getEmployeeName() + ",\n\n"
                        + "Your leave request has been submitted.\n\n"
                        + "Leave ID : " + e.getLeaveId() + "\n"
                        + "From     : " + e.getStartDate() + "\n"
                        + "To       : " + e.getEndDate() + "\n"
                        + "Status   : PENDING\n\n"
                        + "HR Team";

        emailService.sendEmail(e.getEmail(), subject, body);
    }

    private void sendLeaveStatusMail(NotificationEvent e) {

        String subject = "Leave Status Updated";

        String body =
                "Dear " + e.getEmployeeName() + ",\n\n"
                        + "Your leave request status has been updated.\n\n"
                        + "Leave ID : " + e.getLeaveId() + "\n"
                        + "From     : " + e.getStartDate() + "\n"
                        + "To       : " + e.getEndDate() + "\n"
                        + "Status   : " + e.getLeaveStatus() + "\n\n"
                        + "HR Team";

        emailService.sendEmail(e.getEmail(), subject, body);
    }
}
