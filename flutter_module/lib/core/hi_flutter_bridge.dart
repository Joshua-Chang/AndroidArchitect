import 'package:flutter/services.dart';

class HiFlutterBridge {
  static HiFlutterBridge _instance = HiFlutterBridge._();
  MethodChannel _bridge = const MethodChannel("HiFlutterBridge");
  var _listeners = {};

  var header;

  HiFlutterBridge._(){
    _bridge.setMethodCallHandler((MethodCall call) {
      String method = call.method;
      if (_listeners[method] != null) {
        return _listeners[method](call);
      }
      return null;
    });
  }

  static HiFlutterBridge getInstance() => _instance;

  register(String method, Function(MethodCall)callBack) {
    _listeners[method] = callBack;
  }

  unRegister(String method) {
    _listeners.remove(method);
  }

  goToNative(Map params){
    _bridge.invokeListMethod("goToNative",params);
  }
  MethodChannel bridge(){
    return _bridge;
  }
  Future<Map<String,String>> getHeaderParams()async{
    Map header = await _bridge.invokeMethod('getHeaderParams',{});
    return this.header=Map<String,String>.from(header);
  }
}
