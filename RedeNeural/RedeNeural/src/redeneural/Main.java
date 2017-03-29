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


    double entradas[][] = {{1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1},
                           {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1}, 
                           {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}, 
                           {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1}};
    
    double yDs[][] = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
    
    double ws[][] = {{0.2, 0.3, 0.1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1}, 
                     {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1}, 
                     {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0}, 
                     {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1}};
    
    double wBias[] = {0.2, 0.5, 0.3, 0.4};

    private void executar() {
        treinar();
        classificar();
    }

    private void classificar() {
 
    }

    private void treinar() {
        double erroQuadratico = 1; // Erro da Época
        int epoca = 0;
        // Inicializando pesos
        if (wRandon) {
            Random r = new Random();
            ws = new double[entradas.length][entradas[0].length];
            for (int j = 0; j < ws.length; j++) {
                for(int k = 0; k < ws[j].length; k++){
                    ws[j][k] = r.nextDouble();
                    if (r.nextInt(2) != 0) {
                        ws[j][k] *= -1;
                    }
                }
            }
            for(int j = 0; j < wBias.length; j++){
                wBias[j] = r.nextDouble();
                if (r.nextInt(2) != 0) {
                    wBias[j] *= -1;
                }
            }
        }
        // Treinamento
        while (erroQuadratico > erroMaximoAceitavel && epoca < maxEpocas) {
            // Epoca
            erroQuadratico = 0;
            System.out.println("Época: " + (epoca + 1));
            System.out.println("Eta: " + eta);
            for (int i = 0; i < entradas.length; i++) {
                double v[] = new double[yDs.length];
                double y[] = new double[yDs.length];
                double erroInstantaneo[] = new double[yDs.length];
                for(int j = 0; j < yDs.length; j++){
                    // Iteração
                    // Calcular o v
                    
                    v[j] = (wBias[i] * bias);
                    for (int k = 0; k < entradas[i].length; k++) {
                        v[j] += ws[i][k] * entradas[i][k];
                    }
                    // Calcular o y
                    y[j] = calcularY(v[j]);
                    
                    
                    // Calcular o erro da iteração
                    erroInstantaneo[j] = yDs[i][j] - y[j];
                    // Ajustar os pesos
                    wBias[i] += (bias * erroInstantaneo[j] * eta);
                    for (int k = 0; k < entradas[i].length; k++) {
                        ws[i][k] += (entradas[i][k] * erroInstantaneo[j] * eta);
                    }
                    // Incrementa o erro da epoca
                    erroQuadratico += (erroInstantaneo[j] * erroInstantaneo[j]);
                    //ISSO AQUI VAI DENTRO DO FOR VARIANDO O I DO YDS

                    for (int k = 0; k < entradas[i].length; k++) {
                        System.out.print(entradas[i][k] + " | ");
                    }
                    System.out.print(yDs[i][j] + " -> ");
                    System.out.print(y[j] + " (" + erroInstantaneo[j] + ")");
                    System.out.println("");
                }
            }
            erroQuadratico = erroQuadratico / entradas.length;
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
