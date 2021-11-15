// To parse this JSON data, do
//
//     final welcome = welcomeFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

class Welcome {
    Welcome({
        required this.transposrts,
    });

    List<Transposrt> transposrts;

    factory Welcome.fromRawJson(String str) => Welcome.fromJson(json.decode(str));

    String toRawJson() => json.encode(toJson());

    factory Welcome.fromJson(Map<String, dynamic> json) => Welcome(
        transposrts: List<Transposrt>.from(json["transposrts"].map((x) => Transposrt.fromJson(x))),
    );

    Map<String, dynamic> toJson() => {
        "transposrts": List<dynamic>.from(transposrts.map((x) => x.toJson())),
    };
}

class Transposrt {
    Transposrt({
        required this.type,
        required this.id,
        required this.name,
        required this.number,
        required this.description,
        required this.passengerCapacity,
        required this.startTime,
        required this.departureTime,
        required this.startLocation,
        required this.endLocation,
        required this.createdAt,
        required this.updatedAt,
        required this.v,
    });

    String type;
    String id;
    String name;
    int number;
    String description;
    int passengerCapacity;
    DateTime startTime;
    DateTime departureTime;
    String startLocation;
    String endLocation;
    DateTime createdAt;
    DateTime updatedAt;
    int v;

    factory Transposrt.fromRawJson(String str) => Transposrt.fromJson(json.decode(str));

    String toRawJson() => json.encode(toJson());

    factory Transposrt.fromJson(Map<String, dynamic> json) => Transposrt(
        type: json["type"],
        id: json["_id"],
        name: json["name"],
        number: json["number"],
        description: json["description"],
        passengerCapacity: json["passengerCapacity"],
        startTime: DateTime.parse(json["startTime"]),
        departureTime: DateTime.parse(json["departureTime"]),
        startLocation: json["startLocation"],
        endLocation: json["endLocation"],
        createdAt: DateTime.parse(json["createdAt"]),
        updatedAt: DateTime.parse(json["updatedAt"]),
        v: json["__v"],
    );

    Map<String, dynamic> toJson() => {
        "type": type,
        "_id": id,
        "name": name,
        "number": number,
        "description": description,
        "passengerCapacity": passengerCapacity,
        "startTime": startTime.toIso8601String(),
        "departureTime": departureTime.toIso8601String(),
        "startLocation": startLocation,
        "endLocation": endLocation,
        "createdAt": createdAt.toIso8601String(),
        "updatedAt": updatedAt.toIso8601String(),
        "__v": v,
    };
}
