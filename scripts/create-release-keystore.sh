#!/usr/bin/env bash
set -euo pipefail

ALIAS="${1:-papermarks}"
KEYSTORE="papermarks-release.jks"

read -rsp "Keystore password: " STORE_PASS
echo
read -rsp "Key password: " KEY_PASS
echo

keytool -genkeypair \
  -v \
  -storetype JKS \
  -keystore "$KEYSTORE" \
  -alias "$ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "$STORE_PASS" \
  -keypass "$KEY_PASS" \
  -dname "CN=Papermarks, OU=Papermarks, O=Papermarks, L=Berlin, ST=Berlin, C=DE"

echo
if base64 --help 2>/dev/null | grep -q -- '-w'; then
  BASE64_VALUE=$(base64 -w0 "$KEYSTORE")
else
  BASE64_VALUE=$(base64 "$KEYSTORE" | tr -d '\n')
fi

echo "GitHub secret ANDROID_SIGNING_KEY:"
echo "$BASE64_VALUE"
echo
echo "GitHub secret ANDROID_KEY_ALIAS: $ALIAS"
echo "GitHub secret ANDROID_KEYSTORE_PASSWORD: <your keystore password>"
echo "GitHub secret ANDROID_KEY_PASSWORD: <your key password>"
