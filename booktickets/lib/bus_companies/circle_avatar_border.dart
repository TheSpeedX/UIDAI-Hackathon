import 'package:flutter/material.dart';

class AvatarWithBorder extends StatelessWidget {
  

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 50,
      height: 50,
      padding: EdgeInsets.all(0),
      margin: EdgeInsets.only(
        top: 35,
        right: 10
      ),
      decoration: BoxDecoration(
        border: Border.all(
          color: Colors.white,
          style: BorderStyle.solid,
          width: 1.0
        ),
        shape: BoxShape.circle,
        image: DecorationImage(
            image: AssetImage("assets/user-image.png"),
            fit: BoxFit.cover
        )
      ),
    );
  }
}
