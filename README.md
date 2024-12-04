### RSA Cert generation using openssl (Available on both Windows and GNU/Linux)
- openssl genrsa -out keypair.pem 2048
- openssl rsa -in keypair.pem -pubout -out public.pem
- openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

---

