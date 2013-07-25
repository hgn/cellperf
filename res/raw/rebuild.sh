#!/bin/sh

for SVG in *.svg
do PNG=`basename $SVG svg`png
	inkscape -z  -e ../drawable-xxhdpi/$PNG -w 128 -h 128 $SVG
	inkscape -z  -e ../drawable-xhdpi/$PNG -w 96 -h 96 $SVG
	inkscape -z  -e ../drawable-hdpi/$PNG -w 72 -h 72 $SVG
	inkscape -z  -e ../drawable-mdpi/$PNG -w 48 -h 48 $SVG
done
