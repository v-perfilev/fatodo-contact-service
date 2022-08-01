package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RequestRepository;
import com.persoff68.fatodo.service.RequestService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "kafka.bootstrapAddress=localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1",
        "kafka.autoOffsetResetConfig=earliest"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class EventProducerIT {

    private static final UUID USER_1_ID = UUID.fromString("98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d");
    private static final UUID USER_2_ID = UUID.fromString("8d583dfd-acfb-4481-80e6-0b46170e2a18");

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    RequestService requestService;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestRepository requestRepository;

    @MockBean
    UserServiceClient userServiceClient;

    @SpyBean
    EventServiceClient eventServiceClient;

    private ConcurrentMessageListenerContainer<String, String> eventAddContainer;
    private BlockingQueue<ConsumerRecord<String, String>> eventAddRecords;

    private ConcurrentMessageListenerContainer<String, String> eventDeleteContainer;
    private BlockingQueue<ConsumerRecord<String, String>> eventDeleteRecords;


    @BeforeEach
    void setup() {
        Request requestOneTwo = TestRequest.defaultBuilder()
                .requesterId(USER_1_ID)
                .recipientId(USER_2_ID)
                .build().toParent();
        requestRepository.save(requestOneTwo);

        startEventAddConsumer();
        startEventDeleteConsumer();
    }

    @AfterEach
    void cleanup() {
        requestRepository.deleteAll();

        stopEventAddConsumer();
        stopEventDeleteConsumer();
    }

    @Test
    void testSendContactEvent_ok() throws Exception {
        requestService.accept(USER_1_ID, USER_2_ID);

        ConsumerRecord<String, String> record = eventAddRecords.poll(5, TimeUnit.SECONDS);

        assertThat(eventServiceClient).isInstanceOf(EventProducer.class);
        assertThat(record).isNotNull();
        assertThat(record.key()).isEqualTo("contact");
        verify(eventServiceClient).addContactEvent(any());
    }

    @Test
    void testSendDeleteContactEvents_ok() throws Exception {
        requestService.remove(USER_1_ID, USER_2_ID);

        ConsumerRecord<String, String> record = eventDeleteRecords.poll(5, TimeUnit.SECONDS);

        assertThat(eventServiceClient).isInstanceOf(EventProducer.class);
        assertThat(record).isNotNull();
        assertThat(record.key()).isEqualTo("contact-delete");
        verify(eventServiceClient).deleteContactEvents(any());
    }


    private void startEventAddConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> stringContainerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        eventAddContainer = stringContainerFactory.createContainer("event_add");
        eventAddRecords = new LinkedBlockingQueue<>();
        eventAddContainer.setupMessageListener((MessageListener<String, String>) eventAddRecords::add);
        eventAddContainer.start();
        ContainerTestUtils.waitForAssignment(eventAddContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopEventAddConsumer() {
        eventAddContainer.stop();
    }

    private void startEventDeleteConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> stringContainerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        eventDeleteContainer = stringContainerFactory.createContainer("event_delete");
        eventDeleteRecords = new LinkedBlockingQueue<>();
        eventDeleteContainer.setupMessageListener((MessageListener<String, String>) eventDeleteRecords::add);
        eventDeleteContainer.start();
        ContainerTestUtils.waitForAssignment(eventDeleteContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopEventDeleteConsumer() {
        eventDeleteContainer.stop();
    }


}
