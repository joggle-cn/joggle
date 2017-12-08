define([
        'jquery'
     ], function (jQuery) {//加载依赖js,
	
	
	

(function($) {   
	var html = "" +
		"<div id=\"jquerymodelloading\">" +
			"<div style='position:fixed;_position:absolute;top:50%;left:50%;width:124px;height:124px;overflow:hidden;" +
			"background:url(/lib/jquery/images/loaderc.gif) no-repeat;z-index:7; margin:-62px 0 0 -62px; z-index:10001;'>" +
			"</div>" +
			"<div style='position:absolute;left:0;top:0;bottom:0; right:0; background-color: #000; z-index:10000;" +
			"filter:alpha(opacity=50); -moz-opacity:0.5; opacity:0.5;'></div>" +
		"</div>";
	
	$.openloading = function() { 
		$(document.body).append(html);   
	};
	
	$.closeloading = function() {
		$("#jquerymodelloading").remove(); 
	};
	
	
})(jQuery);    

});