# Notificações Locais no Flutter — Atividade Prática

Projeto de implementação da atividade proposta no seminário sobre
**Notificações Locais no Flutter** (Filipe Lorenzato, Iuri Saad, Felipe Birchal, Gabryelle Franco).

---

## Estrutura dos arquivos

```
lib/
├── main.dart                  # Ponto de entrada + UI da HomePage e DetalhesPage
└── notification_service.dart  # Serviço central (dispararAgora + agendarParaUmMinuto)

android/
└── AndroidManifest_referencia.xml  # Permissões que devem ser adicionadas ao Manifest real
pubspec.yaml                        # Dependências do projeto
```

---

## Dependências (`pubspec.yaml`)

```yaml
dependencies:
  flutter_local_notifications: ^17.0.0  # plugin principal
  timezone: ^0.9.2                       # controle de fusos horários
```

Execute `flutter pub get` para instalar.

---

## Configuração Android

### 1. Permissões no `AndroidManifest.xml`

Adicione dentro de `<manifest>` (antes de `<application>`):

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.USE_EXACT_ALARM"/>
```

### 2. Receivers dentro de `<application>`

```xml
<receiver android:exported="false"
    android:name="com.dexterous.flutterlocalnotifications.ScheduledNotificationReceiver"/>

<receiver android:exported="false"
    android:name="com.dexterous.flutterlocalnotifications.ScheduledNotificationBootReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.MY_PACKAGE_REPLACED"/>
        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</receiver>
```

### 3. Ícone da notificação

Adicione um PNG transparente (silhueta) em:
```
android/app/src/main/res/drawable/app_icon.png
```

---

## Como funciona

### `NotificationService.initialize()`
Deve ser chamado em `main()` antes de `runApp()`. Inicializa:
- Banco de dados de fusos horários (`tz.initializeTimeZones()`)
- Configurações por plataforma (Android/iOS)
- Callback de toque na notificação (`onDidReceiveNotificationResponse`)
- Solicitação de permissão em runtime (Android 13+)

---

### Função 1 — `dispararAgora()` ✅

```dart
await _plugin.show(
  NotificationIds.imediata,   // ID único
  'Olá, Mundo! 👋',           // Título
  'Esta é uma notificação imediata.',
  _notificationDetails,
  payload: 'notificacao_imediata',
);
```

Usa `show()` — dispara **imediatamente** no momento da execução.

---

### Função 2 — `agendarParaUmMinuto()` ✅

```dart
final scheduledDate = tz.TZDateTime.now(tz.local)
    .add(const Duration(minutes: 1));

await _plugin.zonedSchedule(
  NotificationIds.agendada,
  'Lembrete ⏰',
  'Já se passou 1 minuto!',
  scheduledDate,
  _notificationDetails,
  payload: 'notificacao_agendada',
  uiLocalNotificationDateInterpretation:
      UILocalNotificationDateInterpretation.absoluteTime,
  androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle, // Doze Mode
);
```

Usa `zonedSchedule()` com:
- `tz.local` → respeita o fuso horário do dispositivo
- `exactAllowWhileIdle` → dispara mesmo no Doze Mode (Samsung, Xiaomi, etc.)

---

## Boas práticas aplicadas

| Prática | Implementação |
|---|---|
| IDs organizados | Classe `NotificationIds` com constantes |
| Canal Android | `AndroidNotificationDetails` com importância `max` |
| Permissão runtime | `requestNotificationsPermission()` no initialize |
| Doze Mode | `AndroidScheduleMode.exactAllowWhileIdle` |
| Navegação por payload | `onDidReceiveNotificationResponse` + `Navigator.pushNamed` |
| Singleton | `NotificationService._internal()` |

---

## Referência

- GitHub do seminário: https://github.com/saadiuri/SeminarioLDDM
- Documentação oficial: https://pub.dev/packages/flutter_local_notifications