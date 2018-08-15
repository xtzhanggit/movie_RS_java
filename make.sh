#!/bin/sh

export APP_HOME=/home/xtzhang/movie_RS_java
export SRC_PATH=$APP_HOME/src

CLASSES_PATH=$APP_HOME/classes
if [ ! -d "$CLASSES_PATH"]; then
 mkdir "$CLASSES_PATH"
fi

CLASSPATH=$CLASSPATH:$CLASSES_PATH

javac -cp $CLASSPATH $SRC_PATH/item/RateInfo.java -d $CLASSES_PATH
javac -cp $CLASSPATH $SRC_PATH/preprocess/Preprocess.java -d $CLASSES_PATH
javac -cp $CLASSPATH $SRC_PATH/model/Cf.java -d $CLASSES_PATH
javac -cp $CLASSPATH $SRC_PATH/item/GenGraph.java -d $CLASSES_PATH
javac -cp $CLASSPATH $SRC_PATH/model/PersonalRank.java -d $CLASSES_PATH
javac -cp $CLASSPATH $SRC_PATH/Manage.java -d $CLASSES_PATH

