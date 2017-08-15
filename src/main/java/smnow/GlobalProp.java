package smnow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
public class GlobalProp {
    private static Properties prop = new Properties();
    private static final String PROP_FILE_NAME = System.getProperty("user.home") + File.separator + "smnow.properties";

    static {
        loadProperties(PROP_FILE_NAME);
    }
    public static void loadProperties(String path){
        try (FileInputStream fis = new FileInputStream(path)) {
            prop.load(fis);
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

    }

    public static String getProperty(String key) {
        return prop.getProperty(key, "");
    }

    public static void setProperty(String key, String value) {
        prop.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(PROP_FILE_NAME)) {
            prop.store(fos, "smnow");
        } catch (IOException e) {
            throw new RuntimeException("failed to store properties");
        }
    }

    public static void main(String[] args) {
        getProperty("hoge");
        setProperty("hoge", "hoge");
    }
}

