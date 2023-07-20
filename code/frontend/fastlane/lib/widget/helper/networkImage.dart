import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

/// The `FastlaneNetworkImage` class is a widget that displays a network image using
/// the `CachedNetworkImage` widget and shows a progress indicator while the image
/// is being downloaded.
class FastlaneNetworkImage extends StatelessWidget {
  final String url;

  const FastlaneNetworkImage({
    super.key,
    required this.url,
  });

  @override
  Widget build(BuildContext context) {
    return CachedNetworkImage(
      imageUrl: url,
      progressIndicatorBuilder: (context, url, downloadProgress) {
        return Center(
            child: CircularProgressIndicator(value: downloadProgress.progress));
      },
    );
  }
}
