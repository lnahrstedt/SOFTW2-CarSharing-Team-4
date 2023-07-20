import 'package:universal_io/io.dart';

/// The PlatformHelper class in Dart provides a static method to check if the
/// current platform is mobile (iOS or Android).
class PlatformHelper {
  static bool isMobile() {
    return (Platform.isIOS || Platform.isAndroid);
  }
}
