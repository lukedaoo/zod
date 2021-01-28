Ref:
https://github.com/fluent/fluent-bit
https://docs.fluentbit.io/manual/installation/sources/build-and-install
https://medium.com/@anujarosha/how-to-build-fluent-bit-source-on-macos-d7424558614c


git clone https://github.com/fluent/fluent-bit

echo "export MACOSX_DEPLOYMENT_TARGET=10.15" >> ~/.zprofile 

cd build

cmake ..

make 