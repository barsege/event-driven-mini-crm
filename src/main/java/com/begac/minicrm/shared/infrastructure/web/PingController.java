package com.begac.minicrm.shared.infrastructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private static final Logger log = LoggerFactory.getLogger(PingController.class);

    @GetMapping("/api/ping")
    public String ping() {
        log.info("Ping called");
        return "pong";
    }
}