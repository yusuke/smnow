package smnow.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smnow.AttendanceListener;
import smnow.GlobalProp;
import smnow.Person;
import smnow.SMStatus;
import smnow.hall.ExtendedOptional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
public class SlackListener implements AttendanceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackListener.class);

    private static final String SMNW = "ズムばう";

    private static final String SLACK_API_URL = "https://slack.com/api/chat.postMessage";

    private static final String SLACK_TOKEN = "slack.token";

    public static final String GENERAL_CHANNEL = "slack.channel.general";

    private final boolean dryRun;

    private final ExtendedOptional<String> slackToken;

    private final ExtendedOptional<String> channel;

    public SlackListener(boolean dryRun) {
        this.dryRun = dryRun;
        this.slackToken = getKey(SLACK_TOKEN);
        this.channel = getKey(GENERAL_CHANNEL);
    }

    private ExtendedOptional<String> getKey(String key) {
        ExtendedOptional<String> opValue = ExtendedOptional.ofNullable(GlobalProp.getProperty(key));
        opValue.ifPresent(k -> LOGGER.debug(key + "=" + k))
                .ifNotPresent(() -> LOGGER.debug(key + " not set"));
        return opValue;
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
        slackToken.ifPresent(key -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(SLACK_API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                PrintWriter writer = new PrintWriter(connection.getOutputStream());
                writer.append("token=").append(slackToken.get()).append('&')
                        .append("channel=").append(channel.get()).append('&')
                        .append("text=").append(URLEncoder.encode(message, "UTF-8")).append('&')
                        .append("username=").append(URLEncoder.encode(SMNW, "UTF-8")).flush();

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    LOGGER.info("スラックに送れんかった : " + responseCode);
                }
                LOGGER.info("slackにメッセージ投げられたはず！");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("エラーが返されたわー ", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
