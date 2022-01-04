#
# Copyright (c) 2022. All copyright reserved
#

# this part only runs on my pc so be aware
rm ~/cockmod-data/webModules/assets/premium.jar
cp build/libs/premium.jar ~/cockmod-data/webModules/assets/
cd ~/cockmod-data/
git add -A
git commit -m 'update premium.jar'
git push
cd -