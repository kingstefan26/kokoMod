#
# Copyright (c) 2021. All copyright reserved
#

mkdir temp
unzip -q build/libs/kokomod-0.2.8-ALPHA.jar -d temp/
mkdir temp1
cd ./temp/
cp -r --parents io/github/kingstefan26/stefans_util/module/* ../temp1/
cd ../temp1
jar cf premium.jar io/github/kingstefan26/stefans_util/module/*
rm -rf io/
rm -rf ../temp/
cd ..
mv temp1/premium.jar build/libs/
rm -rf temp1
cp build/libs/kokomod-0.2.8-ALPHA.jar build/libs/untouched.jar
zip -d build/libs/kokomod-0.2.8-ALPHA.jar  "io/github/kingstefan26/stefans_util/module/*"
