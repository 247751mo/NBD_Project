FROM mongo:8.0.1
COPY --chown=999 --chmod=600 keyFile /etc/keyFile
RUN chmod 400 /etc/keyFile
COPY --chown=999 --chmod=600 mongod.conf /etc/mongod.conf