FROM grafana/grafana:6.5.0

COPY grafana.ini /etc/grafana/grafana.ini

EXPOSE 9091

USER root
RUN adduser -D appUser

USER appUser