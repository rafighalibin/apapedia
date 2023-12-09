import 'package:apapedia21/screens/profile_page.dart';
import 'package:flutter/material.dart';
import 'package:apapedia21/screens/catalog_page.dart';

class MyDrawer extends StatelessWidget {
  const MyDrawer({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) => Drawer(
        child: SingleChildScrollView(
          child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: <Widget>[
                buildMenuItems(context),
              ]),
        ),
      );

  Widget buildMenuItems(BuildContext context) => Container(
      padding: const EdgeInsets.all(24),
      child: Wrap(
        runSpacing: 12,
        children: [
          ListTile(
            title: const Text('Profile'),
            onTap: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(
                  builder: (context) => const ProfileScreen()));
            },
          ),
          ListTile(
            title: const Text('Catalogue'),
            onTap: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(
                  builder: (context) => const CatalogScreen()));
            },
          ),
        ],
      ));
}
