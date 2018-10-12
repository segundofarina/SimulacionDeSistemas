times15 = importdata('0,15/MediosGranulares_D_0.15_time_stabilized.txt' );
times20 = importdata('0,20/MediosGranulares_D_0.2_time_stabilized.txt' );
times25 = importdata('0,25/MediosGranulares_D_0.25_time_stabilized.txt' );
times30 = importdata('0,30/MediosGranulares_D_0.3_time_stabilized.txt' );

apert=(0.15:0.05:0.3);
error=[6.01, 7.02, 9.049,22.649];
caudal=[353.35, 540.95, 775.98,1070.6];

errorbar(apert,caudal,apert);
xlabel("Apertura [s]");
ylabel("Caudal [particulas/seg]");