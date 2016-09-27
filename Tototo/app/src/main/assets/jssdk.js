(function (global, factory) {
    if (typeof define === 'function' && (define.amd || define.cmd)) {
        define(function() {
            return factory(global);
        });
    } else {
        factory(global, true);
    }
}(this, function (window, isGlobal) {
    if (window.wx) return;

    function _invokeMethod(name, args, ext) {
        var _directInvokeMethod = function() {
            window.WeixinJSBridge && WeixinJSBridge.invoke(name, _addVerifyParams(args), function (res) {
                _formatMethodCallback(name, res, ext);
            });
        }
        if (name != apiSdkMethods.config) {
            _verifyReady(_directInvokeMethod);
        } else {
            _bridgeReady(_directInvokeMethod);
        }
    }

    function _onMethod(name, ext, innerInvokeArgs) {
        _verifyReady(function() {
            window.WeixinJSBridge && WeixinJSBridge.on(name, function (res) {
                if (typeof ext == 'object') {
                    innerInvokeArgs && innerInvokeArgs.trigger && innerInvokeArgs.trigger(res);
                    _formatMethodCallback(name, res, ext);
                } else {
                    _formatMethodCallback(name, res, { complete: ext });
                }
            });
        });
    }

    function _addVerifyParams(args) {
        args = args || {};
        args.appId = config.appId;
        args.verifyAppId = config.appId;
        args.verifySignType = 'sha1';
        args.verifyTimestamp = config.timestamp + '';
        args.verifyNonceStr = config.nonceStr;
        args.verifySignature = config.signature;
        return args;
    }

    function _getPayVerifyParams(args) {
        return {
            timeStamp: args.timestamp + '',
            nonceStr: args.nonceStr,
            package: args.package,
            paySign: args.paySign,
            signType: args.signType || 'SHA1'
        }
    }

    function _formatMethodCallback(name, res, ext) {
        delete res.err_code;
        delete res.err_desc;
        delete res.err_detail;
        var errMsg = res.errMsg;
        if (!errMsg) {
            errMsg = res.err_msg;
            delete res.err_msg;
            errMsg = _formatErrMsg(name, errMsg);
            res.errMsg = errMsg;
        }
        ext = ext || {};
        if (ext._complete) {
            ext._complete(res);
            delete ext._complete;
        }
        errMsg = res.errMsg || '';
        if (config.debug) {
            alert(JSON.stringify(res));
        }
        var index = errMsg.indexOf(':');
        var keyword = errMsg.substring(index + 1);
        switch (keyword) {
            case 'ok':
                ext.success && ext.success(res);
                break;
            case 'cancel':
                ext.cancel && ext.cancel(res);
                break;
            default:
                ext.fail && ext.fail(res);
                break;
        }
        ext.complete && ext.complete(res);
    }

    function _formatErrMsg(name, errMsg) {
        var jsApiName = name;
        var apiSdkName = sdkApiMethods[jsApiName];
        if (apiSdkName) {
            jsApiName = apiSdkName;
        }
        var keyword = 'ok';
        if (errMsg) {
            var index = errMsg.indexOf(':');
            keyword = errMsg.substring(index + 1);
            if (keyword == 'confirm') {
                keyword = 'ok';
            }
            if (keyword == 'failed') {
                keyword = 'fail';
            }
            if (keyword.indexOf('failed_') != -1) {
                keyword = keyword.substring(7);
            }
            if (keyword.indexOf('fail_') != -1) {
                keyword = keyword.substring(5);
            }
            keyword = keyword.replace(/\|/g, ' ');
            keyword = keyword.replace(/_/g, ' ');
            keyword = keyword.toLowerCase();
            if (keyword == 'access denied' || keyword == 'no permission to execute') {
                keyword = 'permission denied';
            }
            if (jsApiName == 'config' && keyword == 'function not exist') {
                keyword = 'ok';
            }
            if (keyword == '') {
                keyword = 'fail';
            }
            if (keyword.substring(0, 2) == 'ok') {
                keyword = 'ok';
            }
        }
        errMsg = jsApiName + ':' + keyword;
        return errMsg;
    }

    function _getRealJsApiList(jsApiList) {
        if (!jsApiList) return;
        for (var i = 0, length = jsApiList.length; i < length; ++i) {
            var jsApiName = jsApiList[i];
            var realJsApiName = apiSdkMethods[jsApiName];
            if (realJsApiName) {
                jsApiList[i] = realJsApiName;
            }
        };
        return jsApiList;
    }

    function _report(args) {
        if (isPc || isDebugger || config.debug || reportData.systemType < 0 || isReported) return;
        isReported = true;
        reportData.appId = config.appId;
        reportData.initTime = timeRecorder.initEndTime - timeRecorder.initStartTime;
        reportData.preVerifyTime = timeRecorder.preVerifyEndTime - timeRecorder.preVerifyStartTime;
        wx.getNetworkType({
            success: function(res) {
                reportData.networkType = res.networkType;
                var url = 'http://open.weixin.qq.com/sdk/report?v=' + reportData.version + '&o=' + reportData.preVerifyState + '&s=' + reportData.systemType + '&c=' + reportData.clientVersion + '&a=' + reportData.appId + '&n=' + reportData.networkType + '&i=' + reportData.initTime + '&p=' + reportData.preVerifyTime + '&u=' + reportData.url;
                var img = new Image;
                img.src = url;
            }
        });
    }

    function _getTime() {
        return (new Date).getTime();
    }

    function _bridgeReady(callback) {
        if (!isWeixin) return;
        if (!window.WeixinJSBridge) {
            document.addEventListener && document.addEventListener('WeixinJSBridgeReady', callback, false);
        } else {
            callback();
        }
    }

    function _verifyReady(callback) {
        _bridgeReady(callback);
        // if (preVerifyResult.state != 0) {
        //  callback();
        // } else {
        //  preVerifyArgs._completes.push(callback);
        // }
    }

    var apiSdkMethods = {
        config: 'preVerifyJSAPI',
        onMenuShareTimeline: 'menu:share:timeline',
        onMenuShareAppMessage: 'menu:share:appmessage',
        onMenuShareQQ: 'menu:share:qq',
        onMenuShareWeibo: 'menu:share:weiboApp',
        onMenuShareQZone: 'menu:share:QZone',
        previewImage: 'imagePreview',
        getLocation: 'geoLocation',
        openProductSpecificView: 'openProductViewWithPid',
        addCard: 'batchAddCard',
        openCard: 'batchViewCard',
        chooseWXPay: 'getBrandWCPayRequest',
        navigateTo: 'openUrlWithExtraWebview',
        navigateBack: 'closeWindow',
        publish: 'dispatchEvent',
        setNavigationBarTitle: 'setPageTitle',
        setNavigationBarBackground: 'setNavigationBarColor',
        getStorage: 'getLocalStorage',
        setStorage: 'setLocalStorage',
        clearStorage: 'clearLocalStorage',
        setStatusBar: 'setStatusBarStyle',
    }
    var sdkApiMethods = (function() {
        var _sdkApiMethods = {};
        for (var i in apiSdkMethods) {
            _sdkApiMethods[apiSdkMethods[i]] = i;
        }
        return _sdkApiMethods;
    })();
    var document = window.document;
    var title = document.title;
    var ua = navigator.userAgent.toLowerCase();
    var platform = navigator.platform.toLowerCase();
    var isPc = !!(platform.match('mac') || platform.match('win'));
    var isDebugger = ua.indexOf('wechatdevtools') != -1;
    var isWeixin = ua.indexOf('micromessenger') != -1;
    var isAndroid = ua.indexOf('android') != -1;
    var isIos = (ua.indexOf('iphone') != -1) || (ua.indexOf('ipad') != -1);
    var clientVersion = (function() {
        var regVersionResult = ua.match(/micromessenger\/(\d+\.\d+\.\d+)/) || ua.match(/micromessenger\/(\d+\.\d+)/);
        return regVersionResult ? regVersionResult[1] : '';
    })();
    var isReported = false;
    var isErrorExcuted = false;
    var timeRecorder = {
        initStartTime: _getTime(),
        initEndTime: 0,
        preVerifyStartTime: 0,
        preVerifyEndTime: 0
    }
    var reportData = {
        version: 5,
        appId: '',
        initTime: 0,
        preVerifyTime: 0,
        networkType: '',
        preVerifyState: 1,
        systemType: isIos ? 1 : (isAndroid ? 2 : -1),
        clientVersion: clientVersion,
        url: encodeURIComponent(location.href)
    }
    var config = {};
    var preVerifyArgs = {
        _completes: []
    };
    var preVerifyResult = {
        state: 0,
        data: {}
    };
    var uploadVoiceQueue = [];
    var isUploadingVoice = false;
    var downloadVoiceQueue = [];
    var isDownloadingVoice = false;
    var uploadImageQueue = [];
    var isUploadingImage = false;
    var downloadImageQueue = [];
    var isDownloadingImage = false;
    var actionRecord = 'open'; // open/resume/back
    var isPageNavigated = false;
    var navigateUrlRecord = null;
    var onPageLoadCallback = null;
    var onPageUnloadCallback = null;
    var onPageShowCallback = null;
    var onPageHideCallback = null;
    var pageDataFields = [];
    var wx = {
        // 1 基础接口
        // 1.1 注入配置接口
        config: function(args) {
            config = args;
            var isPreVerify = config.check === false ? false : true;
            _bridgeReady(function() {
                if (isPreVerify) {
                    _invokeMethod(apiSdkMethods.config, {
                        verifyJsApiList: _getRealJsApiList(config.jsApiList)
                    }, (function() {
                        preVerifyArgs._complete = function(res) {
                            timeRecorder.preVerifyEndTime = _getTime();
                            preVerifyResult.state = 1;
                            preVerifyResult.data = res;
                        }
                        preVerifyArgs.success = function(res) {
                            reportData.preVerifyState = 0;
                        }
                        preVerifyArgs.fail = function(res) {
                            if (preVerifyArgs._fail) {
                                preVerifyArgs._fail(res);
                            } else {
                                preVerifyResult.state = -1;
                            }
                        }
                        var _completes = preVerifyArgs._completes;
                        _completes.push(function() {
                            _report();
                        });
                        preVerifyArgs.complete = function(res) {
                            for (var i = 0, length = _completes.length; i < length; ++i) {
                                _completes[i]();
                            };
                            preVerifyArgs._completes = [];
                        }
                        return preVerifyArgs;
                    })());
                    timeRecorder.preVerifyStartTime = _getTime();
                } else {
                    preVerifyResult.state = 1;
                    var _completes = preVerifyArgs._completes;
                    for (var i = 0, length = _completes.length; i < length; ++i) {
                        _completes[i]();
                    };
                    preVerifyArgs._completes = [];
                }
            });
        },

        // 2 Web App核心接口
        // 2.1 缓存Web App资源接口
        cache: function(args) {
            var async = args.resourceList ? false : true;
            _invokeMethod('cache', {
                async: async,
                src: args.src || '',
                resourceList: args.resourceList || []
            }, args);
        },
        // 2.2 导航到新页面
        navigateTo: function(url) {
            if (isWeixin) {
                if (!isPageNavigated) {
                    navigateUrlRecord = url;
                    return;
                }
                var aElement = document.createElement('a');
                aElement.href = url;
                var absoluteUrl = aElement.href;
                aElement = null;
                _invokeMethod(apiSdkMethods.navigateTo, {
                    url: absoluteUrl,
                    openType: 1,
                    delay: true
                });
                actionRecord = 'back';
            } else {
                location.href = url;
            }
        },
        // 打开外链
        openLink: function(args) {
            if (isWeixin) {
                if (!isPageNavigated) {
                    navigateUrlRecord = url;
                    return;
                }
                var aElement = document.createElement('a');
                aElement.href = url;
                var absoluteUrl = aElement.href;
                aElement = null;
                _invokeMethod(apiSdkMethods.navigateTo, {
                    url: absoluteUrl,
                    openType: 1,
                    delay: true
                });
                actionRecord = 'back';
            } else {
                location.href = url;
            }
        },
        // 2.3 回退到之前页面
        navigateBack: function() {
            if (isWeixin) {
                _invokeMethod(apiSdkMethods.navigateBack);
            } else {
                history.back();
            }
        },
        // 2.4 发送webview事件
        publish: function(eventName, data, callback) {
            _invokeMethod(apiSdkMethods.publish, {
                eventName: eventName,
                data: data
            }, { complete: callback });
        },
        // 2.5 接收webview事件
        subscribe: function(eventName, callback) {
            wx.on(eventName, callback);
        },
        // 2.6 开启后台运行
        enableBackgroundProcess: function(args) {
            _invokeMethod('enableBackgroundProcess', {}, args);
        },
        // 2.7 监听页面状态改变
        onPageStateChange: function(callback) {
            _onMethod('onPageStateChange', function(res) {
                if (actionRecord == 'open') {
                    isPageNavigated = true;
                    if (navigateUrlRecord) {
                        wx.navigateTo(navigateUrlRecord);
                    }
                }
                if (res.active) {
                    res.action = actionRecord;
                }
                callback(res);
                if (res.active) {
                    actionRecord = 'resume';
                }
            });
        },
        // 2.8 加载完毕通知客户端渲染
        initReady: function() {
            _invokeMethod('initReady');
        },
        // 2.9 发起网络通信
        request: function(args) {
            _invokeMethod('request', {
                needUser: args.needUser,
                url: args.url,
                data: args.data
            }, args);
        },
        // 2.10 获取用户信息
        getUserInfo: function(args) {
            _invokeMethod('getUserInfo', {}, args);
        },
        // 监听App Service中数据变更
        onPageDataChange: function(dataFields, callback) {
            pageDataFields = dataFields;
            _onMethod('onPageDataChange', function(res) {
                var isPageDataChange = false;
                var changeAppData = res.data;
                var changePageData = {};
                for (var dataField in dataFields) {
                    if (changeAppData.hasOwnProperty(dataField)) {
                        isPageDataChange = true;
                        changePageData[dataField] = changeAppData[dataField];
                    }
                }
                if (isPageDataChange) {
                    res.data = changePageData;
                    callback(res);
                    wx.initReady();
                }
            });
        },
        // 发送Action操作到App Service
        dispatchAction: function(actionName, data) {
            var dataWithAction = { 
                action: actionName,
                data: data 
            };
            data._action = actionName;
            _invokeMethod('dispatchAction', {
                actionName: 'onAppAction',
                data: dataWithAction
            });
        },
        // 监听页面加载完成
        onPageLoad: function(callback) {
            onPageLoadCallback = callback;
        },
        onPageUnload: function(callback) {
            onPageUnloadCallback = callback;
        },
        onPageShow: function(callback) {
            onPageShowCallback = callback;
        },
        onPageHide: function(callback) {
            onPageHideCallback = callback;
        },
        login: function(args) {
            wx.invoke('checkLogin', {}, function(res) {
                if (res.err_msg.indexOf(':fail') != -1) {
                    _invokeMethod('login', {
                        scope: args.scope,
                        url: args.url
                    }, args);
                }
            });
        },

        // 3 音频接口
        // 3.1 开始录音
        startRecord: function(args) {
            _invokeMethod('startRecord', {}, args);
        },
        // 3.2 停止录音
        stopRecord: function(args) {
            _invokeMethod('stopRecord', {}, args);
        },
        // 3.3 监听录音自动停止
        onVoiceRecordEnd: function(args) {
            _onMethod('onVoiceRecordEnd', args);
        },
        // 3.4 播放音频
        playVoice: function(args) {
            _invokeMethod('playVoice', {
                localId : args.localId
            }, args);
        },
        // 3.5 暂停播放音频
        pauseVoice: function(args) {
            _invokeMethod('pauseVoice', {
                localId : args.localId
            }, args);
        },
        // 3.6 停止播放音频
        stopVoice: function(args) {
            _invokeMethod('stopVoice', {
                localId : args.localId
            }, args);
        },
        // 3.7 监听音频播放自动停止
        onVoicePlayEnd: function(args) {
            _onMethod('onVoicePlayEnd', args);
        },
        // 3.8 上传语音
        uploadVoice: function(args) {
            if (isUploadingVoice) {
                uploadVoiceQueue.push(args);
                return;
            }
            isUploadingVoice = true;
            _invokeMethod('uploadVoice', {
                localId : args.localId,
                isShowProgressTips: args.isShowProgressTips == 0 ? 0 : 1
            }, (function(){
                args._complete = function(res) {
                    isUploadingVoice = false;
                    if (uploadVoiceQueue.length != 0) {
                        var uploadVoiceArgs = uploadVoiceQueue.shift();
                        wx.uploadVoice(uploadVoiceArgs);
                    }
                }
                return args;
            })());
        },
        // 3.9 下载语音
        downloadVoice: function(args) {
            if (isDownloadingVoice) {
                downloadVoiceQueue.push(args);
                return;
            }
            isDownloadingVoice = true;
            _invokeMethod('downloadVoice', {
                serverId: args.serverId,
                isShowProgressTips: args.isShowProgressTips == 0 ? 0 : 1
            }, (function(){
                args._complete = function(res) {
                    isDownloadingVoice = false;
                    if (downloadVoiceQueue.length != 0) {
                        var downloadVoiceArgs = downloadVoiceQueue.shift();
                        wx.downloadVoice(downloadVoiceArgs);
                    }
                }
                return args;
            })());
        },
        // 3.10 识别音频并返回识别结果
        translateVoice: function(args) {
            _invokeMethod('translateVoice', {
                localId: args.localId,
                isShowProgressTips: args.isShowProgressTips == 0 ? 0 : 1
            }, args);
        },
        // 3.11 拍照、本地选图
        chooseImage: function(args) {
            _invokeMethod('chooseImage', { 
                scene: '1|2',
                count: args.count || 9,
                sizeType: args.sizeType || ['original', 'compressed'],
                sourceType: args.sourceType || ['album', 'camera']
            }, (function(){
                args._complete = function(res) {
                    if (isAndroid) {
                        var localIds = res.localIds;
                        if (localIds) {
                            res.localIds = JSON.parse(localIds);
                        }
                    }
                }
                return args;
            })());
        },
        // 3.12 图片预览
        previewImage: function(args) {
            _invokeMethod(apiSdkMethods.previewImage, {
                current: args.current,
                urls: args.urls     
            }, args);
        },
        // 3.13 上传图片
        uploadImage: function(args) {
            if (isUploadingImage) {
                uploadImageQueue.push(args);
                return;
            }
            isUploadingImage = true;
            _invokeMethod('uploadImage', {
                localId: args.localId,
                isShowProgressTips: args.isShowProgressTips == 0 ? 0 : 1
            }, (function(){
                args._complete = function(res) {
                    isUploadingImage = false;
                    if (uploadImageQueue.length != 0) {
                        var uploadImageArgs = uploadImageQueue.shift();
                        wx.uploadImage(uploadImageArgs);
                    }
                }
                return args;
            })());
        },
        // 3.14 下载图片
        downloadImage: function(args) {
            if (isDownloadingImage) {
                downloadImageQueue.push(args);
                return;
            }
            isDownloadingImage = true;
            _invokeMethod('downloadImage', {
                serverId: args.serverId,
                isShowProgressTips: args.isShowProgressTips == 0 ? 0 : 1
            }, (function(){
                args._complete = function(res) {
                    isDownloadingImage = false;
                    if (downloadImageQueue.length != 0) {
                        var downloadImageArgs = downloadImageQueue.shift();
                        wx.downloadImage(downloadImageArgs);
                    }
                }
                return args;
            })());
        },

        // 4 硬件设备接口
        // 4.1 获取当前网络类型
        getNetworkType: function(args) {
            var _getNetworType = function(res) {
                var errMsg = res.errMsg;
                res.errMsg = 'getNetworkType:ok';
                var subtype = res.subtype;
                delete res.subtype;
                if (subtype) {
                    res.networkType = subtype;
                } else {
                    var index = errMsg.indexOf(':');
                    var keyword = errMsg.substring(index + 1);
                    switch (keyword) {
                        case 'wifi':
                        case 'edge':
                        case 'wwan':
                            res.networkType = keyword;
                            break;
                        default:
                            res.errMsg = 'getNetworkType:fail';
                            break;
                    }
                }
                return res;
            }
            _invokeMethod('getNetworkType', {}, (function(){
                args._complete = function(res) {
                    res = _getNetworType(res);
                }
                return args;
            })());
        },
        // 4.2 打开当前地理位置地图
        openLocation: function(args) {
            _invokeMethod('openLocation', {
                latitude: args.latitude,
                longitude: args.longitude,
                name: args.name || '',
                address: args.address || '',
                scale: args.scale || 28,
                infoUrl: args.infoUrl || ''
            }, args);
        },
        // 4.3 获取当前地理位置
        getLocation: function(args) {
            _invokeMethod(apiSdkMethods.getLocation, {
                type: args.type || 'wgs84'
            }, (function(){
                args._complete = function(res) {
                    delete res.type;
                }
                return args;
            })());
        },

        // 5 用户界面接口
        // 5.1 关闭当前窗口
        closeWindow: function() {
            _invokeMethod('closeWindow');
        },
        // 5.2 显示键盘 
        showKeyboard: function(args) {
            // var _preventTouchEvent = function(e) {
            //  e.preventDefault();
            //  document.removeEventListener('touchend', _preventTouchEvent, false);
            // }
            // document.addEventListener('touchend', _preventTouchEvent, false);
            _invokeMethod('showKeyboard', {
                placeholder: args.placeholder || '',
                maxLength: args.maxLength || 0,
                text: args.text,
                y: touchY
            }, args);
        },
        // 5.3 清除webview背景区域
        setBounceBackground: function(args) {
            _invokeMethod('setBounceBackground', {
                backgroundColor: args.topColor,
                footerBounceColor: args.bottomColor
            }, args);
        },
        // 5.4 清除弹跳滚动
        disableBounceScroll: function(args) {
            _invokeMethod('disableBounceScroll', {
                position: args.place || ['top', 'bottom']
            }, args);
        },
        // 5.5 设置页面标题
        setNavigationBarTitle: function(args) {
            _invokeMethod(apiSdkMethods.setNavigationBarTitle, {
                title: args.text,
                color: args.color
            }, args)
        },
        // 5.6 设置导航栏颜色
        setNavigationBarBackground: function(args) {
            _invokeMethod(apiSdkMethods.setNavigationBarBackground, {
                color: args.color,
                alpha: 1 
            }, args);
        },
        // 5.7 设置导航栏两侧按钮
        setNavigationBarButtons: function(args) {
            _invokeMethod('setNavigationBarButtons', {
                left: args.left,
                right: args.right
            }, args);
            var onRightButtonClick = args.right && args.right.onClick;
            if (onRightButtonClick) {
                wx.on('onNavigationBarRightButtonClick', onRightButtonClick);
            }
        },
        // 5.8 显示导航栏加载动画
        showNavigationBarLoading: function(args) {
            _invokeMethod('showNavigationBarLoading', {}, args);
        },
        // 5.9 隐藏导航栏加载动画
        hideNavigationBarLoading: function(args) {
            _invokeMethod('hideNavigationBarLoading', {}, args);
        },
        // 5.10 开启全屏
        enableFullScreen: function(args) {
            _invokeMethod('enableFullScreen', {}, args);
        },
        // 5.11 开启下拉刷新
        enablePullDownRefresh: function(args) {
            _invokeMethod('enablePullDownRefresh', {}, args);
        },
        // 5.12 监听下拉刷新
        onPullDownRefresh: function(callback) {
            wx.on('onPullDownRefresh', callback);
        },
        // 5.13 停止下拉刷新
        stopPullDownRefresh: function(args) {
            _invokeMethod('stopPullDownRefresh', {}, args);
        },
        // 5.14 禁止下拉刷新
        disablePullDownRefresh: function(args) {
            _invokeMethod('disablePullDownRefresh');
        },
        // 5.15 设置状态栏样式
        setStatusBar: function(args) {
            _invokeMethod(apiSdkMethods.setStatusBar, {
                color: args.style
            }, args);
        },
        // 5.16 设置底部标签栏
        setTabBar: function(args) {
            _invokeMethod('setTabBar', {
                backgroundColor: args.backgroundColor,
                borderColor: args.borderStyle,
                textColor: args.textColor,
                selectedTextColor: args.selectedTextColor,
                selectedIndex: args.selectedIndex,
                items: args.items
            }, args);
            var onItemSelect = args.onSelect;
            if (onItemSelect) {
                wx.on('onTabBarClick', onItemSelect);
            }
        },

        // 6 本地数据
        // 6.1 读取数据
        getStorage: function(key, callback) {
            _invokeMethod(apiSdkMethods.getStorage, {
                key: key
            }, { 
                complete: function(res) {
                    var dataType = res.dataType;
                    var data = res.data;
                    if (dataType == 'String') {
                        data = data;
                    } else if (dataType == 'Array' || dataType == 'Object') {
                        data = JSON.parse(data);
                    } else if (dataType == 'Number') {
                        data = parseFloat(data);
                    } else if (dataType == 'Boolean') {
                        data = data == 'true';
                    } else if (dataType == 'Date') {
                        data = new Date(data);
                    } else {
                        data = null;
                    }
                    res.data = data;
                    delete res.dataType;
                    callback(res);
                }
            });
        },
        // 6.2 设置数据
        setStorage: function(key, data, callback) {
            var dataType = Object.prototype.toString.call(data).split(' ')[1].split(']')[0];
            if (dataType == 'Array' || dataType == 'Object') {
                data = JSON.stringify(data);
            } else if (dataType == 'String' || dataType == 'Number' || dataType == 'Boolean' || dataType == 'Date') {
                data = data.toString();
            } else {
                data = '';
            }
            _invokeMethod(apiSdkMethods.setStorage, {
                key: key,
                data: data,
                dataType: dataType
            }, { complete: callback });
        },
        // 6.3 清空数据
        clearStorage: function(callback) {
            _invokeMethod(apiSdkMethods.clearStorage, {}, { complete: callback });
        },

        // 菜单设置接口
        // 批量隐藏菜单项
        hideMenuItems: function(args) {
            _invokeMethod('hideMenuItems', {
                menuList: args.menuList
            }, args);
        },
        // 批量显示菜单项
        showMenuItems: function(args) {
            _invokeMethod('showMenuItems', {
                menuList: args.menuList
            }, args);
        },
        // 隐藏所有非基本菜单项
        hideAllNonBaseMenuItem: function(args) {
            _invokeMethod('hideAllNonBaseMenuItem', {}, args);
        },
        // 显示所有非基本菜单项
        showAllNonBaseMenuItem: function(args) {
            _invokeMethod('showAllNonBaseMenuItem', {}, args);
        },

        // 分享接口
        // 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
        onMenuShareTimeline: function(args) {
            _onMethod(apiSdkMethods.onMenuShareTimeline, {
                complete: function() {
                    _invokeMethod('shareTimeline', {
                        title: args.title || title,
                        desc: args.title || title,
                        img_url: args.imgUrl || '',
                        link: args.link || location.href,
                        type: args.type || 'link',
                        data_url: args.dataUrl || ''
                    }, args);
                }
            }, args);
        },
        // 监听“分享给朋友”按钮点击、自定义分享内容及分享结果接口
        onMenuShareAppMessage: function(args) {
            _onMethod(apiSdkMethods.onMenuShareAppMessage, {
                complete: function() {
                    _invokeMethod('sendAppMessage', {
                        title: args.title || title,
                        desc: args.desc || '',
                        link: args.link || location.href,
                        img_url: args.imgUrl || '',
                        type: args.type || 'link',
                        data_url: args.dataUrl || ''
                    }, args);
                }
            }, args);
        },
        // 监听“分享到QQ”按钮点击分享状态及自定义分享内容
        onMenuShareQQ: function(args) {
            _onMethod(apiSdkMethods.onMenuShareQQ, {
                complete: function() {
                    _invokeMethod('shareQQ', {
                        title: args.title || title,
                        desc: args.desc || '',
                        img_url: args.imgUrl || '',
                        link: args.link || location.href
                    }, args);
                }
            }, args);
        },
        // 监听“分享到微博”按钮点击分享状态及自定义分享内容
        onMenuShareWeibo: function(args) {
            _onMethod(apiSdkMethods.onMenuShareWeibo, {
                complete: function() {
                    _invokeMethod('shareWeiboApp', {
                        title: args.title || title,
                        desc: args.desc || '',
                        img_url: args.imgUrl || '',
                        link: args.link || location.href
                    }, args);
                }
            }, args);
        },
        // 监听“分享到QZone”按钮点击分享状态及自定义分享内容
        onMenuShareQZone: function(args) {
            _onMethod(apiSdkMethods.onMenuShareQZone, {
                complete: function() {
                    _invokeMethod('shareQZone', {
                        title: args.title || title,
                        desc: args.desc || '',
                        img_url: args.imgUrl || '',
                        link: args.link || location.href
                    }, args);
                }
            }, args);
        },

        // 微信原生接口
        // 扫描二维码
        scanQRCode: function(args) {
            _invokeMethod('scanQRCode', {
                needResult : args.needResult || 0,
                scanType: args.scanType || ["qrCode","barCode"]
            }, (function() {
                args._complete = function(res) {
                    if (isIos) {
                        var resultStr = res.resultStr;
                        if (resultStr) {
                            var resultStrData = JSON.parse(resultStr);
                            res.resultStr = resultStrData && resultStrData.scan_code && resultStrData.scan_code.scan_result;
                        }
                    }
                }
                return args;
            })());
        },

        // 微信支付接口
        // 发起微信支付
        chooseWXPay: function(args) {
            _invokeMethod(apiSdkMethods.chooseWXPay, _getPayVerifyParams(args), args);
        },

        // 微信卡券接口
        // 添加卡券
        addCard: function(args) {
            var rawCardList = args.cardList;
            var newCardList = [];
            for (var i = 0, length = rawCardList.length; i < length; ++i) {
                var rawCard = rawCardList[i];
                var cardExt = rawCard.cardExt;
                if (typeof cardExt == 'object') {
                    cardExt = JSON.stringify(cardExt);
                }
                var newCard = {
                    card_id: rawCard.cardId,
                    card_ext: cardExt
                };
                newCardList.push(newCard);
            };
            _invokeMethod(apiSdkMethods.addCard, {
                card_list: newCardList
            }, (function(){
                args._complete = function(res) {
                    var cardList = res.card_list;
                    if (cardList) {
                        cardList = JSON.parse(cardList);
                        for (var i = 0, length = cardList.length; i < length; ++i) {
                            var card = cardList[i];
                            card.cardId = card.card_id;
                            card.cardExt = card.card_ext;
                            card.isSuccess = card.is_succ ? true : false;
                            delete card.card_id;
                            delete card.card_ext;
                            delete card.is_succ;
                        };
                        res.cardList = cardList;
                        delete res.card_list;
                    }
                }
                return args;
            })());
        },
        // 选择卡券
        chooseCard: function(args) {
            _invokeMethod('chooseCard', {
                app_id: config.appId,
                location_id: args.shopId || '',
                sign_type: args.signType || 'SHA1',
                card_id: args.cardId || '',
                card_type: args.cardType || '',
                card_sign: args.cardSign,
                time_stamp: args.timestamp + '',
                nonce_str: args.nonceStr
            }, (function(){
                args._complete = function(res) {
                    res.cardList = res.choose_card_info;
                    delete res.choose_card_info;
                }
                return args;
            })());
        },
        // 打开卡券
        openCard: function(args) {
            var rawCardList = args.cardList;
            var newCardList = [];
            for (var i = 0, length = rawCardList.length; i < length; ++i) {
                var rawCard = rawCardList[i];
                var newCard = {
                    card_id: rawCard.cardId,
                    code: rawCard.code
                };
                newCardList.push(newCard);
            };
            _invokeMethod(apiSdkMethods.openCard, {
                card_list: newCardList
            }, args);
        },
        // 核销并分享卡券
        consumeAndShareCard: function(args) {
            _invokeMethod(apiSdkMethods.consumeAndShareCard, {
                consumedCardId: args.cardId,
                consumedCode: args.code
            }, args);
        },

        // 以下接口不在最新文档中显示
        // 判断当前版本是否支持指定JS接口,支持批量判断
        checkJsApi: function(args) {
            var _getCheckResult = function(res) {
                var checkResult = res.checkResult;
                for (var i in checkResult) {
                    var jsSdkName = sdkApiMethods[i];
                    if (jsSdkName) {
                        checkResult[jsSdkName] = checkResult[i];
                        delete checkResult[i];
                    }
                }
                return res;
            }
            _invokeMethod('checkJsApi', {
                jsApiList: _getRealJsApiList(args.jsApiList)
            }, (function(){
                args._complete = function(res) {
                    if (isAndroid) {
                        var checkResult = res.checkResult;
                        if (checkResult) {
                            res.checkResult = JSON.parse(checkResult);
                        }
                    }
                    res = _getCheckResult(res);
                }
                return args;
            })());
        },
        // config验证后会执行ready方法,不管是否验证成功
        ready: function(callback) {
            _verifyReady(callback);
        },
        // 域名没有权限或者签名失败时执行
        error: function(callback) {
            if (isErrorExcuted) return;
            isErrorExcuted = true;
            if (preVerifyResult.state == -1) {
                callback(preVerifyResult.data);
            } else {
                preVerifyArgs._fail = callback;
            }
        },
        // 没有封装输入输出的invoke方法
        invoke: function(name, args, callback) {
            _bridgeReady(function() {
                window.WeixinJSBridge && WeixinJSBridge.invoke(name, _addVerifyParams(args), callback);
            });
        },
        // 没有封装输入输出的on方法
        on: function(name, callback) {
            _bridgeReady(function() {
                window.WeixinJSBridge && WeixinJSBridge.on(name, callback);
            });
        },
        // 获取GetA8Key后带有用户态信息的url
        getWXUrl: function(callback) {
            _bridgeReady(function() {
                var result = { errMsg: 'getWXUrl:ok' };
                if (window.isWeixinCached) {
                    var url = window.getA8KeyUrl;
                    if (url) {
                        result.url = url;
                        callback(result);
                    } else {
                        wx.on('onGetA8KeyUrl', function(res) {
                            result.url = res.url;
                            callback(result);
                        });
                    }
                } else {
                    result.url = location.href;
                    callback(result);
                }
            });
        },

        // // 隐藏菜单栏
        // hideOptionMenu: function(args) {
        //  _invokeMethod('hideOptionMenu', {}, args);
        // },
        // // 显示菜单栏
        // showOptionMenu: function(args) {
        //  _invokeMethod('showOptionMenu', {}, args);
        // },
        // // 跳转到微信商品特定界面
        // openProductSpecificView: function(args) {
        //  _invokeMethod(apiSdkMethods.openProductSpecificView, {
        //      pid: args.productId,
        //      view_type: args.viewType || 0,
        //      ext_info: args.extInfo
        //  }, args);
        // },
        // // 开始搜索Beacon设备
        // startSearchBeacons: function(args) {
        //  _invokeMethod(apiSdkMethods.startSearchBeacons, {
        //      ticket: args.ticket
        //  }, args);
        // },
        // // 停止搜索Beacon设备
        // stopSearchBeacons: function(args) {
        //  _invokeMethod(apiSdkMethods.stopSearchBeacons, {}, args);
        // },
        // // 监听搜索到的Beacon设备
        // onSearchBeacons: function(args) {
        //  _onMethod(apiSdkMethods.onSearchBeacons, args);
        // },
    }

    var touchY = 0;
    document.addEventListener('touchstart', function(e) {
        touchY = e.touches[0].pageY;
    }, false);

    var errorImgs = {};
    var errorImgCounter = 1;
    document.addEventListener('error', function(e){
        if (isAndroid) return;
        var element = e.target;
        var tag = element.tagName;
        var src = element.src;
        if (tag == 'IMG' || tag == 'VIDEO' || tag == 'AUDIO' || tag == 'SOURCE') {
            var isLocalId = src.indexOf('wxlocalresource://') != -1;
            if (isLocalId) {
                e.preventDefault();
                e.stopPropagation();
                var imgId = element['wx-id'];
                if (!imgId) {
                    imgId = errorImgCounter++;
                    element['wx-id'] = imgId;
                }
                if (errorImgs[imgId]) return;
                errorImgs[imgId] = true;
                wx.invoke('getLocalImgData', { 
                    localId: src 
                }, function(res){
                    if (res.err_msg.indexOf(':ok') != -1) {
                        element.src = res.localData;
                    }
                });
            }
        }
    }, true);

    document.addEventListener('load', function(e) {
        if (isAndroid) return;
        var element = e.target;
        var tag = element.tagName;
        var src = element.src;
        if (tag == 'IMG' || tag == 'VIDEO' || tag == 'AUDIO' || tag == 'SOURCE') {
            var imgId = element['wx-id'];
            if (imgId) {
                errorImgs[imgId] = false;
            }
        }
    }, true);

    _bridgeReady(function() {
        timeRecorder.initEndTime = _getTime();

        wx.on('onPageLoad', function(res) {
            if (pageDataFields && pageDataFields.length == 0) {
                wx.initReady();
            }
            onPageLoadCallback && onPageLoadCallback(res);
        });

        wx.on('onPageUnload', function(res) {
            onPageUnloadCallback && onPageUnloadCallback(res);
        });

        wx.on('onPageShow', function(res) {
            if (actionRecord == 'open') {
                isPageNavigated = true;
                if (navigateUrlRecord) {
                    wx.navigateTo(navigateUrlRecord);
                }
            }
            onPageShowCallback && onPageShowCallback(res);
        });

        wx.on('onPageHide', function(res) {
            onPageHideCallback && onPageHideCallback(res);
        });

        // 开发者可能不调用，但需要依赖该事件做一些事情，所以全局调用一次
        wx.onPageStateChange(function(res) {});
    });

    if (isGlobal) {
        window.wx = wx;
    }

    return wx;
}));
