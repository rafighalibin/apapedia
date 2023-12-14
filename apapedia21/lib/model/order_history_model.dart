// To parse this JSON data, do
//
//     final orderHistoryModel = orderHistoryModelFromJson(jsonString);

import 'dart:convert';

List<OrderHistoryModel> orderHistoryModelFromJson(String str) => List<OrderHistoryModel>.from(json.decode(str).map((x) => OrderHistoryModel.fromJson(x)));

String orderHistoryModelToJson(List<OrderHistoryModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class OrderHistoryModel {
  String orderId;
  String createdAt;
  String updatedAt;
  int status;
  int totalPrice;
  String customerId;
  String sellerId;
  List<ListOrderItem> listOrderItem;

  OrderHistoryModel({
    required this.orderId,
    required this.createdAt,
    required this.updatedAt,
    required this.status,
    required this.totalPrice,
    required this.customerId,
    required this.sellerId,
    required this.listOrderItem,
  });

  factory OrderHistoryModel.fromJson(Map<String, dynamic> json) => OrderHistoryModel(
    orderId: json["orderId"],
    createdAt:json["createdAt"],
    updatedAt: json["updatedAt"],
    status: json["status"],
    totalPrice: json["totalPrice"],
    customerId: json["customerId"],
    sellerId: json["sellerId"],
    listOrderItem: List<ListOrderItem>.from(json["listOrderItem"].map((x) => ListOrderItem.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "orderId": orderId,
    "createdAt": createdAt,
    "updatedAt": updatedAt,
    "status": status,
    "totalPrice": totalPrice,
    "customerId": customerId,
    "sellerId": sellerId,
    "listOrderItem": List<dynamic>.from(listOrderItem.map((x) => x.toJson())),
  };
}

class ListOrderItem {
  String orderItemId;
  String productId;
  String order;
  int quantity;
  String productName;
  int productPrice;

  ListOrderItem({
    required this.orderItemId,
    required this.productId,
    required this.order,
    required this.quantity,
    required this.productName,
    required this.productPrice,
  });

  factory ListOrderItem.fromJson(Map<String, dynamic> json) => ListOrderItem(
    orderItemId: json["orderItemId"],
    productId: json["productId"],
    order: json["order"]??"",
    quantity: json["quantity"],
    productName: json["productName"],
    productPrice: json["productPrice"],
  );

  Map<String, dynamic> toJson() => {
    "orderItemId": orderItemId,
    "productId": productId,
    "order": order,
    "quantity": quantity,
    "productName": productName,
    "productPrice": productPrice,
  };
}
