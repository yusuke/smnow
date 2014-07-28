package smnow;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
public enum SMStatus {

    SMOUT {
        @Override
        public String message(Person person) {
            return String.format("%s %s ズムあうと",
                    LocalDateTime.now(ZoneId.of(TOKYO)).format(FORMAT), person.name);
        }
    }, SMIN {
        @Override
        public String message(Person person) {
            return String.format("%s %s ズムなう",
                    LocalDateTime.now(ZoneId.of(TOKYO)).format(FORMAT), person.name);
        }
    };

    private static final String TOKYO = "Asia/Tokyo";

    private static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    abstract public String message(Person person);
}
