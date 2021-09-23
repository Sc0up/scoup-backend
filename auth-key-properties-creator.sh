#!/usr/bin/env bash

AUTH_KEYS=()

for auth_key in $1; do
  AUTH_KEYS+=($auth_key)
done

export JASYPT_ENCRYPTOR_PASSWORD="${AUTH_KEYS[0]}"
export JWT_SECRET_MAC="${AUTH_KEYS[1]}"
