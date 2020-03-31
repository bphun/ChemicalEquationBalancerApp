FROM nginx:1.17.8-alpine

RUN rm /etc/nginx/nginx.conf

COPY nginx.conf /etc/nginx/nginx.conf
COPY htpasswd /etc/apache2/.htpasswd

COPY self-signed.conf /etc/nginx/snippets/self-signed.conf
COPY ssl-params.conf /etc/nginx/snippets/ssl-params.conf

COPY nginx-selfsigned.key /etc/ssl/private/nginx-selfsigned.key
COPY nginx-selfsigned.crt /etc/ssl/certs/nginx-selfsigned.crt
COPY dhparam.pem /etc/ssl/certs/dhparam.pem

USER root

RUN adduser -D nginxUser

RUN touch /var/run/nginx.pid && \
    chown -R nginxUser:nginxUser /var/cache/nginx/ && \
    chown -R nginxUser:nginxUser /var/log/nginx/ && \
    chown -R nginxUser:nginxUser /etc/nginx/nginx.conf && \
    chown -R nginxUser:nginxUser /var/run/nginx.pid && \
    chown -R nginxUser:nginxUser /etc/apache2/.htpasswd && \
    chown -R nginxUser:nginxUser /etc/nginx/snippets/self-signed.conf && \
    chown -R nginxUser:nginxUser /etc/nginx/snippets/ssl-params.conf && \
    chown -R nginxUser:nginxUser /etc/ssl/private/nginx-selfsigned.key && \
    chown -R nginxUser:nginxUser /etc/ssl/certs/nginx-selfsigned.crt && \
    chown -R nginxUser:nginxUser /etc/ssl/certs/dhparam.pem

USER nginxUser

