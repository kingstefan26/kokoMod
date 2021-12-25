#
# Copyright (c) 2021. All copyright reserved
#

mkdir temp
unzip -q build/libs/stefans_util-0.2.8-ALPHA-all.jar -d temp/
mkdir temp1
cd ./temp/
cp --parents io/github/kingstefan26/stefans_util/module/premium/* ../temp1/
cd ../temp1
jar cf premium.jar io/github/kingstefan26/stefans_util/module/premium/*.class
rm -rf io/
rm -rf ../temp/
cd ..
mv temp1/premium.jar build/libs/
rm -rf temp1
cp build/libs/stefans_util-0.2.8-ALPHA-all.jar build/libs/untouched.jar
zip -d build/libs/stefans_util-0.2.8-ALPHA-all.jar  "io/github/kingstefan26/stefans_util/module/premium/*.class"