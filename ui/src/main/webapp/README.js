<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script>
$(function() {
		/*$.ajax({
			xhrFields: {
			    withCredentials: true
			},
			crossDomain: true,
			dataType: 'jsonp',
			jsonp:"callback", //请求php的参数名
            jsonpCallback: "_jsonp",//要执行的回调函数
			url: "http://localhost:8080/user/login",
			success: function( result ) {
				console.log('s1:' + result)
			}
		});*/

		$.ajax({
			xhrFields: {
			    withCredentials: true
			},
			crossDomain: true,
			dataType: 'jsonp',
			jsonp:"callback", //请求php的参数名
            jsonpCallback: "_jsonp",//要执行的回调函数
			url: "http://localhost:8080/user/test",
			success: function( result ) {
				console.log('s1:' + result)
			}
		});
		

		/*$.ajax({
			xhrFields: {
			    withCredentials: true
			},
			crossDomain: true,
			dataType: 'jsonp',
			jsonp:"callback", //请求php的参数名
            jsonpCallback: "_jsonp",//要执行的回调函数
			url: "http://localhost:8081/user/test",
			success: function( result ) {
				console.log('s2:' + result)
			}
		});*/
	})

//预定义个全局函数
function _jsonp(resultData){
    console.log(resultData)
}
</script>