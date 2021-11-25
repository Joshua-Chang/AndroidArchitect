import 'dart:convert';

import 'package:get/get_core/src/get_main.dart';
import 'package:get/get_instance/src/extension_instance.dart';
import 'package:get/get_utils/src/platform/platform.dart';
import 'package:lovenote_flutter/app/core/common/global/app_config_service.dart';
import 'package:lovenote_flutter/app/core/common/storage_keys.dart';
import 'package:lovenote_flutter/app/core/utils/http/dio_new.dart';
import 'package:lovenote_flutter/app/core/utils/platform_bridge/origin_method.dart';
import 'package:lovenote_flutter/app/core/utils/platform_bridge/platform_bridge_controller.dart';
import 'package:package_info/package_info.dart';

class RequestInterceptor extends Interceptor {
  @override
  Future<void> onRequest(
      RequestOptions options, RequestInterceptorHandler handler) async {
    var domain;
    var env;
    var userAgent;
    var userInfo;

    final AppConfigService config = Get.find();
    final PlatformBridgeController platformBridge = Get.find();

    if (options.baseUrl.isEmpty) {
      if (config.box.hasData(StorageKeys.host))
        domain = config.box.read(StorageKeys.host);
      else {
        env = await platformBridge.callOriginMethod(OriginMethod.getEnv);
        switch (env) {
          case "dev":
            domain = "https://dev-api.didiapp.com";
            break;
          case "xlab":
            domain = "https://xlab-api.didiapp.com";
            break;
          case "release":
            domain = "https://api.didiapp.com";
            break;
          default:
            break;
        }
        config.box.writeInMemory(StorageKeys.host, domain);
      }
      options.baseUrl = domain;
    }

    if (options.headers.isEmpty) {
      var headers = {
        /*"User-Agent": userAgent,*/
        "Accept": "application/json",
        /*"access_token": config.box.read(StorageKeys.token),*/
      };
      /*userAgent*/
      if (config.box.hasData(StorageKeys.userAgent))
        userAgent = config.box.read(StorageKeys.userAgent);
      else {
        userAgent = await platformBridge
            .callOriginMethod(OriginMethod.getUserAgent) as String;
        config.box.writeInMemory(StorageKeys.userAgent, userAgent);
      }
      headers['User-Agent'] = userAgent;
      /*userInfo token*/
      if (config.box.hasData(StorageKeys.userInfo))
        userInfo = config.box.read(StorageKeys.userInfo);
      else {
        userInfo = await Get.find<PlatformBridgeController>()
            .callOriginMethod(OriginMethod.getUserInfo);
        if (GetPlatform.isAndroid) userInfo = json.decode(userInfo);
        config.box.writeInMemory(StorageKeys.token, userInfo['token']);
        config.box.writeInMemory(StorageKeys.userInfo, userInfo);
        /*update when userInfo changed*/
        final packageInfo = await PackageInfo.fromPlatform();
        ta.setSuperProperties({
          'app_build_version': packageInfo.buildNumber,
          'user_id': userInfo['user']['id'],
          'lovers_id': userInfo['lover']['id'],
          'intimate_id': userInfo['other']['id'],
        });
      }
      headers['token'] = userInfo['token'];
      options.headers = headers;
    }
  }
}

HttpClient
  clearHeaders(){
    _dio.options.headers.clear();
  }
