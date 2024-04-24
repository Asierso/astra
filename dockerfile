FROM ubuntu:22.04

# Update local repos and install dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ca-certificates \
    --no-install-recommends

# Install chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    --no-install-recommends

# Install java jdk 18
RUN apt-get install openjdk-18-jre -y

# Create astra home and copy jar
RUN mkdir /home/astra \
    && chown -R root:root /home/astra \
    && chmod -R 755 /home/astra

COPY ./astra-server/target/astra-server-*-fullserver-*.jar /home/astra/astra-server.jar

WORKDIR /home/astra

# Start server at execute container
CMD ["java","-jar","/home/astra/astra-server.jar"]