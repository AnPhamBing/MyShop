#!/bin/sh

JAR_FILE=icommerce-core.jar
CURRENT_DIR=$PWD

java -Dcom.sun.management.jmxremote=true \
-Dcom.sun.management.jmxremote.port=5000 \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.local.only=false \
-Dsun.java2d.cmm=sun.java2d.cmm.kcms.KcmsServiceProvider \
-Dlogging.config=$CURRENT_DIR/config/logback.xml \
-jar $JAR_FILE &