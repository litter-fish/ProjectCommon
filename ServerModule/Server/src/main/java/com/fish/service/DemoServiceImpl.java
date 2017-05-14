package com.fish.service;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * Created by fish on 2017/5/14.
 */
@Service(version = "1.0.0")
public class DemoServiceImpl implements IDemoService {
    @Override
    public void sayHell(String name) {
        System.out.println("hello " + name);
    }
}
