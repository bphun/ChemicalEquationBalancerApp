FROM prom/prometheus

ADD prometheus.yml /etc/prometheus/

USER root
RUN adduser -D prometheusUser

RUN chown -R prometheusUser:prometheusUser /etc/prometheus/

USER prometheusUser

EXPOSE 9090
