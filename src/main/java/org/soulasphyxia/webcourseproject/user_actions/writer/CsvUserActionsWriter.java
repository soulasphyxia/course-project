package org.soulasphyxia.webcourseproject.user_actions.writer;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
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
public class CsvUserActionsWriter implements UserActionsWriter {

    @Override
    public InputStream write(List<UserAction> userActions) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
            CSVWriter writer = new CSVWriter(streamWriter);
            StatefulBeanToCsvBuilder<UserAction> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<UserAction> csv = builder
                    .withSeparator(';')
                    .build();
            csv.write(userActions);
            streamWriter.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (CsvException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LogType getLogType() {
        return LogType.CSV;
    }
}
