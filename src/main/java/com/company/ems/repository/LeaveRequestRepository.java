package com.company.ems.repository;

import com.company.ems.entity.LeaveRequest;
import com.company.ems.entity.Employee;
import com.company.ems.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    boolean existsByEmployeeAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Employee employee,
            List<LeaveStatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );

    boolean existsByEmployeeAndStartDateAndEndDateAndStatusIn(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate,
            List<LeaveStatus> statuses
    );

}
