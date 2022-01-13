#
# Copyright (c) 2021. All copyright reserved
#

uuid=$(uuidgen)

mkdir temp

unzip -q build/libs/kokomod-1.0.0.jar -d temp/
mkdir temp1
cd ./temp/
cp -r --parents io/github/kingstefan26/stefans_util/module/* ../temp1/
cd ../temp1

echo "Manifest-Version: 1.0\nbuildNumber: $(date '+%d-%m-%Y') " > date.mf
cat date.mf
jar cmf date.mf premium-$uuid.jar io/github/kingstefan26/stefans_util/module/*
rm date.mf

rm -rf io/
rm -rf ../temp/
cd ..
mv temp1/premium-$uuid.jar build/libs/

rm -rf temp1
cp build/libs/kokomod-1.0.0.jar build/libs/untouched.jar
zip -d build/libs/kokomod-1.0.0.jar  "io/github/kingstefan26/stefans_util/module/*"
