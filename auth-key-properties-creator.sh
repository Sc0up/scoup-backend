#!/usr/bin/env bash

for _AUTH_KEY in $AUTH_KEY; do

  IFS='=' read -ra VALUES <<<"$_AUTH_KEY"
  echo "${VALUES[@]}"
  if [ "${VALUES[0]}" = "jasypt.encryptor.password" ]; then
    export JASYPT_ENCRYPTOR_PASSWORD="${VALUES[1]}"
  elif [ "${VALUES[0]}" = "jwt.secret.mac" ]; then
    export JWT_SECRET_MAC="${VALUES[1]}"
  fi
done
