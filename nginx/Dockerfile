FROM nginx:1.17.8-alpine

RUN rm /etc/nginx/nginx.conf

COPY nginx.conf /etc/nginx/nginx.conf
COPY htpasswd /etc/apache2/.htpasswd

#COPY self-signed.conf /etc/nginx/self-signed.conf
#COPY ssl-params.conf /etc/nginx/ssl-params.conf

#COPY self-signed.crt /etc/ssl/certs/self-signed.crt
#COPY self-signed.key /etc/ssl/private/self-signed.key
#COPY dhparam.pem /etc/ssl/certs/dhparam.pem

#USER root

#RUN adduser -D appUser

#RUN chown -R appUser:appUser /var/cache/nginx/ && \
#    chown -R appUser:appUser /var/log/nginx/

#USER appUser

EXPOSE 80
