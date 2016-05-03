#/bin/bash

PROJ_HOME='/home/binu/playground/CGBF/'
#echo $PROJ_HOME

OUT_FILE='tools/MergedPlayer.out'
#echo $OUT_FILE

PLAYER_FILE='tools/Player.java'

SRC_MAIN='src/main/java/'

outFilePath=$PROJ_HOME$OUT_FILE
playerFilePath=$PROJ_HOME$PLAYER_FILE
srcMainPath=$PROJ_HOME$SRC_MAIN

if [[ -f $outFilePath ]];
then
    echo "File exists... removing."
    rm $outFilePath
fi

cat $playerFilePath >> $outFilePath

for i in `find $srcMainPath -type f | grep java$`
do
    echo "File names "$i
    cat $i >> $outFilePath
done






