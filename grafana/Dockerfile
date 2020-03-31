FROM grafana/grafana:6.5.0

COPY grafana.ini /etc/grafana/grafana.ini

EXPOSE 9091

USER root
RUN adduser -D grafanaUser

RUN chown -R grafanaUser:grafanaUser /etc/grafana/ && \
    chown -R grafanaUser:grafanaUser /var/lib/grafana && \
    chown -R grafanaUser:grafanaUser /var/log/grafana && \
    chown -R grafanaUser:grafanaUser /usr/share/grafana/ 

USER grafanaUser
