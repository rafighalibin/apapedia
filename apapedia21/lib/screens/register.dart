import 'dart:convert';

import 'package:apapedia21/screens/login.dart';
import 'package:apapedia21/utils/color_pallette.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
// Import any other packages or files you need, such as http for API calls

class RegisterFormScreen extends StatefulWidget {
  static const routeName = '/register';
  const RegisterFormScreen({super.key});

  @override
  State<RegisterFormScreen> createState() => _RegisterFormScreenState();
}

class _RegisterFormScreenState extends State<RegisterFormScreen> {
  final _formKey = GlobalKey<FormState>();

  // Controllers for the text fields
  final usernameController = TextEditingController();
  final nameController = TextEditingController();
  final emailController = TextEditingController();
  final passwordController = TextEditingController();
  final addressController = TextEditingController();

  @override
  void dispose() {
    // Dispose the controllers when the widget is disposed
    usernameController.dispose();
    nameController.dispose();
    passwordController.dispose();
    addressController.dispose();
    emailController.dispose();
    super.dispose();
  }

  Future<bool> register(
      String username, String name, String password, String address, String email) async {
    final response = await http.post(
      Uri.parse('http://localhost:10140/api/user/add'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'username': username,
        'name': name,
        'password': password,
        'address': address,
        'email': email,
        'role': 'Customer',
      }),
    );

    if (response.statusCode == 200) {
      // Navigate to the next screen
      return true;
    } else {
      // Handle login failure
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[300],
      body: Center(
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              const Text(
                "APAPEDIA 21",
                style: TextStyle(fontSize: 28, fontWeight: FontWeight.w700),
              ),
              const SizedBox(height: 10),
              const Icon(Icons.android, size: 100),
              const SizedBox(height: 10),
              Container(
                padding:
                    const EdgeInsets.symmetric(horizontal: 8, vertical: 20),
                margin: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(8),
                  color: Colors.white,
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const Text(
                      "Register to Explore!",
                      style: TextStyle(fontSize: 24),
                    ),
                    Form(
                      key: _formKey,
                      child: SingleChildScrollView(
                        child: Column(
                          children: [
                            const SizedBox(height: 20),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.8,
                              child: TextFormField(
                                controller: usernameController,
                                decoration: const InputDecoration(
                                  border: OutlineInputBorder(),
                                  labelText: "Username",
                                ),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your username';
                                  }
                                  return null;
                                },
                              ),
                            ),
                            const SizedBox(height: 20),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.8,
                              child: TextFormField(
                                controller: passwordController,
                                obscureText: true,
                                decoration: const InputDecoration(
                                  border: OutlineInputBorder(),
                                  labelText: 'Password',
                                ),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter a password';
                                  }
                                  return null;
                                },
                              ),
                            ),
                            const SizedBox(height: 20),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.8,
                              child: TextFormField(
                                controller: nameController,
                                decoration: const InputDecoration(
                                  border: OutlineInputBorder(),
                                  labelText: "Name",
                                ),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your name';
                                  }
                                  return null;
                                },
                              ),
                            ),
                            const SizedBox(height: 20),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.8,
                              child: TextFormField(
                                controller: emailController,
                                decoration: const InputDecoration(
                                  border: OutlineInputBorder(),
                                  labelText: "Email",
                                ),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your Email';
                                  }
                                  return null;
                                },
                              ),
                            ),
                            const SizedBox(height: 20),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.8,
                              child: TextFormField(
                                controller: addressController,
                                decoration: const InputDecoration(
                                  border: OutlineInputBorder(),
                                  labelText: 'Address',
                                ),
                                validator: (value) {
                                  if (value == null || value.isEmpty) {
                                    return 'Please enter your address';
                                  }
                                  return null;
                                },
                              ),
                            ),
                            const SizedBox(height: 20),
                            ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                fixedSize: Size(
                                    MediaQuery.of(context).size.width * 0.8,
                                    50),
                                backgroundColor: ColorPallete
                                    .primary, // Make sure this is defined in your color palette
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(10),
                                ),
                              ),
                              onPressed: () async {
                                if (_formKey.currentState!.validate()) {
                                  final success = await register(
                                      usernameController.text,
                                      nameController.text,
                                      passwordController.text,
                                      addressController.text,
                                      emailController.text);
                                  if (success) {
                                    // Navigate to the next screen if login is successful
                                    Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                            builder: (context) =>
                                                const LoginFormScreen()));
                                  } else {
                                    // Show error message if login failed
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      SnackBar(
                                          content: Text('Register failed')),
                                    );
                                  }
                                }
                              },
                              child: const Text(
                                'Register',
                                style: TextStyle(
                                    fontSize: 20, color: Colors.black),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
