package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WsServiceClientWrapper implements WsServiceClient {

    @Qualifier("feignWsServiceClient")
    private final WsServiceClient wsServiceClient;

    @Override
    public void sendRequestIncomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendRequestIncomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendRequestOutcomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendRequestOutcomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendAcceptIncomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendAcceptIncomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendAcceptOutcomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendAcceptOutcomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendDeleteRequestIncomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendDeleteRequestIncomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendDeleteRequestOutcomingEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendDeleteRequestOutcomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendDeleteRelationEvent(WsEventDTO<UUID> event) {
        try {
            wsServiceClient.sendDeleteRelationEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
