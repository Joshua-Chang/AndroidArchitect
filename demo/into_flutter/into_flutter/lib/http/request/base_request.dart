enum HttpMethod { GET, POST }

abstract class BaseRequest {
  String url() {
    return "10.0.2.2:8080";
  }

  HttpMethod httpMethod();

  String path();

  bool needLogin();

  Map<String, String> params = Map();

  Map<String, String> add(String k, Object v) {
    params[k] = v.toString();
    return params;
  }
}
