package ar.edu.itba.ss;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}


/*  mass    = 70 kg
    k       = 10000 N/m
    gamma   = 100 kg/s
    finaltime = 5s

    ---- Condiciones iniciales -----
    r(t=0) = 1 m
    v(t=0) = -C *gamma / 2

    -- Solucion de la ecuacion ----
    A * exp(-(gamma/(2*mass))*t) * cos( (k/m - gamma^2/(4*m^2) ) ^0.5 * t)
 */