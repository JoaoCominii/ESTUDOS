/**
 * Implementa os algoritmos de integração por frações parciais.
 * Foco: Resolver os 3 problemas propostos no trabalho. [cite: 21]
 */
import java.util.ArrayList;
import java.util.List;

public class SymbolicIntegrator {

    // Epsilon para comparação de ponto flutuante
    private static final double EPSILON = 1e-9;

    /*
     * Melhorias na saída:
     * - Formatação de coeficientes para até 2 casas, sem zeros finais (ex: 3,5 ou 2)
     * - Omissão de termos com coeficiente zero
     * - Sinal e espaçamento consistentes (ex: "3 ln(...) - 2 arctan(...) + K")
     * - Parênteses tratados para evitar "+ (-... )" (imprime "- (...)")
     */

    /**
     * Resolve o Problema 1: integral((Ax+B) / ((x-x1)(x-x2))) dx [cite: 22]
     * A decomposição é: C1/(x-x1) + C2/(x-x2)
     */
    public String solveCase1(double A, double B, double x1, double x2) {
        System.out.println("--- Resolvendo Caso 1 ---");
    System.out.printf("Funcao: (%s) / ((x - %.2f)(x - %.2f))\n",
                Polynomial.linear(A, B).toString(), x1, x2);

        if (Math.abs(x1 - x2) < EPSILON) {
            return "Erro: Raizes identicas. Use o Caso 2 (Delta = 0).";
        }

        // Resolve o sistema 2x2 para C1 e C2
        // A = C1 + C2
        // B = -C1*x2 - C2*x1
        double C2 = (B + A * x2) / (x2 - x1);
        double C1 = A - C2;

    System.out.printf("Decomposicao: %.2f/(x - %.2f) + %.2f/(x - %.2f)\n", C1, x1, C2, x2);

        // Monta a string do resultado simbólico
        String part1 = formatTerm(C1, String.format("ln|x - %.2f|", x1));
        String part2 = formatTerm(C2, String.format("ln|x - %.2f|", x2));

        String combined = combineTerms(part1, part2);
        return String.format("Resultado: %s + K", combined);
    }

    /**
     * Resolve o Problema 2: integral((Ax+B) / (ax^2+bx+c)) dx [cite: 23]
     * Esta função identifica o tipo de raiz (delta) e resolve.
     */
    public String solveCase2(double A, double B, double a, double b, double c) {
    System.out.println("--- Resolvendo Caso 2 ---");
    System.out.printf("Funcao: (%s) / (%s)\n",
                Polynomial.linear(A, B).toString(),
                Polynomial.quadratic(a, b, c).toString());

        double delta = b * b - 4 * a * c;

    // ----------------------------------------------------
    // Caso 2a: Delta > 0 (Duas raizes reais distintas)
    // ----------------------------------------------------
        if (delta > EPSILON) {
            System.out.println("Info: Delta > 0. Reduzindo ao Caso 1.");
            double x1 = (-b + Math.sqrt(delta)) / (2 * a);
            double x2 = (-b - Math.sqrt(delta)) / (2 * a);

            // Lembre-se: (ax^2+bx+c) = a(x-x1)(x-x2)
            // A integral é (1/a) * integral((Ax+B) / (x-x1)(x-x2)) dx
            String case1Result = solveCase1(A, B, x1, x2);
            // Remove o cabeçalho "Resultado: " do solveCase1
            String integralPart = case1Result.substring(case1Result.indexOf(":") + 2);

            return String.format("Resultado: (1.0/%.2f) * (%s)", a, integralPart);
        }
        
        // ----------------------------------------------------
        // Caso 2b: Delta = 0 (Uma raiz real dupla)
        // ----------------------------------------------------
        else if (Math.abs(delta) < EPSILON) {
            System.out.println("Info: Delta = 0. Caso de raiz dupla.");
            double r = -b / (2 * a); // A raiz dupla

            // Decomposição: (Ax+B) / (a(x-r)^2) = (1/a) * [C1/(x-r) + C2/(x-r)^2]
            // Ax + B = C1(x-r) + C2 = C1*x + (C2 - C1*r)
            double C1 = A;
            double C2 = B + C1 * r;
            
            System.out.printf("Decomposicao: (1/%.2f) * [%.2f/(x - %.2f) + %.2f/(x - %.2f)^2]\n", a, C1, r, C2, r);

            // Integrar: (1/a) * [ C1*ln|x-r| - C2/(x-r) ]
            String part1 = formatTerm(C1, String.format("ln|x - %.2f|", r));
            String part2 = formatTerm(-C2, String.format("1/(x - %.2f)", r));

            String combo = combineTerms(part1, part2);
            return String.format("Resultado: (1.0/%.2f) * (%s) + K", a, combo);
        } 
        
        // ----------------------------------------------------
        // Caso 2c: Delta < 0 (Quadrático irredutível)
        // ----------------------------------------------------
        else {
            System.out.println("Info: Delta < 0. Caso de ln + arctan.");
            
            // 1. Reescrever o numerador (Ax+B) = K1 * (derivada) + K2
            // Derivada do denominador = 2ax + b
            // Ax+B = K1(2ax+b) + K2 = (2a*K1)x + (b*K1 + K2)
            double K1 = A / (2 * a);
            double K2 = B - K1 * b;

            // 2. Separar a integral:
            // I1 = K1 * integral((2ax+b) / (ax^2+bx+c)) dx
            // I2 = K2 * integral(1 / (ax^2+bx+c)) dx
            
            // 3. Resolver I1 (substituição u = ax^2+bx+c)
            String lnPart = formatTerm(K1, String.format("ln|%s|", Polynomial.quadratic(a, b, c).toString()));

            // 4. Resolver I2 (completar o quadrado e usar arctan)
            // Denom: a(x^2 + (b/a)x) + c
            //        = a(x + b/2a)^2 + c - b^2/4a
            //        = a(x + b/2a)^2 + (4ac - b^2)/4a
            //        = a( (x + b/2a)^2 + (-delta)/(4a^2) )
            // 
            
            double u_offset = b / (2 * a); // u = x + u_offset
            double k_squared = -delta / (4 * a * a); // k^2
            double k = Math.sqrt(k_squared);

            // Integral de I2 = K2 * integral(1 / (a * (u^2 + k^2))) du
            //                = (K2/a) * (1/k) * arctan(u/k)
            // 
            
            double arctanCoeff = (K2 / a) * (1 / k);
            String arctanPart = formatTerm(arctanCoeff, 
                                  String.format("arctan((x + %.2f) / %.2f)", u_offset, k));

            String combo = combineTerms(lnPart, arctanPart);
            return String.format("Resultado: %s + K", combo);
        }
    }

    /**
     * Resolve o Problema 3: integral((Ax+B) / ((x^2+c1)(x^2+c2))) dx [cite: 24, 25]
     * Assume c1, c2 > 0 e c1 != c2 (quadráticos irredutíveis distintos)
     */
    public String solveCase3(double A, double B, double c1, double c2) {
    System.out.println("--- Resolvendo Caso 3 ---");
    System.out.printf("Funcao: (%s) / ((x^2 + %.2f)(x^2 + %.2f))\n",
                Polynomial.linear(A, B).toString(), c1, c2);

        if (Math.abs(c1 - c2) < EPSILON) {
            return "Erro: Denominadores quadraticos identicos (caso de raiz dupla).";
        }
        if (c1 < 0 || c2 < 0) {
            return "Erro: c1 ou c2 e negativo. Fatore em raizes lineares (Caso 1).";
        }

        // Decomposição: (C1*x + D1)/(x^2+c1) + (C2*x + D2)/(x^2+c2)
        // Ax+B = (C1x+D1)(x^2+c2) + (C2x+D2)(x^2+c1)
        //      = (C1+C2)x^3 + (D1+D2)x^2 + (C1c2+C2c1)x + (D1c2+D2c1)
        
        // Sistema para C (termos ímpares):
        // C1 + C2 = 0   (coef. x^3)
        // C1c2 + C2c1 = A (coef. x^1)
        double C2 = A / (c1 - c2);
        double C1 = -C2;

        // Sistema para D (termos pares):
        // D1 + D2 = 0   (coef. x^2)
        // D1c2 + D2c1 = B (coef. x^0)
        double D2 = B / (c1 - c2);
        double D1 = -D2;
        
    System.out.printf("Decomposicao: (%.2fx + %.2f)/(x^2 + %.2f) + (%.2fx + %.2f)/(x^2 + %.2f)\n",
                          C1, D1, c1, C2, D2, c2);

        // Integrar termo a termo: integral( (Cx+D) / (x^2+c) ) dx
        String part1 = integrateQuadraticTerm(C1, D1, c1);
        String part2 = integrateQuadraticTerm(C2, D2, c2);

        String combo = combineTerms(part1, part2);
        return String.format("Resultado: %s + K", combo);
    }
    
    /**
     * Função auxiliar para integrar um termo quadrático irredutível:
     * integral( (Cx+D) / (x^2+c) ) dx, onde c > 0
     * = C * integral(x / (x^2+c)) dx + D * integral(1 / (x^2+c)) dx
     * = (C/2) * ln|x^2+c| + (D/sqrt(c)) * arctan(x/sqrt(c))
     */
    private String integrateQuadraticTerm(double C, double D, double c) {
        double k = Math.sqrt(c);
        
        // Parte do ln (u=x^2+c, du=2x dx)
        double lnCoeff = C / 2.0;
        String lnPart = formatTerm(lnCoeff, String.format("ln|x^2 + %.2f|", c));
        
        // Parte do arctan (integral padrão)
        double arctanCoeff = D / k;
        String arctanPart = formatTerm(arctanCoeff, String.format("arctan(x / %.2f)", k));

        String combo = combineTerms(lnPart, arctanPart);
        return "(" + combo + ")";
    }

    /**
     * Formata um termo "Coeficiente * Função" para impressão limpa.
     */
    private String formatTerm(double coeff, String function) {
        if (Math.abs(coeff) < EPSILON) return "0";
        // coeff exactly 1 or -1 (within EPSILON)
        if (Math.abs(coeff - 1.0) < EPSILON) return function;
        if (Math.abs(coeff + 1.0) < EPSILON) return "-" + function;

        String coeffStr = formatNumber(coeff);
        // Return like "3 ln(...)" or "-2.5 arctan(...)"
        return coeffStr + " " + function;
    }

    /**
     * Formata um número com até 2 casas significativas, removendo zeros finais
     * e o ponto decimal quando não necessário.
     */
    private String formatNumber(double v) {
        String s = String.format("%.2f", v);
        // Remove zeros finais
        if (s.indexOf('.') >= 0) {
            while (s.endsWith("0")) s = s.substring(0, s.length() - 1);
            if (s.endsWith(".")) s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * Junta vários termos já formatados respeitando sinais, omitindo termos nulos.
     * Ex: ["3 ln|x-1|", "-2 arctan(...)", "0"] => "3 ln|x-1| - 2 arctan(...)"
     */
    private String combineTerms(String... parts) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String p : parts) {
            if (p == null) continue;
            p = p.trim();
            if (p.equals("0") || p.isEmpty()) continue;

            if (first) {
                sb.append(p);
                first = false;
            } else {
                // If part starts with a negative sign, print as subtraction
                if (p.startsWith("-")) {
                    String rest = p.substring(1).trim();
                    sb.append(" - ").append(rest);
                }
                // If part is parenthesized but inner starts with '-', treat as subtraction and keep parentheses
                else if (p.startsWith("(") && p.length() > 1 && p.charAt(1) == '-') {
                    // p like "(-0.33 ln... + 0.17 arctan(...))"
                    String inner = p.substring(1, p.length() - 1).trim();
                    String rest = inner.substring(1).trim();
                    sb.append(" - (").append(rest).append(")");
                } else {
                    sb.append(" + ").append(p);
                }
            }
        }
        if (first) return "0"; // all parts were zero
        return sb.toString();
    }


    // --- Método Main para Testar ---
    /**
     * Integra automaticamente dada a lista de coeficientes do numerador e denominador.
     * Numerador/denominador: índice = potência (0 = termo constante).
     * Suporte mínimo (casos do trabalho):
     *  - denom grau 2: usa solveCase2
     *  - denom grau 4 e sem termos ímpares (x^3 e x^1 zeros): tenta fatorar como (x^2+c1)(x^2+c2) e usa solveCase3
     * Se numerador tiver grau >= denom, executa divisão polinomial e integra o quociente polinomial separadamente.
     */
    public String integrate(List<Double> numerCoeffs, List<Double> denomCoeffs) {
        // Normalize polynomials (remove trailing zeros)
        Polynomial numer = new Polynomial(numerCoeffs);
        Polynomial denom = new Polynomial(denomCoeffs);

        // If degree(numer) >= degree(denom), perform division
        List<Double> rem = new ArrayList<>(numerCoeffs);
        List<Double> quot = new ArrayList<>();
        if (numer.getDegree() >= denom.getDegree()) {
            DivisionResult dr = polynomialDivide(numerCoeffs, denomCoeffs);
            quot = dr.quotient;
            rem = dr.remainder;
        }

    StringBuilder result = new StringBuilder();
    String detection = "Nao detectado";
        // Integrate quotient polynomial (if any)
        if (!isZeroPoly(quot)) {
            String qInt = integratePolynomial(new Polynomial(quot));
            result.append(qInt);
            // if remainder is non-zero, add separator
            if (!isZeroPoly(rem)) result.append(" + ");
        }

        if (isZeroPoly(rem)) {
            result.append("K");
            return "Resultado: " + result.toString();
        }

        Polynomial remainder = new Polynomial(rem);
        if (denom.getDegree() == 2) {
            double A = remainder.getCoefficient(1);
            double B = remainder.getCoefficient(0);
            double a = denom.getCoefficient(2);
            double b = denom.getCoefficient(1);
            double c = denom.getCoefficient(0);

            double delta = b * b - 4 * a * c;
            if (delta > EPSILON) detection = "Caso 2a: Delta>0 (duas raizes reais) - reduz ao Caso 1";
            else if (Math.abs(delta) < EPSILON) detection = "Caso 2b: Delta=0 (raiz dupla)";
            else detection = "Caso 2c: Delta<0 (quadratico irredutivel)";

            String part = solveCase2(A, B, a, b, c);
            // extrair parte após "Resultado: " e juntar
            String body = part.substring(part.indexOf(":") + 2);
            result.append(body);
            return String.format("Resultado (Detectado: %s): %s", detection, result.toString());
        }

        // Detect simple (x^2 + c1)(x^2 + c2) pattern for degree 4 denominators
        if (denom.getDegree() == 4) {
            // coeffs: [c0, c1, c2, c3, c4]
            double a4 = denom.getCoefficient(4);
            double a3 = denom.getCoefficient(3);
            double a2 = denom.getCoefficient(2);
            double a1 = denom.getCoefficient(1);
            double a0 = denom.getCoefficient(0);

            if (Math.abs(a3) < EPSILON && Math.abs(a1) < EPSILON && a4 != 0) {
                // normalize to monic: x^4 + A x^2 + B
                double A = a2 / a4;
                double B = a0 / a4;
                // Solve u^2 - A u + B = 0 for c1,c2
                double delta = A * A - 4 * B;
                if (delta >= -EPSILON) {
                    double sqrtD = Math.sqrt(Math.max(0, delta));
                    double c1 = (A + sqrtD) / 2.0;
                    double c2 = (A - sqrtD) / 2.0;
                    // Now remainder numerator should be linear: Ax+B
                    double A_num = remainder.getCoefficient(1);
                    double B_num = remainder.getCoefficient(0);

                    detection = "Caso 3: produto de quadraticos (x^2 + c1)(x^2 + c2)";
                    String part = solveCase3(A_num, B_num, c1, c2);
                    String body = part.substring(part.indexOf(":") + 2);
                    if (Math.abs(a4 - 1.0) > EPSILON) {
                        result.append(String.format("(1.0/%.2f) * (%s)", a4, body));
                    } else {
                        result.append(body);
                    }
                    return String.format("Resultado (Detectado: %s): %s", detection, result.toString());
                }
            }
        }

        // Fallback: not supported automatically
    detection = "Caso misto/indeterminado: nao suportado automaticamente";
    return String.format("Erro: tipo de denominador nao suportado pela rotina automatica. (Detectado: %s)", detection);
    }

    /**
     * Parser simples que aceita expressões do tipo "(5x-3)/(x^2-2x-3)".
     * Converte numerador e denominador em listas de coeficientes (índice = potência)
     * e chama integrate(...).
     * Limitações: aceita apenas polinômios somados/subtraídos (termos com x^k e constantes).
     */
    public String parseAndIntegrate(String expr) {
    if (expr == null || expr.isEmpty()) return "Erro: expressao vazia.";
        String s = expr.trim();
        // find division at top level (depth 0)
        int depth = 0;
        int slashPos = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == '/' && depth == 0) { slashPos = i; break; }
        }
    if (slashPos == -1) return "Erro: expressao deve ser uma razao (numerador/denominador).";

        String left = s.substring(0, slashPos).trim();
        String right = s.substring(slashPos + 1).trim();
        left = stripOuterParens(left);
        right = stripOuterParens(right);

        List<Double> numer = parsePolynomial(left);
    if (numer == null) return "Erro: nao foi possivel parsear o numerador.";
    List<Double> denom = parsePolynomial(right);
    if (denom == null) return "Erro: nao foi possivel parsear o denominador.";

        return integrate(numer, denom);
    }

    private String stripOuterParens(String s) {
        String t = s.trim();
        while (t.length() >= 2 && t.startsWith("(") && t.endsWith(")")) {
            // check matching
            int depth = 0; boolean match = true;
            for (int i = 0; i < t.length(); i++) {
                char c = t.charAt(i);
                if (c == '(') depth++;
                else if (c == ')') {
                    depth--;
                    if (depth == 0 && i < t.length() - 1) { match = false; break; }
                }
            }
            if (match) t = t.substring(1, t.length() - 1).trim();
            else break;
        }
        return t;
    }

    /**
     * Parse polinômio simples como "5x^2-3x+1". Retorna lista de coeficientes index=potência.
     */
    private List<Double> parsePolynomial(String s) {
        if (s == null) return null;
        String t = s.replaceAll("\\s+", "");
        if (t.isEmpty()) return null;

        // Detect multiplication of factors like (x^2+1)(x^2+4) or 2(x^2+1)
        boolean hasParen = t.indexOf('(') >= 0;
        if (hasParen || t.indexOf('*') >= 0) {
            // split into factors
            List<String> factors = new ArrayList<>();
            int i = 0;
            while (i < t.length()) {
                char c = t.charAt(i);
                if (c == '*') { i++; continue; }
                if (c == '(') {
                    int depth = 0; int j = i;
                    for (; j < t.length(); j++) {
                        if (t.charAt(j) == '(') depth++;
                        else if (t.charAt(j) == ')') { depth--; if (depth == 0) break; }
                    }
                    if (j >= t.length()) return null; // unmatched
                    factors.add(t.substring(i, j + 1));
                    i = j + 1;
                } else {
                    // read until '*' or '('
                    int j = i;
                    while (j < t.length() && t.charAt(j) != '*' && t.charAt(j) != '(') j++;
                    factors.add(t.substring(i, j));
                    i = j;
                }
            }

            // multiply factors
            List<Double> acc = new ArrayList<>(); acc.add(1.0);
            for (String f : factors) {
                String fb = f;
                if (fb.startsWith("(") && fb.endsWith(")")) fb = stripOuterParens(fb);
                List<Double> poly = parsePolynomialSimple(fb);
                if (poly == null) return null;
                acc = multiplyPolynomials(acc, poly);
            }
            return acc;
        } else {
            return parsePolynomialSimple(t);
        }
    }

    private List<Double> parsePolynomialSimple(String t) {
        if (t == null) return null;
        String s = t;
        if (s.isEmpty()) return null;
        if (s.charAt(0) != '+' && s.charAt(0) != '-') s = "+" + s;
        List<String> terms = new ArrayList<>();
        int i = 0;
        for (int j = 1; j < s.length(); j++) {
            char c = s.charAt(j);
            if (c == '+' || c == '-') { terms.add(s.substring(i, j)); i = j; }
        }
        terms.add(s.substring(i));

        java.util.Map<Integer, Double> map = new java.util.HashMap<>();
        try {
            for (String term : terms) {
                if (term.equals("+") || term.equals("-")) continue;
                char signChar = term.charAt(0);
                int sign = signChar == '-' ? -1 : 1;
                String body = term.substring(1);
                if (body.isEmpty()) continue;
                if (body.contains("x")) {
                    int idx = body.indexOf('x');
                    String coeffStr = body.substring(0, idx);
                    double coeff = coeffStr.isEmpty() ? 1.0 : Double.parseDouble(coeffStr);
                    coeff = coeff * sign;
                    int power = 1;
                    if (idx + 1 < body.length() && body.charAt(idx + 1) == '^') {
                        String powStr = body.substring(idx + 2);
                        power = Integer.parseInt(powStr);
                    }
                    map.put(power, map.getOrDefault(power, 0.0) + coeff);
                } else {
                    double cval = Double.parseDouble(body) * sign;
                    map.put(0, map.getOrDefault(0, 0.0) + cval);
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }

        int maxPow = 0;
        for (int p : map.keySet()) if (p > maxPow) maxPow = p;
        List<Double> coeffs = new ArrayList<>();
        for (int k = 0; k <= maxPow; k++) coeffs.add(0.0);
        for (java.util.Map.Entry<Integer, Double> e : map.entrySet()) coeffs.set(e.getKey(), e.getValue());
        int deg = coeffs.size() - 1;
        while (deg > 0 && Math.abs(coeffs.get(deg)) < EPSILON) deg--;
        return new ArrayList<>(coeffs.subList(0, deg + 1));
    }

    private List<Double> multiplyPolynomials(List<Double> a, List<Double> b) {
        int da = a.size() - 1; int db = b.size() - 1;
        List<Double> r = new ArrayList<>();
        for (int i = 0; i <= da + db; i++) r.add(0.0);
        for (int i = 0; i <= da; i++) {
            for (int j = 0; j <= db; j++) {
                double v = r.get(i + j) + a.get(i) * b.get(j);
                r.set(i + j, v);
            }
        }
        int deg = r.size() - 1; while (deg > 0 && Math.abs(r.get(deg)) < EPSILON) deg--;
        return new ArrayList<>(r.subList(0, deg + 1));
    }

    private boolean isZeroPoly(List<Double> coeffs) {
        if (coeffs == null) return true;
        for (double v : coeffs) if (Math.abs(v) > EPSILON) return false;
        return true;
    }

    private String integratePolynomial(Polynomial p) {
        // ∫ (sum c_i x^i) dx = sum c_i/(i+1) x^{i+1}
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = p.getDegree(); i >= 0; i--) {
            double coeff = p.getCoefficient(i);
            if (Math.abs(coeff) < EPSILON) continue;
            double integCoeff = coeff / (i + 1.0);
            String term;
            int newPower = i + 1;
            if (newPower == 1) {
                term = String.format("%s x", formatNumber(integCoeff));
            } else {
                term = String.format("%s x^%d", formatNumber(integCoeff), newPower);
            }
            if (first) {
                sb.append(term);
                first = false;
            } else {
                if (term.startsWith("-")) sb.append(" - ").append(term.substring(1));
                else sb.append(" + ").append(term);
            }
        }
        if (first) return "0";
        return sb.toString();
    }

    private static class DivisionResult {
        List<Double> quotient;
        List<Double> remainder;
        DivisionResult(List<Double> q, List<Double> r) { this.quotient = q; this.remainder = r; }
    }

    /**
     * Divisão polinomial simples (numer / denom). Coeficiente list: index = power.
     */
    private DivisionResult polynomialDivide(List<Double> numer, List<Double> denom) {
        int degN = numer.size() - 1;
        int degD = denom.size() - 1;
        List<Double> n = new ArrayList<>(numer);
        // ensure size
        while (n.size() < degN + 1) n.add(0.0);
        List<Double> q = new ArrayList<>();
        for (int i = 0; i <= Math.max(0, degN - degD); i++) q.add(0.0);

            for (int i = degN; i >= degD; i--) {
            double coeffN = (i < n.size()) ? n.get(i) : 0.0;
            double coeffD = denom.get(degD);
            double factor = coeffD == 0 ? 0.0 : coeffN / coeffD;
            int qIndex = i - degD;
            q.set(qIndex, factor);
            // subtract factor * denom shifted
            for (int j = 0; j <= degD; j++) {
                int idx = qIndex + j;
                double val = n.get(idx) - factor * denom.get(j);
                n.set(idx, val);
            }
        }

        // Remainder: coefficients 0..degD-1
        List<Double> rem = new ArrayList<>();
        for (int i = 0; i < Math.min(n.size(), degD); i++) rem.add(n.get(i));
        // Normalize sizes
        return new DivisionResult(trimTrailingZeros(q), trimTrailingZeros(rem));
    }

    private List<Double> trimTrailingZeros(List<Double> v) {
        int deg = v.size() - 1;
        while (deg > 0 && Math.abs(v.get(deg)) < EPSILON) deg--;
        return new ArrayList<>(v.subList(0, deg + 1));
    }
    public static void main(String[] args) {
        SymbolicIntegrator integrator = new SymbolicIntegrator();

        // --- Teste do Problema 1 ---
        // integral( (5x - 3) / (x^2 - 2x - 3) ) dx = integral( (5x-3) / (x-3)(x+1) ) dx
        // A=5, B=-3, x1=3, x2=-1
    System.out.println("### TESTE 1 (Problema 1: Raizes Reais) ###");
        String result1 = integrator.solveCase1(5, -3, 3, -1);
        System.out.println(result1);
        // Resultado esperado: 3.00 * ln|x - 3.00| + 2.00 * ln|x - -1.00| + K

        System.out.println("\n");

        // --- Teste do Problema 2 (Delta > 0) ---
        // Mesmo problema de cima, mas alimentado como Caso 2
        // integral( (5x - 3) / (1x^2 - 2x - 3) ) dx
    System.out.println("### TESTE 2 (Problema 2: Delta > 0) ###");
        String result2a = integrator.solveCase2(5, -3, 1, -2, -3);
        System.out.println(result2a);

        System.out.println("\n");

        // --- Teste do Problema 2 (Delta = 0) ---
        // integral( (2x + 5) / (x^2 - 4x + 4) ) dx = integral( (2x+5) / (x-2)^2 ) dx
        // A=2, B=5, a=1, b=-4, c=4
    System.out.println("### TESTE 3 (Problema 2: Delta = 0) ###");
        String result2b = integrator.solveCase2(2, 5, 1, -4, 4);
        System.out.println(result2b);
        // Esperado: (1/1) * [ 2*ln|x-2| - 9/(x-2) ] + K

        System.out.println("\n");
        
        // --- Teste do Problema 2 (Delta < 0) ---
        // integral( (3x + 2) / (x^2 + 2x + 5) ) dx
        // a=1, b=2, c=5. Delta = 4 - 20 = -16
    System.out.println("### TESTE 4 (Problema 2: Delta < 0) ###");
        String result2c = integrator.solveCase2(3, 2, 1, 2, 5);
        System.out.println(result2c);
        // Esperado: 1.5*ln|x^2+2x+5| - 0.5*arctan((x+1)/2) + K

        System.out.println("\n");

        // --- Teste do Problema 3 ---
        // integral( (2x + 1) / ((x^2+1)(x^2+4)) ) dx
        // A=2, B=1, c1=1, c2=4
    System.out.println("### TESTE 5 (Problema 3: Quadratico Duplo) ###");
        String result3 = integrator.solveCase3(2, 1, 1, 4);
        System.out.println(result3);
        // Esperado: ( (-2/3)x + (-1/3) )/(x^2+1) + ( (2/3)x + (1/3) )/(x^2+4)
        // Integral: (-0.33*ln|x^2+1| - 0.33*arctan(x/1)) + (0.33*ln|x^2+4| + 0.17*arctan(x/2)) + K

    System.out.println("\n### TESTE 6 (Rotina automatica integrate) ###");
    // Caso equivalente ao TESTE 1 mas via integrate (numerator: 5x-3 -> [-3,5], denom: x^2-2x-3 -> [-3,-2,1])
    java.util.List<Double> num1 = java.util.List.of(-3.0, 5.0);
    java.util.List<Double> den1 = java.util.List.of(-3.0, -2.0, 1.0);
    String auto1 = integrator.integrate(num1, den1);
    System.out.println(auto1);

    // Caso equivalente ao TESTE 5 ( (2x+1)/((x^2+1)(x^2+4)) )
    java.util.List<Double> num2 = java.util.List.of(1.0, 2.0);
    // denom: (x^2+1)(x^2+4) = x^4 + 5x^2 + 4 -> coeffs [4,0,5,0,1]
    java.util.List<Double> den2 = java.util.List.of(4.0, 0.0, 5.0, 0.0, 1.0);
    String auto2 = integrator.integrate(num2, den2);
    System.out.println(auto2);

    System.out.println("\n### TESTE 7 (Parser textual parseAndIntegrate) ###");
    String parsed1 = integrator.parseAndIntegrate("(5x-3)/(x^2-2x-3)");
    System.out.println(parsed1);

    String parsed2 = integrator.parseAndIntegrate("(2x+1)/((x^2+1)(x^2+4))");
    System.out.println(parsed2);
    }
}