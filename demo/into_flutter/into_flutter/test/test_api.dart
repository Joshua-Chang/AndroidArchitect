import 'package:flutter_test/flutter_test.dart';
import 'package:into_flutter/http/hi_http.dart';
import 'package:into_flutter/http/request/recommend_request.dart';

void main() {
  testAPI();
}

void testAPI() {
  test("description:testApi", () async {
    var request = RecommendRequest();
    request.add('pageIndex', 1);
    request.add('pageSize', 10);
    var result = await HiHttp.getInstance().fire(request);
    expect(result['goodsList'], isNotNull);
    print(result);
  });
}
