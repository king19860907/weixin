<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>测试分享</title>
        <script type="text/javascript" src="http://css.yesmyimg.com/20140808/newWeb/js/lib/jquery-1.4.4.min.js?"></script>
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    </head>
    <body>
    	${config}<br/>
    	
    	<a href="javascript:choosePhoto();"><font size="30">选取照片</font></a><br/><br/>
    	
    	<a href="javascript:void(0);" id="uploadImage"><font size="30">上传照片</font></a><br/><br/>
    	<img alt="" src=""/>
    	
    	<a href="javascript:void(0);" id="downloadImage"><font size="30">下载照片</font></a><br/><br/>
    	
    </body>
    <script type="text/javascript">
    	/**
    	 *	微信jssdkdemo
    	 *	http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
    	 *
    	 **/
    
	    wx.config({
	        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	        appId: '${config.appId}', // 必填，公众号的唯一标识
	        timestamp: ${config.timestamp}, // 必填，生成签名的时间戳
	        nonceStr: '${config.nonceStr}', // 必填，生成签名的随机串
	        signature: '${config.signature}',// 必填，签名，见附录1
	        jsApiList: [
				'onMenuShareTimeline',
				'onMenuShareAppMessage',
				'chooseImage',
				'previewImage',
				'uploadImage',
				'downloadImage'
	        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	    });
	    
	    wx.ready(function(){
	    	wx.onMenuShareTimeline({
	    	    title: 'test', // 分享标题
	    	    link: 'bbb', // 分享链接
	    	    imgUrl: 'http://css.yesmyimg.com/20140808/newWeb/images/11175/share.jpg', // 分享图标
	    	    success: function () { 
	    	        // 用户确认分享后执行的回调函数
	    	    },
	    	    cancel: function () { 
	    	        // 用户取消分享后执行的回调函数
	    	    }
	    	});
	    	
	    	wx.onMenuShareAppMessage({
	    	    title: 'test2', // 分享标题
	    	    desc: 'bbb', // 分享描述
	    	    link: '', // 分享链接
	    	    imgUrl: 'http://css.yesmyimg.com/20140808/newWeb/images/11175/share.jpg', // 分享图标
	    	    type: '', // 分享类型,music、video或link，不填默认为link
	    	    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	    	    success: function () { 
	    	        // 用户确认分享后执行的回调函数
	    	    },
	    	    cancel: function () { 
	    	        // 用户取消分享后执行的回调函数
	    	    }
	    	});
	    	
	    });
	    
	    var localIds;
	    function choosePhoto(){
	    	wx.chooseImage({
	    	    success: function (res) {
	    	        localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	    	    }
	    	});
	    }
	    
		var serverIds = new Array();	    
	    //上传图片
	    $("#uploadImage").click(function(){
	    	var i=0;
		    function uploadPhoto(){
		    	if(localIds.length == 0){
		    		alert("请先选取照片");
		    	}else{
		    		wx.uploadImage({
			    		localId:localIds[i],	//需要上传的图片的本地ID，由chooseImage接口获得
			   			isHowProgressTips:1,	//默认为1，显示进度提示
			    	    success: function (res) {
			    	    	var serverId = res.serverId;	//返回图片的服务器端id
			    	    	serverIds.push(serverId);
			    	    	if(i<localIds.length-1){
			    	    		i=i+1;
			    	    		uploadPhoto();
			    	    	}else{
			    	    		downloadMedia();
			    	    	}
				    	},
			    	    fail:function(res){
			    	    	alert(JSON.stringify(res));
			    	    }
			    	});
		    	}
		    }
		    uploadPhoto();
	    });
	    
	    //下载图片
	    $("#downloadImage").click(function(){
	    	var i =0;
	    	function downloadPhoto(){
	    		wx.downloadImage({
		    	    serverId: serverIds[i], // 需要下载的图片的服务器端ID，由uploadImage接口获得
		    	    isShowProgressTips: 1, // 默认为1，显示进度提示
		    	    success: function (res) {
		    	        var localId = res.localId; // 返回图片下载后的本地ID
		    	        if(i<serverIds.length-1){
		    	    		i++;
		    	    		downloadPhoto();
		    	    	}
		    	    }
		    	});
	    	}
	    	downloadPhoto();
	    });
	 
	    //从微信下载图片到服务器
	    function downloadMedia(){
	    	$.ajax( {    
	    	    url:'/weixin/downloadMedia.do',// 跳转到 action    
	    	    data:{    
	    	       serverIds:serverIds
	    	    },    
	    	    type:'post',    
	    	    cache:false,    
	    	    success:function(data) {    
	    	       var jsonData = $.parseJSON(data);
	    	       for(var i =0;i<jsonData.length;i++){
	    	    	   var html = "<img alt='' src='"+jsonData[i]+"'/>";
	    	    	   $("#uploadImage").after(html);
	    	       }
x	    	    }
	    	});  
	    }
	    
    </script>
</html>