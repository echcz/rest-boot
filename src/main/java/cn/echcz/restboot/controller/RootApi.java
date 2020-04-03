package cn.echcz.restboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RootApi {
    @GetMapping("/ping/{name}")
    public String ping(@PathVariable String name) {
        if ("nothing".equals(name)) {
            return null;
        }
        if ("error".equals(name)) {
            throw new RuntimeException();
        }
        return name;
    }
}
