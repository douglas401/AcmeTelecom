package com.acmetelecom.unit;

import com.acmetelecom.billing.BillingSystem;
import com.acmetelecom.billing.IBillGenerator;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class BillingSystemTest {

    Mockery context = new Mockery();

    IBillGenerator generator = context.mock(IBillGenerator.class);
    BillingSystem system = new BillingSystem(generator);

    // TODO: Write unit tests for BillingSystem
    @Test
    public void testAnything() throws Exception {
        // Write any unit tests with expectations and additional mocks
    }
}
