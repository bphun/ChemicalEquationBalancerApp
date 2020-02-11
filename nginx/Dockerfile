FROM nginx:1.17.8-alpine

RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf
COPY htpasswd /etc/apache2/.htpasswd

EXPOSE 80
