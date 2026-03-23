import 'package:flutter/material.dart';

class EntradaCheckBox extends StatefulWidget {
	const EntradaCheckBox({super.key});

	@override
	State<EntradaCheckBox> createState() => _EntradaCheckBoxState();
}

class _EntradaCheckBoxState extends State<EntradaCheckBox> {
	bool _masculino = false;
	bool _feminino = false;

	@override
	Widget build(BuildContext context) {
		return Column(
			crossAxisAlignment: CrossAxisAlignment.start,
			children: [
				const Text(
					'Gênero',
					style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
				),
				const SizedBox(height: 8),
				Wrap(
					spacing: 16,
					children: [
						Row(
							mainAxisSize: MainAxisSize.min,
							children: [
								Checkbox(
									value: _masculino,
									onChanged: (valor) {
										setState(() {
											final marcado = valor ?? false;
											_masculino = marcado;
											if (marcado) {
												_feminino = false;
											}
										});
									},
								),
								const Text('Masculino'),
							],
						),
						Row(
							mainAxisSize: MainAxisSize.min,
							children: [
								Checkbox(
									value: _feminino,
									onChanged: (valor) {
										setState(() {
											final marcado = valor ?? false;
											_feminino = marcado;
											if (marcado) {
												_masculino = false;
											}
										});
									},
								),
								const Text('Feminino'),
							],
						),
					],
				),
			],
		);
	}
}
