#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
	int i, n;
	int a, b, c;
	double e, f;

	srand(238409);
	
	printf("int main() {\n");
	printf("	int dummy;\n");
	for (i='a'; i<='z'; i++) {
		printf("	real %c;\n", i);
	}
	
	double vals[256];
	for (i='a'; i<='z'; i++) {
		printf("	%c := 0.0;\n", i);
		vals[i]=0.0;
	}
	for (n=0; n<1000; n++) {
		int m = rand()%5;
		switch (m) {
			case 0: 
				a=(rand()%26)+'a';
				b=(rand()%26)+'a';
				c=(rand()%26)+'a';
				e=((float)rand()/RAND_MAX);
				f=((float)rand()/RAND_MAX);
				printf("	%c := (%.15f * %c) + (%.15f * %c);\n", a, e, b, f, c);
				vals[a]=(e*vals[b])+(f*vals[c]);
				break;
			case 1:
				a=(rand()%26)+'a';
				e=((float)rand()/RAND_MAX);
				printf("	%c := %c + %.15f;\n", a, a, e);
				vals[a]+=e;
				break;
			case 2:
				a=(rand()%26)+'a';
				e=((float)rand()/RAND_MAX);
				printf("	%c := %c - %.15f;\n", a, a, e);
				vals[a]-=e;
				break;
			case 3:
				a=(rand()%26)+'a';
				e=((float)rand()/RAND_MAX);
				printf("	%c := %c * %.15f;\n", a, a, e);
				vals[a]*=e;
				break;
			case 4:
				a=(rand()%26)+'a';
				e=((float)rand()/RAND_MAX);
				printf("	%c := %c / %.15f;\n", a, a, e);
				vals[a]/=e;
				break;
		}

	}
	for (i='a'; i<='z'; i++) {
		printf("	dummy := writeReal(%c);\n", i);
	}
	printf("}\n");
	for (i='a'; i<='z'; i++) {
		//fprintf(stderr, "%c = %f;\n", i, vals[i]);
		fprintf(stderr, "%f\n", vals[i]);
	}
	return 0;
}
