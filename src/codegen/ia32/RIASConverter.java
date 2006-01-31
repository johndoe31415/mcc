class RIASConverter {
	double f;
	int valueRIAS1;			/* Higher word */
	int valueRIAS0;			/* Lower word */
	
	RIASConverter(double f) {
		this.f = f;
		convertFloatToRIAS();
	}

	private void setBit(int i) {
		int word = i/32;
		int bit = i%32;
		if (word > 1) throw new RuntimeException("No such bit " + i);
//		System.out.println("Setting bit " + i);
		int value = (1<<bit);
		if (word==1) valueRIAS1 |= value;
			else valueRIAS0 |= value;
	}
	
	private void convertFloatToRIAS() {
		valueRIAS1 = 0;
		valueRIAS0 = 0;

		/* Set sign bit if negative */
		double q = f;
		if (f < 0) {
			setBit(63);
			q = -q;
		}
	
		/* First normalize */
		int exponent = 1023;
		if (q > 2.0) {
			while (q > 2.0) {
				q /= 2.0;
				exponent++;
			}
		} else {
			if (q < 1.0) {
				while ((q < 1.0) && (exponent >= 0)) {
					q *= 2.0;
					exponent--;
				}
				if (exponent == -1)	return;				/* Number is zero */
			}
		}

		q *= 2.0;
		if (q > 2.0) q -= 2.0;
		/* Now we have a normalized number, proceed with calculating the mantissa */
		for (int currentBit = 51; currentBit >= 0; currentBit--) {
			q *= 2.0;
			if (q >= 2.0) {
				q -= 2.0;
				setBit(currentBit);
			}
		}
	
		/* And set the exponent finally */		
		for (int currentBit = 62; currentBit >= 52; currentBit--) {
			int bit = currentBit - 52;
			int bitVal = (1<<bit);
			if ((exponent & bitVal) != 0) {
				setBit(currentBit);
			}
		}
	}

	public void setFloatValue(double f) {
		this.f = f;
		convertFloatToRIAS();
	}
	
	public double getFloatValue() {
		return f;
	}

	private void displayWord(int v) {
		for (int i = 31; i >= 0; i--) {
			int bitv = (1<<i);
			if ((((i + 1) % 8) == 0) && (i != 31)) System.out.print(" ");
			if ((v & bitv) == 0) System.out.print("0");
				else System.out.print("1");
		}
	}
	
	private void displayWordAsIs(int v) {
		for (int i = 31; i >= 0; i--) {
			int bitv = (1<<i);
			if ((v & bitv) == 0) System.out.print("0");
				else System.out.print("1");
		}
	}

	public void displayRIAS() {
		System.out.println("============================================================================");
		System.out.println("Displaying " + f + ":");
		System.out.println(getRIASValue1() + " " + getRIASValue0());
		System.out.println(Integer.toHexString(getRIASValue1()) + "   " + Integer.toHexString(getRIASValue0()));
		displayWord(valueRIAS1);		
		System.out.print("      ");
		displayWord(valueRIAS0);
		System.out.println();
		System.out.println("SEEEEEEE EEEEMMMM MMMMMMMM MMMMMMMM      MMMMMMMM MMMMMMMM MMMMMMMM MMMMMMMM");
		displayWordAsIs(valueRIAS1);		
		displayWordAsIs(valueRIAS0);
		System.out.println();
		System.out.println("SEEEEEEEEEEEMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
		System.out.println("============================================================================");
	}

	public int getRIASValue1() {
		return valueRIAS1;
	}

	public int getRIASValue0() {
		return valueRIAS0;
	}
	
	public static void main(String[] args) {
		RIASConverter x;
		x = new RIASConverter(599.12345);
		x.displayRIAS();
		x = new RIASConverter(29.5894);
		x.displayRIAS();
		x = new RIASConverter(18.666345);
		x.displayRIAS();
		x = new RIASConverter(6.5894);
		x.displayRIAS();
	}
}

