int fak(int n) {
	if(n <= 1){
		return 1;
	}
	else {
		return n * fak(n - 1);
	}
}

int main() {
	int dummy;
	int x;
	while (1 = 1) {
		x := readInt();
		if (x < 0) {
			return 0;
		}
		dummy := writeInt(fak(x));
		dummy := writeChar(10);
	}
	return 0;
}

