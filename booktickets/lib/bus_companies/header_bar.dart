import '../bus_companies/back_arrow.dart';
import '../bus_companies/circle_avatar_border.dart';
import 'package:flutter/material.dart';

class HeaderBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      height: 250,
      color: Color(0xFF3834AF),
      child: Container(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            BackArrow(),
            AvatarWithBorder(),
          ],
        ),
      )
    );
  }
}
