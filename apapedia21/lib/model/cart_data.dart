class CartItemModel {
  late final String id;
  late final String idSeller;
  late final int price;
  late final String productName;
  late final String productDescription;
  late final Map<String, dynamic> category;
  late final int stock;
  late final String image;
  late final String productNameLower;
  int quantity;

  String cartID;

  CartItemModel({
    required this.id,
    required this.idSeller,
    required this.price,
    required this.productName,
    required this.productDescription,
    required this.category,
    required this.stock,
    required this.image,
    required this.productNameLower,
    this.quantity = 1, // Default quantity is set to 1 TODO
    this.cartID = '',
  });

  factory CartItemModel.fromJson(Map<String, dynamic> json) {
    return CartItemModel(
      id: json['id'],
      idSeller: json['idSeller'],
      price: json['price'],
      productName: json['productName'],
      productDescription: json['productDescription'],
      category: json['category'] ?? {},
      stock: json['stock'],
      image: json['image'] ?? 'IMAGE_PLACEHOLDER',
      productNameLower: json['productNameLower'],
    );
  }
}
