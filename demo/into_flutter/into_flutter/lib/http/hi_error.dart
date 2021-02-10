class NeedLogin implements HiError {
  @override
  int get code => 401;

  @override
  String get message => "login first";
}

class NeedAuth implements HiError {
  @override
  int get code => 403;

  @override
  String get message => "auth first";
}

class HiError {
  final int code;
  final String message;

  HiError(this.code, this.message);
}
