int main() {
	int a;
	int b;
	real c;
	// Hallo\n
	b := writeChar(72);
	b := writeChar(97);
	b := writeChar(108);
	b := writeChar(108);
	b := writeChar(111);
	b := writeChar(10);
	
	a := readChar();
	b := writeChar(a);
	b := writeChar(10);
	a := readChar();
	b := writeChar(a);
	b := writeChar(10);
	a := readChar();
	b := writeChar(a);
	b := writeChar(10);
	a := readInt();
	b := writeInt(a);
	b := writeChar(10);
	c := readReal();
	b := writeReal(c);
	b := writeChar(10);


	// Welt!\n
	b := writeChar(87);
	b := writeChar(101);
	b := writeChar(108);
	b := writeChar(116);
	b := writeChar(33);
	b := writeChar(10);

	return 0;
}
