# VERSION 1.0

#Install this image by command from root docker folder (csi-config/docker).
#Command: docker build --force-rm --no-cache -t postgres-rgs -f postgres-rgs/Dockerfile .

# Pull base image.
FROM postgres:9.5

#Change default locales
RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8

ENV LANG en_US.utf8

ADD ./*.sql /docker-entrypoint-initdb.d/