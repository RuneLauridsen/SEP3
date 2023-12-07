package boardgames.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class LogicApplicationTests {

    private RunLogicServer logicServer;
    private String simonJwt;

    @BeforeEach
    void setup() {
        logicServer = new RunLogicServer();
        logicServer.run();
    }

    void setdown() {
        logicServer.shutdown();
    }

    @Test
    void contextLoads() {
    }

}
