#include <stdio.h>
int readChar() {
	return getchar();
}

int writeChar(int a) {
	putchar(a);
	return 0;
}

int readInt() {
	int a;
	scanf("%d", &a);
	return a;
}

int writeInt(int a) {
	printf("%d", a);
	return 0;
}

double readReal() {
	double a;
	scanf("%lf", &a);
	return a;
}

int writeReal(double x) {
	printf("%lf", x);
	return 0;
}
