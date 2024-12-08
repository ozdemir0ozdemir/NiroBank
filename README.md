
## Info
**auth-service** manages **user-service** and **token-service** via their own
feign clients (modules: **user-client**, **token-client**)

### RSA Cert generation using openssl (Available on both Windows and GNU/Linux)
- openssl genrsa -out keypair.pem 2048
- openssl rsa -in keypair.pem -pubout -out public.pem
- openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

---

