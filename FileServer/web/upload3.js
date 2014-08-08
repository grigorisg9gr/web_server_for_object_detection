
//var BASE_URL = ;
var request = new XMLHttpRequest();
var response;
var percentComplete;

onload = function loading() {
    ;
};

function sendFile() {
    	document.getElementById("status").innerHTML = "";
    	var file = document.getElementById("filechooser").files[0];
   	var extension = file.name.split(".").pop();
    	var category = document.getElementById("cat").value;//console.info(category);
    	var type;
    	if (extension === "jpg" || extension === "jpeg" ||
       	      extension === "JPG" || extension === "JPEG") {
  	      type = "image/jpeg";
   	 } else if (extension === "png" || extension === "PNG") {
   	     type = "image/png";
   	 } else if (extension === "bmp" || extension === "BMP") {
   	     type = "image/bmp";
   	 }else {
   	     document.getElementById("status").innerHTML = "Only \"jpeg\", \"png\" and \"bmp\" file types accepted";
   	     return;
    	}
	$('#progressbar').show('blind',500);//show progressbar
	var formdata = new FormData();
	formdata.append("description", type); 
	formdata.append("file", file);
	formdata.append("category", category);

	request.upload.addEventListener("progress", uploadProgress, false); 
	request.addEventListener("abort", uploadCanceled, false);
	request.open("POST", "http://imagedetector.com/resources/images", true);
	request.send(formdata);
	//alert("Pom pon!")
	request.onreadystatechange = printOutMessage;
}

	// 2 functions for the listeners
      function uploadProgress(evt) {
	document.getElementById("resultIm").innerHTML = ""; //for waiting gif
        if (evt.lengthComputable) {
          	percentComplete = Math.round(evt.loaded * 100 / evt.total);
          	//document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';

		if (percentComplete>99){
			document.getElementById("resultIm").innerHTML = "<img src='http://imagedetector.com/icons/tumblr_mpfqbgafiB1rzy733o1_500.gif' />";
		}
        }
        else {
          //document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
      }

      function uploadCanceled(evt) {
        alert("The upload has been canceled by the user or the browser dropped the connection.");
      }


	 $(function() {
		var progressbar = $( "#progressbar" ),
		progressLabel = $( ".progress-label" );
		progressbar.progressbar({
			value: false,
			change: function() {
			progressLabel.text( "Progress: "+progressbar.progressbar( "value" ) + "%" );
			},
			complete: function() {
			progressLabel.text( "Complete!" );
			}
		});
		function progress() {
			var val = progressbar.progressbar( "value" ) || 0;
			progressbar.progressbar( "value",percentComplete );
		    	progressbar //following two lines for changing the colour of the progress bar
				.removeClass("beginning middle end")
				.addClass( percentComplete< 40 ? "beginning" : percentComplete < 80 ? "middle" : "end");
			if ( val < 99 ) {
			setTimeout( progress, 100 );
			}
		}
		setTimeout( progress, 3000 );
	});

function printOutMessage(){ 
        if (request.readyState == 4){
		if ((request.status>=200)&&(request.status<300)){
			response=request.responseText;
			var ref='http://imagedetector.com/uploads/'+response;
			document.getElementById('messageForResult').innerHTML ="<a href="+ref+" >Resulting Image </a>";//"<img src="+ref+"/>";
			percentComplete=100;

		}
        } 
    }
//make the actual request
function makeRequest(temprequest, link, type,data){
	temprequest.open("POST", link, true);
    	temprequest.setRequestHeader("Content-Type", type);
    	temprequest.setRequestHeader('X-PINGOTHER', 'pingpong');
    	temprequest.withCredentials = "true"; 
	temprequest.send(data);
}

//for google analytics

  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-43796007-1', 'imagedetector.com');
  ga('send', 'pageview');






