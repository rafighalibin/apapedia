import 'package:flutter/material.dart';
import '../model/cart_data.dart';

class CartItemWidget extends StatefulWidget {
  final CartItemModel item;
  const CartItemWidget({Key? key, required this.item}) : super(key: key);

  @override
  State<CartItemWidget> createState() => _CartItemWidgetState();
}

class _CartItemWidgetState extends State<CartItemWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            child: Image.asset(
              "assets/images/your_placeholder_image.png", // Replace with the actual image path
              width: 40,
              height: 40,
            ),
          ),
          SizedBox(width: 16),
          Expanded(
            flex: 2,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  widget.item.productName,
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(height: 8),
                Text(
                  "Rp. ${widget.item.price}",
                  style: TextStyle(
                    color: Color(0xffFF324B),
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(height: 8),
                Text(
                  "Quantity: ${widget.item.quantity}",
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.black,
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              InkWell(
                onTap: () {
                  setState(() {
                    if (widget.item.quantity > 0) {
                      widget.item.quantity--;
                    }
                  });
                },
                child: Image.asset(
                  "assets/images/remove_icon.png",
                  width: 24,
                  height: 24,
                ),
              ),
              SizedBox(width: 8),
              Text(
                "${widget.item.quantity}",
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Colors.black,
                ),
              ),
              SizedBox(width: 8),
              InkWell(
                onTap: (() {
                  setState(() {
                    widget.item.quantity++;
                  });
                }),
                child: Image.asset(
                  "assets/images/add_icon.png",
                  width: 24,
                  height: 24,
                ),
              ),
            ],
          ),
          SizedBox(width: 16), // Add an empty SizedBox at the end of the row
        ],
      ),
    );
  }
}
