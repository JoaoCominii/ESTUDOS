import 'package:flutter/material.dart';

class EntradaSwitch extends StatefulWidget {
  const EntradaSwitch({super.key});

  @override
  State<EntradaSwitch> createState() => _EntradaSwitchState();
}

class _EntradaSwitchState extends State<EntradaSwitch> {
  bool _notificacoesEmail = false;
  bool _notificacoesCelular = false;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Notificações:',
          style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
        ),
        SwitchListTile(
          title: const Text('E-mail'),
          value: _notificacoesEmail,
          onChanged: (bool value) {
            setState(() {
              _notificacoesEmail = value;
            });
          },
        ),
        SwitchListTile(
          title: const Text('Celular'),
          value: _notificacoesCelular,
          onChanged: (bool value) {
            setState(() {
              _notificacoesCelular = value;
            });
          },
        ),
      ],
    );
  }
}
