package com.lierlier.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {
    @Test
    void contextLoads() {
        try {
            int i = 8 / 0;
            System.out.println("8/0");
        } catch (Exception e) {
            System.out.println("catch");
        }
        System.out.println("end");
    }

}
