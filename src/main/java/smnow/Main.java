package smnow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smnow.hall.HallListener;

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
public class Main implements Runnable {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Main().run();
    }

    public boolean alive = true;

    @Override
    public void run() {
        Person[] persons = Person.load();
        boolean dryRun = false;

        Attendance attendance = new Attendance(new TwitterListener(dryRun), new HallListener(dryRun));
        while (alive) {
            for (Person person : persons) {
                attendance.recordAttendance(person, person.isAtSamuraism());
            }
            try {
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
