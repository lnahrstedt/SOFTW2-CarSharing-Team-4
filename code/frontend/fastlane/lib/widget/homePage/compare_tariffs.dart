import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:flutter/material.dart';

/// The `CompareTariffs` class is a stateful widget in Dart that allows for
/// comparing tariffs.
class CompareTariffs extends StatefulWidget {
  const CompareTariffs({super.key});

  @override
  State<StatefulWidget> createState() => _CompareTariffsState();
}

/// The function creates a table cell with a centered and bolded text.
///
/// Args:
///   text (String): The "text" parameter is a string that represents the title text
/// that will be displayed in the title cell.
///
/// Returns:
///   a TableCell widget.
TableCell createTitleCell(String text) {
  return TableCell(
      child: Padding(
    padding: const EdgeInsets.all(12.0),
    child: Center(
      child: Text(text,
          style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
    ),
  ));
}

/// The function creates a table cell with a given text.
///
/// Args:
///   text (String): The "text" parameter is a string that represents the text
/// content that will be displayed inside the TableCell.
///
/// Returns:
///   a TableCell widget.
TableCell createCell(String text) {
  return TableCell(
    child: Padding(
      padding: const EdgeInsets.all(12.0),
      child: Text(text),
    ),
  );
}

/// The function creates a centered table cell with the given text.
///
/// Args:
///   text (String): The text parameter is a string that represents the content of
/// the cell.
///
/// Returns:
///   a TableCell widget with a child widget that consists of a Padding widget with
/// a child widget that consists of a Center widget with a Text widget.
TableCell createCellCenter(String text) {
  return TableCell(
    child: Padding(
      padding: const EdgeInsets.all(12.0),
      child: Center(child: Text(text)),
    ),
  );
}

/// The function creates a small title cell with the given text.
///
/// Args:
///   text (String): The "text" parameter is a string that represents the text
/// content of the small title cell.
///
/// Returns:
///   a TableCell widget.
TableCell createSmallTitleCell(String text) {
  return TableCell(
      child: Padding(
    padding: const EdgeInsets.all(12.0),
    child: Text(text,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
  ));
}

/// The `createTable` function returns a Dart `Table` widget with predefined column
/// widths and cell values for creating a pricing table.
///
/// Returns:
///   The function `createTable()` returns a `Table` widget.
Table createTable() {
  return Table(
    defaultVerticalAlignment: TableCellVerticalAlignment.middle,
    border:
        TableBorder.all(borderRadius: BorderRadius.circular(5.0), width: 0.5),
    columnWidths: const <int, TableColumnWidth>{
      0: FixedColumnWidth(250),
      1: FixedColumnWidth(200),
      2: FixedColumnWidth(200),
      3: FixedColumnWidth(200)
    },
    children: [
      TableRow(
        children: [
          createTitleCell(''),
          createTitleCell('Junior'),
          createTitleCell('Comfort'),
          createTitleCell('Premium'),
        ],
      ),
      TableRow(decoration: const BoxDecoration(color: GRAY_BLUEISH), children: [
        createCell('Anmeldegebühr'),
        createCellCenter('25€'),
        createCellCenter('25€'),
        createCellCenter('20€'),
      ]),
      TableRow(children: [
        createCell('Monatliche Gebühr'),
        createCellCenter('0€'),
        createCellCenter('10€'),
        createCellCenter('20€'),
      ]),
      TableRow(decoration: const BoxDecoration(color: GRAY_BLUEISH), children: [
        createCell('Anmeldegebühr je weitere Person'),
        createCellCenter('--'),
        createCellCenter('30€'),
        createCellCenter('20€'),
      ]),
      TableRow(children: [
        createCell('Monatliche Gebühr je weitere Person'),
        createCellCenter('--'),
        createCellCenter('5€'),
        createCellCenter('3€'),
      ]),
      TableRow(decoration: const BoxDecoration(color: GRAY_BLUEISH), children: [
        createSmallTitleCell('Zeitpreis'),
        createCellCenter(''),
        createCellCenter(''),
        createCellCenter(''),
      ]),
      TableRow(children: [
        createCell('Stunde'),
        createCellCenter('2€'),
        createCellCenter('1,50€'),
        createCellCenter('1€'),
      ]),
      TableRow(decoration: const BoxDecoration(color: GRAY_BLUEISH), children: [
        createSmallTitleCell('Preis pro Kilometer'),
        createCellCenter(''),
        createCellCenter(''),
        createCellCenter(''),
      ]),
      TableRow(children: [
        createCell('bis 100 km'),
        createCellCenter('0,30€'),
        createCellCenter('0,27€'),
        createCellCenter('0,25€'),
      ]),
      TableRow(decoration: const BoxDecoration(color: GRAY_BLUEISH), children: [
        createCell('ab 100 km'),
        createCellCenter('0,22€'),
        createCellCenter('0,20€'),
        createCellCenter('0,18€'),
      ])
    ],
  );
}

/// The `_CompareTariffsState` class is a stateful widget that displays a tariff
/// comparison table in a column layout.
class _CompareTariffsState extends State<CompareTariffs> {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Text(
          'Tarifvergleich',
          style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
        ),
        Padding(
          padding: const EdgeInsets.only(top: 8.0),
          child: FastlaneScrollable(
              direction: Axis.horizontal, child: createTable()),
        )
      ],
    );
  }
}