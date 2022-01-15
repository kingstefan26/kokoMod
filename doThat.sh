#
# Copyright (c) 2021. All copyright reserved
#

#gen the uuid used for premium.jar
uuid=$(uuidgen)


# remove old premium.jar and make temp directory
rm build/libs/premium*
mkdir temp

# extract freshly build kokomod into temp folder
unzip -q build/libs/kokomod-1.0.0.jar -d temp/
mkdir temp1
cd ./temp/
# copy all module classes into temp1
cp -r --parents io/github/kingstefan26/stefans_util/module/* ../temp1/
cd ../temp1

# create the premium jar with a custom manifest
echo "Manifest-Version: 1.0\nbuildNumber: $(date '+%d-%m-%Y') " > date.mf
cat date.mf
jar cmf date.mf premium-$uuid.jar io/github/kingstefan26/stefans_util/module/*
rm date.mf

# move premium jar into libs folder and remove unnecessary files
rm -rf io/
rm -rf ../temp/
cd ..
mv temp1/premium-$uuid.jar build/libs/
rm -rf temp1

# create untouched jar for personal use
cp build/libs/kokomod-1.0.0.jar build/libs/untouched.jar
# remove all module classes from the kokomod jar
zip -d build/libs/kokomod-1.0.0.jar  "io/github/kingstefan26/stefans_util/module/*"
