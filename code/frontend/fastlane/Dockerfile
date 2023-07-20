FROM ubuntu:mantic AS build-env
RUN apt update
RUN apt install git -y
RUN apt install curl -y
RUN apt install unzip -y
RUN apt install cmake -y
RUN git clone https://github.com/flutter/flutter.git --branch 3.10.3 --single-branch /usr/local/flutter
ENV PATH="/usr/local/flutter/bin:/usr/local/flutter/bin/cache/dart-sdk/bin:${PATH}"

RUN flutter doctor -v
RUN flutter config --enable-web

RUN mkdir /app/

COPY assets /app/assets
COPY lib /app/lib
COPY web /app/web
COPY test /app/test
COPY .env /app
COPY pubspec.yaml /app
WORKDIR /app/

RUN flutter packages get
RUN flutter build web

FROM nginx:stable-perl
COPY --from=build-env /app/build/web /usr/share/nginx/html
COPY /nginx/fastlane_nginx.conf /etc/nginx/conf.d/default.conf