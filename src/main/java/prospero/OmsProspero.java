package prospero;

import static java.lang.Math.pow;
//import static org.jgrasstools.gears.libs.modules.JGTConstants.isNovalue;
//import static org.jgrasstools.gears.libs.modules.JGTConstants.isNovalue;



import java.util.HashMap;

import java.util.Map.Entry;
import java.util.Set;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;
import oms3.annotations.Unit;
import prosperoClasses.*;


import org.geotools.feature.SchemaException;
import org.jgrasstools.gears.libs.modules.JGTModel;

import org.jgrasstools.hortonmachine.i18n.HortonMessageHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Description("The Prospero model")
@Author(name = "Michele Bottazzi", contact = "michele.bottazzi@gmail.com")
@Keywords("Evapotranspiration")
@Label("")
@Name("")
@Status(Status.CERTIFIED)
@License("General Public License Version 3 (GPLv3)")
public class OmsProspero extends JGTModel implements Parameters {
	
	/////////////////////////////////////////////
	// ENVIRONMENTAL VARIABLES - INPUT
	/////////////////////////////////////////////

	@Description("Air temperature.")
	@In
	@Unit("K")
	public HashMap<Integer, double[]> inAirTemperature;
	
	@Description("The wind speed.")
	@In
	@Unit("m s-1")
	public HashMap<Integer, double[]> inWindVelocity;
	
	@Description("The air relative humidity.")
	@In
	@Unit("%")
	public HashMap<Integer, double[]> inRelativeHumidity;
	
	@Description("The short wave radiation at the surface.")
	@In
	@Unit("W m-2")
	public HashMap<Integer, double[]> inShortWaveRadiationDirect;
	
	@Description("The short wave radiation at the surface.")
	@In
	@Unit("W m-2")
	public HashMap<Integer, double[]> inShortWaveRadiationDiffuse;
	
	@Description("The long wave radiation at the surface.")
	@In
	@Unit("W m-2")
	public HashMap<Integer, double[]> inLongWaveRadiation;
	
	@Description("The Net long wave radiation at the surface.")
	@In
	@Unit("W m-2")
	public HashMap<Integer, double[]> inNetLongWaveRadiation;

	@Description("The atmospheric pressure.")
	@In
	@Unit("Pa")
	public HashMap<Integer, double[]> inAtmosphericPressure;

	@Description("The soilflux.")
	@In
	@Unit("W m-2")
	public HashMap<Integer, double[]> inSoilFlux;
	
	@Description("Leaf area index.")
	@In
	@Unit("m2 m-2")
	public HashMap<Integer, double[]> inLeafAreaIndex;
	
	@Description("Input soil moisture.")
	@In
	@Unit("m3 m-3")
	public HashMap<Integer, double[]> inSoilMoisture;
	
	/////////////////////////////////////////////
	// ENVIRONMENTAL VARIABLES - DEFAULT
	/////////////////////////////////////////////

	@Description("The air temperature default value in case of missing data.")
	@In
	@Unit("K")
	public double defaultAirTemperature = 15.0+273.0;
	  
	@Description("The wind default value in case of missing data.")
	@In
	@Unit("m s-1")
	public double defaultWindVelocity = 0.5;
	
	@Description("The humidity default value in case of missing data.")
	@In
	@Unit("%")
	public double defaultRelativeHumidity = 70.0;
		
	@Description("The short wave radiation default value in case of missing data.")
	@In
	@Unit("W m-2")
	public double defaultShortWaveRadiationDirect = 0.0;
	
	@Description("The atmospheric pressure default value in case of missing data.")
	@In
	@Unit("Pa")
	public double defaultAtmosphericPressure = 101325.0;
	
	@Description("The soilflux default value in case of missing data.")
	@In
	@Unit("W m-2")
	public double defaultSoilFlux = 0.0;
	
	@Description("The leaf area index default value in case of missing data.")
	@In
	@Unit("m2 m-2")
	public double defaultLeafAreaIndex = 1.0;
		
	@Description("Default soil moisture.")
	@In
	@Unit("m3 m-3")
	public double defaultSoilMoisture = 0.20;
	
	@Description("The short wave radiation highlighting the canopy in sunlight.")
	@In
	@Unit("W m-2")
	public double shortWaveRadiationInSun;
	
	@Description("The short wave radiation highlighting the canopy in shadow.")
	@In
	@Unit("W m-2")
	public double shortWaveRadiationInShadow;
		
	/////////////////////////////////////////////
	// GEOGRAPHIC VARIABLES - DEFAULT
	/////////////////////////////////////////////
	@Description("The elevation of the centroid.")
	@In
	@Unit("m")
	public String centroidElevation;
	

	@In public double elevation;
	@In public double latitude;
	@In public double longitude;
	@In public double canopyHeight;
	
	@In	public double alpha;
	@In public double theta;
	@In public double VPD0;

	@In	public double T0;
	@In public double Tl;
	@In public double Th;
	
	@In public double waterFieldCapacity;
	@In public double waterWiltingPoint;
	@In public double rootsDepth;
	@In public double depletionFraction;
		
	@Description(" The vetor containing the id of the station")
	Object []ID;
		@Description("Type of transpiring area")
	@In
	public String typeOfTerrainCover;

	@Description("The first day of the simulation.")
	@In
	public String tStartDate;
	public DateTime date;
	
	@Description("The first day of the simulation.")
	@In
	public int temporalStep;
	@Description("It is needed to iterate on the date")
	int step;
	
	
	double nullValue = -9999.0;
	public int time;
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").withZone(DateTimeZone.UTC);
	
	
	/////////////////////////////////////////////
	// ECOLOGICAL VARIABLES - INPUT
	/////////////////////////////////////////////
	
	/////////////////////////////////////////////
	// ECOLOGICAL VARIABLES - DEFAULT
	/////////////////////////////////////////////
	
	/////////////////////////////////////////////
	// OUTPUT
	/////////////////////////////////////////////
	
	
	@Description("The latent heat.")
	@Unit("mm h-1")
	@Out
	public double totalTranspiration; 
	
	@Description("The latent heat.")
	@Unit("mm h-1")
	@Out
	public HashMap<Integer, double[]> outLatentHeat;
	
	@Description("The latent heat.")
	@Unit("mm h-1")
	@Out
	public HashMap<Integer, double[]> outLatentHeatShade;
	
	@Description("The transpirated water.")
	@Unit("mm h-1")
	@Out
	public HashMap<Integer, double[]> outTranspiration;
	
	@Description("The sensible heat.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outSensibleHeat;
	
	@Description("The sensible heat.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outSensibleHeatShade;
	
	@Description("The leaf Temperature.")
	@Unit("K")
	@Out
	public HashMap<Integer, double[]> outLeafTemperature;
	
	@Description("The leaf Temperature.")
	@Unit("K")
	@Out
	public HashMap<Integer, double[]> outLeafTemperatureShade;
	
	@Description("The solar radiation absorbed by the sunlit canopy.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outRadiation;
	
	@Description("The solar radiation absorbed by the shaded canopy.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outRadiationShade;
	
	@Description("The solar radiation absorbed by the shaded canopy.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outRadiationSoil;
	
	@Description("Fraction of highlighted canopy.")
	@Unit("-")
	@Out
	public HashMap<Integer, double[]> outCanopy;
	
	@Description("Evaporation from soil.")
	@Unit("W m-2")
	@Out
	public HashMap<Integer, double[]> outEvaporation;
	
	/////////////////////////////////////////////
	// OTHERS - DO
	/////////////////////////////////////////////
	@In
	public HashMap<Integer, double[]> inStressSun;
	//public double inStressSun;
	@In
	public HashMap<Integer, double[]> inStressShade;

	@In
	public double defaultStress;
		
	@Description("Switch that defines if it is hourly.")
	@In
	public boolean doHourly = true;
	
	@Description("Define the closure of energy budget: single or full iteration.")
	@In
	public boolean doIterative = true;
	
	@Description("Choose if you want to print only the latent heat or all the other outputs.")
	@In
	public boolean doFullPrint = true;
	
	@In
	public boolean useTemperatureStress = true;
	@In
	public boolean useVDPStress = true;
	@In
	public boolean useWaterStress = true;
	@In
	public String typeOfCanopy;

	
	@In
	String printo;
	// METHODS FROM CLASSES		
	SensibleHeatMethods sensibleHeat 	= new SensibleHeatMethods();
	LatentHeatMethods latentHeat 		= new LatentHeatMethods();
	PressureMethods pressure 			= new PressureMethods(); 
	RadiationMethod radiationMethods 	= new RadiationMethod();
	SolarGeometry solarGeometry 		= new SolarGeometry();
	EnvironmentalStress environmentalStress	= new EnvironmentalStress();
	

	
	
	@Execute
	public void process() throws Exception {
		if (doHourly == true) {
			time =temporalStep*60;

			} else {
			time = 86400;
			}
		DateTime startDateTime = formatter.parseDateTime(tStartDate);
		DateTime date=(doHourly==false)?startDateTime.plusDays(step).plusHours(12):startDateTime.plusMinutes(temporalStep*step);
		latitude = Math.toRadians(latitude);

		Leaf propertyOfLeaf = new Leaf();
		double poreRadius = propertyOfLeaf.poreRadius;
		double poreArea = propertyOfLeaf.poreArea;
		double poreDepth = propertyOfLeaf.poreDepth;
		double poreDensity = propertyOfLeaf.poreDensity;
		double leafLength = propertyOfLeaf.length;
		int leafSide = propertyOfLeaf.side;
		int leafStomaSide = propertyOfLeaf.stomaSide;
		double longWaveEmittance = propertyOfLeaf.longWaveEmittance;
		
		outLatentHeatShade 	= new HashMap<Integer, double[]>();
		outLatentHeat		= new HashMap<Integer, double[]>();
		outTranspiration 	= new HashMap<Integer, double[]>();
		if (doFullPrint == true) {
			outLeafTemperature 		= new HashMap<Integer, double[]>();
			outRadiation 			= new HashMap<Integer, double[]>();
			outRadiationShade 		= new HashMap<Integer, double[]>();
			outSensibleHeat 		= new HashMap<Integer, double[]>();
			outSensibleHeatShade 	= new HashMap<Integer, double[]>();
			outRadiationShade 		= new HashMap<Integer, double[]>();
			outRadiationSoil 		= new HashMap<Integer, double[]>();
			outLeafTemperatureShade	= new HashMap<Integer, double[]>();
			outCanopy 				= new HashMap<Integer, double[]>();
			outEvaporation			= new HashMap<Integer, double[]>();
			}
		
		Set<Entry<Integer, double[]>> entrySet = inAirTemperature.entrySet();
		for( Entry<Integer, double[]> entry : entrySet ) {
			Integer ID = entry.getKey();
			
		//	Coordinate coordinate = (Coordinate) stationCoordinates.get(idIterator.next());
		////	Point [] idPoint=getPoint(coordinate,sourceCRS, targetCRS);
		//	elevation = coordinate.z;
		//	longitude = (idPoint[0].getX());
		//	latitude = Math.toRadians(idPoint[0].getY());
		
			//double solarElevationAngle = solarGeometry.getSolarElevationAngle(date, latitude,longitude, doHourly);
			
			/////////////////////////////////////////////
			// INPUT READER
			/////////////////////////////////////////////
				
			double airTemperature = inAirTemperature.get(ID)[0]+273.0;
			if (airTemperature == (nullValue+273.0)) {airTemperature = nullValue;}//defaultAirTemperature;}		
			double leafTemperatureSun = airTemperature;
			double leafTemperatureShade = airTemperature;
			/*double leafTemperatureSoil = airTemperature;*/
			
			double leafAreaIndex = defaultLeafAreaIndex;
			if (inLeafAreaIndex != null)				
				leafAreaIndex = inLeafAreaIndex.get(ID)[0];
			if (leafAreaIndex == nullValue) {leafAreaIndex = defaultLeafAreaIndex;}
			
			
			
										
				double shortWaveRadiationDirect = inShortWaveRadiationDirect.get(ID)[0];
				if (shortWaveRadiationDirect == nullValue) {shortWaveRadiationDirect = defaultShortWaveRadiationDirect;}
			//	shortWaveRadiationDirect = ((shortWaveRadiationDirect>5)?shortWaveRadiationDirect:0);

				double shortWaveRadiationDiffuse = inShortWaveRadiationDiffuse.get(ID)[0];
				if (shortWaveRadiationDiffuse == nullValue) {shortWaveRadiationDiffuse = 0.159*shortWaveRadiationDirect;} 						
				
				double longWaveRadiation = inLongWaveRadiation.get(ID)[0];
				if (longWaveRadiation == nullValue) {longWaveRadiation = longWaveEmittance * stefanBoltzmannConstant * pow (airTemperature, 4);}//defaultLongWaveRadiation;}	
				longWaveRadiation = longWaveEmittance * stefanBoltzmannConstant * pow (airTemperature, 4);
				
				double netLongWaveRadiation = inNetLongWaveRadiation.get(ID)[0];
				if (netLongWaveRadiation == nullValue) {netLongWaveRadiation = 0;}//defaultLongWaveRadiation;}	
				//longWaveRadiation = longWaveEmittance * stefanBoltzmannConstant * pow (airTemperature, 4);
				
				double windVelocity = defaultWindVelocity;
				if (inWindVelocity != null){windVelocity = inWindVelocity.get(ID)[0];}
				if (windVelocity == nullValue) {windVelocity = defaultWindVelocity;}
				if (windVelocity == 0) {windVelocity = defaultWindVelocity;}			
				
				double atmosphericPressure = 101325;
				if (inAtmosphericPressure != null){atmosphericPressure = inAtmosphericPressure.get(ID)[0];}
				if (atmosphericPressure == nullValue) {atmosphericPressure = pressure.computePressure(defaultAtmosphericPressure, massAirMolecule, gravityConstant, elevation,boltzmannConstant, airTemperature);;}			
				
				double relativeHumidity = defaultRelativeHumidity;
				if (inRelativeHumidity != null){relativeHumidity = inRelativeHumidity.get(ID)[0];}
				if (relativeHumidity == nullValue) {relativeHumidity = defaultRelativeHumidity;}				
				
				double soilFlux = defaultSoilFlux;
				if (inSoilFlux != null){soilFlux = inSoilFlux.get(ID)[0];}
				if (soilFlux == nullValue) {soilFlux = defaultSoilFlux;}
				
				double soilMoisture = defaultSoilMoisture;
				if (inSoilMoisture != null){soilMoisture = inSoilMoisture.get(ID)[0];}
				if (soilMoisture == nullValue) {soilMoisture = defaultSoilMoisture;}
				
				// WIND
				WindProfile windVelocityProfile = new WindProfile();
				double windInCanopy = windVelocityProfile.computeWindProfile(windVelocity, canopyHeight);
				double windSoil = windVelocityProfile.computeWindProfile(windVelocity, 0.2);

			/*	double stressSun = defaultStress;
				if (inStressSun != null){stressSun =  inStressSun.get(ID)[0];}	//resistance.outStress;//.get(ID)[0];} //

				double stressShade = defaultStress;
				if (inStressShade != null){stressShade = inStressShade.get(ID)[0];}	*/

				// Compute the saturation pressure
				double saturationVaporPressure = pressure.computeSaturationVaporPressure(airTemperature, waterMolarMass, latentHeatEvaporation, molarGasConstant);			
				// Compute the actual vapour pressure
				double vaporPressure = pressure.computeVaporPressure(relativeHumidity, saturationVaporPressure);		
				// Compute the delta
				double delta = pressure.computeDelta(airTemperature, waterMolarMass, latentHeatEvaporation, molarGasConstant);			
				// Compute the convective transfer coefficient - hc
				double convectiveTransferCoefficient = sensibleHeat.computeConvectiveTransferCoefficient(airTemperature, windInCanopy, leafLength, criticalReynoldsNumber, prandtlNumber);
				// Compute the sensible transfer coefficient - cH
				double sensibleHeatTransferCoefficient = sensibleHeat.computeSensibleHeatTransferCoefficient(convectiveTransferCoefficient, leafSide);
				// Compute the latent transfer coefficient - cE
				double latentHeatTransferCoefficient = latentHeat.computeLatentHeatTransferCoefficient(airTemperature, atmosphericPressure, leafStomaSide, convectiveTransferCoefficient, airSpecificHeat,
						airDensity, molarGasConstant, molarVolume, waterMolarMass, latentHeatEvaporation, poreDensity, poreArea, poreDepth, poreRadius);			

				
				// RADIATION
				double solarElevationAngle = solarGeometry.getSolarElevationAngle(date, latitude,longitude, doHourly, time);
				double shortwaveCanopySun = radiationMethods.computeAbsordebRadiationSunlit(leafAreaIndex, solarElevationAngle, shortWaveRadiationDirect*2.1, shortWaveRadiationDiffuse*2.1);
				double radFactorSun = (shortWaveRadiationDirect*2.1 + shortWaveRadiationDiffuse*2.1)/ shortwaveCanopySun;
				
				//shortwaveCanopySun = ((solarElevationAngle>0)?shortwaveCanopySun:0);
	
				// Compute the area in sunlight
	            double areaCanopySun = radiationMethods.computeSunlitLeafAreaIndex(typeOfCanopy,leafAreaIndex, solarElevationAngle);
	         //   shortwaveCanopySun = shortwaveCanopySun/areaCanopySun;
				//shortwaveCanopySun = ((shortwaveCanopySun>shortWaveRadiationDirect)?shortWaveRadiationDirect:shortwaveCanopySun);
				/*if ("grassland".equals(typeOfCanopy)) {
					
				}*/
				double shortwaveCanopyShade = radiationMethods.computeAbsordebRadiationShadow(leafAreaIndex, solarElevationAngle, shortWaveRadiationDirect*2.1, shortWaveRadiationDiffuse*2.1);
				double radFactorShade = (shortWaveRadiationDirect*2.1 + shortWaveRadiationDiffuse*2.1)/ shortwaveCanopyShade;
				shortwaveCanopySun = (shortWaveRadiationDirect+shortWaveRadiationDiffuse)/radFactorSun;
				shortwaveCanopyShade = (shortWaveRadiationDirect+shortWaveRadiationDiffuse)/radFactorShade;

				//	shortwaveCanopyShade = ((solarElevationAngle>0)?shortwaveCanopyShade:0);
				if (solarElevationAngle <0) {
					 shortwaveCanopySun=0;
					 shortwaveCanopyShade=0;
					 areaCanopySun=0;
				 }
				// Compute the area in shadow
				double areaCanopyShade = leafAreaIndex - areaCanopySun;				
			//	shortwaveCanopyShade = shortwaveCanopyShade/areaCanopyShade;
				double netLong = shortWaveRadiationDirect-netLongWaveRadiation;
				
				double incidentSolarRadiationSoil = shortWaveRadiationDirect + shortWaveRadiationDiffuse - shortwaveCanopySun - shortwaveCanopyShade-netLong;
				incidentSolarRadiationSoil=(incidentSolarRadiationSoil<0)?0:incidentSolarRadiationSoil;
				//solarElevationAngle =(solarElevationAngle<0)?0:solarElevationAngle;
				//shortwaveCanopySun = shortWaveRadiationDirect;
				
				 
				// LAYER SOIL
	            double evaporation = computeEvaporation(incidentSolarRadiationSoil, windSoil, airTemperature, relativeHumidity, atmosphericPressure, soilFlux)* latentHeatEvaporation / 86400;
	            //double evaporation = computeEvapotranspirationPt( incidentSolarRadiationSoil, airTemperature, atmosphericPressure, soilFlux);
	            evaporation=(evaporation<0)?0:evaporation;//evaporation)*(time/latentHeatEvaporation);


				////////////////////////////////////////
				////////////////////////////////////////
				///////////////  SUN LAYER  ////////////
				////////////////////////////////////////
				////////////////////////////////////////	       
	            
	        	// FIRST ITERATION ENERGY BALANCE SUN
				// Initialization of the residual of the energy balance
	            double energyBalanceResidualSun = 0;
								
				double vaporPressureDew = pressure.computeVapourPressureDewPoint(airTemperature);		
				double vapourPressureDeficit = pressure.computeVapourPressureDeficit(vaporPressure, vaporPressureDew);


	            double stressRadiationSun = environmentalStress.computeRadiationStress(shortwaveCanopySun*2.1, alpha, theta);
	            double stressRadiationShade = environmentalStress.computeRadiationStress(shortwaveCanopyShade*2.1, alpha, theta);

            	double stressTemperature = 1;
	            if (useTemperatureStress == true) {
	            	stressTemperature = environmentalStress.computeTemperatureStress(airTemperature, Tl, Th, T0);
	            	}

	            double stressVPD = 1;
	            if (useVDPStress == true) {
	            	stressVPD = environmentalStress.computeVapourPressureStress(vapourPressureDeficit, VPD0);
	            	}
	              
            	double stressWater = 1;
	            if (useWaterStress == true) {
		            stressWater = environmentalStress.computeFAOWaterStress(soilMoisture, waterFieldCapacity, waterWiltingPoint, rootsDepth, depletionFraction);
	            	}
	            	            
	            double stressSun = 1*stressRadiationSun * stressTemperature * stressWater * stressVPD;
	           /* System.out.println("stressRadiationSun     "+ stressRadiationSun);
	            System.out.println("stressTemperature     "+ stressTemperature);
	            System.out.println("stressVPD     "+ stressVPD);
	            System.out.println("stressWater     "+ stressWater);
	            
	            System.out.println("stress sun     "+ stressSun);*/
	            double stressShade = defaultStress*stressRadiationShade * stressTemperature * stressWater * stressVPD;
	           // stressShade=(stressRadiationShade==0)?0:defaultStress;
	           // System.out.println("stress shade     "+ stressShade);

				
				// Compute the leaf temperature in sunlight				
	            leafTemperatureSun =  computeSurfaceTemperature(shortwaveCanopySun, energyBalanceResidualSun, sensibleHeatTransferCoefficient,airTemperature,
	            		areaCanopySun, stressSun,latentHeatTransferCoefficient,delta,vaporPressure,saturationVaporPressure,leafSide,longWaveRadiation);
				// Compute the net longwave radiation in sunlight
				double netLongWaveRadiationSun = areaCanopySun*radiationMethods.computeLongWaveRadiationBalance(leafSide, longWaveEmittance, airTemperature, leafTemperatureSun, stefanBoltzmannConstant);
				// Compute the latent heat flux from the sunlight area
				double latentHeatFluxSun 	= areaCanopySun*stressSun*latentHeat.computeLatentHeatFlux(delta,  leafTemperatureSun,  airTemperature,  
						latentHeatTransferCoefficient,sensibleHeatTransferCoefficient,  vaporPressure,  saturationVaporPressure);
				// Compute the sensible heat flux from the sunlight area
				double sensibleHeatFluxSun = areaCanopySun*sensibleHeat.computeSensibleHeatFlux(sensibleHeatTransferCoefficient, leafTemperatureSun, airTemperature);
				
				// Compute the residual of the energy balance for the sunlight area								
				energyBalanceResidualSun = computeEnergyBalance(shortwaveCanopySun, energyBalanceResidualSun, netLongWaveRadiationSun, latentHeatFluxSun, sensibleHeatFluxSun);
				//System.out.println(energyBalanceResidualSun);
				// Solve it in iterative way
				if (doIterative == true) {
					double iteration = 0;
					while (Math.abs(energyBalanceResidualSun) > 10 && iteration<10) {
						leafTemperatureSun =  computeSurfaceTemperature(shortwaveCanopySun, energyBalanceResidualSun, sensibleHeatTransferCoefficient,airTemperature,
								areaCanopySun, stressSun,latentHeatTransferCoefficient,delta,vaporPressure,saturationVaporPressure,leafSide,longWaveRadiation);
						
						netLongWaveRadiationSun = areaCanopySun*radiationMethods.computeLongWaveRadiationBalance(leafSide, longWaveEmittance, airTemperature, leafTemperatureSun, stefanBoltzmannConstant);
						
						latentHeatFluxSun 	= areaCanopySun*stressSun*latentHeat.computeLatentHeatFlux(delta,  leafTemperatureSun,  airTemperature,  latentHeatTransferCoefficient,
								sensibleHeatTransferCoefficient,  vaporPressure,  saturationVaporPressure);
						
						sensibleHeatFluxSun = areaCanopySun*sensibleHeat.computeSensibleHeatFlux(sensibleHeatTransferCoefficient, leafTemperatureSun, airTemperature);
						
						energyBalanceResidualSun = computeEnergyBalance(shortwaveCanopySun, energyBalanceResidualSun, netLongWaveRadiationSun, latentHeatFluxSun, sensibleHeatFluxSun);
						iteration++;
					//	System.out.println("Actual iteration	"+iteration);
					//	System.out.println("initial residual	"+energyBalanceResidualSun);


					}
				}
				////////////////////////////////////////
				////////////////////////////////////////
				//////////////  SHADE LAYER  ///////////
				////////////////////////////////////////
				////////////////////////////////////////
				double latentHeatFluxShade;
				
            	double sensibleHeatFluxShade;
				
				// FIRST ITERATION ENERGY BALANCE SHADE
				// Initialization of the residual of the energy balance
	            double energyBalanceResidualShade = 0;

	            if ("grassland".equals(typeOfCanopy)) {
	            	leafTemperatureShade = airTemperature;
	            	latentHeatFluxShade = 0;
					
	            	sensibleHeatFluxShade = 0;
	            	
	            }
	            else {
				// Compute the leaf temperature in shadow
				leafTemperatureShade =  computeSurfaceTemperature(shortwaveCanopyShade, energyBalanceResidualShade, sensibleHeatTransferCoefficient,airTemperature,
						areaCanopyShade, stressShade,latentHeatTransferCoefficient,delta,vaporPressure,saturationVaporPressure,leafSide,longWaveRadiation);
				// Compute the net longwave radiation in shade
				double netLongWaveRadiationShade = areaCanopyShade*radiationMethods.computeLongWaveRadiationBalance(leafSide, longWaveEmittance, airTemperature, leafTemperatureShade, stefanBoltzmannConstant);
				// Compute the latent heat flux from the shaded area
				latentHeatFluxShade 	= areaCanopyShade*stressShade*latentHeat.computeLatentHeatFlux(delta,  leafTemperatureShade,  airTemperature,  latentHeatTransferCoefficient,
						sensibleHeatTransferCoefficient,  vaporPressure,  saturationVaporPressure);
				// Compute the sensible heat flux from the shaded area				
				sensibleHeatFluxShade = areaCanopyShade*sensibleHeat.computeSensibleHeatFlux(sensibleHeatTransferCoefficient, leafTemperatureShade, airTemperature);
				
				// Compute the residual of the energy balance for the shaded area				
				energyBalanceResidualShade = computeEnergyBalance(shortwaveCanopyShade, energyBalanceResidualShade, netLongWaveRadiationShade, latentHeatFluxShade, sensibleHeatFluxShade);

				if (doIterative == true) {
					double iteration = 0;
					while (Math.abs(energyBalanceResidualShade) > 10 && iteration<10) {
						leafTemperatureShade =  computeSurfaceTemperature(shortwaveCanopyShade, energyBalanceResidualShade, sensibleHeatTransferCoefficient,airTemperature,
								areaCanopyShade,stressShade,latentHeatTransferCoefficient,delta,vaporPressure,saturationVaporPressure,leafSide,longWaveRadiation);
						
						netLongWaveRadiationShade = 1*radiationMethods.computeLongWaveRadiationBalance(leafSide, longWaveEmittance, airTemperature, leafTemperatureShade, stefanBoltzmannConstant);
						
						latentHeatFluxShade 	= areaCanopyShade*stressShade*latentHeat.computeLatentHeatFlux(delta,  leafTemperatureShade,  airTemperature,  latentHeatTransferCoefficient,
								sensibleHeatTransferCoefficient,  vaporPressure,  saturationVaporPressure);
						
						sensibleHeatFluxShade = areaCanopyShade*sensibleHeat.computeSensibleHeatFlux(sensibleHeatTransferCoefficient, leafTemperatureShade, airTemperature);
						
						energyBalanceResidualShade = computeEnergyBalance(shortwaveCanopyShade, energyBalanceResidualShade, netLongWaveRadiationShade, latentHeatFluxShade, sensibleHeatFluxShade);
						iteration++;
						}
					}	
	            }
			/*	latentHeatFluxSun = latentHeatFluxSun*areaCanopySun;
				latentHeatFluxShade = latentHeatFluxShade*areaCanopyShade;
				
				sensibleHeatFluxSun = sensibleHeatFluxSun*areaCanopySun;
				sensibleHeatFluxShade = sensibleHeatFluxShade*areaCanopyShade;*/
				
				latentHeatFluxSun=(latentHeatFluxSun<0)?0:latentHeatFluxSun;
				latentHeatFluxShade=(latentHeatFluxShade<0)?0:latentHeatFluxShade;
				totalTranspiration = (latentHeatFluxSun+latentHeatFluxShade+evaporation);//*(time/latentHeatEvaporation);  //
				if (airTemperature == nullValue) {	
					totalTranspiration=nullValue;	
					}
			
				if (doFullPrint == true) {			
					storeResultFull((Integer)ID, latentHeatFluxSun, latentHeatFluxShade, totalTranspiration, 
							sensibleHeatFluxSun,sensibleHeatFluxShade,
							leafTemperatureSun, leafTemperatureShade,shortwaveCanopySun, shortwaveCanopyShade,incidentSolarRadiationSoil,areaCanopySun,evaporation);
				}
			else {
				storeResult((Integer)ID,latentHeatFluxSun, latentHeatFluxShade, totalTranspiration);
				}
			}
		
		step++;	
			}
		


	private void storeResultFull(int ID,double latentHeatSun, double latentHeatShadow,double totalTranspiration, 
			double sensibleHeatFluxLight, double sensibleHeatFluxShadow,
			double leafTemperatureSun, double leafTemperatureShadow, 
			double radiationCanopyInLight, double radiationCanopyInShadow, double incidentSolarRadiationSoil,
			double leafInSunlight, double evaporation) 
			throws SchemaException {		
		
		outLatentHeat.put(ID, new double[]{latentHeatSun});
		outLatentHeatShade.put(ID, new double[]{latentHeatShadow});
		outTranspiration.put(ID, new double[]{totalTranspiration});
		
		outSensibleHeat.put(ID, new double[]{sensibleHeatFluxLight});
		outSensibleHeatShade.put(ID, new double[]{sensibleHeatFluxShadow});

		outLeafTemperature.put(ID, new double[]{leafTemperatureSun});
		outLeafTemperatureShade.put(ID, new double[]{leafTemperatureShadow});

		outRadiation.put(ID, new double[]{radiationCanopyInLight});
		outRadiationShade.put(ID, new double[]{radiationCanopyInShadow});
		outRadiationSoil.put(ID, new double[]{incidentSolarRadiationSoil});
		outCanopy.put(ID, new double[]{leafInSunlight});
		
		outEvaporation.put(ID, new double[]{evaporation});
		}
	private void storeResult(int ID,double latentHeatSun, double latentHeatShadow,double totalTranspiration) 
			throws SchemaException {
		outLatentHeat.put(ID, new double[]{latentHeatSun});
		outLatentHeatShade.put(ID, new double[]{latentHeatShadow});
		outTranspiration.put(ID, new double[]{totalTranspiration});
		}
	
	/*private Point[] getPoint(Coordinate coordinate, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS)
			throws Exception{
		Point[] point = new Point[] { GeometryUtilities.gf().createPoint(coordinate) };
		CrsUtilities.reproject(sourceCRS, targetCRS, point);
		return point;
	}*/
	private double computeEvaporation( double netRadiation, double windVelocity, double airTemperature, double relativeHumidity, 
    		double atmosphericPressure, double soilHeatFlux) {
		netRadiation = netRadiation * 86400/1E6;
		atmosphericPressure = atmosphericPressure/1000;
		airTemperature = airTemperature-273.15;
		soilHeatFlux = soilHeatFlux * 86400/1E6;
        // Computation of Delta [KPa °C-1]
        double denDelta = Math.pow((airTemperature + 237.3), 2);
        double expDelta = (17.27 * airTemperature) / (airTemperature + 237.3);
        double numDelta = 4098 * (0.6108 * Math.exp(expDelta));
        double delta = numDelta / denDelta;
        // Computation of Psicrometric constant [kPa °C-1]
        double psychrometricConstant = 0.665 * 0.001 * atmosphericPressure;
        // Computation of mean saturation vapour pressure [kPa]
        double saturationVaporPressure = 0.6108 * Math.exp(expDelta);
        // Computation of average hourly actual vapour pressure [kPa]
        double vaporPressure = saturationVaporPressure * relativeHumidity / 100;
        // Computation of ET [mm day-1]
        double num = 0.408 * delta * (netRadiation - soilHeatFlux) + (900 * psychrometricConstant * windVelocity * (saturationVaporPressure - vaporPressure)) / (airTemperature + 273);
        double den = delta + psychrometricConstant * (1 + 0.34 * windVelocity);
        double result = (num / den);//*(2.45*1E6)/86400;
        result = (result <0)?0:result;
        return result;
    }
	private double computeEvapotranspirationPt( double netRadiation, double airTemperature, double atmosphericPressure, double soilHeatFlux) {
		netRadiation = netRadiation * 86400/1E6;
		airTemperature = airTemperature-273.15;

		soilHeatFlux = soilHeatFlux * 86400/1E6;

		double denDelta = Math.pow((airTemperature + 237.3), 2);
        double expDelta = (17.27 * airTemperature) / (airTemperature + 237.3);
        double numDelta = 4098 * (0.6108 * Math.exp(expDelta));
        double delta = numDelta / denDelta;
        double psychrometricConstant = 0.665 * 0.001 * atmosphericPressure;
		double result = ((1.26/2.45) * delta * (netRadiation - soilHeatFlux)) / (psychrometricConstant + delta); //* (1E6)/86400;
		return result;  // -----> [mm/day]
}
	//public double computeSurfaceTemperatureIrradiatedSurface() {
		//double reducedShortWave = shortWaveCanopyLight/surfaceInSunlight;
		//double reducedShortWave2 = (Double.isNaN(reducedShortWave)?0:reducedShortWave);
		
	//}
   /* public double computeSurfaceTemperature(
    double shortWaveRadiation,
    double residual,
    double sensibleHeatTransferCoefficient,
    double airTemperature,
	double stress,
	double latentHeatTransferCoefficient,
	double delta,
	double vaporPressure,
	double saturationVaporPressure,
	int	side,
	double longWaveRadiation){
    	
	/*double surfaceTemperature1 = (shortWaveRadiation - residual);
	double surfaceTemperature2 = sensibleHeatTransferCoefficient*airTemperature;
	double surfaceTemperature3 = stress * latentHeatTransferCoefficient*(delta*airTemperature + vaporPressure - saturationVaporPressure);
	double surfaceTemperature4 = side * longWaveRadiation * 4;
	double surfaceTemperature5 =sensibleHeatTransferCoefficient;
			
	double surfaceTemperature6 =stress*latentHeatTransferCoefficient * delta;
	double surfaceTemperature7 =side * (longWaveRadiation/airTemperature) * 4;
	double surfaceTemperature = (surfaceTemperature1+surfaceTemperature2+surfaceTemperature3+surfaceTemperature4)/(surfaceTemperature5+surfaceTemperature6+surfaceTemperature7);
	return surfaceTemperature;	*/
	
/*	double surfaceTemperatureIrradiated1 = (shortWaveCanopyLight  - soilHeatFlux + 
		sensibleHeatTransferCoefficient*airTemperature*surfaceInSunlight +
		stressSun * 
		latentHeatTransferCoefficient*(delta*airTemperature + vaporPressure - saturationVaporPressure)*surfaceInSunlight  + 
		side * longWaveRadiation * 4 *surfaceInSunlight);
	double surfaceTemperatureIrradiated2 =(1/(
		sensibleHeatTransferCoefficient*surfaceInSunlight + 
		stressSun*latentHeatTransferCoefficient * delta *surfaceInSunlight +	
		side * longWaveRadiation/airTemperature * 4*surfaceInSunlight));
	double surfaceTemperatureIrradiated = surfaceTemperatureIrradiated1*surfaceTemperatureIrradiated2;
	this.surfaceIrradiatedTemperature = surfaceTemperatureIrradiated;
	return surfaceIrradiatedTemperature;*/	
	
	
    public double computeSurfaceTemperature(
    	    double shortWaveRadiation,
    	    double residual,
    	    double sensibleHeatTransferCoefficient,
    	    double airTemperature,
    	    double surfaceArea,
    		double stress,
    		double latentHeatTransferCoefficient,
    		double delta,
    		double vaporPressure,
    		double saturationVaporPressure,
    		int	side,
    		double longWaveRadiation){
    		double surfaceTemperature1 = (shortWaveRadiation - residual + sensibleHeatTransferCoefficient*airTemperature*surfaceArea +
    							stress * latentHeatTransferCoefficient*(delta*airTemperature + vaporPressure - saturationVaporPressure)*surfaceArea  +
    							side * longWaveRadiation * 4 *1);
    		double surfaceTemperature2 =(1/(sensibleHeatTransferCoefficient*surfaceArea +
    				stress*latentHeatTransferCoefficient * delta *surfaceArea +
    				side * longWaveRadiation/airTemperature * 4*1));
    		double surfaceTemperature = surfaceTemperature1*surfaceTemperature2;
    		return surfaceTemperature;	
    		}
    public double computeEnergyBalance(double shortWaveRadiation,double residual,
    		double longWaveRadiation,double latentHeatFlux,double sensibleHeatFlux){
    		double energyResidual = shortWaveRadiation - residual - longWaveRadiation - latentHeatFlux - sensibleHeatFlux;
    		return energyResidual;	
    }
	
		
}
