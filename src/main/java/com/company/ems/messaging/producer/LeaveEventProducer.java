package com.company.ems.messaging.producer;

import com.company.ems.dto.event.NotificationEvent;
import com.company.ems.entity.LeaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveEventProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "ems.exchange";
    private static final String ROUTING_KEY = "notification.key";

    public void publishLeaveApplied(LeaveRequest leave) {
        try {
            NotificationEvent event = new NotificationEvent();
            event.setEventType("LEAVE_APPLIED");
            event.setEmail(leave.getEmployee().getEmail());
            event.setEmployeeName(leave.getEmployee().getFullName());
            event.setLeaveId(leave.getLeaveId());
            event.setStartDate(leave.getStartDate());
            event.setEndDate(leave.getEndDate());
            event.setLeaveStatus("PENDING");

            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
            log.info("LEAVE_APPLIED event published | leaveId={}", leave.getLeaveId());

        } catch (Exception ex) {
            log.error("Failed to publish LEAVE_APPLIED event", ex);
            throw new RuntimeException("Leave applied notification failed");
        }
    }


    public void publishLeaveStatusChanged(LeaveRequest leave) {
        try {
            NotificationEvent event = new NotificationEvent();
            event.setEventType("LEAVE_STATUS_CHANGED");
            event.setEmail(leave.getEmployee().getEmail());
            event.setEmployeeName(leave.getEmployee().getFullName());
            event.setLeaveId(leave.getLeaveId());
            event.setStartDate(leave.getStartDate());
            event.setEndDate(leave.getEndDate());
            event.setLeaveStatus(leave.getStatus().name());

            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
            log.info("LEAVE_STATUS_CHANGED event published | leaveId={} | status={}",
                    leave.getLeaveId(), leave.getStatus());

        } catch (Exception ex) {
            log.error("Failed to publish LEAVE_STATUS_CHANGED event", ex);
            throw new RuntimeException("Leave status notification failed");
        }
    }
}
