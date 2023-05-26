FROM postgres:latest

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=db

COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432

CMD ["postgres"]
