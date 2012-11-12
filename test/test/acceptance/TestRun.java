package test.acceptance;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TestRun {
    Mockery mock = new JUnit4Mockery();

    @Test
    public void TestRunWithNoCalls() throws Exception {
        System.out.println("making conflict");
    }
}
