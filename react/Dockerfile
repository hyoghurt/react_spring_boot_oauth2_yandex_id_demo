FROM node:16-alpine as builder

WORKDIR /usr/src/app
# update npm
RUN npm install -g npm@8.19.1

COPY package*.json ./

# ci -best install
# --legacy-peer-deps -ignore all peerDependencies when installing
# --silent -suppressing the output of npm
RUN npm ci --silent --legacy-peer-deps

COPY . ./
RUN npm run build

FROM nginx:stable-alpine
COPY --from=builder /usr/src/app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
