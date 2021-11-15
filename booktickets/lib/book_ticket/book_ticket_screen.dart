import '../book_ticket/ticket_card_grid.dart';
import 'package:flutter/material.dart';


class BookTicketScreen extends StatelessWidget {

  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0.0,
        leading: Icon(Icons.more_vert, color: Color(0xff2521A7)),
        title: Text(
          "Book any ticket online using your UIDAI Number",
          style: TextStyle(
            color: Color(0xFF3834AF),
            fontFamily: "Roboto",
            fontSize: 17
          ),
        ),
        centerTitle: true,
        actions: <Widget>[
          Container(
            margin: EdgeInsets.only(
              right: 10.0,
            ),
            child: CircleAvatar(
              radius: 30,
              backgroundImage: AssetImage("assets/user-image.png"),
            ),
          )
        ],
      ),
      body: TicketsGrid(),
    );
  }
}
