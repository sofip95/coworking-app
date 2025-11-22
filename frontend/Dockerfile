# ETAPA 1: Build de Angular
FROM node:22-alpine AS build

WORKDIR /app

# Copiar dependencias
COPY package*.json ./
RUN npm install

# Copiar el código fuente
COPY . .

# Build de producción (Angular 20)
RUN npm run build -- --configuration production

# ETAPA 2: Servir con NGINX
FROM nginx:alpine

# Copiar build (solo carpeta browser)
COPY --from=build /app/dist/front-app/browser /usr/share/nginx/html

# Copiar archivo de configuración de NGINX
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
