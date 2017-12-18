package etp;
import static java.lang.Math.pow;
import static java.lang.Math.abs;

public class SensibleHeatTransferCoefficient {

	double criticalReynoldsNumber = 3000; 	//fixed
	double prandtlNumber = 0.71; 			// fixed
	
	public double computeConvectiveTransferCoefficient (double airTemperature, double windVelocity, double leafLength) { 
		//double criticalReynoldsNumber = this.criticalReynoldsNumber;
		// Formula from Monteith & Unsworth, 2007
		double thermalConductivity = (6.84 * pow(10,-5)) * airTemperature + 5.62 * pow(10,-3); 
		// Formula from Monteith & Unsworth, 2007
		double kinematicViscosity = (9 * pow(10,-8)) * airTemperature - 1.13 * pow(10,-5);
		double reynoldsNumber = (windVelocity * leafLength)/ kinematicViscosity;
		// from Incropera et al, 2006
		double c3 = criticalReynoldsNumber - reynoldsNumber;
		double c2 = (reynoldsNumber + criticalReynoldsNumber - abs(c3))/2;
		double c1 = 0.037 * pow(c2,4/5) - 0.664 * pow(c2,1/2);
		double nusseltNumber = (0.037 * pow(reynoldsNumber,4/5) - c1) * pow(prandtlNumber,1/3);
		// Formula from Schymanski and Or, 2017
		double convectiveTransferCoefficient = (thermalConductivity * nusseltNumber)/leafLength;
		return convectiveTransferCoefficient;
		}
	public double computeSensibleHeatTransferCoefficient(double convectiveTransferCoefficient, int leafSide) {
		double sensibleHeatTransferCoefficient = convectiveTransferCoefficient * leafSide;
		return sensibleHeatTransferCoefficient;
	}
}