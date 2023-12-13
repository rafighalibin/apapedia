import 'package:apapedia21/screens/checkout_page.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:apapedia21/utils/drawer.dart';
import 'package:apapedia21/utils/reusable_widget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../model/cart_data.dart';
import '../widgets/cart_item.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({Key? key}) : super(key: key);

  @override
  _CartScreenState createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
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
    String? userId = await getUserIdFromJwt();
    String? token = await getJwtToken();

    final response = await http.get(
      Uri.parse('http://localhost:10141/api/cart/get/${userId}'),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        backgroundColor: ColorPallete.primary,
        title: Text(
          "Cart",
          style: TextStyle(
              color: ColorPallete.darkText,
              fontSize: 30,
              fontWeight: FontWeight.bold,
              fontFamily: 'Poppins'),
        ),
        actions: [
          Padding(
              padding: const EdgeInsets.only(right: 10),
              child: IconButton(
                onPressed: () {
                  popUpExit(context, "Log out of your account?");
                },
                icon: Icon(
                  Icons.logout,
                  size: 40,
                  color: ColorPallete.darkText,
                ),
              ))
        ],
      ),
      drawer: const MyDrawer(),
      body: Stack(
        children: [
          Column(
            children: [
              Expanded(
                child: Container(
                  margin: const EdgeInsets.symmetric(vertical: 16),
                  child: ListView.separated(
                    separatorBuilder: (context, index) {
                      return Divider();
                    },
                    itemCount: cartItems.length,
                    itemBuilder: (context, index) {
                      return CartItemWidget(
                        item: cartItems[index],
                        onQuantityChanged: (newQuantity) {
                          // Update the quantity in the cartItems list
                          setState(() {
                            cartItems[index].quantity = newQuantity;
                          });
                        },
                      );
                    },
                  ),
                ),
              ),
              SizedBox(
                  height:
                      16), // Add an empty SizedBox between the ListView and the ElevatedButton
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: FractionallySizedBox(
                  widthFactor: 1,
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => const CheckoutScreen()));
                    },
                    style: TextButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      textStyle: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                        color: Colors.white,
                      ),
                      shape: StadiumBorder(),
                      backgroundColor: ColorPallete.buttonPrimary,
                    ),
                    child: Text("Checkout"),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
