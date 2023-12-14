import 'package:apapedia21/model/order_history_model.dart';
import 'package:flutter/material.dart';

import '../converter.dart';
import '../utils/color_pallette.dart';

class OrderItem extends StatelessWidget {
  const OrderItem({Key? key, required this.listOrderItem}) : super(key: key);
  final ListOrderItem listOrderItem;
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(listOrderItem.productName,style: TextStyle(fontSize: 14,fontWeight: FontWeight.bold,color: Colors.black),),
              // Text("Order Item Id : "+listOrderItem.orderItemId.substring(listOrderItem.orderItemId.length-5),style: TextStyle(fontSize: 14,fontWeight: FontWeight.bold,color: Colors.black),),

            ],
          ),
          SizedBox(height: 10,),
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text("Quantity : ${listOrderItem.quantity}",style: TextStyle(fontSize: 14,fontWeight: FontWeight.w400,color: Colors.black),),
              Text("${CurrencyFormat.convertToIdr(listOrderItem.productPrice * listOrderItem.quantity)}",style: TextStyle(fontSize: 14,fontWeight: FontWeight.w600,color: ColorPallete.primary),),
            ],
          ),
        ],
      ),
    );
  }
}
