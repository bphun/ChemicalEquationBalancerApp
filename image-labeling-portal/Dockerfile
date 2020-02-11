FROM node:12.2.0-alpine

WORKDIR /app

COPY build/ ./build/
COPY .env.production ./.env.production

RUN npm install -g serve

EXPOSE 5000

ENTRYPOINT ["serve", "-s", "build"]
