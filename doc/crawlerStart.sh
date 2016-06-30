#!/bin/sh
#
#
#####################################

#
# Parameter check
#

if [ $# -lt 12 ]
then
    echo "Parameter Errors !!!"
    exit 1
fi

TOPIC_CRAWLER_JAR=
WHOLE_SITE_CRAWLER_JAR=gycrawlerWebmagicDemo-1.0-SNAPSHOT.jar
HOME_PAGE_CRAWLER_JAR=

LOGS_DIR=`pwd`/crawler.logs

######################################
#
# $(1):  depth
# $(2):  pass
# $(3):  tid
# $(4):  startTime
# $(5):  seedPath
# $(6):  protocolDir
# $(7):  type
# $(8):  recallDepth
# $(9):  templateDir
# $(10): clickRegexDi
# $(11): postRegexDir
# $(12): configPath
#
######################################


LOG_FILE=$LOGS_DIR/crawler_$3_$(date +'%Y%m%d%H%M%S').out

case $7 in

"TOPIC")
echo "TOPIC ===>> Topic."
touch $LOG_FILE
java -jar $TOPIC_CRAWLER_JAR \
-depth $1 \
-pass $2  \
-tid $3 \
-starttime $4 \
-seedpath $5 \
-protocolDir $6 \
-type $7 \
-recalldepth $8 \
-templateDir $9 \
-clickregexDir $10 \
-postregexDir $11  \
-configpath $12  >& $LOG_FILE &

  ;;

"WHOLESITE")
echo "WHOLESITE ===>> Whole site."
touch $LOG_FILE
java -jar $WHOLE_SITE_CRAWLER_JAR \
-depth $1 \
-pass $2  \
-tid $3 \
-starttime $4 \
-seedpath $5 \
-protocolDir $6 \
-type $7 \
-recalldepth $8 \
-templateDir $9 \
-clickregexDir $10 \
-postregexDir $11  \
-configpath $12 >& $LOG_FILE &

  ;;

"UNSET")
echo "USET ===>> Home page."
touch $LOG_FILE
java -jar $HOME_PAGE_CRAWLER_JAR \
-depth $1 \
-pass $2  \
-tid $3 \
-starttime $4 \
-seedpath $5 \
-protocolDir $6 \
-type $7 \
-recalldepth $8 \
-templateDir $9 \
-clickregexDir $10 \
-postregexDir $11  \
-configpath $12 >& $LOG_FILE &

;;

esac


