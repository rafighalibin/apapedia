import 'dart:convert';

import 'package:apapedia21/model/user_model.dart';
import 'package:apapedia21/screens/profile_page.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:apapedia21/utils/drawer.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class TopUpScreen extends StatefulWidget {
  @override
  _TopUpScreenState createState() => _TopUpScreenState();
}

class _TopUpScreenState extends State<TopUpScreen> {
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

  TextEditingController amountController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        backgroundColor: ColorPallete.primary,
        title: Text(
          "Top-Up",
          style: TextStyle(
              color: ColorPallete.darkText,
              fontSize: 30,
              fontWeight: FontWeight.bold,
              fontFamily: 'Poppins'),
        ),
      ),
      drawer: MyDrawer(),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextFormField(
              controller: amountController,
              keyboardType: TextInputType.number, // Allow only numeric input
              decoration: InputDecoration(labelText: 'Top-Up Amount'),
            ),
            SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      primary: ColorPallete.buttonPrimary),
                  onPressed: () {
                    String amount = amountController.text;
                    sendTopUpRequest(amount).then((String status) {
                      if (status == "200") {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Top-up successful!'),
                          ),
                        );
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => const ProfileScreen()));
                      } else {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Top-up failed!'),
                          ),
                        );
                      }
                    });
                  },
                  child: Text('Top-Up', style: TextStyle(color: Colors.white)),
                ),
                SizedBox(width: 16),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                      primary: ColorPallete.buttonSecondary),
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text('Back', style: TextStyle(color: Colors.white)),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  Future<String> sendTopUpRequest(String amount) async {
    String? token = await getJwtToken();
    int? balance = await getBalance(token);
    if (token == null) {
      return "Login required";
    }
    var myInt = 0;
    try {
      myInt = int.parse(amount);
    } catch (e) {
      return "Amount must be a number";
    }

    if (amount.isEmpty) {
      return "Amount cannot be empty";
    }

    if (myInt <= 0) {
      return "Amount must be greater than 0";
    }
    try {
      final response = await http.put(
        Uri.parse('https://apap-140.cs.ui.ac.id/api/user/update-balance'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode(<String, int>{
          'balance': myInt + balance,
        }),
      );

      if (response.statusCode == 200) {
        return "200";
      } else {
        return "Gagal top-up. Status code: ${response.statusCode}";
      }
    } catch (e) {
      return e.toString();
    }
  }

  @override
  void dispose() {
    // Dispose of the TextEditingController when the widget is disposed
    amountController.dispose();
    super.dispose();
  }

  Future<int> getBalance(String? token) async {
    try {
      final response = await http.get(
        Uri.parse('https://apap-140.cs.ui.ac.id/api/user/get'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        if (response.body.isNotEmpty) {
          final Map<String, dynamic> data = json.decode(response.body);
          UserModel user = UserModel.fromJson(data);
          return user.balance;
        } else {
          print('Empty response body');
          return 0;
        }
      } else {
        print('Failed to load profile. Status code: ${response.statusCode}');
        return 0;
      }
    } catch (e) {
      print('Exception occurred: $e');
      return 0;
    }
  }
}
