import 'package:apapedia21/converter.dart';
import 'package:apapedia21/model/order_history_model.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:apapedia21/widgets/order_item.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class OrderHistoryItem extends StatelessWidget {
  const OrderHistoryItem({Key? key, required this.orderHistoryModel}) : super(key: key);
  final OrderHistoryModel orderHistoryModel;
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20,vertical: 10),
      margin: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            spreadRadius: 3,
            blurRadius: 3,
            offset: Offset(0, 3), // changes position of shadow
          ),
        ],
      ),
      child:  Column(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [

          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text("Seller Id : ${orderHistoryModel.sellerId.substring(orderHistoryModel.sellerId.length - 5)}",style: TextStyle(fontSize: 14,fontWeight: FontWeight.bold,color: Colors.black),),
              RichText(
                text: TextSpan(
                    text: 'Status : ',
                    style: TextStyle(
                        color: Colors.black, fontSize: 14),
                    children: <TextSpan>[
                      TextSpan(text: 'Dikirim',
                        style: TextStyle(
                            color: ColorPallete.primary, fontSize: 14),
                      )
                    ]
                ),
              ),
            ],
          ),
          SizedBox(height: 20,),
          ListView.separated(
            shrinkWrap: true,
            itemBuilder: (context,index){
              return OrderItem(listOrderItem: orderHistoryModel.listOrderItem[index]);
            },
            separatorBuilder: (context,index){
              return Divider();
            },
            itemCount: orderHistoryModel.listOrderItem.length,
          ),
          SizedBox(height: 30,),
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text("Total Harga : ",style: TextStyle(fontSize: 14,fontWeight: FontWeight.w400,color: Colors.black),),
              Text("${CurrencyFormat.convertToIdr(orderHistoryModel.totalPrice)}",style: TextStyle(fontSize: 14,fontWeight: FontWeight.w600,color: ColorPallete.primary),),
            ],
          ),


        ],
      ),
    );
  }
  getFormatedDate(date) {
    var inputFormat = DateFormat('yyyy-MM-DDTHH:mm:ss');
    var inputDate = inputFormat.parse(date);
    var outputFormat = DateFormat('dd MMMM yyyy');
    return outputFormat.format(inputDate);
  }
}
