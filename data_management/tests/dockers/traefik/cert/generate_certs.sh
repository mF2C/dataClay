#!/bin/bash

mkdir -p client
mkdir -p client-ca
mkdir -p server
mkdir -p server-ca 

pushd client-ca
rm *

openssl genrsa -out client-ca.key 4096
openssl req -x509 -new -nodes -key client-ca.key -subj "/C=ES/ST=CAT/O=BSC/CN=client-ca" -sha256 -days 1024 -out client-ca.crt
popd

pushd server-ca
rm *
openssl genrsa -out server-ca.key 4096
openssl req -x509 -new -nodes -key server-ca.key -subj "/C=ES/ST=CAT/O=BSC/CN=server-ca" -sha256 -days 1024 -out server-ca.crt
popd

pushd client
rm *
openssl genrsa -out client.key 2048
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in client.key -out client.pem
openssl req -new -sha256 -key client.key -subj "/C=ES/ST=CAT/O=BSC/CN=client" -out ../client-ca/client.csr
popd

pushd server
rm *
openssl genrsa -out server.key 2048 
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in server.key -out server.pem
openssl req -new -sha256 -key server.key -subj "/C=ES/ST=CAT/O=BSC/CN=proxy" -out ../server-ca/server.csr
popd

pushd client-ca
openssl x509 -req -in client.csr -CA client-ca.crt -CAkey client-ca.key -CAcreateserial -out ../client/client.crt -days 500 -sha256
popd

pushd server-ca
openssl x509 -req -in server.csr -CA server-ca.crt -CAkey server-ca.key -CAcreateserial -out ../server/server.crt -days 500 -sha256
popd

