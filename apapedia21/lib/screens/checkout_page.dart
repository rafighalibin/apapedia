import 'package:apapedia21/screens/cart.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../model/cart_data.dart';

class CheckoutScreen extends StatefulWidget {
  const CheckoutScreen({Key? key}) : super(key: key);

  @override
  _CheckoutScreenState createState() => _CheckoutScreenState();
}

class _CheckoutScreenState extends State<CheckoutScreen> {
  List<CartItemModel> cartItems = [];

  @override
  void initState() {
    super.initState();
    // Fetch data when the widget is initialized
    fetchData();
  }

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

  Future<void> fetchData() async {
    String? token = await getJwtToken();
    String? userId = await getUserIdFromJwt();
    final response = await http.get(
      Uri.parse('https://apap-141.cs.ui.ac.id/api/cart/get/${userId}'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final List<dynamic> jsonResponse = json.decode(response.body);
      setState(() {
        for (var item in jsonResponse) {
          CartItemModel cartItem = CartItemModel.fromJson(item['product']);
          cartItem.quantity = item['quantity'];
          cartItem.cartID = item['cartItemId'];
          cartItems.add(cartItem);
        }
      });
    } else {
      throw Exception('Failed to load data');
    }
  }

  Future<void> checkout() async {
    String? userId = await getUserIdFromJwt();
    String? jwt = await getJwtToken();
    final response = await http.post(
      Uri.parse('https://apap-141.cs.ui.ac.id/api/cart/checkout/${userId}'),
      headers: {
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': 'Bearer $jwt',
      },
    );
    if (response.statusCode == 200) {
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => const CartScreen()));
    } else {
      throw Exception('Failed to checkout');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        title: Text(
          "Cart",
          style: TextStyle(
            color: Colors.black,
            fontSize: 30,
            fontWeight: FontWeight.bold,
            fontFamily: 'Poppins',
          ),
        ),
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: cartItems.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(cartItems[index].productName),
                  subtitle: Text("Rp. ${cartItems[index].price}"),
                  trailing: Text("Quantity: ${cartItems[index].quantity}"),
                  // Add any other details you want to display
                );
              },
            ),
          ),
          SizedBox(height: 16),
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: FractionallySizedBox(
              widthFactor: 1,
              child: ElevatedButton(
                onPressed: checkout,
                style: TextButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  textStyle: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w500,
                    color: Colors.white,
                  ),
                  shape: StadiumBorder(),
                  backgroundColor:
                      ColorPallete.buttonPrimary, // Change the color
                ),
                child: Text("Checkout"),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
