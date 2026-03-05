package com.company.ems.controller;

import com.company.ems.dto.request.LeaveRequestCreateRequest;
import com.company.ems.dto.response.LeaveRequestResponse;
import com.company.ems.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> applyLeave(
            @Valid @RequestBody LeaveRequestCreateRequest request) {

        LeaveRequestResponse response =
                leaveRequestService.applyLeave(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{leaveId}/status")
    public ResponseEntity<LeaveRequestResponse> updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                leaveRequestService.updateLeaveStatus(leaveId, status)
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestResponse>> getLeavesByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveRequestService.getLeavesByEmployee(employeeId)
        );
    }
}
