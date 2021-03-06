package com.github.cloudyrock.mongock.changelogs.client.initializer;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.client.Client;
import com.github.cloudyrock.mongock.client.ClientRepository;
import io.changock.migration.api.annotations.NonLockGuarded;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "1")
public class ClientInitializerChangeLog {

    public final static int INITIAL_CLIENTS = 10;

    @ChangeSet(id = "data-initializer-with-repository", order = "001", author = "mongock")
    public void dataInitializer(ClientRepository clientRepository, @NonLockGuarded RestTemplate restTemplate) {
        clientRepository.saveAll(
                IntStream.range(0, INITIAL_CLIENTS)
                        .mapToObj(ClientInitializerChangeLog::getClient)
                        .collect(Collectors.toList())
        );
    }

    private static Client getClient(int i) {
        return new Client("name-" + i, "email-" + i, "phone" + i, "country" + i);
    }
}
