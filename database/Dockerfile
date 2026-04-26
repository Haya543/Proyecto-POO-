# PostgreSQL image
FROM postgres:16 

# Set Variables
ENV POSTGRES_USER=PokeUser
ENV POSTGRES_PASSWORD=PokePassword
ENV POSTGRES_DB=PokeGame

# (Optional)
COPY init.sql /docker-entrupoint-initdb.d 

EXPOSE 5432

