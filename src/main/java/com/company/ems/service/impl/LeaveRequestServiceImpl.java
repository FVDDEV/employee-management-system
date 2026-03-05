package com.company.ems.service.impl;

import com.company.ems.dto.request.LeaveRequestCreateRequest;
import com.company.ems.dto.response.LeaveRequestResponse;
import com.company.ems.entity.Employee;
import com.company.ems.entity.LeaveRequest;
import com.company.ems.enums.LeaveStatus;
import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import com.company.ems.mapper.LeaveRequestMapper;
import com.company.ems.messaging.producer.LeaveEventProducer;
import com.company.ems.repository.EmployeeRepository;
import com.company.ems.repository.LeaveRequestRepository;
import com.company.ems.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveEventProducer leaveEventProducer;


    @Override
    public LeaveRequestResponse applyLeave(
            LeaveRequestCreateRequest request) {

        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException(
                    "Start date cannot be after end date");
        }

        boolean overlapExists =
                leaveRequestRepository
                        .existsByEmployeeAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                employee,
                                List.of(
                                        LeaveStatus.PENDING,
                                        LeaveStatus.APPROVED
                                ),
                                request.getEndDate(),
                                request.getStartDate()
                        );

        if (overlapExists) {
            throw new BadRequestException(
                    "Leave already exists for the given date range");
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus(LeaveStatus.PENDING);

        LeaveRequest savedLeave =
                leaveRequestRepository.save(leaveRequest);

        leaveEventProducer.publishLeaveApplied(savedLeave);

        return LeaveRequestMapper.toResponse(savedLeave);
    }


    @Override
    public LeaveRequestResponse updateLeaveStatus(
            Long leaveId,
            String status) {

        LeaveRequest leaveRequest =
                leaveRequestRepository
                        .findById(leaveId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Leave request not found"));

        LeaveStatus leaveStatus = switch (status.toUpperCase()) {
            case "APPROVED" -> LeaveStatus.APPROVED;
            case "REJECTED" -> LeaveStatus.REJECTED;
            case "PENDING"  -> LeaveStatus.PENDING;
            default -> throw new BadRequestException(
                    "Invalid leave status");
        };

        leaveRequest.setStatus(leaveStatus);

        LeaveRequest updatedLeave =
                leaveRequestRepository.save(leaveRequest);

        leaveEventProducer.publishLeaveStatusChanged(updatedLeave);

        return LeaveRequestMapper.toResponse(updatedLeave);
    }


    @Override
    public List<LeaveRequestResponse> getLeavesByEmployee(
            Long employeeId) {

        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        return leaveRequestRepository
                .findByEmployee(employee)
                .stream()
                .map(LeaveRequestMapper::toResponse)
                .toList();
    }
}
