import 'package:flutter/material.dart';
import 'CampoTexto.dart';
import 'EntradaCheckBox.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Tela de Cadastro',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
      ),
      home: const HomeCadastro(),
    );
  }
}

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
        title: const Text('Cadastro'),
      ),
      body: const SingleChildScrollView(
        child: Padding(
          padding: EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CampoTexto(),
              SizedBox(height: 8),
              EntradaCheckBox(),
            ],
          ),
        ),
      ),
    );
  }
}
