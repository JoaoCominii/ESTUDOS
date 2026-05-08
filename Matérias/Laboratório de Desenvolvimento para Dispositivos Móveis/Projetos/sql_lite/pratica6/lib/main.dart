import 'package:flutter/material.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

void main() {
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Home(),
    );
  }
}

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  // Método para recuperar ou abrir o banco de dados
  _recuperarBD() async {
    // Obtém o caminho onde o banco de dados será salvo no dispositivo
    final caminho = await getDatabasesPath();
    final local = join(caminho, "bancodados.db");

    // Abre o banco de dados e cria a tabela 'usuarios' se ainda não existir
    var retorno = await openDatabase(
      local,
      version: 2,
      onCreate: (db, dbVersaoRecente) {
        // SQL para criar a tabela 'usuarios' com matrícula única e dados do aluno
        String sql = "CREATE TABLE usuarios ("
            "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            "matricula TEXT UNIQUE NOT NULL, "
            "nome VARCHAR NOT NULL, "
            "curso VARCHAR NOT NULL, "
            "idade INTEGER)";
        db.execute(sql);
      },
      onUpgrade: (db, versaoAntiga, versaoNova) async {
        await db.execute("DROP TABLE IF EXISTS usuarios");
        await db.execute(
          "CREATE TABLE usuarios ("
          "id INTEGER PRIMARY KEY AUTOINCREMENT, "
          "matricula TEXT UNIQUE NOT NULL, "
          "nome VARCHAR NOT NULL, "
          "curso VARCHAR NOT NULL, "
          "idade INTEGER)",
        );
      },
    );

    print("Aberto ${retorno.isOpen.toString()}");

    return retorno;
  }

  // Método para inserir um novo usuário no banco de dados
  _salvarDados(BuildContext context, String matricula, String nome,
      String curso, int idade) async {
    Database db = await _recuperarBD();

    // Dados a serem inseridos, representados como um mapa
    Map<String, dynamic> dadosUsuario = {
      "matricula": matricula,
      "nome": nome,
      "curso": curso,
      "idade": idade,
    };

    try {
      // Insere os dados na tabela 'usuarios' e retorna o ID do novo registro
      int id = await db.insert("usuarios", dadosUsuario);
      print("Salvo $id");

      // Exibe um diálogo para o usuário confirmar que o registro foi salvo
      _mostrarDialogo(context, "Aluno salvo com sucesso!");
    } catch (e) {
      _mostrarDialogo(context,
          "Não foi possível salvar. Verifique se a matrícula já não existe.");
    }
  }

  // Método para exibir diálogos de confirmação e mensagens
  _mostrarDialogo(BuildContext context, String mensagem) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text("Resultado"),
          content: Text(mensagem),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text("OK"),
            ),
          ],
        );
      },
    );
  }

  // Método para listar todos os usuários armazenados no banco de dados
  _listarUsuarios() async {
    Database db = await _recuperarBD();
    String sql = "SELECT * FROM usuarios ORDER BY nome";
    List usuarios = await db.rawQuery(sql);

    // Imprime os dados de cada usuário listado no banco
    for (var usu in usuarios) {
      print(
          " id: ${usu['id'].toString()} matricula: ${usu['matricula']} nome: ${usu['nome']} curso: ${usu['curso']} idade: ${usu['idade']}");
    }
  }

  // Método para listar um usuário específico com base na matrícula
  _listarUmUsuario(BuildContext context, String matricula) async {
    Database db = await _recuperarBD();

    // Faz a consulta na tabela 'usuarios' com a matrícula fornecida
    List usuarios = await db.query(
      "usuarios",
      columns: ["id", "matricula", "nome", "curso", "idade"],
      where: "matricula = ?",
      whereArgs: [matricula],
    );

    // Verifica se o usuário existe e exibe um diálogo com as informações
    if (usuarios.isNotEmpty) {
      var usuario = usuarios.first;
      _mostrarDialogo(context,
          "ID: ${usuario['id']} \nMatrícula: ${usuario['matricula']} \nNome: ${usuario['nome']} \nCurso: ${usuario['curso']} \nIdade: ${usuario['idade']}");
    } else {
      _mostrarDialogo(context, "Aluno com matrícula $matricula não encontrado.");
    }
  }

  // Método para excluir um usuário com base na matrícula
  _excluirUsuario(BuildContext context, String matricula) async {
    Database db = await _recuperarBD();

    // Exclui o registro de acordo com a matrícula fornecida
    int retorno = await db.delete(
      "usuarios",
      where: "matricula = ?",
      whereArgs: [matricula],
    );

    print("Itens excluídos: $retorno");

    // Exibe um diálogo para confirmar a exclusão
    if (retorno > 0) {
      _mostrarDialogo(context,
          "Aluno com matrícula $matricula excluído com sucesso.");
    } else {
      _mostrarDialogo(context,
          "Nenhum aluno encontrado com a matrícula $matricula.");
    }
  }

  // Método para atualizar informações de um usuário existente
  _atualizarUsuario(BuildContext context, String matricula, String? nome,
      String? curso, int? idade) async {
    Database db = await _recuperarBD();

    // Cria um mapa para atualizar os dados somente dos campos não nulos
    Map<String, dynamic> dadosUsuario = {};
    if (nome != null && nome.isNotEmpty) {
      dadosUsuario["nome"] = nome;
    }
    if (curso != null && curso.isNotEmpty) {
      dadosUsuario["curso"] = curso;
    }
    if (idade != null) {
      dadosUsuario["idade"] = idade;
    }

    // Realiza a atualização caso existam campos para modificar
    if (dadosUsuario.isNotEmpty) {
      int retorno = await db.update(
        "usuarios",
        dadosUsuario,
        where: "matricula = ?",
        whereArgs: [matricula],
      );

      print("Itens atualizados: $retorno");
      if (retorno > 0) {
        _mostrarDialogo(
            context, "Aluno com matrícula $matricula atualizado com sucesso.");
      } else {
        _mostrarDialogo(context,
            "Nenhum aluno encontrado com a matrícula $matricula.");
      }
    } else {
      _mostrarDialogo(context, "Nenhuma informação para atualizar.");
    }
  }

  final TextEditingController _matriculaController = TextEditingController();
  final TextEditingController _nomeController = TextEditingController();
  final TextEditingController _cursoController = TextEditingController();
  final TextEditingController _idadeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        alignment: Alignment.center,
        child: Column(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              margin: const EdgeInsets.all(0.5),
              width: 300,
              alignment: Alignment.center,
              child: TextField(
                controller: _matriculaController,
                decoration: const InputDecoration(
                  label: Text("Digite a matrícula:"),
                ),
              ),
            ),
            const Padding(padding: EdgeInsets.all(10)),
            Container(
              margin: const EdgeInsets.all(0.5),
              width: 300,
              alignment: Alignment.center,
              child: TextField(
                controller: _nomeController,
                decoration: const InputDecoration(
                  label: Text("Digite o nome:"),
                ),
              ),
            ),
            const Padding(padding: EdgeInsets.all(10)),
            Container(
              margin: const EdgeInsets.all(0.5),
              width: 300,
              alignment: Alignment.center,
              child: TextField(
                controller: _cursoController,
                decoration: const InputDecoration(
                  label: Text("Digite o nome do curso:"),
                ),
              ),
            ),
            const Padding(padding: EdgeInsets.all(10)),
            Container(
              margin: const EdgeInsets.all(0.5),
              width: 300,
              alignment: Alignment.center,
              child: TextField(
                controller: _idadeController,
                decoration: const InputDecoration(
                  label: Text("Digite a idade:"),
                ),
                keyboardType: TextInputType.number,
              ),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                _salvarDados(
                    context,
                    _matriculaController.text.trim(),
                    _nomeController.text.trim(),
                    _cursoController.text.trim(),
                    int.tryParse(_idadeController.text) ?? 0);
              },
              child: const Text("Salvar um aluno"),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: _listarUsuarios,
              child: const Text("Listar todos alunos"),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                String matricula = _matriculaController.text.trim();
                if (matricula.isNotEmpty) {
                  _listarUmUsuario(context, matricula);
                } else {
                  _mostrarDialogo(context,
                      "Por favor, insira uma matrícula válida para listar.");
                }
              },
              child: const Text("Listar um usuário"),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                String matricula = _matriculaController.text.trim();
                if (matricula.isNotEmpty) {
                  _excluirUsuario(context, matricula);
                } else {
                  _mostrarDialogo(context,
                      "Por favor, insira uma matrícula válida para excluir.");
                }
              },
              child: const Text("Excluir usuário"),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: () {
                String matricula = _matriculaController.text.trim();
                if (matricula.isNotEmpty) {
                  String? nome = _nomeController.text.isNotEmpty
                      ? _nomeController.text
                      : null;
                  String? curso = _cursoController.text.isNotEmpty
                      ? _cursoController.text
                      : null;
                  int? idade = int.tryParse(_idadeController.text);
                  _atualizarUsuario(context, matricula, nome, curso, idade);
                } else {
                  _mostrarDialogo(context,
                      "Por favor, insira uma matrícula válida para atualizar.");
                }
              },
              child: const Text("Atualizar usuário"),
            ),
          ],
        ),
      ),
    );
  }
}
