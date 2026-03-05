package com.company.ems.service;

import com.company.ems.dto.request.LeaveRequestCreateRequest;
import com.company.ems.entity.Employee;
import com.company.ems.entity.LeaveRequest;
import com.company.ems.enums.LeaveStatus;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.messaging.producer.LeaveEventProducer;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.repository.LeaveRequestRepository;
import com.company.ems.service.impl.LeaveRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceImplTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveEventProducer leaveEventProducer;

    @InjectMocks
    private LeaveRequestServiceImpl leaveRequestService;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFullName("Bhavesh Zala");
        employee.setEmail("bhavesh@test.com");
    }


    @Test
    void applyLeave_success() {

        LeaveRequestCreateRequest request = new LeaveRequestCreateRequest();
        request.setEmployeeId(1L);
        request.setStartDate(LocalDate.now().plusDays(1));
        request.setEndDate(LocalDate.now().plusDays(3));
        request.setReason("Vacation");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(leaveRequestRepository
                .existsByEmployeeAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        any(), anyList(), any(), any()))
                .thenReturn(false);

        when(leaveRequestRepository.save(any(LeaveRequest.class)))
                .thenAnswer(invocation -> {
                    LeaveRequest leave = invocation.getArgument(0);
                    leave.setLeaveId(10L);
                    return leave;
                });

        var response = leaveRequestService.applyLeave(request);

        assertNotNull(response);
        verify(leaveEventProducer, times(1))
                .publishLeaveApplied(any(LeaveRequest.class));
    }


    @Test
    void applyLeave_employeeNotFound() {

        LeaveRequestCreateRequest request = new LeaveRequestCreateRequest();
        request.setEmployeeId(99L);
        request.setStartDate(LocalDate.now().plusDays(1));
        request.setEndDate(LocalDate.now().plusDays(2));
        request.setReason("Test");

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> leaveRequestService.applyLeave(request));

        verify(leaveEventProducer, never())
                .publishLeaveApplied(any());
    }


    @Test
    void applyLeave_invalidDateRange() {

        LeaveRequestCreateRequest request = new LeaveRequestCreateRequest();
        request.setEmployeeId(1L);
        request.setStartDate(LocalDate.now().plusDays(5));
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setReason("Invalid");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        assertThrows(BadRequestException.class,
                () -> leaveRequestService.applyLeave(request));
    }


    @Test
    void updateLeaveStatus_approved() {

        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(5L);
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);

        when(leaveRequestRepository.findById(5L))
                .thenReturn(Optional.of(leave));

        when(leaveRequestRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        leaveRequestService.updateLeaveStatus(5L, "APPROVED");

        assertEquals(LeaveStatus.APPROVED, leave.getStatus());

        verify(leaveEventProducer, times(1))
                .publishLeaveStatusChanged(any());
    }


    @Test
    void updateLeaveStatus_invalidStatus() {

        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1L);

        when(leaveRequestRepository.findById(1L))
                .thenReturn(Optional.of(leave));

        assertThrows(BadRequestException.class,
                () -> leaveRequestService.updateLeaveStatus(1L, "XYZ"));
    }
}
