package smnow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class Person {
    public final String name;
    private final String address;
    static Logger logger = LoggerFactory.getLogger(Person.class);

    public static Person[] load() {
        String[] members = GlobalProp.getProperty("members").split(",");
        Person[] persons = new Person[members.length];
        for (int i = 0; i < members.length; i++) {
            persons[i] = new Person(members[i], GlobalProp.getProperty(members[i] + ".address"));
        }
        return persons;
    }

    Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public boolean isAtSamuraism() {
        try {
            logger.debug("address of {} is {}", name, InetAddress.getByName(address));
            return true;
        } catch (UnknownHostException e) {
            logger.debug("address of {} not found", name);
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (address != null ? !address.equals(person.address) : person.address != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
