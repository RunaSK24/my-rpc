package cn.edu.szu.examplespringbootconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ConsumerServiceTest {
    @Resource
    private ConsumerServiceImpl consumerService;

    @Test
    void test() {
        consumerService.test();
    }
}
