import 'package:flutter/material.dart';
import 'package:into_flutter/navigator/home_navigator.dart';
import 'package:into_flutter/page/recommend_page.dart';

void main() => runApp(MyApp(HomeNavigator()));

class MyApp extends StatelessWidget {
  final Widget page;

  const MyApp(this.page);
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Android架构师',
        home: Scaffold(
          body: page,
        ));
  }
}
