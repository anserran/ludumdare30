cd svg
for file in $(find . -type f)
do
	imagefile=${file:2}
	echo "Image found: $imagefile"
	imagename=${imagefile%.*}
	imagepng="$imagename$resize.png"
	echo "Converting $imagefile to $imagepng"
	inkscape --export-png=../png/$imagepng --export-dpi=90 --export-background-opacity=0 --without-gui $imagefile
	inkscape --export-png=../png/sm-$imagepng --export-dpi=45 --export-background-opacity=0 --without-gui $imagefile
done