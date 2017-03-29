package redeneural;

import java.util.Random;
import java.util.Scanner;

public class Main {

    double bias = -1;
    double eta = 0.3;
    double deltaEta = 0.000005;
    double erroMaximoAceitavel = 0.00001;
    int maxEpocas = 100000;
    boolean wRandon = true;

    double entradas[][] = {{1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1}, //A
                           {0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1}, //A
                           {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1}, //E
                           {1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1}, //E
                           {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1},//I
                           {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}};//I
    
    double yDs[][] = {{1, 0, 0}, //A
                      {1, 0, 0}, //A
                      {0, 1, 0}, //E
                      {0, 1, 0}, //E
                      {0, 0, 1}, //I
                      {0, 0, 1}};//I
    
    char letras[] = {'A', 'E', 'I'};
    
    double ws[][] = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    
    double wBias[] = {0, 0, 0};

    private void executar() {
        treinar();
        classificar();
    }

    private void classificar() {
        Scanner sc = new Scanner(System.in);
        double entrada[] = new double[ws[0].length];
        //Ler entradas
        System.out.println("Entre com as " + entrada.length + " entradas:");
        for (int i = 0; i < entrada.length; i++) {
            entrada[i] = Double.parseDouble(sc.nextLine());
        }
        
        for(int i = 0; i < yDs[0].length; i++){
            // Calcular o v
            double v = (wBias[i] * bias);
            for (int j = 0; j < entrada.length; j++) {
                v += ws[i][j] * entrada[j];
            }
            // Calcular o y
            double y = calcularY(v);
            if(y == 1){
                System.out.println("Resultado: Letra "+letras[i]);
            }
        }
    }

    private void treinar() {
        double erroQuadratico = 1; // Erro da Época
        int epoca = 0;
        // Inicializando pesos
        if (wRandon) {
            Random r = new Random();
            ws = new double[yDs[0].length][entradas[0].length];
            for (int i = 0; i < ws.length; i++) {
                for (int j = 0; j < ws[i].length; j++) {
                    ws[i][j] = r.nextDouble();
                    if (r.nextInt(2) != 0) {
                        ws[i][j] *= -1;
                    }
                }
            }
            
            wBias = new double[yDs[0].length];
            for (int i = 0; i < ws.length; i++) {
                wBias[i] = r.nextDouble();
                if (r.nextInt(2) != 0) {
                    wBias[i] *= -1;
                }
            }
        }
        // Treinamento
        
        while (erroQuadratico > erroMaximoAceitavel && epoca < maxEpocas) {
            
            erroQuadratico = 0;
            System.out.println("Época: " + (epoca + 1));
            System.out.println("Eta: " + eta);
           // Epoca
            // Iteração
            for (int i = 0; i < entradas.length; i++) {
                
                //Neuronio
                for(int n = 0; n < yDs[0].length; n++){
                    
                    // Calcular o v
                    double v = (wBias[n] * bias);
                    for (int j = 0; j < entradas[i].length; j++) {
                        v += ws[n][j] * entradas[i][j];
                    }
                    // Calcular o y
                    double y = calcularY(v);
                    // Calcular o erro do instante, é calculado no instante que se está apresentando uma instancia para o neuronio
                    double erroInstantaneo = yDs[i][n] - y;
                    // Ajustar os pesos
                    wBias[n] += (bias * erroInstantaneo * eta);
                    for (int j = 0; j < entradas[i].length; j++) {
                        ws[n][j] += (entradas[i][j] * erroInstantaneo * eta);
                    }
                    // Incrementa o erro da epoca
                    erroQuadratico += (erroInstantaneo * erroInstantaneo);

//                    for (int j = 0; j < entradas[i].length; j++) {
//                        System.out.print(entradas[i][j] + " | ");
//                    }
                    System.out.println(letras[n]);
                    System.out.print(yDs[i][n] + " -> ");
                    System.out.print(y + " (" + erroInstantaneo + ")");
                    System.out.println("");
                    
                }
                System.out.println("");
            }
            erroQuadratico = erroQuadratico / (entradas.length * yDs[0].length);
            System.out.println("Erro Quadratico: " + erroQuadratico);
            epoca++;
            eta *= (1 - deltaEta);
        }
    }

    public static void main(String[] args) {
        Main obj = new Main();
        obj.executar();

    }

    // Saidas:0 ou 1
    private double funcaoDegrau(double v) {
        double y = 0;
        if (v >= 0) {
            y = 1;
        }
        return y;
    }

    // Saidas entre -1 e 1
    private double funcaoTanHiperbolica(double v) {
        return StrictMath.tanh(v);
    }

    // Saidas entre 0 e 1
    private double funcaoSigmoidal(double v) {
        double beta = 0.5;
        double e = Math.E;
        return 1 / (1 + Math.pow(e, -(beta) * v));
    }
    
    private double calcularY(double v){
        return funcaoDegrau(v);
    }

}
