FROM alpine:latest
RUN apk update
RUN apk add sysbench
RUN apk add --update --no-cache p7zip
VOLUME /golem/input /golem/output
WORKDIR /golem/work
