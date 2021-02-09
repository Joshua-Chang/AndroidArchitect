import 'package:flutter/material.dart';

const GOODS_ITEM = {
  "goodsId": "1580374361011",
  "categoryId": "16",
  "hot": true,
  "sliderImages": [
    {
      "url":
      "https://o.devio.org/images/as/goods/images/2018-12-21/5c3672e33377b65d5f1bef488686462b.jpeg",
      "type": 1
    },
    {
      "url":
      "https://o.devio.org/images/as/goods/images/2018-12-21/117a40a6d63c5bac590080733512b89d.jpeg",
      "type": 1
    },
    {
      "url":
      "https://o.devio.org/images/as/goods/images/2018-12-21/7d4449179b509531414365460d80a87d.jpeg",
      "type": 1
    }
  ],
  "marketPrice": "¥100",
  "groupPrice": "14",
  "completedNumText": "已拼1348件",
  "goodsName": "男长款羽绒外套",
  "tags": "极速退款 全场包邮 7天无理由退货",
  "joinedAvatars": null,
  "createTime": "2020-01-30 16:52:41",
  "sliderImage":
  "http://pic.banggo.com/sources/images/goods/MB/229386/229386_00.jpg?x-oss-process=image/resize,m_pad,w_720,h_720"
};

class RecommendItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        print("onTap");
      },
      child: Card(
        child: PhysicalModel(
          color: Colors.transparent,
          clipBehavior: Clip.antiAlias,
          borderRadius: BorderRadius.circular(5),
          child: Column(
            children: <Widget>[
              _itemImage(context),
              Container(
                alignment: Alignment.centerLeft,
                padding: EdgeInsets.all(4),
                child: Text(
                  GOODS_ITEM['goodsName'],
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(fontSize: 14, color: Colors.black87),
                ),
              ),
              _infoText()
            ],
          ),
        ),
      ),
    );
  }

  _itemImage(BuildContext context) {
    return Image.network(GOODS_ITEM['sliderImage']);
  }

  _infoText() {
    return Container(
      padding: EdgeInsets.fromLTRB(6, 0, 6, 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Padding(padding: EdgeInsets.only(bottom: 5),
            child: Text(GOODS_ITEM['tags'], style:
            TextStyle(fontSize: 11, color: Colors.deepOrangeAccent),),),
          Row(children: <Widget>[
            Text("¥", style: TextStyle(fontSize: 10, color: Colors.redAccent),),
            Padding(padding: EdgeInsets.only(right: 5),
              child: Text(GOODS_ITEM['groupPrice'],
                style: TextStyle(fontSize: 18, color: Colors.redAccent),),),
            Text(GOODS_ITEM['completedNumText'],
              style: TextStyle(fontSize: 12, color: Colors.grey),),
          ],)
        ],
      ),
    );
  }
}
