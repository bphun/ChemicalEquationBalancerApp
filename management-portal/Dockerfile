FROM node:12.2.0-alpine

WORKDIR /app

COPY build/ ./build/
COPY .env.production ./.env.production

RUN npm install -g serve

EXPOSE 5000

USER root
RUN adduser -D appUser

USER appUser

ENTRYPOINT ["serve", "-s", "build"]
