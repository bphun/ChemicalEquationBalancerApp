FROM ubuntu:18.04

RUN apt update && \
    apt install -y python3 python3-pip python3-venv && \
    mkdir dockerImageUpdater/ && \
    cd dockerImageUpdater/ && \
    mkdir logs/ && \
    pip3 install gunicorn flask docker

COPY DockerhubWebhookCallback.py dockerImageUpdater/DockerhubWebhookCallback.py
COPY ObjJsonEncoder.py dockerImageUpdater/ObjJsonEncoder.py
COPY logger.py dockerImageUpdater/logger.py
COPY server.py dockerImageUpdater/server.py
COPY wsgi.py dockerImageUpdater/wsgi.py
COPY dockerImageUpdater.service /etc/systemd/system/dockerImageUpdater.service

EXPOSE 8081
WORKDIR dockerImageUpdater

ENTRYPOINT ["gunicorn", "--workers", "1", "--bind", "0.0.0.0:8081", "-m", "007", "wsgi:app"]
