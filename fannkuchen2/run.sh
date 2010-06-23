
PROJECT="profile.fannkuchen2"
ARGS=10

LIB_DIR="../lib"
JAVA="java -Xmx1024m"


. $LIB_DIR/functions.inc

run_clojure "equal"  $PROJECT $ARGS
run_clojure "master" $PROJECT $ARGS
