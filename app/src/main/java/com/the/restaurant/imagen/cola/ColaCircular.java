package com.the.restaurant.imagen.cola;

/**
 * Created by choqu_000 on 24/12/2015.
 */
public class ColaCircular {

    public static class ClaseColaCircular { // Declaracion de la clase de la Cola Circular
        static int max=5; // Tamano maximo de la Cola Circular
        static int colacirc[]=new int[max]; // Declaracion del arreglo para almacenar la Cola Circular
        static int frente, fin; // Inidicadores del inicio y final de la Cola Circular

        public ClaseColaCircular() { // Constructor que inicializa el frente y el final de la Cola Circular
            frente=-1;    fin=-1;
            //System.out.println("Cola circular inicializada !!!");
        }

        public static void Mostrar() {
            int i=0;
            //System.out.println("\n\n<<< MOSTRAR COLA CIRCULAR >>>");
            if(frente==-1) {
                //System.out.println("\nCola Circular vacia !!!");
            }
            else {
                i=frente;
                do {
                    //System.out.println("colacircssss["+i+"]="+colacirc[i]);
                    i++;
                    if(i==max && frente>fin) i=0; // Reiniciar en cero (dar la vuelta)
                }while(i!=fin+1);
            }

            //System.out.println("frente="+frente);
            //System.out.println("fin="+fin);


            //System.out.println("max="+max);
        }

        public static void Insertar(int dato) {
            if((fin==max-1 && frente==0) || (fin+1==frente)) {
                //System.out.println("\nCola Circular llena !!!");
                return;
            }
            if(fin==max-1 && frente!=0) fin=0; else fin++;
            colacirc[fin]=dato;
            if(frente==-1) frente=0;
        }

        public static void Eliminar() {
            //System.out.println("\n\n<<< ELIMINAR DATO >>>");

            if(frente==-1) {
                //System.out.println("Cola Circular vacia !!!");
                return;
            }
            //System.out.println("Dato eliminado = "+colacirc[frente]);//elimina en el vector
            if(frente==fin) {                                    //el primer fuente
                frente=-1;   fin=-1; //si frente igual a fin vacios mostrar -1
                return;
            }
            if(frente==max) frente=0; else frente++;
        }
    }





}