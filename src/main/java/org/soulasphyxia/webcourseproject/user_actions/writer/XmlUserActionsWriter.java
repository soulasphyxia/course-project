package org.soulasphyxia.webcourseproject.user_actions.writer;

import com.thoughtworks.xstream.XStream;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Component
public class XmlUserActionsWriter implements UserActionsWriter{

    @Override
    public InputStream write(List<UserAction> userActions) {
        try {
            XStream xstream = new XStream();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            xstream.toXML(userActions, writer);
            writer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LogType getLogType() {
        return LogType.XML;
    }
}
