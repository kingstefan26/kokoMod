#
# Copyright (c) 2022. All copyright reserved
#

# this part only runs on my pc so be aware
#cp build/libs/premium.jar ~/cockmod-data/webModules/assets/
#cd ~/cockmod-data/
#git add -A
#git commit -m 'update premium.jar'
#git push
#cd -

# send the build to a discord channel
curl
  -F 'payload_json={"username": "cockmod upates", "content": "new version", "avatar":"https://cdn.glitch.global/3e8413f7-7b17-4585-b62a-091aaebcfacd/nerd.jpg?v=1641361122288"}'
  -F "jar=@build/libs/kokomod-1.0.0.jar"
  https://discord.com/api/webhooks/928159794479656960/xsL23bMePB2oRj4LUryYWYK4kn7ovAq70R3VyoOrVKIRlxlqRafEbPDgzg61476lls5e