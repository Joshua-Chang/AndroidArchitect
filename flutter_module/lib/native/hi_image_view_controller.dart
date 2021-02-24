import 'package:flutter/services.dart';

class HiImageViewController {
  MethodChannel _channel;

  HiImageViewController(int id) {
    _channel = MethodChannel("HiImageView_$id");
    _channel.setMethodCallHandler(_handleMethod);
  }

  /*native 通过channel调flutter*/
  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "your method name":
        var text = call.arguments as String;
        /*假如native传入的是String*/
        return new Future.value("Text from native: $text");
    /*flutter 回复native*/
    }
  }

  /*flutter 通过channel调native*/
  Future<void> setUrl(String url) async {
    try {
      final String result = await _channel.invokeMethod("setUrl", {"url": url});
      print("Result from native:$result");
    }on PlatformException catch(e){
      print("Error from native:${e.message}");
    }
  }
}