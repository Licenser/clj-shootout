run_scala() {
  echo "#################### SCALA ####################"
  SCALA_C="$LIB_DIR/scala/bin/scalac"
  SCALA_CP="$LIB_DIR/scala/lib/scala-library.jar"
  echo "Running scala version..."
  echo "  cleaning compilation..."
  ls scala/*.class 1> /dev/null 2> /dev/null && rm scala/*.class
  echo -n "  compiling... "
  cd scala && ../$SCALA_C $1.scala; cd ..
  echo $1
  echo "  benchmarking... "
  time $JAVA -cp $SCALA_CP:scala/ $1 $2
  echo -e "###############################################\n\n"
}

run_clojure() {
  echo "################### CLOJURE ###################"
  CP="$LIB_DIR/$1/clojure.jar:$LIB_DIR/$1/clojure-contrib.jar"
  echo "Running clojure-$1 version..."
  echo "  cleaning compilation..."
  if [ -d classes ]
    then
    rm -r classes
  fi
  mkdir classes
  echo -n "  compiling... "
  $JAVA -cp $CP:src clojure.main -e "(compile '$2)"
  echo "  benchmarking.. "
  time $JAVA -cp $CP:classes $2 $3
  echo -e "###############################################\n\n"
}
