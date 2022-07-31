package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ClearEventDTO;
import com.persoff68.fatodo.model.dto.RequestEventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WsServiceClientWrapper implements WsServiceClient {

    @Qualifier("feignWsServiceClient")
    private final WsServiceClient wsServiceClient;

    @Override
    public void sendRequestIncomingEvent(WsEventDTO<RequestEventDTO> event) {
        try {
            wsServiceClient.sendRequestIncomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendRequestOutcomingEvent(WsEventDTO<RequestEventDTO> event) {
        try {
            wsServiceClient.sendRequestOutcomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendAcceptIncomingEvent(WsEventDTO<RequestEventDTO> event) {
        try {
            wsServiceClient.sendAcceptIncomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendAcceptOutcomingEvent(WsEventDTO<RequestEventDTO> event) {
        try {
            wsServiceClient.sendAcceptOutcomingEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void sendClearEvent(WsEventDTO<ClearEventDTO> event) {
        try {
            wsServiceClient.sendClearEvent(event);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
