import 'dart:convert';
import 'package:apapedia21/product/product.dart';
import 'package:apapedia21/utils/drawer.dart';
import 'package:apapedia21/utils/reusable_widget.dart';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class CatalogScreen extends StatefulWidget {
  static const routeName = '/appointment/list';
  const CatalogScreen({Key? key}) : super(key: key);

  @override
  State<CatalogScreen> createState() => _CatalogScreenState();
}

class _CatalogScreenState extends State<CatalogScreen> {
  Future<List<Product>?> fetchProduct(String? token) async {
    print(token);
    if (token == null) {
      print('JWT token is null');
      return null;
    }

    try {
      final response = await http.get(
        Uri.parse(''),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
      );
      // print("RESPONSE BODY : " + response.body);

      if (response.statusCode == 200) {
        if (response.body.isNotEmpty) {
          List<dynamic> productJson = json.decode(response.body);

          List<Product> listproduct = [];
          for (var d in productJson) {
            if (d != null) {
              // print(d);
              listproduct.add(Product.fromJson(d));
            }
          }
          print('HAHAHAHAHAHA');
          print(listproduct);
          return listproduct;
        } else {
          print('Empty response body');
          return null;
        }
      } else {
        print('Failed to load product. Status code: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('Exception occurred: $e');
      return null;
    }
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

  Future<String?> getUsernameFromJwt() async {
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
    String? username = payloadMap['sub']; // 'sub' is used for the username

    return username;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        backgroundColor: const Color(0XFF5d675b),
        title: FutureBuilder<String?>(
          future: getUsernameFromJwt(),
          builder: (BuildContext context, AsyncSnapshot<String?> snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Text("Loading...");
            } else if (snapshot.hasData) {
              // Display the username if available
              return Text(snapshot.data ?? 'No Username',
                  style: TextStyle(color: Colors.white));
            } else {
              // Handle the case where there is no username or an error
              return const Text('No Username',
                  style: TextStyle(color: Colors.white));
            }
          },
        ),
        actions: [
          Padding(
              padding: const EdgeInsets.only(right: 10),
              child: IconButton(
                onPressed: () {
                  popUpExit(context, "Log out of your account?");
                },
                icon: const Icon(
                  Icons.account_circle_outlined,
                  size: 40,
                  color: Colors.white,
                ),
              ))
        ],
      ),
      drawer: const MyDrawer(),
      body: FutureBuilder<List<Product>?>(
        future: getJwtToken().then((token) => fetchProduct(
            token)), // a previously-obtained Future<String> or null
        builder:
            (BuildContext context, AsyncSnapshot<List<Product>?> snapshot) {
          if (snapshot.data == null) {
            if (snapshot.connectionState == ConnectionState.done) {
              return const Padding(
                padding: EdgeInsets.all(12),
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "product tidak ditemukan",
                        style: TextStyle(fontSize: 16),
                      ),
                      SizedBox(height: 8),
                    ],
                  ),
                ),
              );
            }
            return const Center(child: CircularProgressIndicator());
          }

          if (!snapshot.hasData) {
            return const Column(
              children: [
                Text(
                  "product tidak ditemukan",
                  style: TextStyle(fontSize: 16),
                ),
                SizedBox(height: 8),
              ],
            );
          } else {
            final data = snapshot.data as List<Product>;

            return Padding(
                padding: const EdgeInsets.all(20),
                child: ListView.builder(
                    shrinkWrap: true,
                    itemCount: snapshot.data!.length,
                    itemBuilder: (_, index) {
                      Product product = data[index];
                      return Container(
                        margin: const EdgeInsets.symmetric(
                            horizontal: 16, vertical: 12),
                        padding: const EdgeInsets.all(20.0),
                        decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(15.0),
                            boxShadow: const [
                              BoxShadow(color: Colors.black, blurRadius: 2.0)
                            ]),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(product.photo,
                                style: const TextStyle(
                                    fontSize: 18, fontWeight: FontWeight.bold)),
                            Text('Tahun Terbit: ${product.productName}',
                                style: const TextStyle(fontSize: 16)),
                            Text('Harga: ${product.price.toString()}',
                                style: const TextStyle(fontSize: 16)),
                          ],
                        ),
                      );
                    }));
          }
        },
      ),
    );
  }
}