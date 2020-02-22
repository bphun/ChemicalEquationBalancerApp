FROM prom/prometheus

ADD prometheus.yml /etc/prometheus/

USER root
RUN adduser -D appUser

USER appUser

EXPOSE 9090
