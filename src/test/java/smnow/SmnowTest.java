package smnow;

import org.junit.Test;

import static org.junit.Assert.*;

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
public class SmnowTest {
    @Test
    public void 人員ロード() {
        GlobalProp.loadProperties("./smnow.properties");
        Person[] persons = Person.load();
        assertEquals(3, persons.length);
    }

    @Test
    public void 在席チェックロジック() {
        Person macmini = new Person("ymacmini7", "ymacmini7.local");
        assertTrue(macmini.isAtSamuraism());
        Person notexist = new Person("notexist", "notexist.local");
        assertFalse(notexist.isAtSamuraism());
    }

    @Test
    public void 在席チェック() {
        GlobalProp.setProperty("dummy.attendanceHistory","false,false,false");
        GlobalProp.setProperty("dummy.isAtSamuraism","false");

        MyAttendanceListener listener = new MyAttendanceListener();
        Attendance attendance = new Attendance(listener);
        Person person = new Person("dummy", "dummy.local");
        assertFalse(listener.entered);
        assertFalse(listener.left);
        listener.entered = false;listener.left = false;
        attendance.recordAttendance(person, true);
        assertTrue(listener.entered);
        assertFalse(listener.left);
        listener.entered = false;listener.left = false;

        attendance.recordAttendance(person, true);
        //連続で在席を確認しても通知しない
        assertFalse(listener.entered);
        assertFalse(listener.left);
        listener.entered = false;listener.left = false;


        attendance.recordAttendance(person, false);
        //不在確認しても1回目では通知しない
        assertFalse(listener.entered);
        assertFalse(listener.left);
        listener.entered = false;listener.left = false;

        attendance.recordAttendance(person, false);
        assertFalse(listener.entered);
        assertFalse(listener.left);
        listener.entered = false;listener.left = false;
        //不在確認を30回すると通知する
        for (int i = 0; i < 30; i++) {
            attendance.recordAttendance(person, false);
        }
        assertFalse(listener.entered);
        assertTrue(listener.left);
        listener.entered = false;listener.left = false;

        attendance.recordAttendance(person, true);
        //不在後の在席確認なので即時通知
        assertTrue(listener.entered);
        assertFalse(listener.left);
    }

    class MyAttendanceListener implements AttendanceListener {

        boolean entered = false;
        boolean left = false;

        @Override
        public void entered(Person person) {
            entered = true;
        }


        @Override
        public void left(Person person) {
            left = true;
        }

        @Override
        public void allLeft(Person person) {

        }

        @Override
        public void firstEntered(Person person) {

        }
    }
}
