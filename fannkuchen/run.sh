PROJECT=fannkuchen
ARGS=11

LIB_DIR="../lib"
JAVA="java -Xmx1024m"


. $LIB_DIR/functions.inc


run_scala $PROJECT $ARGS

run_clojure "equal" "profile.$PROJECT" $ARGS
#run_clojure "own" "profile.$PROJECT" $ARGS
