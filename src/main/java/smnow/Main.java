package smnow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by yusuke on 7/25/14.
 * <p>
 * Copyright 2014 yusuke
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
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));


    public static void main(String[] args) {
        Person[] persons = Person.load();
        Twitter twitter = TwitterFactory.getSingleton();
        boolean dryRun = false;

        Attendance attendance = new Attendance(new AttendanceListener() {
            @Override
            public void entered(Person person) {
                String message = String.format("%s %s ズムなう", format, person.name);
                logger.debug(message);
                if (!dryRun) {
                    try {
                        twitter.updateStatus(message);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void left(Person person) {
                String message = String.format("%s %s ズムあうと", format, person.name);
                logger.debug(message);
                if (!dryRun) {
                    try {
                        twitter.updateStatus(message);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        while (true) {
            for (Person person : persons) {
                attendance.recordAttendance(person, person.isAtSamuraism());
            }
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
