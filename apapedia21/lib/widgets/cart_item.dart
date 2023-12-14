import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import '../model/cart_data.dart';

class CartItemWidget extends StatefulWidget {
  final CartItemModel item;
  final Function(int) onQuantityChanged; // This line should be present
  const CartItemWidget(
      {Key? key, required this.item, required this.onQuantityChanged})
      : super(key: key);

  @override
  State<CartItemWidget> createState() => _CartItemWidgetState();
}

class _CartItemWidgetState extends State<CartItemWidget> {
  Future<String?> getJwtToken() async {
    const FlutterSecureStorage storage = const FlutterSecureStorage();

    try {
      // Attempt to read the JWT token from storage
      String? jwt = await storage.read(key: 'jwt');
      return jwt; // This will return null if the token doesn't exist
    } catch (e) {
      print('Error reading JWT from storage: ${e.toString()}');
      return null;
    }
  }

  Future<String?> getUserIdFromJwt() async {
    const storage = FlutterSecureStorage();
    String? jwt = await storage.read(key: 'jwt');
    if (jwt == null) return null;

    List<String> parts = jwt.split('.');
    if (parts.length != 3) {
      throw Exception("Invalid token");
    }

    String payload = parts[1];
    String normalized = base64Url.normalize(payload);
    String decoded = utf8.decode(base64Url.decode(normalized));

    Map<String, dynamic> payloadMap = json.decode(decoded);
    String? userId =
        payloadMap['userId']; // Assuming 'userId' is the key for user ID

    return userId;
  }

  Future<bool> updateQuantity(String cartItemId, int quantity) async {
    String? userId = await getUserIdFromJwt();

    String? jwt = await getJwtToken();
    if (jwt == null) return false;

    if (cartItemId == '') return false;
    final response = await http.put(
      Uri.parse('http://localhost:10141/api/cart/update-item/${userId}'),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': 'Bearer $jwt',
      },
      body: jsonEncode(<String, dynamic>{
        'cartItemId': cartItemId,
        'quantity': quantity,
      }),
    );
    if (response.statusCode == 200) {
      return true;
    } else {
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            child: Image.asset(
              "assets/images/your_placeholder_image.png", // Replace with the actual image path
              width: 40,
              height: 40,
            ),
          ),
          SizedBox(width: 16),
          Expanded(
            flex: 2,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  widget.item.productName,
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(height: 8),
                Text(
                  "Rp. ${widget.item.price}",
                  style: TextStyle(
                    color: Color(0xffFF324B),
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(height: 8),
                Text(
                  "Quantity: ${widget.item.quantity}",
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.black,
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              InkWell(
                onTap: () {
                  updateQuantity(widget.item.cartID, widget.item.quantity - 1)
                      .then((value) {
                    if (value) {
                      setState(() {
                        widget.item.quantity--;
                      });
                    }
                  });
                },
                child: Image.asset(
                  "assets/images/remove_icon.png",
                  width: 24,
                  height: 24,
                ),
              ),
              SizedBox(width: 8),
              Text(
                "${widget.item.quantity}",
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Colors.black,
                ),
              ),
              SizedBox(width: 8),
              InkWell(
                onTap: (() {
                  updateQuantity(widget.item.cartID, widget.item.quantity + 1)
                      .then((value) {
                    if (value) {
                      setState(() {
                        widget.item.quantity++;
                      });
                    }
                  });
                }),
                child: Image.asset(
                  "assets/images/add_icon.png",
                  width: 24,
                  height: 24,
                ),
              ),
            ],
          ),
          SizedBox(width: 16), // Add an empty SizedBox at the end of the row
        ],
      ),
    );
  }
}
