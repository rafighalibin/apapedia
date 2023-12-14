import 'package:apapedia21/utils/color_pallette.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

void popUpExit(BuildContext context, String title) async {
  final storage = FlutterSecureStorage();
  showDialog(
    context: context,
    builder: (context) {
      return AlertDialog(
        content: Column(mainAxisSize: MainAxisSize.min, children: [
          Text(
            title,
            textAlign: TextAlign.center,
          )
        ]),
        actions: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    fixedSize: const Size(100, 25),
                    backgroundColor: Colors.white,
                    side: BorderSide(
                      color: ColorPallete.primary500,
                      width: 2,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: const Text('Cancel'),
                ),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    fixedSize: const Size(100, 25),
                    backgroundColor: ColorPallete.primary,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  onPressed: () async {
                    await storage.delete(key: 'jwt');
                    {
                      Navigator.pushNamed(context, "/");
                    };
                  },
                  child: const Text('Log Out'),
                ),
              ],
            ),
          ),
        ],
      );
    },
  );
}
