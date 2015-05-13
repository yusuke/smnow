package smnow.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smnow.AttendanceListener;
import smnow.GlobalProp;
import smnow.Person;
import smnow.SMStatus;
import smnow.hall.ExtendedOptional;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yusuke on 5/13/15.
 * <p>
 * Copyright 2015 yusuke
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SlackListner implements AttendanceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackListner.class);

    private static final String SMNW = "ズムばう";

    private static final String HALL_API_URL = "https://hall.com/api/1/services/generic/";

    private static final String SLACK_KEY = "slack.key";

    private final boolean dryRun;

    private final ExtendedOptional<String> hallKey;

    public SlackListner(boolean dryRun) {
        this.dryRun = dryRun;
        this.hallKey = ExtendedOptional.ofNullable(GlobalProp.getProperty(SLACK_KEY));
        this.hallKey
                .ifPresent(key -> LOGGER.debug("hall.key=" + this.hallKey.get()))
                .ifNotPresent(() -> LOGGER.debug("hall.key not set"));
    }

    @Override
    public void entered(Person person) {
        if (!dryRun)
            sendMessage(SMStatus.SMIN.message(person));
    }

    @Override
    public void left(Person person) {
        if (!dryRun)
            sendMessage(SMStatus.SMOUT.message(person));
    }

    @Override
    public void allLeft(Person person) {

    }

    @Override
    public void firstEntered(Person person) {

    }

    private void sendMessage(String message) {
        hallKey.ifPresent(key -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(HALL_API_URL + key);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                PrintWriter writer = new PrintWriter(connection.getOutputStream());
                writer.print(toJson(message));
                writer.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode != 201) {
                    LOGGER.info("エラーが返されたわー : " + responseCode);
                }
                LOGGER.info("Hallにメッセージ投げられたはず！");
            } catch (Exception e) {
                LOGGER.info("エラーが返されたわー ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    String toJson(String message) {
        return "{\"title\":\"" + SMNW + "\",\"message\":\"" +
                message.replaceAll("\"", "\\\\\"") +
                "\"}";
    }
}
