energy = importdata ('MediosGranulares_D_0.2_energy2018-10-09T19:24:29.112.txt' );
dt = 0.1 * sqrt(0.01/10^5);
final = 100000/dt;
X = (0:dt:final-1*dt);
plot(X, energy);