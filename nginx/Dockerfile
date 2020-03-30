FROM nginx:1.17.8-alpine

RUN rm /etc/nginx/nginx.conf

COPY nginx.conf /etc/nginx/nginx.conf
COPY htpasswd /etc/apache2/.htpasswd

COPY self-signed.conf /etc/nginx/snippets/self-signed.conf
COPY ssl-params.conf /etc/nginx/snippets/ssl-params.conf

COPY nginx-selfsigned.key /etc/ssl/private/nginx-selfsigned.key
COPY nginx-selfsigned.crt /etc/ssl/certs/nginx-selfsigned.crt
COPY dhparam.pem /etc/ssl/certs/dhparam.pem

#USER root

#RUN adduser -D appUser

#RUN chown -R appUser:appUser /var/cache/nginx/ && \
#    chown -R appUser:appUser /var/log/nginx/

#USER appUser

EXPOSE 80
EXPOSE 443
