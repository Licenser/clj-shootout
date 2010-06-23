
PROJECT=regexdna
ARGS=7

LIB_DIR="../lib"
JAVA="java -Xmx1024m"


. $LIB_DIR/functions.inc


run_scala $PROJECT $ARGS

run_clojure "equal" $PROJECT $ARGS
