int main() {
	real a;
	real b;
	int dummy;
	a := 0.0-10.0;
	b := 20.0;
	if (a = b) {		// A
		dummy := writeChar(65);
	}
	if (a != b) {		// B
		dummy := writeChar(66);
	}
	if (a > b) {		// C
		dummy := writeChar(67);
	}
	if (a < b) {		// D
		dummy := writeChar(68);
	}
	if (a >= b) {		// E
		dummy := writeChar(69);
	}
	if (a <= b) {		// F
		dummy := writeChar(70);
	}
	dummy := writeChar(10);
}
 
