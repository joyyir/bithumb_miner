package com.joyyir.bithumb_miner.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @GetMapping(path = "/mining")
    public String mining() {
        return "success";
    }
}
