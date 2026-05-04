package com.app.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Hello", description = "Health/sample endpoints")
public class HelloController {

    @ResponseBody
    @GetMapping(value="/hello")
    @Operation(summary = "Hello endpoint", description = "Public sample endpoint")
    public Map<String, String> sample1() {
        return Collections.singletonMap("key", "Hello world.");
    }
}
