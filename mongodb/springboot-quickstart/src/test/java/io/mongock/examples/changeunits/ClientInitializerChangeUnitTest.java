package io.mongock.examples.changeunits;

import io.mongock.examples.client.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.Times;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * This unit test class shows an example(but incomplete) of how to provide unit test to your change units.
 *
 * At this level, this kind of testing doesn't differ much from any other unit test.
 */
class ClientInitializerChangeUnitTest {


    @Test
    void beforeExecution() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        new ClientInitializerChangeUnit().beforeExecution(mongoTemplate);

        ArgumentCaptor<String> collectionNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(mongoTemplate, new Times(1)).createCollection(collectionNameCaptor.capture());
        assertEquals("clientCollection", collectionNameCaptor.getValue());

    }

    @Test
    void execution() {
        ClientRepository repository = mock(ClientRepository.class);
        new ClientInitializerChangeUnit().execution(repository);

        ArgumentCaptor<List> itemsCaptor = ArgumentCaptor.forClass(List.class);
        verify(repository, new Times(1)).saveAll(itemsCaptor.capture());
        assertEquals(10, itemsCaptor.getValue().size());
        //more relevant unit testing

    }

    @Test
    void rollbackExecution() {
        ClientRepository repository = mock(ClientRepository.class);
        new ClientInitializerChangeUnit().rollbackExecution(repository);

        verify(repository, new Times(1)).deleteAll();

    }

}