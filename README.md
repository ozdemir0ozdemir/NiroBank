## Auth-Server Notes
### RSA Cert generation using openssl (Available on both Windows and GNU/Linux)
- openssl genrsa -out keypair.pem 2048
- openssl rsa -in keypair.pem -pubout -out public.pem
- openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem

---

## TODO: 
- 1 - Store **refresh** token id in **redis** cache _(or in-memory storage username -> tokenid map)_
- 2 - **Access** token generation using **basic** authentication _(return access and refresh token)_
- 3 - **Refresh** token revoke endpoint
- 4 - **Refresh** token auto-revoke scheduler
- 5 - **Access** token refresher endpoint _(expired access token + refresh token needed)_
  - 5a - Return **same access** token while access token is **valid**
  - 5b - Return new access and refresh token, **revoke** old refresh token
- 6 - Verify access token endpoint _(May not be needed since i use **asymmetric** key)_
- 7 - Create the **user-service**
                 
