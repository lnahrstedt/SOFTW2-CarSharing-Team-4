import 'package:fastlane/constants/colors.dart';
import 'package:flutter/material.dart';

Text buildCardTitle(String title) {
  return Text(
    title,
    style: TextStyle(fontWeight: FontWeight.w600, fontSize: 24),
  );
}

const RESERVATION_TEXT_STYLE = TextStyle(
  color: INBETWEEN_GRAY,
  fontSize: 12,
  fontWeight: FontWeight.w400,
);
