# 在席通知bot - ズムなう

## 使い方
```
$ git clone https://github.com/yusuke/smnow.git  
$ smnow  
$ mvn package  
$ vi src/resources/twitter4j.properties  
(書き込み可能なTwitter APIのAPIキー、アクセストークン  
oauth.consumerSecret=hb9kbvw9cniNZFoNJXwghONU6uFYjKndjWcT3KQQzX5ChApVkE  
oauth.consumerKey=Mk1kJtqha9n8uCKdTMISvcyAL  
oauth.accessToken=**********  
oauth.accessTokenSecret=*********
$ vi smnow.properties  
\# メンバ名をカンマ区切りで  
members=yusuke,johtani,...  
\# メンバ名.address 以下にメンバのマシンのbonjour名を指定  
yusuke.address=yPhone5s.local  
johtani.address=*****.local  
$ nohup java -Xms10m -Xmx50m -cp target/classes:~/.m2/repository/org/twitter4j/twitter4j-core/4.0.2/twitter4j-core-4.0.2.jar:~/.m2/repository/org/slf4j/slf4j-api/1.6.6/slf4j-api-1.6.6.jar:~/.m2/repository/ch/qos/logback/logback-core/1.0.7/logback-core-1.0.7.jar:~/.m2/repository/ch/qos/logback/logback-classic/1.0.7/logback-classic-1.0.7.jar smnow.Main > /tmp/smnow.log 2>&1 &
```

