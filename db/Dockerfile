FROM mysql:8.0

COPY ./init/init.sql /docker-entrypoint-initdb.d/

# MySQL 설정 파일을 복사
COPY ./config/my.cnf /etc/mysql/conf.d/custom.cnf

# 계정정보 설정
ENV MYSQL_USER=myuser
ENV MYSQL_PASSWORD=mypassword
ENV MYSQL_DATABASE=withins
ENV MYSQL_ROOT_PASSWORD=rootpassword