package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
	private int period;
	private int state;	

	public StrangeBitwiseGenerator(int period) {
		state = 0;
		this.period = period;
		if(period == 0) {
			throw new RuntimeException("can`t divided by zero, or 0 period isn`t accept");
		}
	}
	
	
	@Override
	public double next() {
		state = (state + 1);
//	    int weirdState = state & (state >> 3) % period;
//	    int weirdState = state & (state >> 3) & (state >> 8) % period;
		int weirdState = state & (state >> 7) % period;
		return ((double)weirdState%period)*2/period - 1;	
	}
}
