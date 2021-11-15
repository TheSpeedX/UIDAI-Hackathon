import 'package:flutter/material.dart';
import '../book_ticket/ticket_card.dart';

class TicketsGrid extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
        child: GridView.count(
          crossAxisCount: 2,
          children: <Widget>[
            TicketCard(Icons.airplanemode_active, "Flight", "509 flights Available", Color(0xff01C6F8), Color(0xff01EAE2), 1),
            TicketCard(Icons.directions_bus, "Bus", "800+ Bus Available", Color(0xffDCBE22), Color(0xffF9F5A9), 2),
            TicketCard(Icons.train, "Train", "60 train Available", Color(0xff08C79E), Color(0xff80F6BC), 3),
            TicketCard(Icons.sports_cricket, "IPL Match Booking", "Limited Seats Availability", Color(0xffFFA4AA), Color(0xffFB6BB1), 4),
          ],
        )
    );
  }
}
