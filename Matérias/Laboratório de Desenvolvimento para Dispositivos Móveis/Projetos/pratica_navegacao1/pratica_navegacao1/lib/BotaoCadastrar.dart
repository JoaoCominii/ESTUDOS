import 'package:flutter/material.dart';

class BotaoCadastrar extends StatelessWidget {
  const BotaoCadastrar({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      height: 50,
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.blue,
          foregroundColor: Colors.white,
        ),
        onPressed: () {
          print("Cadastrado!");
        },
        child: const Text('Cadastrar', style: TextStyle(fontSize: 18)),
      ),
    );
  }
}
