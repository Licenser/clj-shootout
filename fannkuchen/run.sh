
PROJECT=fannkuchen
ARGS=10

LIB_DIR="../lib"
JAVA="java -Xmx1024m"


. $LIB_DIR/functions.inc


run_scala $PROJECT $ARGS

run_clojure "equal" $PROJECT $ARGS

run_clojure "equal"  "fannkuchen2" $ARGS
run_clojure "master" "fannkuchen2" $ARGS
