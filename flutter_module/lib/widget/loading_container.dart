import 'package:flutter/material.dart';

class LoadingContainer extends StatelessWidget {
  final Widget child;
  final bool isLoading;
  final bool isCover; /* 是否是覆盖页面的loading */

  const LoadingContainer(
      {Key key, this.child, this.isLoading, this.isCover = false})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return !isCover
        ? !isLoading
            ? child
            : _loadingView
        : Stack(
            children: [child, isLoading ? _loadingView : Container()],
          );
  }

  Widget get _loadingView {
    return Center(
      child: CircularProgressIndicator(
        valueColor: AlwaysStoppedAnimation<Color>(Color(0xffd44949)),
      ),
    );
  }
}
