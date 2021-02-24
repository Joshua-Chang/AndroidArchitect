import 'package:into_flutter/http/request/base_request.dart';

class RecommendRequest extends BaseRequest {
  @override
  HttpMethod httpMethod() {
    return HttpMethod.GET;
  }

  @override
  bool needLogin() {
    return false;
  }

  @override
  String path() {
    return "as/goods/recommend";
  }
}
