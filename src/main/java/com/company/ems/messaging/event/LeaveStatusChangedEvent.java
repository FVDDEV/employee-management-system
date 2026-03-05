package com.company.ems.messaging.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveStatusChangedEvent {

    private Long leaveId;
    private Long employeeId;
    private String employeeName;
    private String oldStatus;
    private String newStatus;
}
