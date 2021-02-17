```shell script
react-native bundle --platform android --dev false --entry-file index.js --bundle-output ../ASProj/app/src/main/assets/index.android.bundle --assets-dest ../ASProj/app/src/main/res
```
打包到Android项目下的assest目录，不开启nmp服务也可加载，用于js打包后Android打包。
再调试时删除bundle，否则默认还加载assest目录


Preferences | Languages & Frameworks | JavaScript | Libraries下

@types/react-native/
@types/react-native-autocomplete-input/
html/node_module等要全选（横线是半选）Eslint munu模式 才能输入自动补全
