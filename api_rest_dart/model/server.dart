import 'dart:io';

import 'package:shelf/shelf.dart';
import 'package:shelf/shelf_io.dart';
import 'package:shelf_router/shelf_router.dart';
import 'package:translator/translator.dart';
import 'Translate.dart';


// Configure routes.
final _router =
    Router()
      ..get('/', _rootHandler)
      ..get('/echo/<message>', _echoHandler)
      ..post("/translate", postTranslateHandler);

Response _rootHandler(Request req) {
  return Response.ok('Hello, World!\n');
}

Response _echoHandler(Request request) {
  final message = request.params['message'];
  return Response.ok('$message\n');
}

Future<Response> postTranslateHandler(Request request) async {
  String body = await request.readAsString();
  print("request: "+body);
  

  Translate translate = translateFromJson(body);
  print("language from:"+translate.from);

  Translation translation = await GoogleTranslator().translate(
    translate.text,
    from: translate.from,
    to: translate.to,
  );
  if (translation.text.isNotEmpty) {
    print ('answer: ${translation.text}');
    return Response.ok('${translation.text}\n');
  } else {
    return Response.badRequest();
  }
}

void main(List<String> args) async {
  // Use any available host or container IP (usually `0.0.0.0`).
  final ip = InternetAddress.anyIPv4;

  // Configure a pipeline that logs requests.
  final handler = Pipeline().addMiddleware(logRequests()).addHandler(_router.call);

  // For running in containers, we respect the PORT environment variable.
  final port = int.parse(Platform.environment['PORT'] ?? '8280');
  final server = await serve(handler, ip, port);
  print('Server listening on port ${server.port}');
}
