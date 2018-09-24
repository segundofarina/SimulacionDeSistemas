% plotter for images
Beeman3=importdata('Beeman-dt:0.001-tf:5.0.txt');
Beeman2=importdata('Beeman-dt:0.01-tf:5.0.txt');
Beeman1=importdata('Beeman-dt:0.1-tf:5.0.txt');
Beeman4=importdata('Beeman-dt:1.0E-4-tf:5.0.txt');
Beeman5=importdata('Beeman-dt:1.0E-5-tf:5.0.txt');
Beeman6=importdata('Beeman-dt:1.0E-6-tf:5.0.txt');
GearPredictor3=importdata('GearPredictor-dt:0.001-tf:5.0.txt');
GearPredictor2=importdata('GearPredictor-dt:0.01-tf:5.0.txt');
GearPredictor1=importdata('GearPredictor-dt:0.1-tf:5.0.txt');
GearPredictor4=importdata('GearPredictor-dt:1.0E-4-tf:5.0.txt');
GearPredictor5=importdata('GearPredictor-dt:1.0E-5-tf:5.0.txt');
GearPredictor6=importdata('GearPredictor-dt:1.0E-6-tf:5.0.txt');
Real3=importdata('Real-dt:0.001-tf:5.0.txt');
Real2=importdata('Real-dt:0.01-tf:5.0.txt');
Real1=importdata('Real-dt:0.1-tf:5.0.txt');
Real4=importdata('Real-dt:1.0E-4-tf:5.0.txt');
Real5=importdata('Real-dt:1.0E-5-tf:5.0.txt');
Real6=importdata('Real-dt:1.0E-6-tf:5.0.txt');
Verlet3=importdata('Verlet-dt:0.001-tf:5.0.txt');
Verlet2=importdata('Verlet-dt:0.01-tf:5.0.txt');
Verlet1=importdata('Verlet-dt:0.1-tf:5.0.txt');
Verlet4=importdata('Verlet-dt:1.0E-4-tf:5.0.txt');
Verlet5=importdata('Verlet-dt:1.0E-5-tf:5.0.txt');
Verlet6=importdata('Verlet-dt:1.0E-6-tf:5.0.txt');

T1=0:1E-1:5;
T2=0:1E-2:5;
T3=0:1E-3:5;
T4=0:1E-4:5;
T5=0:1E-5:5;
T6=0:1E-6:5;
T5=T5(1,1:500000);

figure(1);
plot(T1,Real1,'r',T1,GearPredictor1,'b',T1,Verlet1,'g',T1,Beeman1,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-1.png';

figure(2);
plot(T2,Real2,'r',T2,GearPredictor2,'b',T2,Verlet2,'g',T2,Beeman2,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-2.png';

figure(3);
plot(T3,Real3,'r',T3,GearPredictor3,'b',T3,Verlet3,'g',T3,Beeman3,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-3.png';

figure(4);
plot(T4,Real4,'r',T4,GearPredictor4,'b',T4,Verlet4,'g',T4,Beeman4,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-4.png';

figure(5);
plot(T5,Real5,'r',T5,GearPredictor5,'b',T5,Verlet5,'g',T5,Beeman5,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-5.png';

figure(6);
plot(T6,Real6,'r',T6,GearPredictor6,'b',T6,Verlet6,'g',T6,Beeman6,'k');
xlabel("Tiempo [s]");
ylabel("Posicion [m]");
legend('Real','GearPredictor','Verlet','Beeman');
print '1E-6.png';


EcBeeman = [ecm(Beeman1,Real1),ecm(Beeman2,Real2),ecm(Beeman3,Real3),ecm(Beeman4,Real4),ecm(Beeman5,Real5),ecm(Beeman6,Real6)];
EcGearPredictor = [ecm(GearPredictor1,Real1),ecm(GearPredictor2,Real2),ecm(GearPredictor3,Real3),ecm(GearPredictor4,Real4),ecm(GearPredictor5,Real5),ecm(GearPredictor6,Real6)];
EcVerlet = [ecm(Verlet1,Verlet1),ecm(Verlet2,Verlet2),ecm(Verlet3,Verlet3),ecm(Verlet4,Verlet4),ecm(Verlet5,Verlet5),ecm(Verlet6,Verlet6)];
EcReal = [ecm(Real1,Real1),ecm(Real2,Real2),ecm(Real3,Real3),ecm(Real4,Real4),ecm(Real5,Real5),ecm(Real6,Real6)];





