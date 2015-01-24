<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>测试分享</title>
        <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    </head>
    <body>
    	${config}<br/>
    	
    	<a href="javascript:show();"><font size="30">显示</font></a><br/>
    	
    	<a href="javascript:choosePhoto();"><font size="30">选取照片</font></a><br/>
    	
    </body>
    <script type="text/javascript">
	    wx.config({
	        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	        appId: '${config.appId}', // 必填，公众号的唯一标识
	        timestamp: ${config.timestamp}, // 必填，生成签名的时间戳
	        nonceStr: '${config.nonceStr}', // 必填，生成签名的随机串
	        signature: '${config.signature}',// 必填，签名，见附录1
	        jsApiList: [
				'onMenuShareTimeline',
				'onMenuShareAppMessage',
				'showMenuItems',
				'hideMenuItems',
				'hideAllNonBaseMenuItem',
				'chooseImage'
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
	    	
	    	/* wx.hideMenuItems({
	    	    menuList: [
	    	     'menuItem:readMode',
	    	     'menuItem:share:email',
	    	     'menuItem:exposeArticle',
	    	     'menuItem:setFont',
	    	     'menuItem:share:qq',
	    	     'menuItem:copyUrl',
	    	     'menuItem:refresh'
	    	    ] // 要隐藏的菜单项，所有menu项见附录3
	    	});
	    	
	    	wx.showMenuItems({
	    	    menuList: [
	    	               'menuItem:share:qq'
	    	    ] // 要显示的菜单项，所有menu项见附录3
	    	}); */
	    	
	    	wx.hideAllNonBaseMenuItem({});
	    });
	    
	    function show(){
	    	wx.showMenuItems({
	    	    menuList: [
	    	               'menuItem:share:weiboApp',
	    	               'menuItem:share:qq',
	    	               'menuItem:dayMode'
	    	    ] // 要显示的菜单项，所有menu项见附录3
	    	});
	    }
	    
	    function choosePhoto(){
	    	wx.chooseImage({
	    	    success: function (res) {
	    	        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	    	    	alert(localIds);
	    	    }
	    	});
	    }
    </script>
</html>