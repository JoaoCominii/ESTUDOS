import 'package:flutter/material.dart';

void main() {
  runApp(const KalScoreApp());
}

class KalScoreApp extends StatelessWidget {
  const KalScoreApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'KalScore',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF2ECC71)),
        useMaterial3: true,
        scaffoldBackgroundColor: const Color(0xFFF4F6F8),
        fontFamily: 'Roboto',
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF4F6F8),
      body: SafeArea(
        child: Stack(
          children: [
            SingleChildScrollView(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const _Header(),
                  const SizedBox(height: 24),
                  const _DailySummaryCard(),
                  const SizedBox(height: 24),
                  const _WaterConsumptionCard(),
                  const SizedBox(height: 24),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text(
                        "Atividade dos Amigos",
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF0D1B2A),
                        ),
                      ),
                      TextButton(
                        onPressed: () {},
                        child: const Text(
                          "Ver todos",
                          style: TextStyle(color: Color(0xFF2ECC71)),
                        ),
                      )
                    ],
                  ),
                  const SizedBox(height: 12),
                  const _FriendActivityCard(
                    avatarColor: Colors.grey,
                    name: "João Silva",
                    action: "registrou um almoço saudável.",
                    timeAgo: "Há 2 horas",
                    contentTitle: "Salada de Quinoa",
                    contentSubtitle: "350 kcal • Rico em proteínas",
                    iconData: Icons.restaurant,
                    likes: 12,
                    comments: 3,
                    contentColor: Color(0xFFF5F5F5),
                    contentIconColor: Colors.green,
                  ),
                  const SizedBox(height: 16),
                  const _FriendActivityCard(
                    avatarColor: Colors.blueGrey,
                    name: "Marla Costa",
                    action: "atingiu a meta diária! 🎉",
                    timeAgo: "Há 4 horas",
                    contentTitle: "Meta de 1.800 kcal atingida",
                    contentSubtitle: "Ofensiva de 21 dias!",
                    iconData: Icons.emoji_events,
                    likes: 24,
                    comments: 5,
                    contentColor: Color(0xFFE0F2F1),
                    contentIconColor: Colors.amber,
                    isHighlight: true,
                  ),
                  const SizedBox(height: 16),
                  const _FriendActivityCard(
                    avatarColor: Colors.brown,
                    name: "Pedro Alves",
                    action: "e o grupo Fit Familia ficaram em 1º lugar! 🥇",
                    timeAgo: "Há 6 horas",
                    contentTitle: "1º Lugar no Ranking Semanal",
                    contentSubtitle: "Total de 150km corridos",
                    iconData: Icons.directions_run,
                    likes: 45,
                    comments: 10,
                    contentColor: Color(0xFFF5F5F5),
                    contentIconColor: Colors.blue,
                  ),
                  const SizedBox(height: 80),
                ],
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(bottom: 10),
        child: SizedBox(
          height: 64,
          width: 64,
          child: FloatingActionButton(
            onPressed: () {},
            backgroundColor: const Color(0xFF2ECC71),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
            child: const Icon(Icons.add, color: Colors.white, size: 32),
          ),
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      bottomNavigationBar: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: const BorderRadius.only(
            topLeft: Radius.circular(25),
            topRight: Radius.circular(25)
          ),
          boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 10)]
        ),
        padding: const EdgeInsets.symmetric(vertical: 10),
        child: const Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _NavBarItem(icon: Icons.home_filled, label: "Início", isSelected: true),
            _NavBarItem(icon: Icons.restaurant_menu, label: "Refeições"),
            _NavBarItem(icon: Icons.group_outlined, label: "Grupos"),
            _NavBarItem(icon: Icons.emoji_events_outlined, label: "Ranking"),
            _NavBarItem(icon: Icons.person_outline, label: "Perfil"),
          ],
        ),
      ),
    );
  }
}

class _NavBarItem extends StatelessWidget {
  final IconData icon;
  final String label;
  final bool isSelected;
  const _NavBarItem({required this.icon, required this.label, this.isSelected = false});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          padding: const EdgeInsets.all(8),
          decoration: isSelected ? BoxDecoration(
            color: const Color(0xFFE8F5E9),
            borderRadius: BorderRadius.circular(12)
          ) : null,
          child: Icon(
            icon, 
            color: isSelected ? const Color(0xFF2ECC71) : Colors.grey,
          ),
        ),
        Text(
          label,
          style: TextStyle(
            fontSize: 10,
            color: isSelected ? const Color(0xFF2ECC71) : Colors.grey,
          ),
        )
      ],
    );
  }
}

class _Header extends StatelessWidget {
  const _Header();

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: const Color(0xFF2ECC71),
                borderRadius: BorderRadius.circular(12),
              ),
              child: const Text("K", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold, fontSize: 20)),
            ),
            const SizedBox(width: 12),
            const Text(
              "KalScore",
              style: TextStyle(
                fontSize: 22,
                fontWeight: FontWeight.bold,
                color: Color(0xFF0D1B2A),
              ),
            ),
          ],
        ),
        Stack(
          children: [
            Container(
              padding: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
                border: Border.all(color: Colors.white, width: 2),
                boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 4)]
              ),
              child: const Icon(Icons.people_outline, color: Color(0xFF0D1B2A)),
            ),
            Positioned(
              right: 0,
              top: 0,
              child: Container(
                width: 12,
                height: 12,
                decoration: BoxDecoration(
                  color: const Color(0xFF2ECC71),
                  shape: BoxShape.circle,
                  border: Border.all(color: Colors.white, width: 2),
                ),
              ),
            )
          ],
        )
      ],
    );
  }
}

class _DailySummaryCard extends StatelessWidget {
  const _DailySummaryCard();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 10, offset: Offset(0, 4))],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Text("RESUMO DO DIA", style: TextStyle(color: Colors.grey, fontWeight: FontWeight.bold, fontSize: 12)),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                decoration: BoxDecoration(
                  color: const Color(0xFFFFF3E0),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: const Row(
                  children: [
                    Icon(Icons.local_fire_department, color: Colors.orange, size: 16),
                    SizedBox(width: 4),
                    Text("14 dias", style: TextStyle(color: Colors.deepOrange, fontWeight: FontWeight.bold)),
                  ],
                ),
              )
            ],
          ),
          const SizedBox(height: 20),
          Row(
            children: [
              SizedBox(
                height: 120,
                width: 120,
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    SizedBox(
                      width: 120,
                      height: 120,
                      child: CircularProgressIndicator(
                        value: 0.75,
                        strokeWidth: 10,
                        backgroundColor: Colors.grey[100],
                        valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFF00BFA5)),
                        strokeCap: StrokeCap.round,
                      ),
                    ),
                    const Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text("75%", style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold)),
                        Text("da meta", style: TextStyle(fontSize: 12, color: Colors.grey)),
                      ],
                    )
                  ],
                ),
              ),
              const SizedBox(width: 20),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Row(children: [
                      Icon(Icons.local_fire_department_outlined, color: Colors.orange, size: 18),
                      SizedBox(width: 4),
                      Text("Consumido", style: TextStyle(color: Colors.grey))
                    ]),
                    const SizedBox(height: 4),
                    const Row(
                      crossAxisAlignment: CrossAxisAlignment.baseline,
                      textBaseline: TextBaseline.alphabetic,
                      children: [
                         Text("1.500", style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
                         Text(" kcal", style: TextStyle(color: Colors.grey)),
                      ],
                    ),
                    const SizedBox(height: 12),
                     const Row(children: [
                      Icon(Icons.track_changes, color: Color(0xFF00BFA5), size: 18),
                      SizedBox(width: 4),
                       Text("Meta Diária", style: TextStyle(color: Colors.grey))
                    ]),
                    const SizedBox(height: 4),
                    const Row(
                      crossAxisAlignment: CrossAxisAlignment.baseline,
                       textBaseline: TextBaseline.alphabetic,
                      children: [
                         Text("2.000", style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
                         Text(" kcal", style: TextStyle(color: Colors.grey)),
                      ],
                    ),
                  ],
                ),
              )
            ],
          ),
          const SizedBox(height: 24),
          const _NutrientBar(label: "Proteínas", value: 95, max: 120, color: Colors.blue),
          const SizedBox(height: 12),
          const _NutrientBar(label: "Carboidratos", value: 180, max: 250, color: Colors.orange),
          const SizedBox(height: 12),
          const _NutrientBar(label: "Gorduras", value: 45, max: 65, color: Colors.redAccent),
        ],
      ),
    );
  }
}

class _NutrientBar extends StatelessWidget {
  final String label;
  final double value;
  final double max;
  final Color color;

  const _NutrientBar({required this.label, required this.value, required this.max, required this.color});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(label, style: const TextStyle(color: Colors.grey, fontWeight: FontWeight.w500)),
            RichText(text: TextSpan(
              children: [
                TextSpan(text: "${value.toInt()}g", style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold)),
                TextSpan(text: " / ${max.toInt()}g", style: const TextStyle(color: Colors.grey)),
              ]
            ))
          ],
        ),
        const SizedBox(height: 6),
        ClipRRect(
          borderRadius: BorderRadius.circular(4),
          child: LinearProgressIndicator(
            value: value / max,
            backgroundColor: color.withOpacity(0.1),
            valueColor: AlwaysStoppedAnimation<Color>(color),
            minHeight: 8,
          ),
        )
      ],
    );
  }
}

class _WaterConsumptionCard extends StatelessWidget {
  const _WaterConsumptionCard();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
        boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 10, offset: Offset(0, 4))],
      ),
      child: Column(
        children: [
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Colors.blue[50],
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.water_drop, color: Colors.blue),
              ),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                   const Text("Consumo de Água", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                   Text("1.250ml de 2.000ml", style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                ],
              ),
              const Spacer(),
              Container(
                decoration: const BoxDecoration(
                  color: Colors.blue,
                  shape: BoxShape.circle
                ),
                child: const Icon(Icons.add, color: Colors.white),
              )
            ],
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              ...List.generate(5, (index) => const Icon(Icons.water_drop, color: Colors.blue, size: 32)),
              ...List.generate(3, (index) => Icon(Icons.water_drop_outlined, color: Colors.grey[300], size: 32)),
            ],
          ),
           const SizedBox(height: 8),
           const Align(alignment: Alignment.centerRight, child: Text("63%", style: TextStyle(color: Colors.blue, fontWeight: FontWeight.bold)))
        ],
      ),
    );
  }
}

class _FriendActivityCard extends StatelessWidget {
  final String name;
  final String action;
  final String timeAgo;
  final String contentTitle;
  final String contentSubtitle;
  final IconData iconData;
  final Color contentIconColor;
  final Color avatarColor;
  final int likes;
  final int comments;
  final Color contentColor;
  final bool isHighlight;

  const _FriendActivityCard({
    required this.name,
    required this.action,
    required this.timeAgo,
    required this.contentTitle,
    required this.contentSubtitle,
    required this.iconData,
    this.contentIconColor = Colors.green,
    this.avatarColor = Colors.grey,
    required this.likes,
    required this.comments,
    required this.contentColor,
    this.isHighlight = false,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: const [BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, 2))],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              CircleAvatar(
                backgroundColor: avatarColor, 
                child: Text(name[0], style: const TextStyle(color: Colors.white)),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: RichText(
                  text: TextSpan(
                    style: const TextStyle(color: Colors.black, fontSize: 13),
                    children: [
                      TextSpan(text: name, style: const TextStyle(fontWeight: FontWeight.bold)),
                      const TextSpan(text: " "),
                      TextSpan(text: action),
                    ],
                  ),
                ),
              ),
            ],
          ),
          Padding(
            padding: const EdgeInsets.only(left: 52), // Align with text start
            child: Text(timeAgo, style: TextStyle(color: Colors.grey[400], fontSize: 12)),
          ),
          const SizedBox(height: 12),
          Container(
             padding: const EdgeInsets.all(12),
             decoration: BoxDecoration(
               color: contentColor, 
               borderRadius: BorderRadius.circular(16)
             ),
             child: Row(
               children: [
                 Container(
                   padding: const EdgeInsets.all(8),
                   decoration: const BoxDecoration(
                     color: Colors.white,
                     borderRadius: BorderRadius.all(Radius.circular(12))
                   ),
                   child: Icon(iconData, color: contentIconColor),
                 ),
                 const SizedBox(width: 12),
                 Expanded(
                   child: Column(
                     crossAxisAlignment: CrossAxisAlignment.start,
                     children: [
                       Text(contentTitle, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                       Text(contentSubtitle, style: TextStyle(color: Colors.grey[600], fontSize: 12)),
                     ],
                   ),
                 )
               ],
             ),
          ),
          const SizedBox(height: 12),
           Row(
            children: [
              // Like
              Row(children: [
                const Icon(Icons.favorite_border, size: 20, color: Colors.grey),
                const SizedBox(width: 4),
                Text("$likes", style: const TextStyle(color: Colors.grey)),
              ]),
              const SizedBox(width: 16),
              // Comment & Chat 
              Row(children: [
                 const Icon(Icons.chat_bubble_outline, size: 20, color: Colors.grey),
                 const SizedBox(width: 4),
                 Text("$comments", style: const TextStyle(color: Colors.grey)),
              ]),
            ],
          )
        ],
      ),
    );
  }
}
