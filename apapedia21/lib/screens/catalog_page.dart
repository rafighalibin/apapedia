import 'dart:convert';
import 'dart:typed_data';

import 'package:apapedia21/model/catalogue.dart';
import 'package:apapedia21/product/product.dart';
import 'package:apapedia21/screens/cart.dart';
import 'package:apapedia21/screens/product_detail_page.dart';
import 'package:apapedia21/screens/profile_page.dart';
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
  String _selectedSortOption = 'Nama Asc';

  void _goToCartPage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => CartScreen(),
      ),
    ); // Replace '/cart' with your actual cart page route
  }

  void _goToProfilePage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ProfileScreen(),
      ),
    ); // Replace '/cart' with your actual cart page route
  }

  Future<List<ProductModel>?> fetchProduct(String? token) async {
    print(token);
    if (token == null) {
      print('JWT token is null');
      return null;
    }

    try {
      final response = await http.get(
        Uri.parse('http://localhost:10142/api/catalogue/viewall'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        if (response.body.isNotEmpty) {
          List<dynamic> productJson = json.decode(response.body);

          List<ProductModel> listproduct = [];
          for (var d in productJson) {
            if (d != null) {
              listproduct.add(ProductModel.fromJson(d));
            }
          }
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

  void _sortProducts(List<ProductModel> products) {
    switch (_selectedSortOption) {
      case 'Nama Asc':
        products.sort((a, b) => a.productName.compareTo(b.productName));
        break;
      case 'Nama Desc':
        products.sort((a, b) => b.productName.compareTo(a.productName));
        break;
      case 'Harga Asc':
        products.sort((a, b) => a.price.compareTo(b.price));
        break;
      case 'Harga Desc':
        products.sort((a, b) => b.price.compareTo(a.price));
        break;
    }
  }

  Future<String?> getJwtToken() async {
    const FlutterSecureStorage storage = const FlutterSecureStorage();

    try {
      String? jwt = await storage.read(key: 'jwt');
      return jwt;
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
    String? username = payloadMap['sub'];

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
              return Text(snapshot.data ?? 'No Username',
                  style: TextStyle(color: Colors.white));
            } else {
              return const Text('No Username',
                  style: TextStyle(color: Colors.white));
            }
          },
        ),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 10),
            child: IconButton(
              onPressed:  
                _goToProfilePage,
              
              icon: const Icon(
                Icons.account_circle_outlined,
                size: 40,
                color: Colors.white,
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 10),
            child: IconButton(
              onPressed: _goToCartPage,
              icon: const Icon(
                Icons.shopping_cart,
                size: 40,
                color: Colors.white,
              ),
            ),
          ),
        ],
      ),
      drawer: const MyDrawer(),
      body: FutureBuilder<List<ProductModel>?>(
        future: getJwtToken().then((token) => fetchProduct(token)),
        builder: (BuildContext context,
            AsyncSnapshot<List<ProductModel>?> snapshot) {
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
            final data = snapshot.data as List<ProductModel>;
            _sortProducts(data);

            return Column(
              children: [
                DropdownButton<String>(
                  value: _selectedSortOption,
                  onChanged: (String? newValue) {
                    if (newValue != null) {
                      setState(() {
                        _selectedSortOption = newValue;
                        _sortProducts(data);
                      });
                    }
                  },
                  items: [
                    'Nama Asc',
                    'Nama Desc',
                    'Harga Asc',
                    'Harga Desc',
                  ].map((String option) {
                    return DropdownMenuItem<String>(
                      value: option,
                      child: Text(option),
                    );
                  }).toList(),
                ),
                Expanded(
                  child: ListView.builder(
                    shrinkWrap: true,
                    itemCount: data.length,
                    itemBuilder: (_, index) {
                      ProductModel product = data[index];
                      return GestureDetector(
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) =>
                                  ProductDetailPage(product: product),
                            ),
                          );
                        },
                        child: Container(
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
                              Image.memory(
                                Uint8List.fromList(product.image),
                                height: 150, // Adjust the height as needed
                                width: 150, // Take the full width
                              ),
                              Text('Nama Produk: ${product.productName}',
                                  style: const TextStyle(fontSize: 16)),
                              Text('Harga: ${product.price.toString()}',
                                  style: const TextStyle(fontSize: 16)),
                              Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  ElevatedButton(
                                    onPressed: () {
                                      _goToCartPage();
                                    },
                                    child: Text('Add to Cart'),
                                  ),
                                  ElevatedButton(
                                    onPressed: () {
                                      _goToCartPage();
                                    },
                                    child: Text('Order Now'),
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            );
          }
        },
      ),
    );
  }
}
