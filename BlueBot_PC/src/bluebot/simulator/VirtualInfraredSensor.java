package bluebot.simulator;

/**
 * The VirtualInfraredSenro
 * @author Dieter
 *
 */
public class VirtualInfraredSensor {
	
	/*
	 * STATIC CONSTANT REPRESENTING A DIRECTION WHERE A IR SIGNAL IS MEASURED.
	 */
	/**
	 * IR constant representing no IR signal.
	 */
	public static final int NO_IR = 0;
	/**
	 * IR constant representing signal from left back.
	 */
	public static final int IR_8_O_CLOCK = 1;
	/**
	 * IR constant representing signal from straight left.
	 */
	public static final int IR_9_O_CLOCK = 2;
	/**
	 * IR constant representing signal from halfway between left and full front.
	 */
	public static final int IR_10_O_CLOCK = 3;
	/**
	 * IR constant representing signal from left front.
	 */
	public static final int IR_11_O_CLOCK = 4;
	/**
	 * IR constant representing signal from straight front.
	 */
	public static final int IR_12_O_CLOCK = 5;
	/**
	 * IR constant representing signal from right front.
	 */
	public static final int IR_1_O_CLOCK = 6;
	/**
	 * IR constant representing signal from halfway between right and full front.
	 */
	public static final int IR_2_O_CLOCK = 7;
	/**
	 * IR constant representing signal from straight right.
	 */
	public static final int IR_3_O_CLOCK = 8;
	/**
	 * IR constant representing signal from right back.
	 */
	public static final int IR_4_O_CLOCK = 9;
	
	/*
	 * Other fields.
	 */
	
	/**
	 * Sensors object of the given VirtualInfraRedSensor.
	 */
	private final Sensors sensors;
	
	/**
	 * The maximum distance an IRSensor can see in centimeters.
	 */
    private static int distanceIRSensorCanSee_CM = 80;
	
	
	/**
	 * Creates a new VirtualInfraredSensor with a given sensors object.
	 * 
	 * @param 	sensors
	 * 			The sensors object of this VirtualInfraredSensor.
	 */
	public VirtualInfraredSensor(Sensors sensors) {
		this.sensors = sensors;
	}
     
    /**
    * Returns the infrared ball that is detected, if any.
    * If no infrared ball is withing detection range, null is returned.
    */     
    private InfraRedSource getInfraredBall(int x, int y, float heading){
             for(InfraRedSource irSource : sensors.getWorld().getIRModel().getAllIRSoucres()){
            	 	double distBetween = Sensors.getDistanceFromTo(x, y, irSource.getIRSourceX(), irSource.getIRSourceY());
                    if(distBetween < distanceIRSensorCanSee_CM){
                            /*
                             * Methode noWallBetween zal moeten checken op een muur tussen robot en IRBall-coordinaat
                             * Dit kan mbv de sonar want je kent de afstand tussen robot en de IRball?
                             * Bovendien kan ze ook checken of de wip gesloten is, en dit dan beschouwen als een muur.
                             * Deze moet nog geïmplementeerd worden en gebruik maken van GameState?.
                             */
                    	//TODO: implement seeing further then one tile? 
                            if(noWallBetween(x, y, heading, distBetween) && !sensors.getWorld().isSeesawLocked(sensors.getTileAt(x,y).getBarCode())){
                                    return irSource;
                            }
                     }
             }
             return null;
    }

    /**
     * Returns whether or not there is a wall between 
     * the given point and the distance in the given direction 
     * 
     * @param x
     * @param y
     * @param heading
     * @param distBetween
     * @return
     */
	private boolean noWallBetween(int x, int y, float heading,
			double distBetween) {
		int sonarValue = sensors.getSonarValue(x, y, heading);
		return sonarValue > distBetween;
	}

	/**
     * Returns the direction of the infrared signal. This direction is a number between 1 and 9 (inclusive).
     * Returns 0 if no signal is detected (seeInfrared()==false).
     * 
     * @param x 
     * @param y 
     * @param heading 
     */
    //FYI   Ik zoek dus eerst of er een mogelijk IRBall is, dan zoek ik de hoek tussen onze heading en de IRBall
    //              Deze hoek bepaald dan welke richting er wordt aangeduid.
    public int getInfraredDirection(int x, int y, float heading) {
            // TODO: Implementation
            InfraRedSource irSource = getInfraredBall(x,y,heading);
            if(irSource!=null){
                    //angle(double) tussen vector van onze robot naar de IRBall en de Y-as.
                    double angleD = Math.atan2(irSource.getIRSourceX()-x, irSource.getIRSourceY()-y)*180/Math.PI;  
                    if(angleD<0){
                            angleD += 360;  //atan2 geeft neg waarden terug in kwadranten 3 en 4, wij werken met [0,360]
                    }
                    float angle = (float) angleD;   //cast naar float want dat gebruiken wij
                    float angleBetweenIRBallAndHeading = angle - heading;         //de gezochte hoek
                    return determineDirectionOfIRSignal(angleBetweenIRBallAndHeading);      //bepaal richting
            }else{
                    return 0;
            }
    }
  
    /**
     * Returns the direction of an IRSignal coming from a given heading.
     * 
     * @param 	angleBetweenIRBallAndHeading
     * 			The heading from which the signal comes.
     * @return	An integer representing a IR signal direction.
     */
    //TODO: Exact Angles still unknown.
    public static int determineDirectionOfIRSignal(float angleBetweenIRBallAndHeading) {
    	if(angleBetweenIRBallAndHeading > 350 || angleBetweenIRBallAndHeading <10){
			return IR_12_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading > 340 && angleBetweenIRBallAndHeading<= 350){
			return IR_11_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading >= 280 && angleBetweenIRBallAndHeading<= 340){
			return IR_10_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading > 260 && angleBetweenIRBallAndHeading < 280){
			return IR_9_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading >= 225 && angleBetweenIRBallAndHeading <= 260){
			return IR_8_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading >= 10 && angleBetweenIRBallAndHeading < 20){
			return IR_1_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading >= 20 && angleBetweenIRBallAndHeading <= 80){
			return IR_2_O_CLOCK;
		} else if (angleBetweenIRBallAndHeading > 80 && angleBetweenIRBallAndHeading < 100){
			return IR_3_O_CLOCK;
		}  else if (angleBetweenIRBallAndHeading >= 100 && angleBetweenIRBallAndHeading <= 135){
			return IR_4_O_CLOCK;
		} 
    	return NO_IR;
    }
    
    /**
     * Returns whether the given integer represents a valid direction.
     * 
     * In our implementation a backwards or straight left or right direction is invalid.
     * 
     * @param 	dirNum
     * 			The integer of the direction to check.
     * @return	True	when the number represents a nonbackward direction nor straight left or right
     * 			False	otherwise.
     */
    public static boolean isValidDirection(int dirNum){
        if(dirNum>2 && dirNum<8 || dirNum == 0){
            return true;
	    }
        return false;
    }
}
