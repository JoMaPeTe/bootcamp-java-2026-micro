package com.programandoenjava.chekinservice.controller;

import com.programandoenjava.chekinservice.dto.BoardingPassDto;
import com.programandoenjava.chekinservice.service.CheckinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkin")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    @PostMapping("/{bookingId}")
    public ResponseEntity<BoardingPassDto> createCheckin(@PathVariable String bookingId) {
        return ResponseEntity.ok(checkinService.doCheckIn(bookingId));
    }
}