# Object Detector online
This is a package that allows you to build an image server for uploading photos and detecting objects on them. 
It uploads the image that the user posts in the web-server and then it runs the detector for the selected object category. The detector is the popular ["Deformable Part Models"](https://github.com/rbgirshick/voc-dpm) framework. For setting up the detector, you have to follow the instructions in the project website. 


Installation: 
1) Download the files.
2) Setup the detector from their site (should not take more than few clicks, since the web-server only uses the *.mat files and few functions).
3) Put the recognition.m in the folder of the detector.
4) Change the paths to match your installation folders.
5) Launch the java project.

The project was developed in a machine with a linux kernel and the Java web services and the GlassFish server were used for the server-side programming. All the external libraries are included in the project. 


