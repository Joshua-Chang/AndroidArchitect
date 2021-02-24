import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:into_flutter/http/hi_error.dart';
import 'package:into_flutter/http/request/base_request.dart';
import 'package:http/http.dart' as http;

const userDio = true;

class HiHttp {
  HiHttp._();

  static HiHttp _instance;

  static HiHttp getInstance() {
    if (_instance == null) {
      _instance = HiHttp._();
    }
    return _instance;
  }

  Future fire(BaseRequest request) async {
    if (request.httpMethod() == HttpMethod.GET) {
      return _doGet(request);
    }
  }

  Future _doGet(BaseRequest request) async {
    var uri;
    if (request.params.length != 0) {
      // uri = Uri.https(request.url(), request.path(), request.params);
      uri = Uri.http(request.url(), request.path(), request.params);
    } else {
      // uri = Uri.https(request.url(), request.path());
      uri = Uri.http(request.url(), request.path());
    }
    var header = getHeaderParams();
    var response, result;
    if (userDio) {
      response =
          await Dio().get(uri.toString(), options: Options(headers: header));
      result = response.data;
    } else {
      response = await http.get(uri.toString(), headers: header);
      var utf8decoder = Utf8Decoder();
      if (response.headers["content-type"].contains("/json")) {
        result = json.decode(utf8decoder.convert(response.bodyBytes));
      } else {
        result = utf8.decode(response.bodyBytes);
      }
    }

    if (response.statusCode == 200) {
      if (result['code'] == 0) {
        return result['data'];
      } else {
        throw Exception(result['msg']);
      }
    } else if (response.statusCode == 401) {
      throw Exception(NeedLogin());
    } else if (response.statusCode == 403) {
      throw Exception(NeedAuth());
    } else {
      throw Exception(
          'statusCode${response.statusCode},message${result.toString()}');
    }
  }

  getHeaderParams() {
    return {"auth-token": "MTUdfaklsdfjdf"}; /* :才是map */
  }
}
