import 'dart:convert';

import 'package:apapedia21/model/user_model.dart';
import 'package:apapedia21/screens/profile_page.dart';
import 'package:flutter/material.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class EditProfileScreen extends StatefulWidget {
  final UserModel userModel;

  EditProfileScreen({required this.userModel});

  @override
  _EditProfileScreenState createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreen> {
  UserModel get userModel => widget.userModel;
  TextEditingController userIdController = TextEditingController();
  TextEditingController nameController = TextEditingController();
  TextEditingController usernameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController addressController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController confirmPasswordController = TextEditingController();
  TextEditingController saldoController = TextEditingController();

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

  @override
  void initState() {
    super.initState();

    // Initialize controller texts when the state is created
    userIdController.text = userModel.id;
    nameController.text = userModel.name;
    usernameController.text = userModel.username;
    emailController.text = userModel.email;
    addressController.text = userModel.address;
    saldoController.text = userModel.balance.toString();
  }

  @override
  void dispose() {
    // Dispose of the controllers when the widget is disposed
    userIdController.dispose();
    nameController.dispose();
    usernameController.dispose();
    emailController.dispose();
    addressController.dispose();
    passwordController.dispose();
    confirmPasswordController.dispose();
    saldoController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        automaticallyImplyLeading: false,
        backgroundColor: ColorPallete.primary,
        title: Text(
          "Edit Profile",
          style: TextStyle(
              color: ColorPallete.darkText,
              fontSize: 30,
              fontWeight: FontWeight.bold,
              fontFamily: 'Poppins'),
        ),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(height: 16),
              Form(
                  // Replace with your form logic
                  child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('User ID'),
                  TextFormField(
                    controller: userIdController,
                    enabled: false,
                  ),
                  Text('Name'),
                  TextFormField(
                    controller: nameController,
                  ),
                  Text('Username'),
                  TextFormField(
                    controller: usernameController,
                  ),
                  Text('Email'),
                  TextFormField(
                    controller: emailController,
                  ),
                  Text('Address'),
                  TextFormField(
                    controller: addressController,
                  ),
                  Text('Password'),
                  TextFormField(
                    controller: passwordController,
                    obscureText: true,
                  ),
                  Text('Confirm Password'),
                  TextFormField(
                    controller: confirmPasswordController,
                    obscureText: true,
                  ),
                  Text('Saldo'),
                  TextFormField(
                    controller: saldoController,
                    enabled: false,
                  ),
                ],
              )),
              SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                        primary: ColorPallete.buttonPrimary),
                    onPressed: () {
                      sendEditProfileRequest().then((String status) {
                        Text statusText = Text(status);
                        if (status == "Success") {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('Update profile successful!'),
                            ),
                          );
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => const ProfileScreen()));
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: statusText,
                            ),
                          );
                        }
                      });
                    },
                    child: Text('Update Profile',
                        style: TextStyle(color: Colors.white)),
                  ),
                  SizedBox(height: 16),
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
      ),
    );
  }

  Future<String> sendEditProfileRequest() async {
    String? token = await getJwtToken();
    if (token == null) {
      return "Login required";
    }
    if (passwordController.text != confirmPasswordController.text) {
      return "Password and confirm password must be the same";
    }

    try {
      final response = await http.put(
        Uri.parse('https://apap-140.cs.ui.ac.id/api/user/update'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode(<String, dynamic>{
          'id': userIdController.text,
          'name': nameController.text,
          'username': usernameController.text,
          'email': emailController.text,
          'address': addressController.text,
          'password': passwordController.text,
          'confirmPassword': confirmPasswordController.text,
        }),
      );

      if (response.body.isNotEmpty) {
        String condition = response.body.toString();
        return condition;
      } else {
        return "Gagal update profile. Status code: ${response.statusCode}";
      }
    } catch (e) {
      return e.toString();
    }
  }

  Widget buildFormField(
      String label, TextEditingController controller, String initialValue,
      {bool isDisabled = false, bool isPassword = false}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: TextFormField(
        controller: controller,
        initialValue: initialValue,
        readOnly: isDisabled,
        obscureText: isPassword,
        decoration: InputDecoration(
          labelText: label,
          border: OutlineInputBorder(),
        ),
      ),
    );
  }
}
