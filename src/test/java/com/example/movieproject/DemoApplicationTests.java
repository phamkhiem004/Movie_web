package com.example.movieproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // Test này chỉ đơn giản là kiểm tra xem Spring Boot có khởi động thành công không
    }

}
