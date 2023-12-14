import 'dart:convert';

class ProductModel {
  late final String id;
  late final String idSeller;
  late final int price;
  late final String productName;
  late final String productDescription;
  late final String categoryName; // Assuming you want the category name
  late final int stock;
  late final List<int> image; // Assuming your image is represented as a list of integers

  ProductModel({
    required this.id,
    required this.idSeller,
    required this.price,
    required this.productName,
    required this.productDescription,
    required this.categoryName,
    required this.stock,
    required this.image,
  });

  factory ProductModel.fromJson(Map<String, dynamic> json) {
    // Decode Base64-encoded image string
    List<int> decodedImage = base64Decode(json['image'] ?? '');
    return ProductModel(
      id: json['id'],
      idSeller: json['idSeller'],
      price: json['price'],
      productName: json['productName'],
      productDescription: json['productDescription'],
      categoryName: json['category']['name'], // Assuming a nested 'category' field
      stock: json['stock'],
      image: decodedImage,
    );
  }
}
