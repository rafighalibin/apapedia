class Product {
  Product({
    required this.photo,
    required this.productName,
    required this.price,
    // required this.penerbit,
  });

  String photo;
  String productName;
  int price;

  factory Product.fromJson(Map<String, dynamic> json) => Product(
    photo: json["photo"],
    productName: json["productName"],
    price: json["price"],
  );
}
