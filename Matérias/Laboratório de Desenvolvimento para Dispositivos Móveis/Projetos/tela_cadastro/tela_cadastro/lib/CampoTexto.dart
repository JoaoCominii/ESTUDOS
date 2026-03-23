import 'package:flutter/material.dart';

class CampoTexto extends StatefulWidget {
  const CampoTexto({super.key});

  @override
  State<CampoTexto> createState() => _CampoTextoState();
}

class _CampoTextoState extends State<CampoTexto> {
  bool _obscurePassword = true;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const TextField(
          decoration: InputDecoration(
            labelText: 'Nome',
            border: OutlineInputBorder(),
          ),
          keyboardType: TextInputType.name,
          maxLength: 50,
        ),
        const SizedBox(height: 16),
        const TextField(
          decoration: InputDecoration(
            labelText: 'Data de nascimento',
            border: OutlineInputBorder(),
            hintText: 'DD/MM/AAAA',
          ),
          keyboardType: TextInputType.datetime,
        ),
        const SizedBox(height: 16),
        const TextField(
          decoration: InputDecoration(
            labelText: 'Telefone',
            border: OutlineInputBorder(),
          ),
          keyboardType: TextInputType.phone,
          maxLength: 15,
        ),
        const SizedBox(height: 16),
        const TextField(
          decoration: InputDecoration(
            labelText: 'E-mail',
            border: OutlineInputBorder(),
          ),
          keyboardType: TextInputType.emailAddress,
        ),
        const SizedBox(height: 16),
        TextField(
          decoration: InputDecoration(
            labelText: 'Senha',
            border: const OutlineInputBorder(),
            suffixIcon: IconButton(
              icon: Icon(_obscurePassword ? Icons.visibility_off : Icons.visibility),
              onPressed: () {
                setState(() {
                  _obscurePassword = !_obscurePassword;
                });
              },
            ),
          ),
          keyboardType: TextInputType.text,
          obscureText: _obscurePassword,
          maxLength: 20,
        ),
      ],
    );
  }
}