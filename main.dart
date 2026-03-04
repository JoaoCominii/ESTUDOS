import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Tela de login',
      home: const HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      drawer: const Drawer(),
      body: Align(
        alignment: Alignment.center,
        child: Container(
          width: 150,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: const [
              TextField(
                decoration: InputDecoration(labelText: 'Email'),
                style: TextStyle(color: Colors.grey, fontSize: 20),
              ),
              SizedBox(height: 16),
              TextField(
                decoration: InputDecoration(labelText: 'Senha'),
                style: TextStyle(color: Colors.grey, fontSize: 20),
                obscureText: true, // esconder a senha enquanto escreve
              ),
            ],
          ),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        selectedItemColor: Colors.blue, // Cor do item selecionado para todos os itens da BottomNavigationBar
        unselectedItemColor: Colors.grey, // Cor do item não selecionado para todos os itens da BottomNavigationBar
        items: <BottomNavigationBarItem> [
          BottomNavigationBarItem(
          icon: Icon(Icons.home),
          label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: 'Minha Conta',
          ),
        ],
      ),
    );
  }
}




