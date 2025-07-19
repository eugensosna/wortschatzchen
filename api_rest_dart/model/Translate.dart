import 'dart:convert';


String translateToJson(Translate data) => json.encode(data.toJson());

Translate translateFromJson(String str) {
  final jsonData = json.decode(str);
  return Translate.fromJson(jsonData);
}

class Translate {
  final String from;
  final String to;
  final String text;

  Translate(this.from, this.to, this.text);

  factory Translate.fromJson(Map<String, dynamic> json) {
    return Translate(
      json['from'] as String,
      json['to'] as String,
      json['text'] as String,
    );
  }

  Map<String, dynamic> toJson() => {
    'from': from,
    'to': to,
    'text': text,
  };

}