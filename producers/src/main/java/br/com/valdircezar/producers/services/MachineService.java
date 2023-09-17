package br.com.valdircezar.producers.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.dtos.MachineEventDTO;
import models.requests.CreateMachineRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
@Log4j2
@RequiredArgsConstructor
public class MachineService {

    private final KafkaTemplate<String, Object> kaTemplate;

    public void createEvent(CreateMachineRequest request) {
        kaTemplate.send("topic-" + request.machineId(), new MachineEventDTO(request))
                .whenComplete(getResultSendMessage());
    }

    private static BiConsumer<SendResult<String, Object>, Throwable> getResultSendMessage() {
        return (success, ex) -> {
            if (ex != null) {
                log.error("::: MACHINE_SERVICE | ERROR SEND MESSAGE :::");
            } else {
                log.info("::: MACHINE_SERVICE | SEND MESSAGE SUCCESSFULLY :::");
            }
        };
    }
}


