int main() {
	real a;
	real b;
	real ergebnis;
	int daemlicher_rueckgabewert;
	a := 29.5894;
	b := 18.666345;
	ergebnis := a+b;
	daemlicher_rueckgabewert := writeReal(ergebnis);
	daemlicher_rueckgabewert := writeChar(10);
	return 0;
}

