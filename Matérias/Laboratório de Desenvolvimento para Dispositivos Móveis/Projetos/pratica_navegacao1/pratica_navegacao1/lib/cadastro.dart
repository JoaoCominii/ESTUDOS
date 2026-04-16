import 'package:flutter/material.dart';
import 'CampoTexto.dart';
import 'EntradaCheckBox.dart';
import 'EntradaSwitch.dart';
import 'BotaoCadastrar.dart';

class HomeCadastro extends StatefulWidget {
  const HomeCadastro({super.key});

  @override
  State<HomeCadastro> createState() => _HomeCadastroState();
}

class _HomeCadastroState extends State<HomeCadastro> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create an account'),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: const SingleChildScrollView(
        child: Padding(
          padding: EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CampoTexto(),
              SizedBox(height: 16),
              EntradaCheckBox(),
              SizedBox(height: 16),
              EntradaSwitch(),
              SizedBox(height: 32),
              BotaoCadastrar(),
            ],
          ),
        ),
      ),
    );
  }
}
