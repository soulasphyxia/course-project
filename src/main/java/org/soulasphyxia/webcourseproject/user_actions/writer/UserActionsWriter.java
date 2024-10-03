package org.soulasphyxia.webcourseproject.user_actions.writer;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.user_actions.LogType;

import java.io.InputStream;
import java.util.List;

public interface UserActionsWriter {

    InputStream write(List<UserAction> userActions);

    LogType getLogType();
}
