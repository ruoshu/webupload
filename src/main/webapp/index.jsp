<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>  
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	  <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!--引入CSS-->
    <link rel="stylesheet" type="text/css" href="webupload/webuploader.css">
    <link rel="stylesheet" type="text/css" href="webupload/style.css">
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    

    <!-- Documentation extras -->

    <!--[if lt IE 9]><script src="../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>
</head>
<!--引入JS-->
<script src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="webupload/webuploader.js"></script>

<body>
<div id="wrapper">
<div id="post-container" class="container">
<div class="row">
<div class="col-md-12">
<div class="page-container">
<div id="uploader" class="wu-example">
    <div id="thelist" class="uploader-list"></div>
    <div class="btns">
        <div id="picker" class="webuploader-container"><div class="webuploader-pick">选择文件</div><div id="rt_rt_1bpd2uvdb1mletgoee11vv27ms1" style="position: absolute; top: 0px; left: 0px; width: 88px; height: 34px; overflow: hidden; bottom: auto; right: auto;"><input type="file" name="file" class="webuploader-element-invisible" multiple="multiple"><label style="opacity: 0; width: 100%; height: 100%; display: block; cursor: pointer; background: rgb(255, 255, 255);"></label></div></div>
        <button id="ctlBtn" class="btn btn-default">开始上传</button>
    </div>
</div>
</div>
</div>
</div>
</div>
</div>
</body>


<!--SWF在初始化的时候指定，在后面将展示-->




<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="js/ie10-viewport-bug-workaround.js"></script>
<script>
	 /* WebUploader.Uploader.register({  
            "before-send-file":"beforeSendFile"
        },{
        	beforeSendFile:function(file){ 
        		alert(1);
        	}
        	}); */
	var $ = jQuery,
        $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader;
	$(document).ready(function(){
  uploader = WebUploader.create({

    // swf文件路径
    swf: 'webupload/Uploader.swf',

    // 文件接收服务端。
    server: 'http://localhost:8008/test/upload',

    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false
});
 uploader.on( 'fileQueued', function( file ) {
        $list.append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
        '</div>' );
    });
$btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploader.stop();
        } else {
            uploader.upload();
        }
    });

});

</script>
</html>
