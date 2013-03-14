#!/bin/sh
# execute with sudo to allow creation of temp files!!!
echo $1
echo $2
filename=$1
output=$2

lineBreakPreventer="\c"

linecount=1;

cat $filename | while read line
do
    id=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $1}')
    printf "$id" >> $output
    printf "|" >> $output

    printf "<a href=\"" >> $output
    url=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $5}')
    echo "$url$lineBreakPreventer" >> $output
    printf "\">" >> $output
    title=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $2}')
    printf "$title" >> $output
    printf "</a>" >> $output
    printf "\n" >> $output
    let "linecount+=1"
done
