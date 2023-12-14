import 'dart:convert';

import 'package:apapedia21/widgets/order_history_item.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import '../model/order_history_model.dart';

class OrderHistoryPage extends StatefulWidget {
  const OrderHistoryPage({Key? key}) : super(key: key);

  @override
  State<OrderHistoryPage> createState() => _OrderHistoryPageState();
}

class _OrderHistoryPageState extends State<OrderHistoryPage> {
  List<OrderHistoryModel> list=[];
  bool isLoading=true;
  @override
  void initState() {
    getData();
    super.initState();
  }

  getData()async{
    fetchProduct("token");
    isLoading=false;
    setState(() {
    });
  }

  Future<List<OrderHistoryModel>?> fetchProduct(String? token) async {
    print(token);
    // if (token == null) {
    //   print('JWT token is null');
    //   return null;
    // }
    try {
      final response = await http.get(
        Uri.parse('https://mocki.io/v1/52374d1c-c597-4ee2-9475-a0b58c60eefb'),
        // headers: {
        //   'Content-Type': 'application/json; charset=UTF-8',
        //   // 'Authorization': 'Bearer $token',
        // },
      );
      // print("RESPONSE BODY : " + response.body);

      if (response.statusCode == 200) {
        print(response.body);
        list=orderHistoryModelFromJson(json.encode(json.decode(response.body)));
        return list;
      } else {
        print('Failed to load product. Status code: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('Exception occurred: $e');
      return null;
    }

  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 2,
        centerTitle: true,
        title: Text("Order History",style: TextStyle(fontWeight: FontWeight.bold,fontSize: 18),),
      ),
      body: isLoading?Center(child: CircularProgressIndicator(),):SafeArea(
        child: ListView.builder(
          itemBuilder: (context,index){
            return OrderHistoryItem(orderHistoryModel: list[index],);
          },
          itemCount: list.length,
        ),
      ),
    );
  }


}
