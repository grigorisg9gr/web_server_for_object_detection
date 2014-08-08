#!/bin/bash
#greg, 24/1/2014: This is a new version that launches a tmux (multiplexer) in order to use this and accelerae the detection
# After restart it requires the commands: 1) tmux new -s matlab "./matlab -nodesktop -nodisplay -nosplash 
#					  2) (new window) ./multiplex_matlab "cd /home/user/Documents/voc-5-path/; startup;matlabpool open 8;"
# To kill the tmux: tmux kill-session -t matlab, instructions for tmux from: http://stackoverflow.com/questions/12306333/matlab-command-from-bash-command-line-on-an-already-running-session
#connects to the place of the images, compresses them if necessary and calls matlab script 
SRC_FILE=$2; #$2 this is the source file inside which the file will be
IMAGEFL=$1; #$1 name of the image
OUT_FILE=$3; #$3 name of the output folder
EXTENSION=$4; #$4 file type extension of the image
CATEGORY=$5;

cd $SRC_FILE
convert $IMAGEFL -resize 700x650\> $IMAGEFL # this is optional for resizing the image to ensure that it won't be too big for processing
MATLAB=\'$IMAGEFL\'; 
MATLAB2=\'$SRC_FILE\';
MATLAB3=\'$OUT_FILE\';
MATLAB4=\'$EXTENSION\';
MATLAB5=\'$CATEGORY\';
cd /home/user/srcgreg
./multiplex_matlab "
	try recognition($MATLAB,$MATLAB2,  $MATLAB3,$MATLAB4,$MATLAB5); catch e;disp('mistake again'); end" &
MATLABPID=$!;
echo $MATLABPID  #pid process of MATLAB (child process of this script)
wait $MATLABPID  #foreground the matlab process that was put on hold to get its pid
#echo $$ #pid process of the script
#
