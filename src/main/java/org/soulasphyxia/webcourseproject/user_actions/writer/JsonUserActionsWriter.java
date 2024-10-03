package org.soulasphyxia.webcourseproject.user_actions.writer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonUserActionsWriter implements UserActionsWriter {

    private final ObjectMapper objectMapper;
    @Getter
    private final LogType logType = LogType.JSON;

    public JsonUserActionsWriter() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public InputStream write(List<UserAction> userActions) {
        try {
            return new ByteArrayInputStream(objectMapper.writeValueAsBytes(userActions));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
