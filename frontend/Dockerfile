FROM node:alpine as build
WORKDIR /app
COPY package.json .
COPY package-lock.json .
RUN npm install -g @angular/cli
RUN npm install
COPY . .
RUN npm run build
# CMD ["ng", "serve", "--host", "0.0.0.0"]
CMD ["ng", "serve", "--ssl", "true", "--host", "0.0.0.0"]
