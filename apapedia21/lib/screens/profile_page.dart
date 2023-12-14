import 'dart:async';
import 'dart:convert';
import 'package:apapedia21/model/user_model.dart';
import 'package:apapedia21/screens/edit_profile_page.dart';
import 'package:apapedia21/screens/login.dart';
import 'package:apapedia21/screens/topup.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:apapedia21/utils/drawer.dart';
import 'package:apapedia21/utils/reusable_widget.dart';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class ProfileScreen extends StatefulWidget {
  static const routeName = '/profile';
  const ProfileScreen({Key? key}) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  UserModel? userModel;
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

  Future<UserModel?> fetchUser(String? token) async {
    if (token == null) {
      print('JWT token is null');
      return null;
    }

    try {
      final response = await http.get(
        Uri.parse('http://localhost:10140/api/user/get'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        if (response.body.isNotEmpty) {
          final Map<String, dynamic> data = json.decode(response.body);
          userModel = UserModel.fromJson(data);
          return userModel;
        } else {
          print('Empty response body');
          return null;
        }
      } else {
        print('Failed to load profile. Status code: ${response.statusCode}');
        return null;
      }
    } catch (e) {
      print('Exception occurred: $e');
      return null;
    }
  }

  Widget buildDetailRow(String label, String value) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(label + ":",
            style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
        SizedBox(width: 8),
        Expanded(
          child: Text(
            value,
            style: TextStyle(fontSize: 16),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        backgroundColor: ColorPallete.primary,
        title: Text(
          "Profile",
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
      body: FutureBuilder<UserModel?>(
        future: getJwtToken().then((token) => fetchUser(token)),
        builder: (BuildContext context, AsyncSnapshot<UserModel?> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data == null) {
            return const Center(child: Text('User data not found'));
          } else {
            UserModel user = snapshot.data!;

            return Center(
                child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Container(
                  width: double.infinity,
                  padding: EdgeInsets.all(16.0),
                  child: Column(
                    children: [
                      buildDetailRow("User ID", user.id),
                      buildDetailRow("Name", user.name),
                      buildDetailRow("Username", user.username),
                      buildDetailRow("Email", user.email),
                      buildDetailRow("Address", user.address),
                      buildDetailRow("Saldo", user.balance.toString()),
                    ],
                  ),
                ),
                SizedBox(height: 16),
                buildButtons(),
              ],
            ));
          }
        },
      ),
    );
  }

  Widget buildButtons() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        ElevatedButton(
          onPressed: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => TopUpScreen()));
          },
          style: ElevatedButton.styleFrom(
              primary: ColorPallete.buttonPrimary,
              textStyle: TextStyle(color: Colors.white)),
          child: Text('Top Up', style: TextStyle(color: Colors.white)),
        ),
        SizedBox(width: 16),
        ElevatedButton(
          onPressed: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => EditProfileScreen(
                          userModel: userModel!,
                        )));
          },
          style: ElevatedButton.styleFrom(
              primary: ColorPallete.buttonSecondary,
              textStyle: TextStyle(color: Colors.white)),
          child: Text('Edit Profile', style: TextStyle(color: Colors.white)),
        ),
        SizedBox(width: 16),
        ElevatedButton(
          onPressed: () async {
            const storage = FlutterSecureStorage();
            await storage.write(key: 'jwt', value: null);
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => LoginFormScreen()));
          },
          style: ElevatedButton.styleFrom(
              primary: ColorPallete.buttonWarning,
              textStyle: TextStyle(color: Colors.white)),
          child: Text('Sign Out', style: TextStyle(color: Colors.white)),
        ),
      ],
    );
  }
}
