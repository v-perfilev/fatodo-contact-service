package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
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
class WsProducerIT {

    private static final UUID USER_ID_1 = UUID.randomUUID();
    private static final UUID USER_ID_2 = UUID.randomUUID();
    private static final UUID USER_ID_3 = UUID.randomUUID();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RequestService requestService;
    @Autowired
    RequestRepository requestRepository;

    @MockBean
    UserServiceClient userServiceClient;

    @SpyBean
    WsServiceClient wsServiceClient;


    private ConcurrentMessageListenerContainer<String, String> wsContactContainer;
    private BlockingQueue<ConsumerRecord<String, String>> wsContactRecords;
    private ConcurrentMessageListenerContainer<String, String> wsClearContainer;
    private BlockingQueue<ConsumerRecord<String, String>> wsClearRecords;


    @BeforeEach
    void setup() {
        when(userServiceClient.doesIdExist(any())).thenReturn(true);

        Request requestOneTwo = TestRequest.defaultBuilder()
                .requesterId(USER_ID_1)
                .recipientId(USER_ID_2)
                .build().toParent();
        requestRepository.save(requestOneTwo);

        startWsContactConsumer();
        startWsClearConsumer();
    }

    @AfterEach
    void cleanup() {
        requestRepository.deleteAll();

        stopWsContactConsumer();
        stopWsClearConsumer();
    }

    @Test
    void testSendContactRequestEvent() throws Exception {
        requestService.send(USER_ID_1, USER_ID_3, null);

        ConsumerRecord<String, String> record = wsContactRecords.poll(10, TimeUnit.SECONDS);

        assertThat(wsServiceClient).isInstanceOf(WsProducer.class);
        assertThat(record).isNotNull();
        verify(wsServiceClient).sendRequestIncomingEvent(any());
        verify(wsServiceClient).sendRequestOutcomingEvent(any());
    }

    @Test
    void testSendContactAcceptEvent() throws Exception {
        requestService.accept(USER_ID_1, USER_ID_2);

        ConsumerRecord<String, String> record = wsContactRecords.poll(5, TimeUnit.SECONDS);

        assertThat(wsServiceClient).isInstanceOf(WsProducer.class);
        assertThat(record).isNotNull();
        verify(wsServiceClient).sendAcceptIncomingEvent(any());
        verify(wsServiceClient).sendAcceptOutcomingEvent(any());
    }

    @Test
    void testSendClearEvent() throws Exception {
        requestService.remove(USER_ID_1, USER_ID_2);

        ConsumerRecord<String, String> record = wsClearRecords.poll(5, TimeUnit.SECONDS);

        assertThat(wsServiceClient).isInstanceOf(WsProducer.class);
        assertThat(record).isNotNull();
        verify(wsServiceClient, times(2)).sendClearEvent(any());
    }


    private void startWsContactConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> stringContainerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        wsContactContainer = stringContainerFactory.createContainer("ws_contact");
        wsContactRecords = new LinkedBlockingQueue<>();
        wsContactContainer.setupMessageListener((MessageListener<String, String>) wsContactRecords::add);
        wsContactContainer.start();
        ContainerTestUtils.waitForAssignment(wsContactContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopWsContactConsumer() {
        wsContactContainer.stop();
    }

    private void startWsClearConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> stringContainerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        wsClearContainer = stringContainerFactory.createContainer("ws_clear");
        wsClearRecords = new LinkedBlockingQueue<>();
        wsClearContainer.setupMessageListener((MessageListener<String, String>) wsClearRecords::add);
        wsClearContainer.start();
        ContainerTestUtils.waitForAssignment(wsClearContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopWsClearConsumer() {
        wsClearContainer.stop();
    }

}
