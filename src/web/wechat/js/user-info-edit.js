/**
 * Created by fengziboboy on 2017/11/17.
 */
$(function() {

    // 暂时设个全局变量保存是否需要上传图片
    var img_is_need_upload = false;
    var img_url = "";

    //先获取省份列表，再编辑个人数据（若原本就有数据需默认填写）
    getAllprovince(editUserInfo);

    /* 提交事件 */
    submit_form();

    /*获取省份列表 */
    function getAllprovince(callback) {
        $.get(ServerUrl + "school/allProvinces", function(datas) {
            if (datas.status == Status.Status_OK) {
                var $link = $("#user-address-edit").empty();
                //添加一个选项
                for (var i = 0; i < datas.data.length; i++) {
                    $link.append("<option value = '" + datas.data[i].provinceId + "'>" + datas.data[i].province + "</option>");
                }
                /*设置点击事件*/
                clickEvent();
                /*在编辑个人数据（若原本就有数据需默认填写）*/
                callback();
            } else {
                $.toast("数据获取错误");
            }
        })
    }

    /*设置点击事件*/
    function clickEvent() {
        /*改变地区省份时，学校选项列表改变*/
        $("#user-address-edit").change(function() {
            var provinceId = $("#user-address-edit").val();
            if ((provinceId != "") && (provinceId != null)) {
                getSchoolList(provinceId);
            }
        });
        //只改学校时，避免出现因为没电省份修改而没加载出学校列表
        $("#user-school-edit").focus(function() {
            var provinceId = $("#user-address-edit").val();
            if ((provinceId != "") && (provinceId != null)) {
                getSchoolList(provinceId);
            }
        });
    }

    //编辑个人数据（若原本就有数据需默认填写）
    function editUserInfo() {
        /*获取编辑页面数据*/
        $.get(ServerUrl + "my/user", function(datas) {
            //处理展示个人资料
            if (datas.status == Status.Status_OK) {
                $.toast("数据获取成功");
                // 展示个人资料界面
                init_edit_data(datas.data);
            } else {
                $.toast("数据获取错误");
            }
        });
    }

    /**修改个人信息数据初始化 */
    function init_edit_data(datas) {
        $("#user-info-edit").attr("user-id", datas[0].uid);
        $(".headimg").attr("src", datas[0].headimgurl);
        $("#user-name-edit").val(datas[0].nickname);
        // 性别暂时只有男女
        $("input:radio[value='" + datas[0].sex + "']").attr('checked', 'true');
        $("#user-tel-edit").val(datas[0].phone);
        $("#user-qq-edit").val(datas[0].qq);
        $("#user-weixin-edit").val(datas[0].weixin);

        /*设置了省份之后*/
        $("#user-address-edit").val(datas[1].provinceId);
        // 设置了省份之后，获取学校列表
        var provinceId = $("#user-address-edit").val();
        //haveSchoolId为了显示修改前的学校
        if ((provinceId != "") && (provinceId != null)) {
            getSchoolList(provinceId, datas[1].schoolId);
        }
        /*设置图片*/
        dealImage(datas[0].card);
    }

    /*根据省份取值获取学校列表*/
    function getSchoolList(provinceId, haveSchoolId) {
        $.get(ServerUrl + "school/getSchoolByPid/" + provinceId, function(datas) {
            if (datas.status == Status.Status_OK) {
                var $link = $("#user-school-edit").empty();
                var selectSchoolList = datas.data;
                for (var item in selectSchoolList) {
                    if (selectSchoolList[item].schoolId == haveSchoolId) { /*设置默认值*/
                        $link.append("<option value='" + selectSchoolList[item].schoolId + "' selected='selected'>" + selectSchoolList[item].schoolName + "</option>");
                    } else {
                        $link.append("<option value = '" + selectSchoolList[item].schoolId + "'>" + selectSchoolList[item].schoolName + "</option>");
                    }
                }
            } else {
                $.toast("数据获取错误");
            }
        })
    }


    /*提交表单*/
    function submit_form() {
        $("#user-info-submit").click(function() {
            if (img_is_need_upload && (img_url == null || img_url == "")) {
                alert("请上传学生证");
                return;
            }
            /*获取并组装表单项*/
            var datas = {
                nickname: $("#user-name-edit").val(),
                sex: $('input:radio:checked').val(),
                qq: $("#user-qq-edit").val(),
                phone: $("#user-tel-edit").val(),
                weixin: $("#user-weixin-edit").val(),
                schoolId: $("#user-school-edit").val(),
                card: img_url
            };

            /*检查表单项*/
            if (datas.nickname.length == 0) {
                $.alert("用户名不能为空");
                $("#user-name-edit").focus();
                return;
            }
            if (datas.nickname.length > 16) {
                $.alert("用户名的长度不能超过" + 16 + "个字");
                $("#user-name-edit").focus();
                return;
            }
            if (datas.sex.length == 0) {
                $.alert("性别不能为空");
                $("#user-gender").focus();
                return;
            }
            if (datas.schoolId.length == 0) {
                $.alert("学校不能为空");
                $("#user-school-edit").focus();
                return;
            }

            /*上传服务器*/
            $.confirm("确定修改个人信息吗？", function() {
                uploadData(datas);
            });

        });
    }

    /*-------------------------------------------------------*/
    /*---------------------- 提交表单 -----------------------*/
    /*
     * 数据上传思路：
     * 1.先上传图片
     * 2.再上传数据
     *
     * 图片上传思路：
     * 1.用户选择图片,获取localIds;
     * 2.根据localIds上传图片至微信的服务器,返回mediaId;
     * 3.前端根据mediaId请求自己的服务器去微信的服务器下载图片,返回资源地址;
     * 4.前端根据资源地址获取图片;
     */
    /*上传数据到服务器*/
    function uploadData(datas) {
        /**
         * 先上传图片。
         * 成功后，再提交表单。
         **/
        $.showLoading("资料上传中");
        /*如果需要上传图片则上传图片，否则上传数据*/
        if(img_is_need_upload){
            uploadImage(datas, uploadOtherData);
        }else{
            uploadOtherData(datas);
        }
    }
    /*上传其他数据的回调函数*/
    function uploadOtherData(datas) {
        console.log(datas.valueOf());
        $.post(ServerUrl + "my/edit", datas, function(data) {
            $.hideLoading();
            if (data.status == Status.Status_OK) {
                $.alert("信息上传成功", function() {
                    var userId = $("#user-info-edit").attr("user-id");
                    location.href = ServerUrl + "web/wechat/view/user-show.html?id=" + userId;
                });
            } else {
                $.alert("信息上传失败", function() {});
            }
        });
    }
    /*上传图片：并行上传*/
    function uploadImage(datas, callback) {
        var localId = datas.card;
        /*没有图片，直接上传*/
        if (localId.length == 0) callback(datas);

        wx.uploadImage({
            localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 0, // 默认为1，显示进度提示
            success: function(res) {
                var serverId = res.serverId; // 返回图片的服务器端ID
                /*请求服务器下载图片*/
                downImage(serverId, function(data) {
                    /*图片上传成功*/
                    if (data.status == Status.Status_OK) {
                        /*添加图片地址后上传数据*/
                        datas.card = data.data;
                        /*通过回调上传数据*/
                        callback(datas);
                    } else {
                        $.alert("数据上传出现错误1：" + data.status, function() {
                            $.hideLoading();
                        });
                    }
                });
            },
            fail: function(res) {
                $.alert("数据上传出现错误fail--：" + JSON.stringify(res), function() {
                    $.hideLoading();
                });
            }
        });
    }
    /*---------------------- 提交表单 -----------------------*/
    /*-------------------------------------------------------*/

    /*图片处理*/
    function dealImage(card) {
        /*设置用户原来的图片*/
        if (card != null || card.length > 0) {
            img_url = card;
            img_is_need_upload = false;
            setImage("../../../" + card);
        }

        /*关闭*/
        $("#uploaderFiles-gallery span").click(function() {
            $(this).parent().hide();
        });
        /*删除*/
        $("#uploaderFiles-gallery i").click(function() {
            /*隐藏*/
            $("#uploaderFiles-gallery").hide();
            /*删除*/
            var data_for_delete = $("#uploaderFiles-gallery span").attr("data-for-delete");
            $("#uploaderFiles li[data-for-delete=" + data_for_delete + "]").remove();

            /*设置图片的状态*/
            img_is_need_upload = true;
            img_url = "";

            /*若未上传校园卡，则有添加按钮*/
            $("#uploaderInput").parent().show();
        });
        /*添加*/
        $("#uploaderInput").click(function() {
            var count = $("#uploaderFiles li").size();
            if (count >= 1) return;
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function(res) {
                    // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    img_url = res.localIds[0];
                    img_is_need_upload = true;
                    setImage(res.localIds[0]);
                }
            });
        });

        function setImage(img) {
            $("#uploaderFiles").append('<li class="weui-uploader__file" data-for-delete="1" ' +
                'style="background-image:url(' + img + ')"></li>');
            /*禁止添加图片*/
            $("#uploaderInput").parent().hide();
            /*绑定事件*/
            imageClickEvent();
        }

        /*绑定图片点击事件*/
        function imageClickEvent() {
            /*显示某一张图片*/
            $("#uploaderFiles li").off("click").on("click", function() {
                var img = $(this).css("background-image");
                var $span = $("#uploaderFiles-gallery span");
                $span.css("background-image", img).hide();
                $span.parent().fadeIn();
                $span.delay(300);
                $span.fadeIn("slow");
                /*用于删除*/
                $span.attr("data-for-delete", $(this).attr("data-for-delete"));
            });
        }
    }

});