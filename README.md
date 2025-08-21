cloudflared login
cloudflared tunnel create hariworks-tunnel
cloudflared tunnel run hariworks-tunnel


tunnel: hariworks-tunnel
credentials-file: /Users/YOUR_USERNAME/.cloudflared/<YOUR_TUNNEL_ID>.json

ingress:
- hostname: dev.hariworks.in
  service: http://localhost:8080
- service: http_status:404

cloudflared tunnel route dns hariworks-tunnel dev.hariworks.in

cloudflared tunnel run hariworks-tunnel

java -jar one-api.jar --otp.cleanup.fixedRate=180000 --otp.cleanup.expiryMillis=30000 --server.port=8080

URL: https://dev.hariworks.in/swagger-ui/index.html

cloudflared login
cloudflared tunnel create reader-tunnel

tunnel: reader-tunnel
credentials-file: C:/Users/NarahariG/.cloudflared/4350b0bc-1b76-499d-9827-4c668910806d.json

ingress:
- hostname: reader.hariworks.in
  service: http://localhost:8080
- service: http_status:404

cloudflared tunnel route dns reader-tunnel reader.hariworks.in

cloudflared tunnel run reader-tunnel

MACOS:

tunnel: reader-tunnel
credentials-file: /Users/narahari/Documents/codebase/sand-bookings/one-api/bf9160ba-e058-4e95-96f2-632978d7726a.json

ingress:
- hostname: reader.hariworks.in
  service: http://localhost:8080
- service: http_status:404
