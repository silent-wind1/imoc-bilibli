package com.imooc.com.imooc.bilibli.api;

import com.imooc.bilibli.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoApi {
    @Autowired
    private DemoService demoService;

    @GetMapping("/query")
    public Long query(Long id) {
        return demoService.query(id);
    }

    public void test() {
        System.out.println("简单测试");
    }
}
