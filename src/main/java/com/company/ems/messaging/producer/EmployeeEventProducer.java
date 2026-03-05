package com.company.ems.messaging.producer;

import com.company.ems.dto.event.NotificationEvent;
import com.company.ems.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "ems.exchange";
    private static final String ROUTING_KEY = "notification.key";

    public void publishEmployeeCreated(Employee employee) {

        NotificationEvent event = new NotificationEvent();
        event.setEventType("EMPLOYEE_CREATED");
        event.setEmail(employee.getEmail());
        event.setEmployeeId(employee.getEmployeeId());
        event.setEmployeeName(employee.getFullName());
        event.setDepartmentName(
                employee.getDepartment().getDepartmentName()
        );

        rabbitTemplate.convertAndSend(
                EXCHANGE,
                ROUTING_KEY,
                event
        );

        log.info("EMPLOYEE_CREATED event published for employeeId={}",
                employee.getEmployeeId());
    }
}
