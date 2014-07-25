package smnow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

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
public final class Attendance {
    static Logger logger = LoggerFactory.getLogger(Attendance.class);

    private final AttendanceListener listener;
    static final String ATTENDANCE_HISTORY = ".attendanceHistory";
    static final String IS_AT_SAMURAISM = ".isAtSamuraism";

    Attendance(AttendanceListener listener) {
        this.listener = listener;
    }

    void recordAttendance(Person person, boolean latestAttendance) {
        String[] history = GlobalProp.getProperty(person.name + ATTENDANCE_HISTORY).split(",");
        logger.debug("history:{}", Arrays.asList(history));
        boolean isAtSamuraism = Boolean.valueOf((GlobalProp.getProperty(person.name + IS_AT_SAMURAISM)));
        logger.debug("isAtSamuraism:{}", isAtSamuraism);

        ArrayList<String> strings = new ArrayList<>(Arrays.asList(history));
        strings.add(String.valueOf(latestAttendance));

        //直近3回分の在席情報のみ保存
        if (strings.size() > 3) {
            strings.remove(0);
        }

        if (!isAtSamuraism) {
            // 不在状態から在席を確認できたらすぐに在席通知
            if (latestAttendance) {
                logger.debug("{} entered", person.name);
                isAtSamuraism = true;
                listener.entered(person);
            }
        } else {
            // 3回連続で不在状態を検知したら不在を通知
            if (strings.stream().allMatch(e -> e.equals("false"))) {
                logger.debug("{} left", person.name);
                isAtSamuraism = false;
                listener.left(person);
            }
        }

        // 履歴、状態を永続化
        GlobalProp.setProperty(person.name + ATTENDANCE_HISTORY, String.join(",", strings));
        GlobalProp.setProperty(person.name + IS_AT_SAMURAISM, String.valueOf(isAtSamuraism));
    }

}
