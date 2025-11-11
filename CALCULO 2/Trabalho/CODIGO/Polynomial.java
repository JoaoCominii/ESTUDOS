import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa um polinômio como uma lista de coeficientes. [cite: 14]
 * O índice 0 é o termo constante (x^0), índice 1 é o termo (x^1), etc.
 * Ex: 5x^2 - 3x + 1 seria armazenado como [1.0, -3.0, 5.0]
 */
public class Polynomial {
    private List<Double> coefficients;

    // Construtor principal
    public Polynomial(List<Double> coeffs) {
        // Remove zeros à direita para normalizar o grau
        int degree = coeffs.size() - 1;
        while (degree > 0 && Math.abs(coeffs.get(degree)) < 1e-9) {
            degree--;
        }
        this.coefficients = new ArrayList<>(coeffs.subList(0, degree + 1));
    }

    // Construtor estático para um polinômio linear: Ax + B
    // Armazenado como [B, A]
    public static Polynomial linear(double A, double B) {
        return new Polynomial(List.of(B, A));
    }

    // Construtor estático para um polinômio quadrático: ax^2 + bx + c
    // Armazenado como [c, b, a]
    public static Polynomial quadratic(double a, double b, double c) {
        return new Polynomial(List.of(c, b, a));
    }

    public double getCoefficient(int power) {
        if (power >= 0 && power < coefficients.size()) {
            return coefficients.get(power);
        }
        return 0.0;
    }

    public int getDegree() {
        return Math.max(0, coefficients.size() - 1);
    }

    @Override
    public String toString() {
        if (coefficients.isEmpty() || coefficients.stream().allMatch(c -> Math.abs(c) < 1e-9)) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = getDegree(); i >= 0; i--) {
            double coeff = getCoefficient(i);
            if (Math.abs(coeff) < 1e-9) continue; // Pular termos nulos

            // Lidar com o sinal
            if (sb.length() > 0) {
                sb.append(coeff > 0 ? " + " : " - ");
                coeff = Math.abs(coeff);
            } else if (coeff < 0) {
                sb.append("-");
                coeff = Math.abs(coeff);
            }

            // Lidar com o coeficiente (omitir se for 1 e não for termo constante)
            if (Math.abs(coeff - 1.0) > 1e-9 || i == 0) {
                sb.append(String.format("%.2f", coeff));
            }

            // Lidar com a variável x
            if (i > 0) {
                sb.append("x");
            }
            if (i > 1) {
                sb.append("^").append(i);
            }
        }
        return sb.toString();
    }
}