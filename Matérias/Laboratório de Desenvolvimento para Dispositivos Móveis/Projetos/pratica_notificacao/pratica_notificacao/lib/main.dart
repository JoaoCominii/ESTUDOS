// main.dart
//
// Ponto de entrada do app de demonstração.
// Inicializa o NotificationService e exibe a tela principal.

import 'package:flutter/material.dart';
import 'notification_service.dart';

void main() async {
  // Garante que os bindings do Flutter estejam prontos antes de usar plugins
  WidgetsFlutterBinding.ensureInitialized();

  // Inicializa o serviço de notificações (permissões + configurações)
  await NotificationService().initialize();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Notificações Locais',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorSchemeSeed: const Color(0xFF00BFA5), // teal do slide
        useMaterial3: true,
      ),
      // Rotas nomeadas para navegação via payload
      routes: {
        '/': (context) => const HomePage(),
        '/detalhes': (context) => const DetalhesPage(),
      },
      initialRoute: '/',
    );
  }
}

// =============================================================================
// HomePage — Tela principal com os três botões da atividade
// =============================================================================
class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final _service = NotificationService();
  String _statusMessage = '';

  void _setStatus(String msg) => setState(() => _statusMessage = msg);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Notificações Locais'),
        centerTitle: true,
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // ---------------------------------------------------------------
              // BOTÃO 1 — Notificação Imediata (dispararAgora)
              // ---------------------------------------------------------------
              _NotificationButton(
                label: 'Notificação Imediata',
                icon: Icons.notifications_active,
                color: const Color(0xFF00BFA5),
                onPressed: () async {
                  await _service.dispararAgora();
                  _setStatus('✅ Notificação imediata disparada!');
                },
              ),
              const SizedBox(height: 16),

              // ---------------------------------------------------------------
              // BOTÃO 2 — Notificação Agendada (agendarParaUmMinuto)
              // ---------------------------------------------------------------
              _NotificationButton(
                label: 'Agendar Notificação',
                icon: Icons.alarm,
                color: const Color(0xFF0277BD),
                onPressed: () async {
                  await _service.agendarParaUmMinuto();
                  _setStatus('⏰ Notificação agendada para 1 minuto!');
                },
              ),
              const SizedBox(height: 16),

              // ---------------------------------------------------------------
              // BOTÃO 3 — Cancelar todas
              // ---------------------------------------------------------------
              _NotificationButton(
                label: 'Cancelar Todas',
                icon: Icons.notifications_off,
                color: Colors.redAccent,
                onPressed: () async {
                  await _service.cancelarTodas();
                  _setStatus('🚫 Todas as notificações canceladas.');
                },
              ),

              // ---------------------------------------------------------------
              // Área de status
              // ---------------------------------------------------------------
              if (_statusMessage.isNotEmpty) ...[
                const SizedBox(height: 32),
                Container(
                  padding: const EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: Colors.grey.shade100,
                    borderRadius: BorderRadius.circular(12),
                    border: Border.all(color: Colors.grey.shade300),
                  ),
                  child: Text(
                    _statusMessage,
                    textAlign: TextAlign.center,
                    style: const TextStyle(fontSize: 14),
                  ),
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}

// =============================================================================
// DetalhesPage — Aberta via payload ao tocar na notificação
// =============================================================================
class DetalhesPage extends StatelessWidget {
  const DetalhesPage({super.key});

  @override
  Widget build(BuildContext context) {
    final payload = ModalRoute.of(context)?.settings.arguments as String?;

    return Scaffold(
      appBar: AppBar(title: const Text('Tela de Detalhes')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.rocket_launch, size: 64, color: Color(0xFF00BFA5)),
            const SizedBox(height: 16),
            const Text(
              'Você abriu a tela pela notificação 🚀',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              textAlign: TextAlign.center,
            ),
            if (payload != null) ...[
              const SizedBox(height: 8),
              Text(
                'Payload recebido: $payload',
                style: const TextStyle(color: Colors.grey),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

// =============================================================================
// Widget auxiliar reutilizável para os botões
// =============================================================================
class _NotificationButton extends StatelessWidget {
  const _NotificationButton({
    required this.label,
    required this.icon,
    required this.color,
    required this.onPressed,
  });

  final String label;
  final IconData icon;
  final Color color;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return FilledButton.icon(
      style: FilledButton.styleFrom(
        backgroundColor: color,
        padding: const EdgeInsets.symmetric(vertical: 16),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      ),
      icon: Icon(icon),
      label: Text(label, style: const TextStyle(fontSize: 16)),
      onPressed: onPressed,
    );
  }
}