#!/bin/sh

trap `rm -f tmp.$$; exit 1` 1 2 15

for i in 1 2 3 4 5
do
	head -`expr $i \* 200041` ratings_tabbed.dat | tail -200041 > tmp.$$
	sort -t"	" -k 1,1n -k 2,2n tmp.$$ > u$i.test
	head -`expr \( $i - 1 \) \* 200041` ratings_tabbed.dat > tmp.$$
	tail -`expr \( 5 - $i \) \* 200041` ratings_tabbed.dat >> tmp.$$
	sort -t"	" -k 1,1n -k 2,2n tmp.$$ > u$i.base
done

perl allbut.pl ua 1 10 1000205 ratings_tabbed.dat
sort -t"	" -k 1,1n -k 2,2n ua.base > tmp.$$
mv tmp.$$ ua.base
sort -t"	" -k 1,1n -k 2,2n ua.test > tmp.$$
mv tmp.$$ ua.test

perl allbut.pl ub 11 20 1000205 ratings_tabbed.dat
sort -t"	" -k 1,1n -k 2,2n ub.base > tmp.$$
mv tmp.$$ ub.base
sort -t"	" -k 1,1n -k 2,2n ub.test > tmp.$$
mv tmp.$$ ub.test

