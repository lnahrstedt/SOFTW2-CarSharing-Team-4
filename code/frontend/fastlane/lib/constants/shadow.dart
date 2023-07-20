import 'package:flutter/material.dart';

import 'colors.dart';

const NO_SHADOW_CAR_ENTRY = BoxShadow(
  offset: Offset(0, 0),
  spreadRadius: 0,
  blurRadius: 2,
  color: GRAY_BLUEISH,
);

const CARD_SHADOW_NO_HOVER = BoxShadow(
  offset: Offset(0, 0),
  spreadRadius: 0,
  blurRadius: 10,
  color: DROPSHADOW_GRAY,
);

const CARD_SHADOW_HOVER = BoxShadow(
  offset: Offset(0, 0),
  spreadRadius: 0,
  blurRadius: 10,
  color: INBETWEEN_GRAY,
);
