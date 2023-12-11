import 'package:apapedia21/screens/catalog_page.dart';
import 'package:apapedia21/screens/login.dart';
import 'package:apapedia21/screens/topup.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'APAPEDIA 21',
      routes: {
        '/': (context) => LoginFormScreen(),
        '/catalog-page': (context) => const CatalogScreen(),
        '/topup-page': (context) => TopUpScreen(),
      },
    );
  }
}
