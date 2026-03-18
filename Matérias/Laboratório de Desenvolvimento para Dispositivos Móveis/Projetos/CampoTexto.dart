import 'package:flutter/material.dart';

class CampoTexto extends StatelessWidget {
  const CampoTexto({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        TextField(
          keyboardType: TextInputType.emailAddress,
          decoration: const InputDecoration(labelText: "E-mail"),
        ),
        TextField(
          keyboardType: TextInputType.text,
          decoration: const InputDecoration(labelText: "Senha"),
          obscureText: true,
          maxLength: 20,
        ),
      ],
    );
  }
}