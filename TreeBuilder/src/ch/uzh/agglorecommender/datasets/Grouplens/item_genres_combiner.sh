#!/bin/sh
# execute with sudo to allow creation of temp files!!!
echo $1
echo $2
filename=$1
output=$2

prefix=$(basename $0)
echo $prefix

mapdir=$(mktemp -dt ${prefix})
echo $mapdir

trap 'rm -r ${mapdir}' EXIT

put() {
[ "$#" != 3 ] && exit 1
mapname=$1; key=$2; value=$3
[ -d "${mapdir}/${mapname}" ] || mkdir "${mapdir}/${mapname}"
echo $value >"${mapdir}/${mapname}/${key}"
}

get() {
[ "$#" != 2 ] && exit 1
mapname=$1; key=$2
cat "${mapdir}/${mapname}/${key}"
}


#value=$(get "newMap" "company")
#echo $value

#value=$(get "newMap" "name")
#echo $value

put "newMap" "0" "unknown"
put "newMap" "1" "Action"
put "newMap" "2" "Adventure"
put "newMap" "3" "Animation"
put "newMap" "4" "Children's"
put "newMap" "5" "Comedy"
put "newMap" "6" "Crime"
put "newMap" "7" "Docu"
put "newMap" "8" "Drama"
put "newMap" "9" "Fantasy"
put "newMap" "10" "Film-Noir"
put "newMap" "11" "Horror"
put "newMap" "12" "Musical"
put "newMap" "13" "Mystery"
put "newMap" "14" "Romance"
put "newMap" "15" "Sci-Fi"
put "newMap" "16" "Thriller"
put "newMap" "17" "War"
put "newMap" "18" "Western"


linecount=1;

cat $filename | while read line
do
	id=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $1}')
	printf "$id" >> $output
	printf "|" >> $output
	title=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $2}')
	printf "$title" >> $output
	printf "|" >> $output
	v=$(head -$linecount $filename | tail -1 | awk -F"|" '{print $6" "$7" "$8" "$9" "$10" "$11" "$12" "$13" "$14" "$15" "$16" "$17" "$18" "$19" "$20" "$21" "$22" "$23" "$24" "$25" "$26" "$27" "$28}')
	#echo $v
	counter=0
	for j in $v
	do
		
		tmp="$j"
		tmp=$(echo "$tmp" | tr -d [[:space:]])
		#echo "$tmp"
		if [ "$j" = "1" ]
			then
			#echo $counter
			o=$(get "newMap" "$counter")
			#echo $o
			printf "$o" >> $output
			printf ":" >> $output
		fi

		let "counter+=1"
	done
	printf "\n" >> $output
	let "linecount+=1"
done
