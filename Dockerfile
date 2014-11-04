FROM dockerfile/java

MAINTAINER Randy Ridgley, randy.ridgley@gmail.com

ADD target/agon-1.0-SNAPSHOT.jar /

ADD agon.yml /

WORKDIR /

CMD ["java", "-jar", "agon-1.0-SNAPSHOT.jar", "server", "agon.yml"]

EXPOSE 7070

EXPOSE 7071