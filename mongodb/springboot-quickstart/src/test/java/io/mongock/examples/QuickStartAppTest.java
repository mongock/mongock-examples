package io.mongock.examples;

import io.mongock.examples.changelogs.ClientInitializerChangeLog;
import io.mongock.examples.client.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Quick example of testing Mongock migration with Springboot
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuickStartAppTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldStoreClientsInRepository() {
        assertEquals(ClientInitializerChangeLog.INITIAL_CLIENTS, clientRepository.findAll().size());

    }

}