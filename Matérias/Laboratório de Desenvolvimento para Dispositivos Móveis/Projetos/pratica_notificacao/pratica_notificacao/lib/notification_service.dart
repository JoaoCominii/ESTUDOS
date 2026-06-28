// notification_service.dart
//
// Serviço central de notificações locais.
// Implementa as duas funções da atividade prática do seminário:
//   1. dispararAgora()        → show()
//   2. agendarParaUmMinuto()  → zonedSchedule()

import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:timezone/data/latest_all.dart' as tz;
import 'package:timezone/timezone.dart' as tz;

// ---------------------------------------------------------------------------
// IDs de notificação organizados em uma classe de constantes (boa prática)
// ---------------------------------------------------------------------------
class NotificationIds {
  static const int imediata = 0;
  static const int agendada = 1;
}

// ---------------------------------------------------------------------------
// Configurações do canal Android (obrigatório Android 8.0+)
// ---------------------------------------------------------------------------
const _androidChannel = AndroidNotificationDetails(
  'canal_principal',             // ID único do canal
  'Alertas Principais',          // Nome visível nas configurações do SO
  channelDescription: 'Canal para notificações do app de exemplo',
  importance: Importance.max,
  priority: Priority.high,
  playSound: true,
);

const _notificationDetails = NotificationDetails(
  android: _androidChannel,
  iOS: DarwinNotificationDetails(
    presentAlert: true,          // exibe banner
    presentBadge: true,          // atualiza contador no ícone
    presentSound: true,          // reproduz som
  ),
);

// ---------------------------------------------------------------------------
// NotificationService
// ---------------------------------------------------------------------------
class NotificationService {
  // Instância única (Singleton)
  static final NotificationService _instance = NotificationService._internal();
  factory NotificationService() => _instance;
  NotificationService._internal();

  final FlutterLocalNotificationsPlugin _plugin =
      FlutterLocalNotificationsPlugin();

  // -------------------------------------------------------------------------
  // initialize()
  // Deve ser chamado em main() antes de runApp().
  // -------------------------------------------------------------------------
  Future<void> initialize() async {
    // Inicializa banco de dados de fusos horários (necessário para zonedSchedule)
    tz.initializeTimeZones();

    // Configurações por plataforma
    const androidSettings =
        AndroidInitializationSettings('app_icon'); // PNG em res/drawable/
    const iosSettings = DarwinInitializationSettings(
      requestAlertPermission: true,
      requestBadgePermission: true,
      requestSoundPermission: true,
    );

    const initSettings = InitializationSettings(
      android: androidSettings,
      iOS: iosSettings,
    );

    await _plugin.initialize(
      initSettings,
      // Callback disparado quando o usuário toca na notificação
      onDidReceiveNotificationResponse: _onNotificationTap,
    );

    // Solicita permissão em runtime (obrigatório Android 13+)
    await _plugin
        .resolvePlatformSpecificImplementation<
            AndroidFlutterLocalNotificationsPlugin>()
        ?.requestNotificationsPermission();
  }

  // -------------------------------------------------------------------------
  // Callback de toque na notificação
  // Aqui você pode usar Navigator para redirecionar o usuário.
  // -------------------------------------------------------------------------
  void _onNotificationTap(NotificationResponse response) {
    final payload = response.payload;
    if (payload != null) {
      // Exemplo: Navigator.pushNamed(context, '/detalhes', arguments: payload);
      // ignore: avoid_print
      print('[Notificação] Usuário tocou. Payload recebido: $payload');
    }
  }

  // =========================================================================
  // ATIVIDADE PRÁTICA — FUNÇÃO 1
  // dispararAgora()
  //
  // Usa show() para exibir a notificação no exato milissegundo da chamada.
  // Ideal para feedbacks imediatos de ações do usuário.
  // =========================================================================
  Future<void> dispararAgora() async {
    await _plugin.show(
      NotificationIds.imediata,         // ID único
      'Olá, Mundo! 👋',                // Título
      'Esta é uma notificação imediata. Apareceu agora mesmo!', // Corpo
      _notificationDetails,             // Configurações Android / iOS
      payload: 'notificacao_imediata',  // Dado invisível para navegação
    );
  }

  // =========================================================================
  // ATIVIDADE PRÁTICA — FUNÇÃO 2
  // agendarParaUmMinuto()
  //
  // Usa zonedSchedule() para disparar a notificação exatamente 1 minuto
  // no futuro, respeitando o fuso horário local do usuário e o Doze Mode.
  // =========================================================================
  Future<void> agendarParaUmMinuto() async {
    // tz.local → fuso horário do dispositivo (ex: America/Sao_Paulo)
    // .add(Duration(minutes: 1)) → agendado para daqui a 1 minuto
    final scheduledDate =
        tz.TZDateTime.now(tz.local).add(const Duration(minutes: 1));

    await _plugin.zonedSchedule(
      NotificationIds.agendada,         // ID único (diferente da imediata)
      'Lembrete ⏰',                    // Título
      'Já se passou 1 minuto desde que você agendou este lembrete!', // Corpo
      scheduledDate,                    // Data/hora futura com timezone
      _notificationDetails,             // Configurações Android / iOS
      payload: 'notificacao_agendada',  // Dado para navegação ao tocar
      uiLocalNotificationDateInterpretation:
          UILocalNotificationDateInterpretation.absoluteTime,
      // androidAllowWhileIdle: garante que o alarme toque mesmo
      // que o sistema entre em Doze Mode para economizar bateria.
      androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle,
    );
  }

  // =========================================================================
  // EXTRAS — Gestão de notificações
  // =========================================================================

  /// Cancela uma notificação específica pelo ID.
  Future<void> cancelar(int id) async {
    await _plugin.cancel(id);
  }

  /// Cancela TODAS as notificações pendentes e agendadas.
  Future<void> cancelarTodas() async {
    await _plugin.cancelAll();
  }
}
