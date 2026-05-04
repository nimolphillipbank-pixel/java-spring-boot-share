package com.app.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/requests")
@Tag(name = "Requests", description = "Request sample endpoints")
public class RequestController {
    @GetMapping()
    @Operation(summary = "Simple request endpoint")
    public ResponseEntity<String> request() {
        return ResponseEntity.ok("Hi");
    }
}
