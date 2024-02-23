FROM node:20

WORKDIR /app

COPY package-lock.json .
COPY package.json .
RUN npm install

COPY . .
CMD ["npx", "vite", "--host", "0.0.0.0" ]
