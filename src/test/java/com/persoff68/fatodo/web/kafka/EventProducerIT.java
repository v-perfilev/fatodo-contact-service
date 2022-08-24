package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.event.EventDTO;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import com.persoff68.fatodo.service.RelationService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "kafka.bootstrapAddress=localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1",
        "kafka.autoOffsetResetConfig=earliest"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class EventProducerIT {

    private static final UUID USER_ID_1 = UUID.randomUUID();
    private static final UUID USER_ID_2 = UUID.randomUUID();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RequestService requestService;
    @Autowired
    RelationService relationService;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    RelationRepository relationRepository;


    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    WsServiceClient wsServiceClient;

    @SpyBean
    EventServiceClient eventServiceClient;


    private ConcurrentMessageListenerContainer<String, EventDTO> eventContainer;
    private BlockingQueue<ConsumerRecord<String, EventDTO>> eventRecords;


    @BeforeEach
    void setup() {
        when(userServiceClient.doesIdExist(any())).thenReturn(true);

        Request requestOneTwo = TestRequest.defaultBuilder()
                .requesterId(USER_ID_1)
                .recipientId(USER_ID_2)
                .build().toParent();
        requestRepository.save(requestOneTwo);

        startEventConsumer();
    }

    @AfterEach
    void cleanup() {
        requestRepository.deleteAll();

        stopEventConsumer();
    }

    @Test
    void testAndEventEvent() throws Exception {
        requestService.accept(USER_ID_1, USER_ID_2);

        ConsumerRecord<String, EventDTO> record = eventRecords.poll(5, TimeUnit.SECONDS);

        assertThat(eventServiceClient).isInstanceOf(EventProducer.class);
        assertThat(record).isNotNull();
        verify(eventServiceClient, times(2)).addEvent(any());
    }


    private void startEventConsumer() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(EventDTO.class);
        ConcurrentKafkaListenerContainerFactory<String, EventDTO> containerFactory =
                KafkaUtils.buildJsonContainerFactory(embeddedKafkaBroker.getBrokersAsString(),
                        "test", "earliest", javaType);
        eventContainer = containerFactory.createContainer("event");
        eventRecords = new LinkedBlockingQueue<>();
        eventContainer.setupMessageListener((MessageListener<String, EventDTO>) eventRecords::add);
        eventContainer.start();
        ContainerTestUtils.waitForAssignment(eventContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopEventConsumer() {
        eventContainer.stop();
    }

}
