package bluebot.game;



/**
 * Provides utility methods for protocol conventions
 * 
 * @author Ruben Feyen
 */
public abstract class Protocol {
	
	private Protocol() {
		//	disabled
	}
	
	
	
	public static float angleExternalToInternal(final double external) {
//		float internal = (450F - (float)external);
//		for (; internal >= 360F; internal -= 360F);
//		return internal;
		float internal = (float) -external;
		for(; internal <0; internal +=360F);
		return internal;
	}
	
	public static double angleInternalToExternal(final float internal) {
//		double external = (450D - internal);
//		for (; external >= 360D; external -= 360D);
//		return external;
		float external = -internal;
		for(; external <0; external +=360F);
		return external;
	}
	
	public static void main(final String... args) {
		final float[] angles = { 0F, 90F, 180F, 270F };
		for (final float angle : angles) {
			System.out.printf("%f  :  %f  :  %f%n",
					angle,
					angleExternalToInternal(angle),
					angleInternalToExternal(angle));
		}
		
	}
	
}
