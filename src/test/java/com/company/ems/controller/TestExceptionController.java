package com.company.ems.controller;


import com.company.ems.exception.BadRequestException;
import com.company.ems.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestExceptionController {

    @PostMapping("/validation")
    public String validation(@Valid @RequestBody TestDto dto) {
        return "ok";
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new ResourceNotFoundException("Not found");
    }

    @GetMapping("/bad-request")
    public void badRequest() {
        throw new BadRequestException("Bad request");
    }

    @GetMapping("/error")
    public void error() {
        throw new RuntimeException("Something broke");
    }

    public static class TestDto {
        @NotBlank
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
