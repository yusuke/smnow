package smnow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Copyright 2014Shinya Mochida
 * <p>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class TwitterListener implements AttendanceListener {

    private static final Logger logger = LoggerFactory.getLogger(TwitterListener.class);

    private final boolean dryRun;

    private final Twitter twitter;

    public TwitterListener(boolean dryRun) {
        this.dryRun = dryRun;
        this.twitter = TwitterFactory.getSingleton();
    }

    @Override
    public void entered(Person person) {
        String message = SMStatus.SMIN.message(person);
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
        String message = SMStatus.SMOUT.message(person);
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
    public void allLeft(Person person) {

    }

    @Override
    public void firstEntered(Person person) {

    }
}
