Ref:
https://syslog-ng.gitbooks.io/getting-started/content/chapters/chapter_0/section_3.html
https://medium.com/macoclock/how-to-install-syslog-ng-on-macos-965127f1c05c

#Step 1
Install dependencies

brew install \
    automake \
    autoconf \
    binutils \
    glib \
    autoconf-archive \
    flex \
    bison \
    libtool \
    pkg-config \
    ivykis \
    openssl \
    pcre

#Step 2
Setup Env

PATH="$(brew --prefix openssl)/bin:$PATH"

export LDFLAGS="-L/usr/local/opt/openssl@1.1/lib"
export CPPFLAGS="-I/usr/local/opt/openssl@1.1/include"
export PKG_CONFIG_PATH="/usr/local/opt/openssl@1.1/lib/pkgconfig"

#Step 3
Install

git clone https://github.com/syslog-ng/syslog-ng.git

./autogen.sh
./configure --with-ivykis=system --disable-amqp --disable-mongodb --disable-riemann --disable-java --disable-python

make -j4
make install

#Step 4 
Update Syslog-ng config

1. remove system() at /usr/local/etc/syslog-ng.conf

2. Run with pid 
sudo /usr/local/sbin/syslog-ng -F -p /var/run/syslog-ng.pid

